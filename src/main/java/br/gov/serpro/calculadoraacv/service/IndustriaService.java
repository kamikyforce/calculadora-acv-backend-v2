package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.IndustriaRequest;
import br.gov.serpro.calculadoraacv.dto.IndustriaResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioIndustriaRequest;
import br.gov.serpro.calculadoraacv.dto.UsuarioIndustriaResponse;
import br.gov.serpro.calculadoraacv.enums.TipoIndustria;
import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.Industria;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.model.UsuarioIndustria;
import br.gov.serpro.calculadoraacv.repository.IndustriaRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioIndustriaRepository;
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
public class IndustriaService {

    private final IndustriaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioIndustriaRepository usuarioIndustriaRepository;
    private final ValidacaoCruzadaService validacaoCruzadaService;
    private final EntityManager entityManager;

    @Transactional
    public IndustriaResponse salvar(IndustriaRequest request) {
        validarRequest(request);
        Industria entidade = new Industria();
        entidade.setNome(request.getNome());
        entidade.setCnpj(request.getCnpj());
        entidade.setEstado(request.getEstado());
        entidade.setTipo(request.getTipo() != null ? request.getTipo() : TipoIndustria.INDUSTRIA);
        entidade.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        Industria salvo = repository.save(entidade);

        if (request.getUsuarios() != null && !request.getUsuarios().isEmpty()) {
            processarUsuariosIndustria(salvo, request.getUsuarios());
        }

        return new IndustriaResponse(salvo);
    }

    public List<IndustriaResponse> listar(String estado, Boolean ativo) {
        List<Industria> lista = repository.findAll().stream()
                .filter(i -> estado == null || i.getEstado().equalsIgnoreCase(estado))
                .filter(i -> ativo == null || i.isAtivo() == ativo)
                .toList();
        return lista.stream()
                .map(IndustriaResponse::new)
                .collect(Collectors.toList());
    }

    public IndustriaResponse buscarPorId(Long id) {
        Industria industria = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Industria", id));
        return new IndustriaResponse(industria);
    }

    @Transactional
    public IndustriaResponse atualizar(Long id, IndustriaRequest request) {
        validarRequest(request);
        Industria existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Industria", id));
        existente.setNome(request.getNome());
        existente.setCnpj(request.getCnpj());
        existente.setEstado(request.getEstado());
        existente.setTipo(request.getTipo() != null ? request.getTipo() : TipoIndustria.INDUSTRIA);
        existente.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        Industria atualizado = repository.save(existente);

        if (request.getUsuarios() != null) {
            // Deletar todos os relacionamentos existentes
            List<UsuarioIndustria> relacionamentosExistentes = usuarioIndustriaRepository.findByIndustriaId(id);
            usuarioIndustriaRepository.deleteAll(relacionamentosExistentes);
            entityManager.flush(); // Força a execução das deleções antes das inserções

            if (!request.getUsuarios().isEmpty()) {
                processarUsuariosIndustria(atualizado, request.getUsuarios());
            }
        }

        return new IndustriaResponse(atualizado);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Industria", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Industria existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Industria", id));
        existente.setAtivo(ativo);
        repository.save(existente);

        // Se a indústria está sendo inativada, desativar apenas os relacionamentos UsuarioIndustria
        if (!ativo) {
            List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByIndustriaId(id);
            for (UsuarioIndustria usuarioIndustria : usuariosIndustria) {
                // Desativar apenas o relacionamento UsuarioIndustria, mantendo o usuário ativo
                usuarioIndustria.setAtivo(false);
                usuarioIndustriaRepository.save(usuarioIndustria);
            }
        }
    }

    public List<UsuarioIndustriaResponse> buscarUsuariosPorIndustria(Long industriaId) {
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByIndustriaId(industriaId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return usuariosIndustria.stream()
                .map(ui -> new UsuarioIndustriaResponse(
                        ui.getUsuario().getCpf(),
                        ui.getUsuario().getNome(),
                        ui.getDataCadastro() != null ? ui.getDataCadastro().format(formatter) : null,
                        ui.isAtivo()
                ))
                .collect(Collectors.toList());
    }

    private void processarUsuariosIndustria(Industria industria, List<UsuarioIndustriaRequest> usuariosRequest) {
        for (UsuarioIndustriaRequest usuarioRequest : usuariosRequest) {
            String cpfLimpo = usuarioRequest.getCpf().replaceAll("\\D", "");
            
            // Verificar se o CPF já existe nesta indústria (ativo ou inativo)
            List<UsuarioIndustria> usuariosExistentes = usuarioIndustriaRepository.findByUsuarioCpfAndIndustriaId(cpfLimpo, industria.getId());
            if (!usuariosExistentes.isEmpty()) {
                throw new ValidacaoException("CPF " + usuarioRequest.getCpf() + " já está cadastrado nesta indústria.");
            }
            
            Usuario usuario = buscarOuCriarUsuario(usuarioRequest);

            boolean usuarioAtivo = usuarioRequest.getAtivo() != null ? usuarioRequest.getAtivo() : true;
            
            // Verificar se o usuário está inativo no administrador e impedir ativação
            if (usuarioAtivo && !usuario.isAtivo()) {
                throw new ValidacaoException("Não é possível ativar o usuário " + usuarioRequest.getCpf() + " na indústria pois ele está inativo no administrador.");
            }

            // Verificar se já existe um relacionamento para evitar duplicatas
            Optional<UsuarioIndustria> relacionamentoExistente =
                    usuarioIndustriaRepository.findByUsuarioIdAndIndustriaId(usuario.getId(), industria.getId());

            if (relacionamentoExistente.isPresent()) {
                // Atualizar o relacionamento existente
                UsuarioIndustria usuarioIndustria = relacionamentoExistente.get();
                usuarioIndustria.setAtivo(usuarioAtivo);
                usuarioIndustriaRepository.save(usuarioIndustria);
            } else {
                // Criar novo relacionamento
                UsuarioIndustria usuarioIndustria = new UsuarioIndustria();
                usuarioIndustria.setIndustria(industria);
                usuarioIndustria.setUsuario(usuario);
                usuarioIndustria.setAtivo(usuarioAtivo);
                usuarioIndustriaRepository.save(usuarioIndustria);
            }
        }
    }

    private Usuario buscarOuCriarUsuario(UsuarioIndustriaRequest usuarioRequest) {
        String cpfLimpo = usuarioRequest.getCpf().replaceAll("\\D", "");

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCpf(cpfLimpo);

        if (usuarioExistente.isPresent()) {
            return usuarioExistente.get();
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setCpf(cpfLimpo);
        novoUsuario.setNome(usuarioRequest.getNome());
        novoUsuario.setEmail(gerarEmailTemporario(cpfLimpo));
        novoUsuario.setTipo(TipoUsuario.INDUSTRIA);
        novoUsuario.setAtivo(usuarioRequest.getAtivo() != null ? usuarioRequest.getAtivo() : true);

        return usuarioRepository.save(novoUsuario);
    }

    private String gerarEmailTemporario(String cpf) {
        return "usuario_" + cpf + "@industria.temp";
    }

    private void desativarUsuarioEmOutrasIndustrias(Long usuarioId, Long industriaAtualId) {
        List<UsuarioIndustria> outrasRelacoes = usuarioIndustriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(ui -> !ui.getIndustria().getId().equals(industriaAtualId))
                .collect(Collectors.toList());

        for (UsuarioIndustria relacao : outrasRelacoes) {
            relacao.setAtivo(false);
            usuarioIndustriaRepository.save(relacao);
        }
    }

    public boolean verificarCnpjExiste(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        return repository.findByCnpj(cnpjLimpo).isPresent();
    }

    public boolean verificarCpfExisteEmOutraIndustria(String cpf, Long industriaIdAtual) {
        String cpfLimpo = cpf.replaceAll("\\D", "");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpfLimpo);

        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        List<UsuarioIndustria> relacoes = usuarioIndustriaRepository.findByUsuarioId(usuario.getId());

        if (industriaIdAtual == null) {
            // Para criação, verifica se existe alguma relação ATIVA em qualquer indústria
            return relacoes.stream().anyMatch(ui -> ui.isAtivo());
        }

        // Para edição, verifica se existe alguma relação ATIVA em outra indústria
        return relacoes.stream()
                .anyMatch(ui -> !ui.getIndustria().getId().equals(industriaIdAtual) && ui.isAtivo());
    }

    public boolean verificarCnpjExisteEmCertificadora(String cnpj) {
        return validacaoCruzadaService.verificarCnpjExisteEmCertificadora(cnpj);
    }

    public boolean verificarCpfExisteEmCertificadora(String cpf) {
        return validacaoCruzadaService.verificarCpfExisteEmCertificadora(cpf);
    }

    public UsuarioAtivoInfo verificarUsuarioAtivoEmOutroLocal(String cpf, Long industriaIdAtual) {
        String cpfLimpo = cpf.replaceAll("\\D", "");

        // Verificar se está ativo em outra indústria
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByUsuarioCpfAndAtivo(cpfLimpo, true);
        for (UsuarioIndustria ui : usuariosIndustria) {
            // Se industriaIdAtual é null (criação), qualquer indústria ativa é um conflito
            // Se industriaIdAtual não é null (edição), apenas outras indústrias são conflito
            if (industriaIdAtual == null || !ui.getIndustria().getId().equals(industriaIdAtual)) {
                return new UsuarioAtivoInfo("industria", ui.getIndustria().getNome(), ui.getIndustria().getId());
            }
        }

        // Verificar se está ativo em certificadora
        String nomeCertificadora = validacaoCruzadaService.buscarNomeCertificadoraPorCpf(cpfLimpo);
        if (nomeCertificadora != null) {
            return new UsuarioAtivoInfo("certificadora", nomeCertificadora, null);
        }

        return null;
    }

    public static class UsuarioAtivoInfo {
        private String tipo; // "industria" ou "certificadora"
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

    private void validarRequest(IndustriaRequest request) {
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        /*if (request.getCnpj() == null || !request.getCnpj().matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            throw new ValidacaoException("CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX");
        }*/
        if (request.getEstado() == null || request.getEstado().trim().length() != 2) {
            throw new ValidacaoException("Estado deve ter 2 caracteres");
        }
    }
}