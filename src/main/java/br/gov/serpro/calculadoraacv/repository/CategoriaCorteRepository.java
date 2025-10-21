package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.CategoriaCorte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaCorteRepository extends JpaRepository<CategoriaCorte, Long> {
    List<CategoriaCorte> findAllByOrderByCategoriaAscIdadeAsc();
}