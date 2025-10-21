package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(String nome);

    @Query("SELECT p FROM Perfil p ORDER BY p.nome")
    List<Perfil> findAllOrderByNome();
}