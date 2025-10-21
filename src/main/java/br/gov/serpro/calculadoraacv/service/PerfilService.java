package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.model.Perfil;
import br.gov.serpro.calculadoraacv.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfilService {

    private final PerfilRepository repository;

    @Transactional(readOnly = true)
    public Perfil buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Perfil", id));
    }

    @Transactional(readOnly = true)
    public List<Perfil> listar() {
        return repository.findAll();
    }
}