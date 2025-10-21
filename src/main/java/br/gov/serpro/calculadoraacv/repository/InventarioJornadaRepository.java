package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.InventarioJornada;
import br.gov.serpro.calculadoraacv.model.InventarioJornada.StatusInventario;
import br.gov.serpro.calculadoraacv.model.InventarioJornada.TipoRebanho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventarioJornadaRepository extends JpaRepository<InventarioJornada, Long> {
    
    List<InventarioJornada> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
    
    List<InventarioJornada> findByUsuarioIdAndStatusOrderByDataCriacaoDesc(Long usuarioId, StatusInventario status);
    
    List<InventarioJornada> findByUsuarioIdAndTipoRebanhoOrderByDataCriacaoDesc(Long usuarioId, TipoRebanho tipoRebanho);
    
    @Query("SELECT i FROM InventarioJornada i WHERE i.usuarioId = :usuarioId AND i.nome ILIKE %:nome%")
    List<InventarioJornada> findByUsuarioIdAndNomeContaining(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);
    
    @Query("SELECT COUNT(i) FROM InventarioJornada i WHERE i.usuarioId = :usuarioId AND i.status = :status")
    long countByUsuarioIdAndStatus(@Param("usuarioId") Long usuarioId, @Param("status") StatusInventario status);
    
    Optional<InventarioJornada> findByIdAndUsuarioId(Long id, Long usuarioId);
}