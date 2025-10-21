package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.ManejoDejetosLote;
import br.gov.serpro.calculadoraacv.repository.ManejoDejetosLoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManejoDejetosLoteService {

    private final ManejoDejetosLoteRepository repository;

    public ManejoDejetosLoteService(ManejoDejetosLoteRepository repository) {
        this.repository = repository;
    }

    public List<ManejoDejetosLote> listarPorLote(Long loteId) {
        return repository.findByLoteId(loteId);
    }

    public Optional<ManejoDejetosLote> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ManejoDejetosLote criar(ManejoDejetosLote manejo) {
        return repository.save(manejo);
    }

    public Optional<ManejoDejetosLote> atualizar(Long id, ManejoDejetosLote payload) {
        return repository.findById(id).map(existing -> {
            existing.setLoteId(payload.getLoteId());
            existing.setCategoriaAnimal(payload.getCategoriaAnimal());
            existing.setTipoManejo(payload.getTipoManejo());
            existing.setPercentualRebanho(payload.getPercentualRebanho());
            return repository.save(existing);
        });
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<String> listarTiposManejo() {
        return repository.listarTiposManejo();
    }
}