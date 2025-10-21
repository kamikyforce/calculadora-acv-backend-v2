package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.CategoriaLote;
import br.gov.serpro.calculadoraacv.repository.CategoriaLoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaLoteService {

    private final CategoriaLoteRepository repository;

    public CategoriaLoteService(CategoriaLoteRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaLote> listarPorLote(Long loteId) {
        return repository.findByLoteId(loteId);
    }

    public Optional<CategoriaLote> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public CategoriaLote criar(CategoriaLote categoria) {
        return repository.save(categoria);
    }

    public Optional<CategoriaLote> atualizar(Long id, CategoriaLote payload) {
        return repository.findById(id).map(existing -> {
            existing.setLoteId(payload.getLoteId());
            existing.setCategoriaCorteId(payload.getCategoriaCorteId());
            existing.setCategoriaLeiteId(payload.getCategoriaLeiteId());
            existing.setAnimaisFazenda(payload.getAnimaisFazenda());
            existing.setPesoMedioVivo(payload.getPesoMedioVivo());
            existing.setAnimaisComprados(payload.getAnimaisComprados());
            existing.setPesoMedioComprados(payload.getPesoMedioComprados());
            existing.setAnimaisVendidos(payload.getAnimaisVendidos());
            existing.setPesoMedioVendidos(payload.getPesoMedioVendidos());
            existing.setPermanenciaMeses(payload.getPermanenciaMeses());
            existing.setIdadeDesmame(payload.getIdadeDesmame());
            existing.setFemeasPrenhasPercentual(payload.getFemeasPrenhasPercentual());
            existing.setProducaoLeiteAno(payload.getProducaoLeiteAno());
            existing.setTeorGorduraLeite(payload.getTeorGorduraLeite());
            existing.setTeorProteinaLeite(payload.getTeorProteinaLeite());
            return repository.save(existing);
        });
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}