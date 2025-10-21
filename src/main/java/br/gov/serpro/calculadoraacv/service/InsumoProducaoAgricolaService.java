package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.InsumoProducaoAgricolaRequest;
import br.gov.serpro.calculadoraacv.dto.InsumoProducaoAgricolaResponse;

import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.InsumoProducaoAgricola;
import br.gov.serpro.calculadoraacv.repository.InsumoProducaoAgricolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsumoProducaoAgricolaService {

    private final InsumoProducaoAgricolaRepository repository;

    @Transactional
    public InsumoProducaoAgricolaResponse salvar(InsumoProducaoAgricolaRequest request) {
        validarRequest(request);
        validarNomeDuplicado(request.getNomeProduto(), request.getClasse(), request.getUsuarioId(), null);
        
        InsumoProducaoAgricola entidade = new InsumoProducaoAgricola();
        request.setVersao("v1");
        mapearRequestParaEntidade(request, entidade);
        
        InsumoProducaoAgricola salvo = repository.save(entidade);
        return new InsumoProducaoAgricolaResponse(salvo);
    }

    @Transactional
    public InsumoProducaoAgricolaResponse atualizar(Long id, InsumoProducaoAgricolaRequest request) {
        validarRequest(request);
        validarNomeDuplicado(request.getNomeProduto(), request.getClasse(), request.getUsuarioId(), id);
        
        InsumoProducaoAgricola existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo de Produção Agrícola", id));
        
        // Incrementar versão automaticamente na atualização
        String novaVersao = incrementarVersao(existente.getVersao());
        request.setVersao(novaVersao);
        
        mapearRequestParaEntidade(request, existente);
        
        InsumoProducaoAgricola atualizado = repository.save(existente);
        return new InsumoProducaoAgricolaResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public InsumoProducaoAgricolaResponse buscarPorId(Long id) {
        InsumoProducaoAgricola entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo de Produção Agrícola", id));
        return new InsumoProducaoAgricolaResponse(entidade);
    }

    @Transactional(readOnly = true)
    public List<InsumoProducaoAgricolaResponse> listar() {
        return repository.findByAtivoTrue().stream()
                .map(InsumoProducaoAgricolaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsumoProducaoAgricolaResponse> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndAtivoTrue(usuarioId).stream()
                .map(InsumoProducaoAgricolaResponse::new)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<InsumoProducaoAgricolaResponse> buscarPorProduto(Long usuarioId, String produto) {
        return repository.findByUsuarioIdAndNomeProdutoContainingAndAtivoTrue(usuarioId, produto).stream()
                .map(InsumoProducaoAgricolaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        InsumoProducaoAgricola entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo de Produção Agrícola", id));
        
        entidade.setAtivo(false);
        repository.save(entidade);
    }

    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        InsumoProducaoAgricola entidade = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo de Produção Agrícola", id));
        
        entidade.setAtivo(ativo);
        repository.save(entidade);
    }

    @Transactional(readOnly = true)
    public long contarPorUsuario(Long usuarioId) {
        return repository.countByUsuarioIdAndAtivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public boolean existeProdutoParaUsuario(String nomeProduto, Long usuarioId) {
        return repository.existsByNomeProdutoAndUsuarioId(nomeProduto, usuarioId);
    }

    public boolean verificarNomeExistenteConsiderandoClasse(String nomeProduto, Long usuarioId, String classe, Long idExcluir) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            return false;
        }
        
        // Log para debug
        System.out.println("DEBUG - Verificando nome: '" + nomeProduto + "', classe: '" + classe + "', usuarioId: " + usuarioId + ", idExcluir: " + idExcluir);
        
        // Se a classe foi fornecida, verifica se já existe um produto com o mesmo nome E mesma classe
        if (classe != null && !classe.trim().isEmpty()) {
            boolean resultado;
            if (idExcluir != null) {
                resultado = repository.existsByNomeProdutoAndClasseAndUsuarioIdExcludingId(nomeProduto.trim(), classe.trim(), usuarioId, idExcluir);
            } else {
                resultado = repository.existsByNomeProdutoAndClasseAndUsuarioId(nomeProduto.trim(), classe.trim(), usuarioId);
            }
            System.out.println("DEBUG - Resultado da verificação: " + resultado);
            return resultado;
        }
        
        // Se a classe não foi fornecida, usa a validação original (só nome)
        if (idExcluir != null) {
            return repository.existsByNomeProdutoIgnoreCaseAndAccentsAndUsuarioIdExcludingId(nomeProduto.trim(), usuarioId, idExcluir);
        } else {
            return repository.existsByNomeProdutoIgnoreCaseAndAccentsAndUsuarioId(nomeProduto.trim(), usuarioId);
        }
    }

    private void mapearRequestParaEntidade(InsumoProducaoAgricolaRequest request, InsumoProducaoAgricola entidade) {
        // Campos de controle
        entidade.setUsuarioId(request.getUsuarioId());
        
        // Escopo 1 - Classificação
        entidade.setClasse(request.getClasse());
        entidade.setEspecificacao(request.getEspecificacao());
        
        // Escopo 1 - Teor de macronutrientes
        entidade.setTeorNitrogenio(request.getTeorNitrogenio());
        entidade.setTeorFosforo(request.getTeorFosforo());
        entidade.setTeorPotassio(request.getTeorPotassio());
        
        // Escopo 1 - Fator de conversão
        entidade.setFatorConversao(request.getFatorConversao());
        entidade.setFatorConversaoUnidade(request.getFatorConversaoUnidade());
        
        // Escopo 1 - Quantidade e unidade de referência
        entidade.setQuantidade(request.getQuantidade());
        entidade.setUnidadeReferencia(request.getUnidadeReferencia());
        
        // Escopo 1 - Fatores de emissão com referências
        entidade.setFeCo2Biogenico(request.getFeCo2Biogenico());
        entidade.setRefFeCo2Biogenico(request.getRefFeCo2Biogenico());
        entidade.setFeCo2(request.getFeCo2());
        entidade.setRefFeCo2(request.getRefFeCo2());
        entidade.setFeCh4(request.getFeCh4());
        entidade.setRefFeCh4(request.getRefFeCh4());
        entidade.setFeN2oDireto(request.getFeN2oDireto());
        entidade.setRefFeN2oDireto(request.getRefFeN2oDireto());
        entidade.setFracN2oVolatilizacao(request.getFracN2oVolatilizacao());
        entidade.setRefFracN2oVolatilizacao(request.getRefFracN2oVolatilizacao());
        entidade.setFracN2oLixiviacao(request.getFracN2oLixiviacao());
        entidade.setRefFracN2oLixiviacao(request.getRefFracN2oLixiviacao());
        entidade.setFeN2oComposto(request.getFeN2oComposto());
        entidade.setRefFeN2oComposto(request.getRefFeN2oComposto());
        entidade.setFeCo(request.getFeCo());
        entidade.setRefFeCo(request.getRefFeCo());
        entidade.setFeNox(request.getFeNox());
        entidade.setRefFeNox(request.getRefFeNox());
        
        // Escopo 3 - Identificação e classificação
        entidade.setGrupoIngrediente(request.getGrupoIngrediente());
        entidade.setTipoProduto(request.getTipoProduto());
        
        // Escopo 3 - Quantidade e unidade de referência
        entidade.setQuantidadeProdutoReferencia(request.getQuantidadeProdutoReferencia());
        entidade.setUnidadeProdutoReferencia(request.getUnidadeProdutoReferencia());
        entidade.setUnidadeProduto(request.getUnidadeProduto());
        
        // Escopo 3 - Quantidade e unidade
        entidade.setQuantidadeProduto(request.getQuantidadeProduto());
        
        // Escopo 3 - Valores de emissões (GEE)
        entidade.setGeeTotal(request.getGeeTotal());
        entidade.setDioxidoCarbonoFossil(request.getDioxidoCarbonoFossil());
        entidade.setDioxidoCarbonoMetanoTransformacao(request.getDioxidoCarbonoMetanoTransformacao());
        entidade.setMetanoFossil(request.getMetanoFossil());
        entidade.setMetanoBiogenico(request.getMetanoBiogenico());
        entidade.setOxidoNitroso(request.getOxidoNitroso());
        entidade.setOutrasSubstanciasEscopo3(request.getOutrasSubstanciasEscopo3());
        entidade.setGwp100Total(request.getGwp100Total());
        entidade.setGwp100Fossil(request.getGwp100Fossil());
        entidade.setGwp100Biogenico(request.getGwp100Biogenico());
        entidade.setGwp100Transformacao(request.getGwp100Transformacao());
        entidade.setCo2Ch4Transformacao(request.getCo2Ch4Transformacao());
        
        // Observações
        entidade.setComentarios(request.getComentarios());
        
        // Campos de auditoria
        entidade.setNomeProduto(request.getNomeProduto());
        entidade.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        
        if (request.getVersao() != null) {
            entidade.setVersao(request.getVersao());
        }
    }

    private void validarRequest(InsumoProducaoAgricolaRequest request) {
        if (request.getUsuarioId() == null) {
            throw new ValidacaoException("Usuário é obrigatório");
        }
        
        if (request.getNomeProduto() == null || request.getNomeProduto().trim().isEmpty()) {
            throw new ValidacaoException("Nome do produto é obrigatório");
        }
        
        validarValoresNumericos(request);
        validarCamposProducaoAgricola(request);
    }

    private void validarValoresNumericos(InsumoProducaoAgricolaRequest request) {
        // Validação dos campos de GEE
        validarValorEmissao(request.getOutrasSubstanciasEscopo3(), "Outras substâncias");
        
        // Validação dos campos de GWP
        validarValorEmissao(request.getGwp100Total(), "GWP 100 Total");
        validarValorEmissao(request.getGwp100Fossil(), "GWP 100 Fóssil");
        validarValorEmissao(request.getGwp100Biogenico(), "GWP 100 Biogênico");
        validarValorEmissao(request.getGwp100Transformacao(), "GWP 100 Transformação");
        validarValorEmissao(request.getCo2Ch4Transformacao(), "CO2 CH4 Transformação");
    }

    private void validarValorEmissao(BigDecimal valor, String nomeCampo) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacaoException(nomeCampo + " deve ser positivo");
        }
    }

    private void validarCamposProducaoAgricola(InsumoProducaoAgricolaRequest request) {
        // Validações específicas para produção agrícola podem ser adicionadas aqui se necessário
    }
    
    private String incrementarVersao(String versaoAtual) {
        if (versaoAtual == null || versaoAtual.isEmpty()) {
            return "v1";
        }
        
        try {
            String numeroStr = versaoAtual.substring(1);
            int numero = Integer.parseInt(numeroStr);
            return "v" + (numero + 1);
        } catch (Exception e) {
            return "v1";
        }
    }
    
    private void validarNomeDuplicado(String nomeProduto, String classe, Long usuarioId, Long idExcluir) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            return;
        }
        
        boolean existe;
        if (idExcluir == null) {
            // Para criação, verifica se já existe com mesmo nome e classe
            existe = repository.existsByNomeProdutoAndClasseAndUsuarioId(nomeProduto.trim(), classe, usuarioId);
        } else {
            // Para edição, verifica se já existe com mesmo nome e classe excluindo o próprio registro
            existe = repository.existsByNomeProdutoAndClasseAndUsuarioIdExcludingId(nomeProduto.trim(), classe, usuarioId, idExcluir);
        }
        
        if (existe) {
            throw new ValidacaoException("Já existe um insumo com este nome. Por favor, escolha um nome diferente.");
        }
    }
}