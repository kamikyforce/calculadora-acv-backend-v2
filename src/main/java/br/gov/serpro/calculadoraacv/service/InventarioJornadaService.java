package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.InventarioJornadaRequest;
import br.gov.serpro.calculadoraacv.dto.InventarioJornadaResponse;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.InventarioJornada;
import br.gov.serpro.calculadoraacv.model.InventarioJornada.StatusInventario;
import br.gov.serpro.calculadoraacv.model.InventarioJornada.TipoRebanho;
import br.gov.serpro.calculadoraacv.repository.InventarioJornadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventarioJornadaService {

    private final InventarioJornadaRepository repository;

    public InventarioJornadaResponse criar(Long usuarioId, InventarioJornadaRequest request) {
        validarRequest(request);
        
        InventarioJornada inventario = new InventarioJornada();
        inventario.setUsuarioId(usuarioId);
        inventario.setNome(request.getNome());
        inventario.setTipoRebanho(request.getTipoRebanho());
        inventario.setStatus(StatusInventario.RASCUNHO);
        inventario.setFaseAtual(1); // Inicia na fase 1 (Rebanho)
        inventario.setFaseRebanhoConcluida(false);
        inventario.setFaseProducaoAgricolaConcluida(false);
        inventario.setFaseMutConcluida(false);
        inventario.setFaseEnergiaConcluida(false);
        inventario.setDataCriacao(LocalDateTime.now());
        inventario.setDataAtualizacao(LocalDateTime.now());
        
        InventarioJornada salvo = repository.save(inventario);
        return converterParaResponse(salvo);
    }

    public InventarioJornadaResponse atualizar(Long id, Long usuarioId, InventarioJornadaRequest request) {
        validarRequest(request);
        
        InventarioJornada inventario = repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("InventarioJornada", id));
        
        inventario.setNome(request.getNome());
        inventario.setTipoRebanho(request.getTipoRebanho());
        inventario.setStatus(request.getStatus());
        inventario.setFaseAtual(request.getFaseAtual());
        inventario.setFaseRebanhoConcluida(request.getFaseRebanhoConcluida());
        inventario.setFaseProducaoAgricolaConcluida(request.getFaseProducaoAgricolaConcluida());
        inventario.setFaseMutConcluida(request.getFaseMutConcluida());
        inventario.setFaseEnergiaConcluida(request.getFaseEnergiaConcluida());
        inventario.setDataAtualizacao(LocalDateTime.now());
        
        InventarioJornada salvo = repository.save(inventario);
        return converterParaResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<InventarioJornadaResponse> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventarioJornadaResponse> listarPorUsuarioEStatus(Long usuarioId, StatusInventario status) {
        return repository.findByUsuarioIdAndStatusOrderByDataCriacaoDesc(usuarioId, status)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventarioJornadaResponse buscarPorId(Long id) {
        InventarioJornada inventario = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("InventarioJornada", id));
        return converterParaResponse(inventario);
    }

    public void deletar(Long id, Long usuarioId) {
        InventarioJornada inventario = repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("InventarioJornada", id));
        repository.delete(inventario);
    }

    public InventarioJornadaResponse avancarFase(Long id, Long usuarioId) {
        InventarioJornada inventario = repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("InventarioJornada", id));
        
        if (inventario.getFaseAtual() < 4) {
            inventario.setFaseAtual(inventario.getFaseAtual() + 1);
            inventario.setDataAtualizacao(LocalDateTime.now());
            
            InventarioJornada salvo = repository.save(inventario);
            return converterParaResponse(salvo);
        }
        
        throw new ValidacaoException("Inventário já está na última fase");
    }

    public InventarioJornadaResponse marcarFaseConcluida(Long id, Long usuarioId, int fase) {
        InventarioJornada inventario = repository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("InventarioJornada", id));
        
        switch (fase) {
            case 1:
                inventario.setFaseRebanhoConcluida(true);
                break;
            case 2:
                inventario.setFaseProducaoAgricolaConcluida(true);
                break;
            case 3:
                inventario.setFaseMutConcluida(true);
                break;
            case 4:
                inventario.setFaseEnergiaConcluida(true);
                break;
            default:
                throw new ValidacaoException("Fase inválida: " + fase);
        }
        
        inventario.setDataAtualizacao(LocalDateTime.now());
        
        // Se todas as fases estão concluídas, marcar como concluído
        if (inventario.getFaseRebanhoConcluida() && 
            inventario.getFaseProducaoAgricolaConcluida() && 
            inventario.getFaseMutConcluida() && 
            inventario.getFaseEnergiaConcluida()) {
            inventario.setStatus(StatusInventario.CONCLUIDO);
        }
        
        InventarioJornada salvo = repository.save(inventario);
        return converterParaResponse(salvo);
    }

    private void validarRequest(InventarioJornadaRequest request) {
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do inventário é obrigatório");
        }
        
        if (request.getTipoRebanho() == null) {
            throw new ValidacaoException("Tipo de rebanho é obrigatório");
        }
    }

    private InventarioJornadaResponse converterParaResponse(InventarioJornada inventario) {
        InventarioJornadaResponse response = new InventarioJornadaResponse();
        response.setId(inventario.getId());
        response.setUsuarioId(inventario.getUsuarioId());
        response.setNome(inventario.getNome());
        response.setTipoRebanho(inventario.getTipoRebanho());
        response.setStatus(inventario.getStatus());
        response.setFaseAtual(inventario.getFaseAtual());
        response.setFaseRebanhoConcluida(inventario.getFaseRebanhoConcluida());
        response.setFaseProducaoAgricolaConcluida(inventario.getFaseProducaoAgricolaConcluida());
        response.setFaseMutConcluida(inventario.getFaseMutConcluida());
        response.setFaseEnergiaConcluida(inventario.getFaseEnergiaConcluida());
        response.setDataCriacao(inventario.getDataCriacao());
        response.setDataAtualizacao(inventario.getDataAtualizacao());
        // Lotes serão carregados separadamente quando necessário
        return response;
    }
}