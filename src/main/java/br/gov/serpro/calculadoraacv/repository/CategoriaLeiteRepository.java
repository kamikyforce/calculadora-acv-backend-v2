package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.CategoriaLeite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaLeiteRepository extends JpaRepository<CategoriaLeite, Long> {
}