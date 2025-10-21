package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.AditivoDietaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AditivoDietaLoteRepository extends JpaRepository<AditivoDietaLote, Long> {
    List<AditivoDietaLote> findByNutricaoLoteId(Long nutricaoLoteId);
    void deleteByNutricaoLoteId(Long nutricaoLoteId);
}