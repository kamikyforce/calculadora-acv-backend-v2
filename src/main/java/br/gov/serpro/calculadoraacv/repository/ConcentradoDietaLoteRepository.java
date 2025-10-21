package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.ConcentradoDietaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcentradoDietaLoteRepository extends JpaRepository<ConcentradoDietaLote, Long> {
    List<ConcentradoDietaLote> findByNutricaoLoteId(Long nutricaoLoteId);
    void deleteByNutricaoLoteId(Long nutricaoLoteId);
}