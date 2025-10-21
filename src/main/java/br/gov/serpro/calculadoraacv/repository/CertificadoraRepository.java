package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import br.gov.serpro.calculadoraacv.model.Certificadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertificadoraRepository extends JpaRepository<Certificadora, Long> {

    Optional<Certificadora> findByCnpj(String cnpj);

    @Query("SELECT c FROM Certificadora c WHERE " +
            "(:estado IS NULL OR c.estado = :estado) AND " +
            "(:tipo IS NULL OR c.tipo = :tipo) AND " +
            "(:ativo IS NULL OR c.ativo = :ativo)")
    List<Certificadora> findWithFilters(@Param("estado") String estado,
                                        @Param("tipo") TipoCertificadora tipo,
                                        @Param("ativo") Boolean ativo);

    @Query("SELECT c FROM Certificadora c WHERE c.nome ILIKE %:nome%")
    List<Certificadora> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT COUNT(c) FROM Certificadora c WHERE c.ativo = true")
    long countAtivas();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Certificadora c WHERE c.cnpj = :cnpj")
    boolean existsByCnpj(@Param("cnpj") String cnpj);
}