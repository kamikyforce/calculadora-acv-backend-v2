package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.CalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalculoRegistradoRepository extends JpaRepository<CalculoRegistrado, Long> {
    
    // Buscar todos os cálculos ativos
    List<CalculoRegistrado> findByAtivoTrue();
    
    // Buscar cálculos ativos com paginação
    Page<CalculoRegistrado> findByAtivoTrue(Pageable pageable);
    
    // Buscar por CAR
    List<CalculoRegistrado> findByCarAndAtivoTrue(String car);
    
    // Buscar por fazenda
    List<CalculoRegistrado> findByFazendaContainingIgnoreCaseAndAtivoTrue(String fazenda);
    
    // Buscar por status
    List<CalculoRegistrado> findByStatusAndAtivoTrue(StatusCalculoRegistrado status);
    
    // Buscar por certificação
    List<CalculoRegistrado> findByCertificacaoAndAtivoTrue(TipoCertificacao certificacao);
    
    // Buscar por estado
    List<CalculoRegistrado> findByEstadoAndAtivoTrue(String estado);
    
    // Buscar por ano
    List<CalculoRegistrado> findByAnoAndAtivoTrue(Integer ano);
    
    // Buscar por usuário
    List<CalculoRegistrado> findByUsuarioIdAndAtivoTrue(Long usuarioId);
    
    // Buscar por múltiplos filtros
    @Query("SELECT c FROM CalculoRegistrado c WHERE " +
           "(:car IS NULL OR c.car = :car) AND " +
           "(:fazenda IS NULL OR LOWER(c.fazenda) LIKE LOWER(CONCAT('%', :fazenda, '%'))) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:certificacao IS NULL OR c.certificacao = :certificacao) AND " +
           "(:estado IS NULL OR c.estado = :estado) AND " +
           "(:ano IS NULL OR c.ano = :ano) AND " +
           "c.ativo = true " +
           "ORDER BY c.dataAtualizacao DESC")
    Page<CalculoRegistrado> findByFiltros(
        @Param("car") String car,
        @Param("fazenda") String fazenda,
        @Param("status") StatusCalculoRegistrado status,
        @Param("certificacao") TipoCertificacao certificacao,
        @Param("estado") String estado,
        @Param("ano") Integer ano,
        Pageable pageable
    );
    
    // Verificar se CAR já existe
    @Query("SELECT c FROM CalculoRegistrado c WHERE c.car = :car AND c.id != :id AND c.ativo = true")
    Optional<CalculoRegistrado> findByCarAndIdNotAndAtivoTrue(@Param("car") String car, @Param("id") Long id);
    
    // Buscar cálculo ativo por ID
    Optional<CalculoRegistrado> findByIdAndAtivoTrue(Long id);
    
    // Contar por status
    @Query("SELECT COUNT(c) FROM CalculoRegistrado c WHERE c.status = :status AND c.ativo = true")
    Long countByStatusAndAtivoTrue(@Param("status") StatusCalculoRegistrado status);
    
    // Contar por certificação
    @Query("SELECT COUNT(c) FROM CalculoRegistrado c WHERE c.certificacao = :certificacao AND c.ativo = true")
    Long countByCertificacaoAndAtivoTrue(@Param("certificacao") TipoCertificacao certificacao);
    
    // Verificar se fazenda já existe para o usuário (ignorando case e acentos)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CalculoRegistrado c WHERE " +
           "UPPER(TRANSLATE(c.fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "c.usuarioId = :usuarioId AND c.ativo = true")
    boolean existsByFazendaIgnoreCaseAndAccentsAndUsuarioId(@Param("fazenda") String fazenda, @Param("usuarioId") Long usuarioId);
    
    // Verificar se fazenda já existe para o usuário excluindo um registro específico (para edição)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CalculoRegistrado c WHERE " +
           "UPPER(TRANSLATE(c.fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "c.usuarioId = :usuarioId AND c.id != :id AND c.ativo = true")
    boolean existsByFazendaIgnoreCaseAndAccentsAndUsuarioIdExcludingId(@Param("fazenda") String fazenda, @Param("usuarioId") Long usuarioId, @Param("id") Long id);
    
    // Verificar se fazenda + ano já existe para o usuário (ignorando case e acentos)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CalculoRegistrado c WHERE " +
           "UPPER(TRANSLATE(c.fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "c.ano = :ano AND c.usuarioId = :usuarioId AND c.ativo = true")
    boolean existsByFazendaAndAnoIgnoreCaseAndAccentsAndUsuarioId(@Param("fazenda") String fazenda, @Param("ano") Integer ano, @Param("usuarioId") Long usuarioId);
    
    // Verificar se fazenda + ano já existe para o usuário excluindo um registro específico (para edição)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CalculoRegistrado c WHERE " +
           "UPPER(TRANSLATE(c.fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) = " +
           "UPPER(TRANSLATE(:fazenda, 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ', 'AAAAAAaaaaaaOOOOOOooooooEEEEeeeeeCcIIIIiiiiUUUUuuuuyNn')) AND " +
           "c.ano = :ano AND c.usuarioId = :usuarioId AND c.id != :id AND c.ativo = true")
    boolean existsByFazendaAndAnoIgnoreCaseAndAccentsAndUsuarioIdExcludingId(@Param("fazenda") String fazenda, @Param("ano") Integer ano, @Param("usuarioId") Long usuarioId, @Param("id") Long id);
}