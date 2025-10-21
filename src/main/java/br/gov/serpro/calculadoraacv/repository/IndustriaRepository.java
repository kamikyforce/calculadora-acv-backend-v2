package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.Industria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IndustriaRepository extends JpaRepository<Industria, Long> {

    Optional<Industria> findByCnpj(String cnpj);

    @Query("SELECT i FROM Industria i WHERE " +
            "(:estado IS NULL OR i.estado = :estado) AND " +
            "(:ativo IS NULL OR i.ativo = :ativo)")
    List<Industria> findWithFilters(@Param("estado") String estado,
                                    @Param("ativo") Boolean ativo);

    @Query("SELECT i FROM Industria i WHERE i.nome ILIKE %:nome%")
    List<Industria> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT COUNT(i) FROM Industria i WHERE i.ativo = true")
    long countAtivas();

    @Query("SELECT i FROM Industria i WHERE i.inventariosTratados > :quantidade")
    List<Industria> findByInventariosTratadosGreaterThan(@Param("quantidade") int quantidade);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Industria i WHERE i.cnpj = :cnpj")
    boolean existsByCnpj(@Param("cnpj") String cnpj);
}