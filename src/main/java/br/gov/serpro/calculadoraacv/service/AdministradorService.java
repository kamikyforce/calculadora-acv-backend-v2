package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.AdministradorRequest;
import br.gov.serpro.calculadoraacv.dto.AdministradorResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioResponse;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.Administrador;
import br.gov.serpro.calculadoraacv.model.Perfil;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.model.UsuarioIndustria;
import br.gov.serpro.calculadoraacv.model.UsuarioCertificadora;
import br.gov.serpro.calculadoraacv.repository.AdministradorRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioIndustriaRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioCertificadoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdministradorService {

    private final AdministradorRepository repository;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final ValidacaoCruzadaService validacaoCruzadaService;
    private final UsuarioIndustriaRepository usuarioIndustriaRepository;
    private final UsuarioCertificadoraRepository usuarioCertificadoraRepository;
    
    public void alterarStatus(Long id, boolean ativo) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Administrador", id));
        
        Usuario usuario = admin.getUsuario();
        usuario.setAtivo(ativo);
        usuarioService.salvar(usuario);
        
        // Se o usuário está sendo inativado, inativar também em indústrias e certificadoras
        if (!ativo) {
            sincronizarInativacaoUsuario(usuario.getCpf());
        }
    }
    
    private void sincronizarInativacaoUsuario(String cpf) {
        // Inativar usuário em todas as indústrias
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByUsuarioCpfAndAtivo(cpf, true);
        for (UsuarioIndustria usuarioIndustria : usuariosIndustria) {
            usuarioIndustria.setAtivo(false);
            usuarioIndustriaRepository.save(usuarioIndustria);
        }
        
        // Inativar usuário em todas as certificadoras
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByUsuarioCpfAndAtivo(cpf, true);
        for (UsuarioCertificadora usuarioCertificadora : usuariosCertificadora) {
            usuarioCertificadora.setAtivo(false);
            usuarioCertificadoraRepository.save(usuarioCertificadora);
        }
    }

    public AdministradorResponse salvar(AdministradorRequest request) {
        validarRequest(request);

        Usuario usuario = usuarioService.buscarPorId(request.getUsuarioId());
        Perfil perfil = null;
        if (request.getPerfilId() != null) {
            perfil = perfilService.buscarPorId(request.getPerfilId());
        }

        Administrador admin = new Administrador();
        admin.setUsuario(usuario);
        admin.setOrgao(request.getOrgao());
        admin.setPerfil(perfil);

        Administrador salvo = repository.save(admin);
        return convertToResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponse> listar() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdministradorResponse buscarPorId(Long id) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Administrador", id));
        return convertToResponse(admin);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Administrador", id);
        }
        repository.deleteById(id);
    }

    public AdministradorResponse atualizar(Long id, AdministradorRequest request) {
        validarRequest(request);
        Administrador existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Administrador", id));

        Usuario usuario = usuarioService.buscarPorId(request.getUsuarioId());
        Perfil perfil = null;
        if (request.getPerfilId() != null) {
            perfil = perfilService.buscarPorId(request.getPerfilId());
        }

        existente.setUsuario(usuario);
        existente.setOrgao(request.getOrgao());
        existente.setPerfil(perfil);

        Administrador atualizado = repository.save(existente);
        return convertToResponse(atualizado);
    }

    private void validarRequest(AdministradorRequest request) {
        if (request.getUsuarioId() == null) {
            throw new ValidacaoException("Usuario é obrigatório");
        }
        if (request.getOrgao() == null || request.getOrgao().trim().isEmpty()) {
            throw new ValidacaoException("Orgão é obrigatório");
        }
    }

    private AdministradorResponse convertToResponse(Administrador admin) {
        AdministradorResponse response = new AdministradorResponse();
        response.setId(admin.getId());
        response.setOrgao(admin.getOrgao());
        response.setUsuario(convertUsuario(admin.getUsuario()));
        if (admin.getPerfil() != null) {
            response.setPerfil(admin.getPerfil().getNome());
        }
        return response;
    }

    private UsuarioResponse convertUsuario(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId().toString());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setCpf(usuario.getCpf());
        response.setTipo(usuario.getTipo());
        response.setAtivo(usuario.isAtivo());
        if (usuario.getDataCadastro() != null) {
            response.setDataCadastro(usuario.getDataCadastro().toString());
        }
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> verificarCpfVinculado(String cpf) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> entidades = new ArrayList<>();
        
        // Verificar se está vinculado a certificadoras
        if (validacaoCruzadaService.verificarCpfExisteEmCertificadora(cpfLimpo)) {
            String nomeCertificadora = validacaoCruzadaService.buscarNomeCertificadoraPorCpf(cpfLimpo);
            Long idCertificadora = validacaoCruzadaService.buscarIdCertificadoraPorCpf(cpfLimpo);
            if (nomeCertificadora != null) {
                Map<String, Object> entidade = new HashMap<>();
                entidade.put("tipo", "Certificadora");
                entidade.put("nome", nomeCertificadora);
                entidade.put("id", idCertificadora);
                entidades.add(entidade);
            }
        }
        
        // Verificar se está vinculado a indústrias
        if (validacaoCruzadaService.verificarCpfExisteEmIndustria(cpfLimpo)) {
            String nomeIndustria = validacaoCruzadaService.buscarNomeIndustriaPorCpf(cpfLimpo);
            Long idIndustria = validacaoCruzadaService.buscarIdIndustriaPorCpf(cpfLimpo);
            if (nomeIndustria != null) {
                Map<String, Object> entidade = new HashMap<>();
                entidade.put("tipo", "Indústria");
                entidade.put("nome", nomeIndustria);
                entidade.put("id", idIndustria);
                entidades.add(entidade);
            }
        }
        
        resultado.put("vinculado", !entidades.isEmpty());
        resultado.put("entidades", entidades);
        
        return resultado;
    }
    
    @Transactional(readOnly = true)
    public boolean verificarUsuarioInativoAdministrador(String cpf) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        // Buscar administrador pelo CPF
        List<Administrador> administradores = repository.findAll();
        return administradores.stream()
            .filter(admin -> admin.getUsuario().getCpf().equals(cpfLimpo))
            .findFirst()
            .map(admin -> !admin.getUsuario().isAtivo()) // Retorna true se o usuário está inativo
            .orElse(false); // Se não encontrou administrador, retorna false
    }
}