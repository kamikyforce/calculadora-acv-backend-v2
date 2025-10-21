package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.FatorEnergiaEletrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FatorEnergiaEletricaRepository extends JpaRepository<FatorEnergiaEletrica, Long> {
    
    List<FatorEnergiaEletrica> findByAtivoTrue();
    
    Optional<FatorEnergiaEletrica> findByAnoAndMesAndAtivoTrue(Integer ano, Integer mes);
    
    List<FatorEnergiaEletrica> findByAnoAndAtivoTrue(Integer ano);
    
    List<FatorEnergiaEletrica> findByTipoDadoAndAtivoTrue(String tipoDado);
    
    @Query("SELECT DISTINCT f.ano FROM FatorEnergiaEletrica f WHERE f.ativo = true ORDER BY f.ano")
    List<Integer> findDistinctAnosOrderByAno();
    
    @Query("SELECT f FROM FatorEnergiaEletrica f WHERE f.ano = :ano AND f.tipoDado = :tipoDado AND f.ativo = true ORDER BY f.mes")
    List<FatorEnergiaEletrica> findByAnoAndTipoDadoAndAtivoTrue(@Param("ano") Integer ano, @Param("tipoDado") String tipoDado);
    
    @Query("SELECT f FROM FatorEnergiaEletrica f WHERE f.ano >= :anoInicio AND f.ano <= :anoFim AND f.ativo = true ORDER BY f.ano, f.mes")
    List<FatorEnergiaEletrica> findByAnoBetweenAndAtivoTrue(@Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);
}