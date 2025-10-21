package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.BancoFatoresEnergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BancoFatoresEnergiaRepository extends JpaRepository<BancoFatoresEnergia, Long> {
    
    List<BancoFatoresEnergia> findByAtivoTrueOrderByAno();
    
    Optional<BancoFatoresEnergia> findByAnoAndAtivoTrue(Integer ano);
    
    @Query("SELECT b FROM BancoFatoresEnergia b WHERE b.fatorMedioAnual IS NOT NULL AND b.ativo = true ORDER BY b.ano")
    List<BancoFatoresEnergia> findByFatorMedioAnualNotNullAndAtivoTrue();
    
    @Query("SELECT DISTINCT b.ano FROM BancoFatoresEnergia b WHERE b.ativo = true ORDER BY b.ano DESC")
    List<Integer> findDistinctAnosDisponiveis();
}