package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.EnergiaECombustivel;
import br.gov.serpro.calculadoraacv.repository.EnergiaECombustivelRepository;
import br.gov.serpro.calculadoraacv.exception.ResourceNotFoundException;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnergiaECombustivelService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnergiaECombustivelService.class);
    
    @Autowired
    private EnergiaECombustivelRepository energiaRepository;
    
    public List<EnergiaECombustivel> listarPorUsuario(Long usuarioId) {
        return energiaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }
    
    public List<EnergiaECombustivel> listarTodos() {
        return energiaRepository.findByAtivoTrue();
    }
    
    public Optional<EnergiaECombustivel> buscarPorId(Long id) {
        return energiaRepository.findById(id)
                .filter(EnergiaECombustivel::getAtivo);
    }
    
    public List<EnergiaECombustivel> buscarPorTipoEnergia(String tipoEnergia) {
        return energiaRepository.findByTipoEnergiaAndAtivoTrue(tipoEnergia);
    }
    
    public List<EnergiaECombustivel> buscarPorUsuarioETipo(Long usuarioId, String tipoEnergia) {
        return energiaRepository.findByUsuarioIdAndTipoEnergiaAndAtivoTrue(usuarioId, tipoEnergia);
    }
    
    // NOVOS MÉTODOS PARA ESCOPO
    public List<EnergiaECombustivel> listarPorEscopo(String escopo) {
        validarEscopo(escopo);
        String escopoCodigo = EscopoEnum.fromString(escopo).getCodigo(); // normaliza
        return energiaRepository.findByEscopoAndAtivoTrue(escopoCodigo);
    }
    
    public List<EnergiaECombustivel> listarPorUsuarioEEscopo(Long usuarioId, String escopo) {
        validarEscopo(escopo);
        String escopoCodigo = EscopoEnum.fromString(escopo).getCodigo(); // normaliza
        return energiaRepository.findByUsuarioIdAndEscopoAndAtivoTrue(usuarioId, escopoCodigo);
    }
    
    public List<String> listarEscoposDisponiveis() {
        return energiaRepository.findDistinctEscopos();
    }
    
    public List<String> listarTiposEnergia() {
        return energiaRepository.findDistinctTiposEnergia();
    }
    
    public EnergiaECombustivel salvar(EnergiaECombustivel energia) {
        logger.info("Salvando energia - Ano Referencia: {}", energia.getAnoReferencia());
        
        // Validar escopo
        if (energia.getEscopo() != null) {
            validarEscopo(energia.getEscopo());
            // NOVA VALIDAÇÃO: Escopo por tipo de energia
            validarEscopoPorTipoEnergia(energia.getTipoEnergia(), energia.getEscopo());
        }
        
        if (energia.getId() == null) {
            energia.setDataCriacao(LocalDateTime.now());
        }
        energia.setDataAtualizacao(LocalDateTime.now());
        return energiaRepository.save(energia);
    }
    
    private void validarEscopo(String escopo) {
        if (escopo != null && !Arrays.stream(EscopoEnum.values())
                .anyMatch(e -> e.getCodigo().equals(escopo))) {
            throw new IllegalArgumentException("Escopo inválido: " + escopo);
        }
    }
    
    private void validarEscopoPorTipoEnergia(String tipoEnergia, String escopo) {
        if (tipoEnergia == null || escopo == null) {
            return; // Validação será feita em outros lugares
        }
        
        String tipoEnergiaUpper = tipoEnergia.toUpperCase();
        String escopoCodigo = EscopoEnum.fromString(escopo).getCodigo(); // normaliza
        
        // Regra: ENERGIA (ELETRICA, etc.) -> apenas ESCOPO2
        if (tipoEnergiaUpper.contains("ELETRICA") || tipoEnergiaUpper.contains("ENERGIA")) {
            if (!"escopo2".equals(escopoCodigo)) {
                throw new IllegalArgumentException(
                    "Tipo de energia '" + tipoEnergia + "' deve usar apenas Escopo 2. Escopo fornecido: " + escopo
                );
            }
        }
        
        // Regra: COMBUSTIVEL -> aceitar ESCOPO1 ou qualquer variação de ESCOPO3
        if (tipoEnergiaUpper.contains("COMBUSTIVEL")) {
            boolean escopoValido = "escopo1".equals(escopoCodigo) || escopoCodigo.startsWith("escopo3");
            if (!escopoValido) {
                throw new IllegalArgumentException(
                    "Tipo de energia '" + tipoEnergia + "' deve usar Escopo 1 ou Escopo 3. Escopo fornecido: " + escopo
                );
            }
        }
    }
    
    public EnergiaECombustivel atualizar(Long id, EnergiaECombustivel energiaAtualizada) {
        return energiaRepository.findById(id)
                .filter(EnergiaECombustivel::getAtivo)
                .map(energia -> {
                    // Validar escopo antes de atualizar
                    if (energiaAtualizada.getEscopo() != null) {
                        validarEscopo(energiaAtualizada.getEscopo());
                        validarEscopoPorTipoEnergia(energiaAtualizada.getTipoEnergia(), energiaAtualizada.getEscopo());
                    }
                    
                    energia.setTipoEnergia(energiaAtualizada.getTipoEnergia());
                    energia.setFonteEnergia(energiaAtualizada.getFonteEnergia());
                    energia.setConsumoAnual(energiaAtualizada.getConsumoAnual());
                    energia.setUnidade(energiaAtualizada.getUnidade());
                    energia.setFatorEmissao(energiaAtualizada.getFatorEmissao());
                    energia.setFatorMedioAnual(energiaAtualizada.getFatorMedioAnual());
                    energia.setEscopo(energiaAtualizada.getEscopo());
                    
                    // ✅ CAMPOS CALCULADOS ADICIONADOS:
                    energia.setTipoDado(energiaAtualizada.getTipoDado());
                    energia.setDadosMensaisJson(energiaAtualizada.getDadosMensaisJson());
                    energia.setMediaAnualCalculada(energiaAtualizada.getMediaAnualCalculada());
                    energia.setMesesPreenchidos(energiaAtualizada.getMesesPreenchidos());
                    energia.setStatusCalculo(energiaAtualizada.getStatusCalculo());
                    energia.setUsuarioUltimaEdicao(energiaAtualizada.getUsuarioUltimaEdicao());
                    energia.setObservacoesAuditoria(energiaAtualizada.getObservacoesAuditoria());
                    
                    energia.setDataAtualizacao(LocalDateTime.now());
                    return energiaRepository.save(energia);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Dados de energia não encontrados com ID: " + id));
    }
    
    public void deletar(Long id) {
        energiaRepository.findById(id)
                .filter(EnergiaECombustivel::getAtivo)
                .map(energia -> {
                    energia.setAtivo(false);
                    energia.setDataAtualizacao(LocalDateTime.now());
                    return energiaRepository.save(energia);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Dados de energia não encontrados com ID: " + id));
    }
    
    // NOVO MÉTODO PARA BUSCAR POR USUÁRIO+ANO+ESCOPO
    public Optional<EnergiaECombustivel> buscarPorUsuarioAnoEscopo(Long usuarioId, Integer anoReferencia, String escopo) {
        return energiaRepository.findByUsuarioIdAndAnoReferenciaAndEscopoAndAtivoTrue(usuarioId, anoReferencia, escopo);
    }
}