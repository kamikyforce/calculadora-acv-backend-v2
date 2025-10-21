package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.NutricaoAnimalLote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutricaoAnimalLoteRepository extends JpaRepository<NutricaoAnimalLote, Long> {
    
    Optional<NutricaoAnimalLote> findByLoteId(Long loteId);
    
    Optional<NutricaoAnimalLote> findByIdAndLoteId(Long id, Long loteId);
    
    void deleteByLoteId(Long loteId);
}