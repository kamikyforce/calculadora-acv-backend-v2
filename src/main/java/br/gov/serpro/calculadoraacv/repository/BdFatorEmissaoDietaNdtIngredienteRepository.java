package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.BdFatorEmissaoDietaNdtIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdFatorEmissaoDietaNdtIngredienteRepository extends JpaRepository<BdFatorEmissaoDietaNdtIngrediente, Long> {
    
    List<BdFatorEmissaoDietaNdtIngrediente> findByTipoIngrediente(String tipoIngrediente);
    
    List<BdFatorEmissaoDietaNdtIngrediente> findByNomeIngredienteContainingIgnoreCase(String nomeIngrediente);
    
    List<BdFatorEmissaoDietaNdtIngrediente> findByFatoresEmissoesCalculados(String fatoresEmissoesCalculados);
    
    @Query("SELECT DISTINCT f.tipoIngrediente FROM BdFatorEmissaoDietaNdtIngrediente f ORDER BY f.tipoIngrediente")
    List<String> findDistinctTipoIngrediente();
    
    @Query("SELECT f FROM BdFatorEmissaoDietaNdtIngrediente f WHERE f.tipoIngrediente = :tipo AND f.nomeIngrediente LIKE %:nome% ORDER BY f.nomeIngrediente")
    List<BdFatorEmissaoDietaNdtIngrediente> findByTipoAndNomeContaining(@Param("tipo") String tipo, @Param("nome") String nome);
    
    @Query("SELECT f FROM BdFatorEmissaoDietaNdtIngrediente f WHERE f.fatoresEmissoesCalculados = 'Sim' ORDER BY f.nomeIngrediente")
    List<BdFatorEmissaoDietaNdtIngrediente> findByFatoresCalculados();
}