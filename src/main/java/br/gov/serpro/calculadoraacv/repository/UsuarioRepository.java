package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.serpro.calculadoraacv.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    @Query("SELECT u FROM Usuario u WHERE u.tipo = :tipo AND u.ativo = true")
    List<Usuario> findByTipoAndAtivo(@Param("tipo") TipoUsuario tipo);

    @Query("SELECT u FROM Usuario u WHERE u.nome ILIKE %:nome%")
    List<Usuario> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipo = :tipo")
    long countByTipo(@Param("tipo") TipoUsuario tipo);
}
