package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    @Query("SELECT a FROM Administrador a WHERE a.usuario.id = :usuarioId")
    Optional<Administrador> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM Administrador a WHERE a.perfil.nome = :perfilNome")
    List<Administrador> findByPerfilNome(@Param("perfilNome") String perfilNome);

    @Query("SELECT a FROM Administrador a WHERE a.orgao ILIKE %:orgao%")
    List<Administrador> findByOrgaoContaining(@Param("orgao") String orgao);
}