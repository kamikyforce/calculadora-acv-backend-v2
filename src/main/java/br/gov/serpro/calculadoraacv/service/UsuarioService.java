package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.PermissaoResponse;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.Administrador;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.repository.AdministradorRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final AdministradorRepository administradorRepository;

    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario);
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));
        
        // Atualizar apenas os campos permitidos
        existente.setNome(usuarioAtualizado.getNome());
        existente.setEmail(usuarioAtualizado.getEmail());
        existente.setCpf(usuarioAtualizado.getCpf());
        existente.setTipo(usuarioAtualizado.getTipo());
        
        validarUsuario(existente);
        return repository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf).orElse(null);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Usuario", id);
        }
        repository.deleteById(id);
    }

    public PermissaoResponse obterPermissoes(Usuario usuario) {
        PermissaoResponse response = new PermissaoResponse();
        response.setTipoUsuario(usuario.getTipo());

        List<String> permissoes = new ArrayList<>();
        String perfil = null;

        switch (usuario.getTipo()) {
            case ADMINISTRADOR:
                Optional<Administrador> admin = administradorRepository.findByUsuarioId(usuario.getId());
                if (admin.isPresent() && admin.get().getPerfil() != null) {
                    perfil = admin.get().getPerfil().getNome();
                    permissoes = obterPermissoesPorPerfil(perfil);
                }
                break;
            case CERTIFICADORA:
                permissoes = List.of("GERENCIAR_INVENTARIOS", "VISUALIZAR_RELATORIOS");
                break;
            case INDUSTRIA:
                permissoes = List.of("CRIAR_INVENTARIOS", "VISUALIZAR_PROPRIOS_INVENTARIOS");
                break;
        }

        response.setPermissoes(permissoes);
        response.setPerfil(perfil);
        return response;
    }

    private List<String> obterPermissoesPorPerfil(String perfil) {
        return switch (perfil) {
            case "ADMINISTRADOR_SISTEMA" -> List.of("TODAS_PERMISSOES");
            case "CURADOR" -> List.of("GERENCIAR_INVENTARIOS", "APROVAR_INVENTARIOS");
            case "ADMINISTRADOR_CERTIFICADORAS" -> List.of("GERENCIAR_CERTIFICADORAS", "VISUALIZAR_RELATORIOS");
            case "VISUALIZADOR_EMISSOES" -> List.of("VISUALIZAR_EMISSOES");
            case "VISUALIZADOR_INDUSTRIAS" -> List.of("VISUALIZAR_INDUSTRIAS");
            default -> List.of();
        };
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        if (usuario.getEmail() == null || !isValidEmail(usuario.getEmail())) {
            throw new ValidacaoException("Email válido é obrigatório");
        }
        if (usuario.getTipo() == null) {
            throw new ValidacaoException("Tipo de usuário é obrigatório");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}