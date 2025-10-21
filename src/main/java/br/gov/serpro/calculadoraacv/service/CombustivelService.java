package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.Combustivel;
import br.gov.serpro.calculadoraacv.dto.CombustivelResponse;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import br.gov.serpro.calculadoraacv.repository.CombustivelRepository;
import br.gov.serpro.calculadoraacv.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CombustivelService {

    @Autowired
    private CombustivelRepository combustivelRepository;

    public List<Combustivel> listarTodos() {
        return combustivelRepository.findByAtivoTrue();
    }

    public List<Combustivel> listarPorEscopo(EscopoEnum escopo) {
        return combustivelRepository.findByEscopoAndAtivoTrue(escopo);
    }

    public List<Combustivel> listarPorTipo(String tipo) {
        return combustivelRepository.findByTipoAndAtivoTrue(tipo);
    }

    public List<String> listarTipos() {
        return combustivelRepository.findDistinctTipos();
    }

    public Optional<Combustivel> buscarPorId(Long id) {
        return combustivelRepository.findById(id).filter(Combustivel::getAtivo);
    }

    public List<Combustivel> buscarPorNome(String nome) {
        return combustivelRepository.findByNomeContainingAndAtivoTrue(nome);
    }

    public List<Combustivel> buscarPorNomeEEscopo(String nome, EscopoEnum escopo) {
        final String query = (nome == null ? "" : nome).toLowerCase();
        return combustivelRepository.findByEscopoAndAtivoTrue(escopo).stream()
                .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(query))
                .collect(java.util.stream.Collectors.toList());
    }

    public CombustivelResponse buscarPorNomeEEscopoExato(String nome, EscopoEnum escopo) {
        List<Combustivel> combustiveis = combustivelRepository.findByNomeAndEscopoAndAtivoTrue(nome, escopo);
        if (!combustiveis.isEmpty()) {
            return convertToResponse(combustiveis.get(0));
        }
        return null;
    }

    public Combustivel salvar(Combustivel combustivel) {
        Combustivel salvo = combustivelRepository.save(combustivel);
        // Nome/Unidade/Tipo são compartilhados entre escopos; fatores permanecem independentes
        syncNameAndUnitAcrossScopes(salvo, salvo.getNome());
        return salvo;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Combustivel atualizar(Long id, Combustivel combustivelAtualizado) {
        return combustivelRepository.findById(id)
                .filter(Combustivel::getAtivo)
                .map(combustivel -> {
                    String originalName = combustivel.getNome();

                    combustivel.setNome(combustivelAtualizado.getNome());
                    combustivel.setTipo(combustivelAtualizado.getTipo());
                    combustivel.setFatorEmissaoCO2(combustivelAtualizado.getFatorEmissaoCO2());
                    combustivel.setFatorEmissaoCH4(combustivelAtualizado.getFatorEmissaoCH4());
                    combustivel.setFatorEmissaoN2O(combustivelAtualizado.getFatorEmissaoN2O());
                    combustivel.setUnidade(combustivelAtualizado.getUnidade());
                    combustivel.setDataAtualizacao(LocalDateTime.now());

                    Combustivel salvo = combustivelRepository.save(combustivel);

                    // Compartilha Nome/Unidade/Tipo com escopo complementar; fatores NÃO são propagados
                    syncNameAndUnitAcrossScopes(salvo, originalName);

                    return salvo;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Combustível não encontrado com ID: " + id));
    }

    // Novo: upsert de registro por nome+escopo para o mesmo usuário
    public Combustivel upsertPorNomeEEscopoParaUsuario(Combustivel dados, Long usuarioId) {
        if (usuarioId == null) {
            // Sem contexto de usuário, faz fallback: atualiza ignorando escopo via o fluxo padrão do controller
            return dados; // Controller decide o fallback; aqui só não quebra
        }

        List<Combustivel> existentes =
                combustivelRepository.findByNomeAndEscopoAndUsuarioIdAndAtivoTrue(dados.getNome(), dados.getEscopo(), usuarioId);

        if (!existentes.isEmpty()) {
            Combustivel alvo = existentes.get(0);
            alvo.setTipo(dados.getTipo());
            alvo.setUnidade(dados.getUnidade());
            alvo.setFatorEmissaoCO2(dados.getFatorEmissaoCO2());
            alvo.setFatorEmissaoCH4(dados.getFatorEmissaoCH4());
            alvo.setFatorEmissaoN2O(dados.getFatorEmissaoN2O());
            alvo.setDataAtualizacao(LocalDateTime.now());
            return combustivelRepository.save(alvo);
        } else {
            Combustivel novo = new Combustivel(
                    dados.getNome(),
                    dados.getTipo(),
                    dados.getFatorEmissaoCO2(),
                    dados.getFatorEmissaoCH4(),
                    dados.getFatorEmissaoN2O(),
                    dados.getUnidade(),
                    dados.getEscopo(),
                    usuarioId
            );
            novo.setDataCriacao(LocalDateTime.now());
            novo.setDataAtualizacao(LocalDateTime.now());
            return combustivelRepository.save(novo);
        }
    }

    private void syncNameAndUnitToEscopo3(Combustivel escopo1Combustivel, String originalName) {
        final Long usuarioId = escopo1Combustivel.getUsuarioId();
        if (usuarioId == null) return;

        List<Combustivel> escopo3List =
                combustivelRepository.findByNomeAndEscopoAndUsuarioIdAndAtivoTrue(originalName, EscopoEnum.ESCOPO3, usuarioId);

        escopo3List.forEach(escopo3Combustivel -> {
            escopo3Combustivel.setNome(escopo1Combustivel.getNome());
            escopo3Combustivel.setUnidade(escopo1Combustivel.getUnidade());
            escopo3Combustivel.setTipo(escopo1Combustivel.getTipo());
            escopo3Combustivel.setDataAtualizacao(LocalDateTime.now());
            combustivelRepository.save(escopo3Combustivel);
        });
    }

    private void syncNameAndUnitToOtherScope(Combustivel combustivel, EscopoEnum otherEscopo) {
        final Long usuarioId = combustivel.getUsuarioId();
        if (usuarioId == null) return;

        combustivelRepository.findByNomeAndEscopoAndUsuarioIdAndAtivoTrue(combustivel.getNome(), otherEscopo, usuarioId)
                .stream()
                .findFirst()
                .ifPresent(irmao -> {
                    irmao.setNome(combustivel.getNome());
                    irmao.setUnidade(combustivel.getUnidade());
                    irmao.setTipo(combustivel.getTipo());
                    irmao.setDataAtualizacao(LocalDateTime.now());
                    combustivelRepository.save(irmao);
                });
    }

    // Nova sincronização bidirecional: compartilha Nome/Unidade/Tipo entre escopos (1 <-> 3), independente de usuarioId.
    private void syncNameAndUnitAcrossScopes(Combustivel updated, String originalName) {
        if (updated.getEscopo() == null) return;
        EscopoEnum other = updated.getEscopo() == EscopoEnum.ESCOPO1 ? EscopoEnum.ESCOPO3 : EscopoEnum.ESCOPO1;

        List<Combustivel> siblings;
        if (updated.getUsuarioId() != null) {
            siblings = combustivelRepository.findByNomeAndEscopoAndUsuarioIdAndAtivoTrue(originalName, other, updated.getUsuarioId());
            if (siblings.isEmpty()) {
                siblings = combustivelRepository.findByNomeAndEscopoAndAtivoTrue(originalName, other);
            }
        } else {
            siblings = combustivelRepository.findByNomeAndEscopoAndAtivoTrue(originalName, other);
        }

        siblings.forEach(s -> {
            s.setNome(updated.getNome());
            // ⛔ não replicar unidade: cada escopo possui unidade própria
            // s.setUnidade(updated.getUnidade());
            s.setTipo(updated.getTipo());
            s.setDataAtualizacao(LocalDateTime.now());
            combustivelRepository.save(s);
        });
    }

    public boolean existePorNomeEEscopoExcluindoIdPorUsuario(String nome, EscopoEnum escopo, Long id, Long usuarioId) {
        final String normalized = (nome == null ? "" : nome).toLowerCase();
        return combustivelRepository.findByEscopoAndUsuarioIdAndAtivoTrue(escopo, usuarioId).stream()
                .anyMatch(c -> !c.getId().equals(id)
                        && c.getNome() != null
                        && c.getNome().toLowerCase().equals(normalized));
    }

    public boolean existePorNome(String nome) {
        return combustivelRepository.findByNomeAndAtivoTrue(nome).isPresent();
    }

    public boolean existePorNomeEEscopo(String nome, EscopoEnum escopo) {
        final String normalized = (nome == null ? "" : nome).toLowerCase();
        return combustivelRepository.findByEscopoAndAtivoTrue(escopo).stream()
                .anyMatch(c -> c.getNome() != null && c.getNome().toLowerCase().equals(normalized));
    }

    public boolean existePorNomeExcluindoId(String nome, Long id) {
        return combustivelRepository.findByNomeAndIdNotAndAtivoTrue(nome, id).isPresent();
    }

    public boolean existePorNomeEEscopoExcluindoId(String nome, EscopoEnum escopo, Long id) {
        final String normalized = (nome == null ? "" : nome).toLowerCase();
        return combustivelRepository.findByEscopoAndAtivoTrue(escopo).stream()
                .anyMatch(c -> !c.getId().equals(id)
                        && c.getNome() != null
                        && c.getNome().toLowerCase().equals(normalized));
    }

    public boolean existePorNomeEEscopoExcluindoUsuario(String nome, EscopoEnum escopo, Long usuarioId) {
        return combustivelRepository.findByNomeAndEscopoAndAtivoTrue(nome, escopo)
                .stream()
                .anyMatch(c -> !c.getUsuarioId().equals(usuarioId));
    }

    private CombustivelResponse convertToResponse(Combustivel combustivel) {
        CombustivelResponse response = new CombustivelResponse();
        response.setId(combustivel.getId());
        response.setNome(combustivel.getNome());
        response.setTipo(combustivel.getTipo());
        response.setFatorEmissaoCO2(combustivel.getFatorEmissaoCO2());
        response.setFatorEmissaoCH4(combustivel.getFatorEmissaoCH4());
        response.setFatorEmissaoN2O(combustivel.getFatorEmissaoN2O());
        response.setUnidade(combustivel.getUnidade());
        response.setDataCriacao(combustivel.getDataCriacao());
        response.setDataAtualizacao(combustivel.getDataAtualizacao());
        response.setEscopo(combustivel.getEscopo());
        response.setUsuarioId(combustivel.getUsuarioId());
        return response;
    }
}
