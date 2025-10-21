package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.CertificadoraRequest;
import br.gov.serpro.calculadoraacv.dto.CertificadoraResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioCertificadoraRequest;
import br.gov.serpro.calculadoraacv.dto.UsuarioCertificadoraResponse;
import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.Certificadora;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.model.UsuarioCertificadora;
import br.gov.serpro.calculadoraacv.repository.CertificadoraRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioCertificadoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificadoraService {

    private final CertificadoraRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioCertificadoraRepository usuarioCertificadoraRepository;
    private final ValidacaoCruzadaService validacaoCruzadaService;
    private final EntityManager entityManager;

    @Transactional
    public CertificadoraResponse salvar(CertificadoraRequest request) {
        Certificadora entidade = new Certificadora();
        entidade.setNome(request.getNome());
        entidade.setCnpj(request.getCnpj());
        entidade.setEstado(request.getEstado());
        entidade.setTipo(request.getTipo());
        entidade.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        Certificadora salvo = repository.save(entidade);

        if (request.getUsuarios() != null && !request.getUsuarios().isEmpty()) {
            processarUsuariosCertificadora(salvo, request.getUsuarios());
        }

        return new CertificadoraResponse(salvo);
    }

    public List<CertificadoraResponse> listar(String estado, TipoCertificadora tipo, Boolean ativo) {
        List<Certificadora> lista;

        if (estado == null && tipo == null && ativo == null) {
            lista = repository.findAll();
        } else {
            lista = repository.findAll().stream()
                    .filter(c -> (estado == null || c.getEstado().equalsIgnoreCase(estado)))
                    .filter(c -> (tipo == null || c.getTipo() == tipo))
                    .filter(c -> (ativo == null || c.isAtivo() == ativo))
                    .collect(Collectors.toList());
        }

        return lista.stream()
                .map(c -> new CertificadoraResponse(c))
                .collect(Collectors.toList());
    }

    public CertificadoraResponse buscarPorId(Long id) {
        Certificadora entidade = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Certificadora", id));
        return new CertificadoraResponse(entidade);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public CertificadoraResponse atualizar(Long id, CertificadoraRequest request) {
        Certificadora existente = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Certificadora", id));
        existente.setNome(request.getNome());
        existente.setCnpj(request.getCnpj());
        existente.setEstado(request.getEstado());
        existente.setTipo(request.getTipo());
        existente.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        Certificadora atualizado = repository.save(existente);

        if (request.getUsuarios() != null) {
            // Deletar todos os relacionamentos existentes
            List<UsuarioCertificadora> relacionamentosExistentes = usuarioCertificadoraRepository.findByCertificadoraId(id);
            usuarioCertificadoraRepository.deleteAll(relacionamentosExistentes);
            entityManager.flush(); // Força a execução das deleções antes das inserções

            if (!request.getUsuarios().isEmpty()) {
                processarUsuariosCertificadora(atualizado, request.getUsuarios());
            }
        }

        return new CertificadoraResponse(atualizado);
    }

    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Certificadora existente = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Certificadora", id));
        existente.setAtivo(ativo);
        repository.save(existente);

        // Se a certificadora está sendo inativada, desativar apenas os relacionamentos UsuarioCertificadora
        if (!ativo) {
            List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByCertificadoraId(id);
            for (UsuarioCertificadora usuarioCertificadora : usuariosCertificadora) {
                // Desativar apenas o relacionamento UsuarioCertificadora, mantendo o usuário ativo
                usuarioCertificadora.setAtivo(false);
                usuarioCertificadoraRepository.save(usuarioCertificadora);
            }
        }
    }

    public List<UsuarioCertificadoraResponse> buscarUsuariosPorCertificadora(Long certificadoraId) {
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByCertificadoraId(certificadoraId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return usuariosCertificadora.stream()
                .map(uc -> {
                    Usuario usuario = uc.getUsuario();
                    String dataCadastroFormatada = uc.getDataCadastro() != null ?
                            uc.getDataCadastro().format(formatter) : null;
                    return new UsuarioCertificadoraResponse(
                            usuario.getCpf(),
                            usuario.getNome(),
                            dataCadastroFormatada,
                            uc.isAtivo()
                    );
                })
                .collect(Collectors.toList());
    }

    private void processarUsuariosCertificadora(Certificadora certificadora, List<UsuarioCertificadoraRequest> usuariosRequest) {
        for (UsuarioCertificadoraRequest usuarioRequest : usuariosRequest) {
            String cpfLimpo = usuarioRequest.getCpf().replaceAll("\\D", "");
            
            Usuario usuario = buscarOuCriarUsuario(usuarioRequest);

            boolean usuarioAtivo = usuarioRequest.getAtivo() != null ? usuarioRequest.getAtivo() : true;
            
            // Verificar se o usuário está inativo no administrador e impedir ativação
            if (usuarioAtivo && !usuario.isAtivo()) {
                throw new ValidacaoException("Não é possível ativar o usuário " + usuarioRequest.getCpf() + " na certificadora pois ele está inativo no administrador.");
            }

            // Verificar se já existe um relacionamento para evitar duplicatas
            Optional<UsuarioCertificadora> relacionamentoExistente =
                    usuarioCertificadoraRepository.findByUsuarioIdAndCertificadoraId(usuario.getId(), certificadora.getId());

            if (relacionamentoExistente.isPresent()) {
                // Atualizar o relacionamento existente
                UsuarioCertificadora usuarioCertificadora = relacionamentoExistente.get();
                usuarioCertificadora.setAtivo(usuarioAtivo);
                usuarioCertificadoraRepository.save(usuarioCertificadora);
            } else {
                // Criar novo relacionamento
                UsuarioCertificadora usuarioCertificadora = new UsuarioCertificadora();
                usuarioCertificadora.setCertificadora(certificadora);
                usuarioCertificadora.setUsuario(usuario);
                usuarioCertificadora.setAtivo(usuarioAtivo);
                usuarioCertificadoraRepository.save(usuarioCertificadora);
            }
        }
    }

    private Usuario buscarOuCriarUsuario(UsuarioCertificadoraRequest usuarioRequest) {
        String cpfLimpo = usuarioRequest.getCpf().replaceAll("\\D", "");

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCpf(cpfLimpo);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            // Não alterar o status do usuário principal, apenas retornar o usuário existente
            // O status será controlado apenas no relacionamento UsuarioCertificadora
            return usuario;
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setCpf(cpfLimpo);
        novoUsuario.setNome(usuarioRequest.getNome());
        novoUsuario.setEmail(gerarEmailTemporario(cpfLimpo));
        novoUsuario.setTipo(TipoUsuario.CERTIFICADORA);
        novoUsuario.setAtivo(usuarioRequest.getAtivo() != null ? usuarioRequest.getAtivo() : true);

        return usuarioRepository.save(novoUsuario);
    }

    private String gerarEmailTemporario(String cpf) {
        return "usuario_" + cpf + "@certificadora.temp";
    }

    public boolean verificarCnpjExiste(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        return repository.findByCnpj(cnpjLimpo).isPresent();
    }

    public boolean verificarCpfExisteEmOutraCertificadora(String cpf, Long certificadoraIdAtual) {
        String cpfLimpo = cpf.replaceAll("\\D", "");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpfLimpo);

        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        // Buscar apenas relacionamentos ativos
        List<UsuarioCertificadora> relacoesAtivas = usuarioCertificadoraRepository.findByUsuarioId(usuario.getId())
                .stream()
                .filter(UsuarioCertificadora::isAtivo)
                .collect(Collectors.toList());

        if (certificadoraIdAtual == null) {
            return !relacoesAtivas.isEmpty();
        }

        return relacoesAtivas.stream()
                .anyMatch(uc -> !uc.getCertificadora().getId().equals(certificadoraIdAtual));
    }

    public boolean verificarCnpjExisteEmIndustria(String cnpj) {
        return validacaoCruzadaService.verificarCnpjExisteEmIndustria(cnpj);
    }

    public boolean verificarCpfExisteEmIndustria(String cpf) {
        return validacaoCruzadaService.verificarCpfExisteEmIndustria(cpf);
    }

    public static class UsuarioAtivoInfo {
        private String tipo;
        private String nome;
        private Long id;

        public UsuarioAtivoInfo(String tipo, String nome, Long id) {
            this.tipo = tipo;
            this.nome = nome;
            this.id = id;
        }

        public String getTipo() { return tipo; }
        public String getNome() { return nome; }
        public Long getId() { return id; }
    }

    public UsuarioAtivoInfo verificarUsuarioAtivoEmOutroLocal(String cpf, Long certificadoraIdAtual) {
        String cpfLimpo = cpf.replaceAll("\\D", "");

        // Verificar se está ativo em outra certificadora
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByUsuarioCpfAndAtivo(cpfLimpo, true);
        for (UsuarioCertificadora uc : usuariosCertificadora) {
            // Se certificadoraIdAtual é null (criação), qualquer certificadora ativa é um conflito
            // Se certificadoraIdAtual não é null (edição), apenas outras certificadoras são conflito
            if (certificadoraIdAtual == null || !uc.getCertificadora().getId().equals(certificadoraIdAtual)) {
                return new UsuarioAtivoInfo("certificadora", uc.getCertificadora().getNome(), uc.getCertificadora().getId());
            }
        }

        // Verificar se está ativo em indústria
        String nomeIndustria = validacaoCruzadaService.buscarNomeIndustriaPorCpf(cpfLimpo);
        if (nomeIndustria != null) {
            return new UsuarioAtivoInfo("industria", nomeIndustria, null);
        }

        return null;
    }
}