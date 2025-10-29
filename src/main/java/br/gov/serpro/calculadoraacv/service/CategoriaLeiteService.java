package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.CategoriaLeite;
import br.gov.serpro.calculadoraacv.repository.CategoriaLeiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaLeiteService {
    
    @Autowired
    private CategoriaLeiteRepository repository;
    
    public List<CategoriaLeite> listar() {
        return repository.findAll();
    }
    
    public Optional<CategoriaLeite> buscarPorId(Long id) {
        return repository.findById(id);
    }
    
    public CategoriaLeite criar(CategoriaLeite categoria) {
        return repository.save(categoria);
    }
    
    public CategoriaLeite atualizar(CategoriaLeite categoria) {
        return repository.save(categoria);
    }
    
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}