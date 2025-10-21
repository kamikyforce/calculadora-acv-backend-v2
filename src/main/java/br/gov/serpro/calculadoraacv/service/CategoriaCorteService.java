package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.CategoriaCorte;
import br.gov.serpro.calculadoraacv.repository.CategoriaCorteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaCorteService {

    private final CategoriaCorteRepository repository;

    public List<CategoriaCorte> listarTodas() {
        return repository.findAllByOrderByCategoriaAscIdadeAsc();
    }
}