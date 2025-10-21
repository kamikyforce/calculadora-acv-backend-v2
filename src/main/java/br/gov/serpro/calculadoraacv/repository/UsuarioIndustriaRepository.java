package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.UsuarioIndustria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioIndustriaRepository extends JpaRepository<UsuarioIndustria, Long> {

    @Query("SELECT ui FROM UsuarioIndustria ui WHERE ui.usuario.id = :usuarioId")
    List<UsuarioIndustria> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT ui FROM UsuarioIndustria ui WHERE ui.industria.id = :industriaId")
    List<UsuarioIndustria> findByIndustriaId(@Param("industriaId") Long industriaId);

    @Query("SELECT ui FROM UsuarioIndustria ui WHERE ui.usuario.id = :usuarioId AND ui.industria.id = :industriaId")
    Optional<UsuarioIndustria> findByUsuarioIdAndIndustriaId(@Param("usuarioId") Long usuarioId,
                                                             @Param("industriaId") Long industriaId);

    @Query("SELECT CASE WHEN COUNT(ui) > 0 THEN true ELSE false END FROM UsuarioIndustria ui WHERE ui.usuario.cpf = :cpf")
    boolean existsByUsuarioCpf(@Param("cpf") String cpf);

    @Query("SELECT ui FROM UsuarioIndustria ui WHERE ui.usuario.cpf = :cpf AND ui.ativo = :ativo")
    List<UsuarioIndustria> findByUsuarioCpfAndAtivo(@Param("cpf") String cpf, @Param("ativo") boolean ativo);

    @Query("SELECT ui FROM UsuarioIndustria ui WHERE ui.usuario.cpf = :cpf AND ui.industria.id = :industriaId")
    List<UsuarioIndustria> findByUsuarioCpfAndIndustriaId(@Param("cpf") String cpf, @Param("industriaId") Long industriaId);
}