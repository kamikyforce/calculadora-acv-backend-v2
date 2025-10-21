package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.LoteRebanhoRequest;
import br.gov.serpro.calculadoraacv.dto.LoteRebanhoResponse;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.LoteRebanho;
import br.gov.serpro.calculadoraacv.repository.InventarioJornadaRepository;
import br.gov.serpro.calculadoraacv.repository.LoteRebanhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoteRebanhoService {

    private final LoteRebanhoRepository repository;
    private final InventarioJornadaRepository inventarioRepository;

    public LoteRebanhoResponse criar(LoteRebanhoRequest request) {
        validarRequest(request);
        validarInventarioExiste(request.getInventarioId());
        
        LoteRebanho lote = new LoteRebanho();
        lote.setInventarioId(request.getInventarioId());
        lote.setNome(request.getNome());
        lote.setOrdem(obterProximaOrdem(request.getInventarioId()));
        lote.setDataCriacao(LocalDateTime.now());
        lote.setDataAtualizacao(LocalDateTime.now());
        
        LoteRebanho salvo = repository.save(lote);
        return converterParaResponse(salvo);
    }

    public LoteRebanhoResponse atualizar(Long id, LoteRebanhoRequest request) {
        validarRequest(request);
        
        LoteRebanho lote = repository.findByIdAndInventarioId(id, request.getInventarioId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("LoteRebanho", id));
        
        lote.setNome(request.getNome());
        lote.setOrdem(request.getOrdem());
        lote.setDataAtualizacao(LocalDateTime.now());
        
        LoteRebanho salvo = repository.save(lote);
        return converterParaResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<LoteRebanhoResponse> listarPorInventario(Long inventarioId) {
        return repository.findByInventarioIdOrderByOrdem(inventarioId)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LoteRebanhoResponse buscarPorId(Long id, Long inventarioId) {
        LoteRebanho lote = repository.findByIdAndInventarioId(id, inventarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("LoteRebanho", id));
        return converterParaResponse(lote);
    }

    public void deletar(Long id, Long inventarioId) {
        LoteRebanho lote = repository.findByIdAndInventarioId(id, inventarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("LoteRebanho", id));
        repository.delete(lote);
    }

    public void deletarPorId(Long id) {
        LoteRebanho lote = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("LoteRebanho", id));
        repository.delete(lote);
    }

    public void deletarTodosPorInventario(Long inventarioId) {
        if (!inventarioRepository.existsById(inventarioId)) {
            throw new EntidadeNaoEncontradaException("InventarioJornada", inventarioId);
        }
        repository.deleteByInventarioId(inventarioId);
    }

    public void reordenarLotes(Long inventarioId, List<Long> idsOrdenados) {
        List<LoteRebanho> lotes = repository.findByInventarioIdOrderByOrdem(inventarioId);
        
        for (int i = 0; i < idsOrdenados.size(); i++) {
            Long loteId = idsOrdenados.get(i);
            LoteRebanho lote = lotes.stream()
                    .filter(l -> l.getId().equals(loteId))
                    .findFirst()
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("LoteRebanho", loteId));
            
            lote.setOrdem(i + 1);
            lote.setDataAtualizacao(LocalDateTime.now());
            repository.save(lote);
        }
    }

    private void validarRequest(LoteRebanhoRequest request) {
        if (request.getInventarioId() == null) {
            throw new ValidacaoException("ID do inventário é obrigatório");
        }
        
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do lote é obrigatório");
        }
    }

    private void validarInventarioExiste(Long inventarioId) {
        if (!inventarioRepository.existsById(inventarioId)) {
            throw new EntidadeNaoEncontradaException("InventarioJornada", inventarioId);
        }
    }

    private Integer obterProximaOrdem(Long inventarioId) {
        Integer maxOrdem = repository.findMaxOrdemByInventarioId(inventarioId);
        return maxOrdem != null ? maxOrdem + 1 : 1;
    }

    private LoteRebanhoResponse converterParaResponse(LoteRebanho lote) {
        LoteRebanhoResponse response = new LoteRebanhoResponse();
        response.setId(lote.getId());
        response.setInventarioId(lote.getInventarioId());
        response.setNome(lote.getNome());
        response.setOrdem(lote.getOrdem());
        response.setDataCriacao(lote.getDataCriacao());
        response.setDataAtualizacao(lote.getDataAtualizacao());
        // Categorias, nutrição e manejo serão carregados separadamente quando necessário
        return response;
    }
}