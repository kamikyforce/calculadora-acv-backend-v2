package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.UsuarioResponse;
import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUserService {
    
    private final UsuarioRepository usuarioRepository;
    
    public Usuario getOrCreateDefaultUser() {
        Optional<Usuario> existingUser = usuarioRepository.findByCpf("00000000000");
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        // Create default user
        Usuario defaultUser = new Usuario();
        defaultUser.setNome("Administrador");
        defaultUser.setEmail("admin@bndes.gov.br");
        defaultUser.setCpf("00000000000");
        defaultUser.setTipo(TipoUsuario.ADMINISTRADOR);
        defaultUser.setAtivo(true);
        
        return usuarioRepository.save(defaultUser);
    }
    
    public UsuarioResponse toUsuarioResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId().toString());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setCpf(usuario.getCpf());
        response.setTipo(usuario.getTipo());
        response.setPerfis(Arrays.asList("ADMIN"));
        return response;
    }
}