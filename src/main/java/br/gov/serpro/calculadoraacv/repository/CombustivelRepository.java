package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.Combustivel;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;          // ⬅️ IMPORT ADICIONADO
import org.springframework.data.repository.query.Param;     // ⬅️ IMPORT ADICIONADO
import java.util.List;
import java.util.Optional;

public interface CombustivelRepository extends JpaRepository<Combustivel, Long> {
    
    List<Combustivel> findByAtivoTrue();
    
    List<Combustivel> findByTipoAndAtivoTrue(String tipo);
    
    Optional<Combustivel> findByNomeAndAtivoTrue(String nome);
    
    @Query("SELECT c FROM Combustivel c WHERE c.nome LIKE %:nome% AND c.ativo = true")
    List<Combustivel> findByNomeContainingAndAtivoTrue(@Param("nome") String nome);
    
    @Query("SELECT DISTINCT c.tipo FROM Combustivel c WHERE c.ativo = true ORDER BY c.tipo")
    List<String> findDistinctTipos();
    
    @Query("SELECT c FROM Combustivel c WHERE c.nome = :nome AND c.id != :id AND c.ativo = true")
    Optional<Combustivel> findByNomeAndIdNotAndAtivoTrue(@Param("nome") String nome, @Param("id") Long id);
    
    // NEW SCOPE-SPECIFIC METHODS
    List<Combustivel> findByEscopoAndAtivoTrue(EscopoEnum escopo);
    
    List<Combustivel> findByNomeAndEscopoAndAtivoTrue(String nome, EscopoEnum escopo);
    
    @Query("SELECT c FROM Combustivel c WHERE c.nome LIKE %:nome% AND c.escopo = :escopo AND c.ativo = true")
    List<Combustivel> findByNomeContainingAndEscopoAndAtivoTrue(@Param("nome") String nome, @Param("escopo") EscopoEnum escopo);
    
    @Query("SELECT c FROM Combustivel c WHERE c.nome = :nome AND c.escopo != :escopo AND c.ativo = true")
    List<Combustivel> findByNomeAndEscopoNotAndAtivoTrue(@Param("nome") String nome, @Param("escopo") EscopoEnum escopo);
    
    @Query("SELECT c FROM Combustivel c WHERE c.nome = :nome AND c.escopo = :escopo AND c.id != :id AND c.ativo = true")
    Optional<Combustivel> findByNomeAndEscopoAndIdNotAndAtivoTrue(@Param("nome") String nome, @Param("escopo") EscopoEnum escopo, @Param("id") Long id);
    
    // ⬇️ Novos métodos com filtro por usuário
    List<Combustivel> findByNomeAndEscopoAndUsuarioIdAndAtivoTrue(String nome, EscopoEnum escopo, Long usuarioId);
    List<Combustivel> findByEscopoAndUsuarioIdAndAtivoTrue(EscopoEnum escopo, Long usuarioId);
    Optional<Combustivel> findByNomeAndUsuarioIdAndAtivoTrue(String nome, Long usuarioId);
    // ... existing code ...
}