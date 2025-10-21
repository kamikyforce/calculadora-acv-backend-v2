package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.InsumoRebanhoRequest;
import br.gov.serpro.calculadoraacv.dto.InsumoRebanhoResponse;
import br.gov.serpro.calculadoraacv.enums.*;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.InsumoRebanho;
import br.gov.serpro.calculadoraacv.repository.InsumoRebanhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsumoRebanhoService {

    private final InsumoRebanhoRepository repository;

    @Transactional
    public InsumoRebanhoResponse salvar(InsumoRebanhoRequest request) {
        validarRequest(request);
        
        InsumoRebanho entidade = new InsumoRebanho();
        mapearRequestParaEntidade(request, entidade);
        
        InsumoRebanho salvo = repository.save(entidade);
        return new InsumoRebanhoResponse(salvo);
    }

    @Transactional
    public InsumoRebanhoResponse atualizar(Long id, InsumoRebanhoRequest request) {
        validarRequest(request);
        
        InsumoRebanho existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo do Rebanho", id));
        
        mapearRequestParaEntidade(request, existente);
        
        InsumoRebanho atualizado = repository.save(existente);
        return new InsumoRebanhoResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public InsumoRebanhoResponse buscarPorId(Long id) {
        InsumoRebanho entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo do Rebanho", id));
        return new InsumoRebanhoResponse(entidade);
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> listar() {
        return repository.findByAtivoTrue().stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndAtivoTrue(usuarioId).stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> listarComFiltros(Long usuarioId, TipoInsumo tipo, EscopoEnum escopo, 
                                                       GrupoIngredienteAlimentar grupoIngrediente, 
                                                       FazParteDieta fazParteDieta) {
        return repository.findWithFilters(usuarioId, tipo, escopo, grupoIngrediente, fazParteDieta).stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> buscarPorTipo(TipoInsumo tipo) {
        return repository.findByTipoAndAtivoTrue(tipo).stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> buscarPorEscopo(EscopoEnum escopo) {
        return repository.findByEscopoAndAtivoTrue(escopo).stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoRebanhoResponse> buscarPorProduto(Long usuarioId, String produto) {
        return repository.findByUsuarioIdAndIdentificacaoProdutoContainingAndAtivoTrue(usuarioId, produto).stream()
                .map(InsumoRebanhoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> listarModulosDistintos() {
        return repository.findDistinctModulos();
    }

    @Transactional(readOnly = true)
    public List<EscopoEnum> listarEscoposDistintos() {
        return repository.findDistinctEscopos();
    }

    @Transactional(readOnly = true)
    public List<TipoInsumo> listarTiposDistintos() {
        return repository.findDistinctTipos();
    }

    @Transactional
    public void deletar(Long id) {
        InsumoRebanho entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo do Rebanho", id));
        
        entidade.setAtivo(false);
        repository.save(entidade);
    }

    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        InsumoRebanho entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo do Rebanho", id));
        
        entidade.setAtivo(ativo);
        repository.save(entidade);
    }

    @Transactional(readOnly = true)
    public long contarPorUsuario(Long usuarioId) {
        return repository.countByUsuarioIdAndAtivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public boolean existeProdutoParaUsuario(String identificacao, Long usuarioId) {
        String identificacaoNormalizada = identificacao != null ? identificacao.trim().toUpperCase() : null;
        return repository.existsByIdentificacaoProdutoAndUsuarioId(identificacaoNormalizada, usuarioId);
    }

    @Transactional(readOnly = true)
    public List<String> listarIngredientesAlimentares() {
        return repository.findDistinctIdentificacaoProdutoByTipoAndAtivoTrue(TipoInsumo.INGREDIENTES_ALIMENTARES);
    }

    private void mapearRequestParaEntidade(InsumoRebanhoRequest request, InsumoRebanho entidade) {
        entidade.setUsuarioId(request.getUsuarioId());
        entidade.setModulo(request.getModulo());
        entidade.setTipo(request.getTipo());
        entidade.setEscopo(request.getEscopo());
        String identificacaoNormalizada = request.getIdentificacaoProduto() != null ? 
            request.getIdentificacaoProduto().trim().toUpperCase() : null;
        entidade.setIdentificacaoProduto(identificacaoNormalizada);
        entidade.setFonteDataset(request.getFonteDataset());
        entidade.setDatasetProduto(request.getDatasetProduto());
        entidade.setUnidadeProduto(request.getUnidadeProduto());
        entidade.setMetodoAvaliacaoGwp(request.getMetodoAvaliacaoGwp());
        
        entidade.setGrupoIngrediente(request.getGrupoIngrediente());
        entidade.setNomeProduto(request.getNomeProduto());
        entidade.setUuid(request.getUuid());
        entidade.setQuantidade(request.getQuantidade());
        entidade.setQuantidadeProdutoReferencia(request.getQuantidadeProdutoReferencia());
        entidade.setUnidade(request.getUnidade());
        
        entidade.setGwp100Fossil(request.getGwp100Fossil());
        entidade.setGwp100Biogenico(request.getGwp100Biogenico());
        entidade.setGwp100Transformacao(request.getGwp100Transformacao());
        entidade.setCo2Fossil(request.getCo2Fossil());
        entidade.setCo2Ch4Transformacao(request.getCo2Ch4Transformacao());
        entidade.setCh4Fossil(request.getCh4Fossil());
        entidade.setCh4Biogenico(request.getCh4Biogenico());
        entidade.setN2o(request.getN2o());
        entidade.setOutrasSubstancias(request.getOutrasSubstancias());
        
        entidade.setFazParteDieta(request.getFazParteDieta());
        entidade.setIngrediente(request.getIngrediente());
        entidade.setNotEu(request.getNotEu());
        entidade.setEnergiaBruta(request.getEnergiaBruta());
        entidade.setMs(request.getMs());
        entidade.setProteinaBruta(request.getProteinaBruta());
        entidade.setFatoresEmissao(request.getFatoresEmissao());
        
        entidade.setComentarios(request.getComentarios());
        entidade.setComentariosEscopo1(request.getComentariosEscopo1());
        entidade.setComentariosEscopo3(request.getComentariosEscopo3());
        
        entidade.setGeeTotalEscopo1(request.getGeeTotalEscopo1());
        entidade.setCo2FossilEscopo1(request.getCo2FossilEscopo1());
        entidade.setUsoTerraEscopo1(request.getUsoTerraEscopo1());
        entidade.setCh4FossilEscopo1(request.getCh4FossilEscopo1());
        entidade.setCh4BiogenicoEscopo1(request.getCh4BiogenicoEscopo1());
        entidade.setN2oEscopo1(request.getN2oEscopo1());
        entidade.setOutrasSubstanciasEscopo1(request.getOutrasSubstanciasEscopo1());
        
        entidade.setGeeTotalEscopo3(request.getGeeTotalEscopo3());
        entidade.setGwp100FossilEscopo3(request.getGwp100FossilEscopo3());
        entidade.setGwp100BiogenicoEscopo3(request.getGwp100BiogenicoEscopo3());
        entidade.setGwp100TransformacaoEscopo3(request.getGwp100TransformacaoEscopo3());
        entidade.setDioxidoCarbonoFossilEscopo3(request.getDioxidoCarbonoFossilEscopo3());
        entidade.setDioxidoCarbonoMetanoTransformacaoEscopo3(request.getDioxidoCarbonoMetanoTransformacaoEscopo3());
        entidade.setMetanoFossilEscopo3(request.getMetanoFossilEscopo3());
        entidade.setMetanoBiogenicoEscopo3(request.getMetanoBiogenicoEscopo3());
        entidade.setOxidoNitrosoEscopo3(request.getOxidoNitrosoEscopo3());
        entidade.setOutrasSubstanciasEscopo3(request.getOutrasSubstanciasEscopo3());
        
        entidade.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
    }

    private void validarRequest(InsumoRebanhoRequest request) {
        if (request.getModulo() == null || request.getModulo().trim().isEmpty()) {
            throw new ValidacaoException("Módulo é obrigatório");
        }
        
        if (request.getTipo() == null) {
            throw new ValidacaoException("Tipo é obrigatório");
        }
        
        if (request.getEscopo() == null) {
            throw new ValidacaoException("Escopo é obrigatório");
        }
        
        if (request.getIdentificacaoProduto() == null || request.getIdentificacaoProduto().trim().isEmpty()) {
            throw new ValidacaoException("Identificação do produto é obrigatória");
        }
        
        if (request.getUnidadeProduto() == null) {
            throw new ValidacaoException("Unidade do produto é obrigatória");
        }
        
        validarValoresNumericos(request);
        
        validarCamposDieta(request);
    }

    private void validarValoresNumericos(InsumoRebanhoRequest request) {
        if (request.getQuantidade() != null && request.getQuantidade().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacaoException("Quantidade deve ser positiva");
        }
        
        validarValorEmissao(request.getGwp100Fossil(), "GWP 100 Fóssil");
        validarValorEmissao(request.getGwp100Biogenico(), "GWP 100 Biogênico");
        validarValorEmissao(request.getGwp100Transformacao(), "GWP 100 Transformação");
        validarValorEmissao(request.getCo2Fossil(), "CO2 Fóssil");
        validarValorEmissao(request.getCo2Ch4Transformacao(), "CO2 e CH4 Transformação");
        validarValorEmissao(request.getCh4Fossil(), "CH4 Fóssil");
        validarValorEmissao(request.getCh4Biogenico(), "CH4 Biogênico");
        validarValorEmissao(request.getN2o(), "N2O");
        validarValorEmissao(request.getOutrasSubstancias(), "Outras substâncias");
        
        validarValorEmissao(request.getGeeTotalEscopo1(), "GEE Total Escopo 1");
        validarValorEmissao(request.getCo2FossilEscopo1(), "CO2 Fóssil Escopo 1");
        validarValorEmissao(request.getUsoTerraEscopo1(), "Uso da Terra Escopo 1");
        validarValorEmissao(request.getCh4FossilEscopo1(), "CH4 Fóssil Escopo 1");
        validarValorEmissao(request.getCh4BiogenicoEscopo1(), "CH4 Biogênico Escopo 1");
        validarValorEmissao(request.getN2oEscopo1(), "N2O Escopo 1");
        validarValorEmissao(request.getOutrasSubstanciasEscopo1(), "Outras Substâncias Escopo 1");
        
        validarValorEmissao(request.getGeeTotalEscopo3(), "GEE Total Escopo 3");
        validarValorEmissao(request.getGwp100FossilEscopo3(), "GWP 100 Fóssil Escopo 3");
        validarValorEmissao(request.getGwp100BiogenicoEscopo3(), "GWP 100 Biogênico Escopo 3");
        validarValorEmissao(request.getGwp100TransformacaoEscopo3(), "GWP 100 Transformação Escopo 3");
        validarValorEmissao(request.getDioxidoCarbonoFossilEscopo3(), "Dióxido de Carbono Fóssil Escopo 3");
        validarValorEmissao(request.getDioxidoCarbonoMetanoTransformacaoEscopo3(), "Dióxido de Carbono e Metano Transformação Escopo 3");
        validarValorEmissao(request.getMetanoFossilEscopo3(), "Metano Fóssil Escopo 3");
        validarValorEmissao(request.getMetanoBiogenicoEscopo3(), "Metano Biogênico Escopo 3");
        validarValorEmissao(request.getOxidoNitrosoEscopo3(), "Óxido Nitroso Escopo 3");
        validarValorEmissao(request.getOutrasSubstanciasEscopo3(), "Outras Substâncias Escopo 3");
        
        validarValorEmissao(request.getEnergiaBruta(), "Energia bruta");
        validarValorEmissao(request.getMs(), "MS");
        validarValorEmissao(request.getProteinaBruta(), "Proteína bruta");
    }

    private void validarValorEmissao(BigDecimal valor, String nomeCampo) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacaoException(nomeCampo + " deve ser positivo");
        }
    }

    private void validarCamposDieta(InsumoRebanhoRequest request) {
        if (request.getFazParteDieta() == FazParteDieta.SIM) {
            if (request.getIngrediente() == null || request.getIngrediente().trim().isEmpty()) {
                throw new ValidacaoException("Ingrediente é obrigatório quando faz parte da dieta");
            }
            
            if (request.getFatoresEmissao() == null) {
                throw new ValidacaoException("Fatores de emissão é obrigatório quando faz parte da dieta");
            }
        }
    }
}