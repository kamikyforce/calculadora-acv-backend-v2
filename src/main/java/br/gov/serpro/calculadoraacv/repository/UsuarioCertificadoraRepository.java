package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.UsuarioCertificadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioCertificadoraRepository extends JpaRepository<UsuarioCertificadora, Long> {

    @Query("SELECT uc FROM UsuarioCertificadora uc WHERE uc.usuario.id = :usuarioId")
    List<UsuarioCertificadora> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT uc FROM UsuarioCertificadora uc WHERE uc.certificadora.id = :certificadoraId")
    List<UsuarioCertificadora> findByCertificadoraId(@Param("certificadoraId") Long certificadoraId);

    @Query("SELECT uc FROM UsuarioCertificadora uc WHERE uc.usuario.id = :usuarioId AND uc.certificadora.id = :certificadoraId")
    Optional<UsuarioCertificadora> findByUsuarioIdAndCertificadoraId(@Param("usuarioId") Long usuarioId,
                                                                     @Param("certificadoraId") Long certificadoraId);

    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END FROM UsuarioCertificadora uc WHERE uc.usuario.cpf = :cpf")
    boolean existsByUsuarioCpf(@Param("cpf") String cpf);

    @Query("SELECT uc FROM UsuarioCertificadora uc WHERE uc.usuario.cpf = :cpf AND uc.ativo = :ativo")
    List<UsuarioCertificadora> findByUsuarioCpfAndAtivo(@Param("cpf") String cpf, @Param("ativo") boolean ativo);

    @Query("SELECT uc FROM UsuarioCertificadora uc WHERE uc.usuario.cpf = :cpf AND uc.certificadora.id = :certificadoraId")
    List<UsuarioCertificadora> findByUsuarioCpfAndCertificadoraId(@Param("cpf") String cpf, @Param("certificadoraId") Long certificadoraId);
}