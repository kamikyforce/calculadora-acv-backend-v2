package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.IngredienteDietaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteDietaLoteRepository extends JpaRepository<IngredienteDietaLote, Long> {
    List<IngredienteDietaLote> findByNutricaoLoteId(Long nutricaoLoteId);
    void deleteByNutricaoLoteId(Long nutricaoLoteId);
}