package br.gov.serpro.calculadoraacv.repository;


import br.gov.serpro.calculadoraacv.model.InsumoProducaoAgricola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoProducaoAgricolaRepository extends JpaRepository<InsumoProducaoAgricola, Long> {
    
    List<InsumoProducaoAgricola> findByAtivoTrue();
    
    List<InsumoProducaoAgricola> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    @Query("SELECT i FROM InsumoProducaoAgricola i WHERE i.usuarioId = :usuarioId AND i.nomeProduto LIKE %:produto% AND i.ativo = true")
    List<InsumoProducaoAgricola> findByUsuarioIdAndNomeProdutoContainingAndAtivoTrue(@Param("usuarioId") Long usuarioId, @Param("produto") String produto);
    
    @Query("SELECT COUNT(i) FROM InsumoProducaoAgricola i WHERE i.usuarioId = :usuarioId AND i.ativo = true")
    long countByUsuarioIdAndAtivoTrue(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoProducaoAgricola i WHERE i.nomeProduto = :nomeProduto AND i.usuarioId = :usuarioId")
    boolean existsByNomeProdutoAndUsuarioId(@Param("nomeProduto") String nomeProduto, @Param("usuarioId") Long usuarioId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoProducaoAgricola i WHERE " +
           "UPPER(TRANSLATE(i.nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "i.usuarioId = :usuarioId AND i.ativo = true")
    boolean existsByNomeProdutoIgnoreCaseAndAccentsAndUsuarioId(@Param("nomeProduto") String nomeProduto, @Param("usuarioId") Long usuarioId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoProducaoAgricola i WHERE " +
           "UPPER(TRANSLATE(i.nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "i.usuarioId = :usuarioId AND i.id != :id AND i.ativo = true")
    boolean existsByNomeProdutoIgnoreCaseAndAccentsAndUsuarioIdExcludingId(@Param("nomeProduto") String nomeProduto, @Param("usuarioId") Long usuarioId, @Param("id") Long id);
    
    // Métodos para verificar nome e classe juntos
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoProducaoAgricola i WHERE " +
           "UPPER(TRANSLATE(i.nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) " +
           "AND i.classe = :classe AND i.usuarioId = :usuarioId AND i.ativo = true")
    boolean existsByNomeProdutoAndClasseAndUsuarioId(@Param("nomeProduto") String nomeProduto, @Param("classe") String classe, @Param("usuarioId") Long usuarioId);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoProducaoAgricola i WHERE " +
           "UPPER(TRANSLATE(i.nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:nomeProduto, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) " +
           "AND i.classe = :classe AND i.usuarioId = :usuarioId AND i.id != :id AND i.ativo = true")
    boolean existsByNomeProdutoAndClasseAndUsuarioIdExcludingId(@Param("nomeProduto") String nomeProduto, @Param("classe") String classe, @Param("usuarioId") Long usuarioId, @Param("id") Long id);
}