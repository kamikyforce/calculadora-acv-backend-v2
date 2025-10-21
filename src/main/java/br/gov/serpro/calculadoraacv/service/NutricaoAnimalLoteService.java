package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.AditivoDietaLoteRequest;
import br.gov.serpro.calculadoraacv.dto.ConcentradoDietaLoteRequest;
import br.gov.serpro.calculadoraacv.dto.IngredienteDietaLoteRequest;
import br.gov.serpro.calculadoraacv.dto.NutricaoAnimalLoteRequest;
import br.gov.serpro.calculadoraacv.model.AditivoDietaLote;
import br.gov.serpro.calculadoraacv.model.ConcentradoDietaLote;
import br.gov.serpro.calculadoraacv.model.IngredienteDietaLote;
import br.gov.serpro.calculadoraacv.model.NutricaoAnimalLote;
import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import br.gov.serpro.calculadoraacv.repository.AditivoDietaLoteRepository;
import br.gov.serpro.calculadoraacv.repository.ConcentradoDietaLoteRepository;
import br.gov.serpro.calculadoraacv.repository.IngredienteDietaLoteRepository;
import br.gov.serpro.calculadoraacv.repository.NutricaoAnimalLoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NutricaoAnimalLoteService {

    private final NutricaoAnimalLoteRepository repository;
    private final IngredienteDietaLoteRepository ingredienteRepository;
    private final ConcentradoDietaLoteRepository concentradoRepository;
    private final AditivoDietaLoteRepository aditivoRepository;

    public NutricaoAnimalLoteService(NutricaoAnimalLoteRepository repository,
                                     IngredienteDietaLoteRepository ingredienteRepository,
                                     ConcentradoDietaLoteRepository concentradoRepository,
                                     AditivoDietaLoteRepository aditivoRepository) {
        this.repository = repository;
        this.ingredienteRepository = ingredienteRepository;
        this.concentradoRepository = concentradoRepository;
        this.aditivoRepository = aditivoRepository;
    }

    public Optional<NutricaoAnimalLote> buscarPorLote(Long loteId) {
        return repository.findByLoteId(loteId);
    }

    public Optional<NutricaoAnimalLote> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public NutricaoAnimalLote criar(NutricaoAnimalLote nutricao) {
        return repository.save(nutricao);
    }

    public Optional<NutricaoAnimalLote> atualizar(Long id, NutricaoAnimalLote payload) {
        return repository.findById(id).map(existing -> {
            existing.setLoteId(payload.getLoteId());
            existing.setInserirDadosDieta(payload.getInserirDadosDieta());
            existing.setSistemaProducao(payload.getSistemaProducao());
            existing.setTempoPastoHorasDia(payload.getTempoPastoHorasDia());
            existing.setTempoPastoDiasAno(payload.getTempoPastoDiasAno());
            return repository.save(existing);
        });
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public void deletarPorLote(Long loteId) {
        repository.deleteByLoteId(loteId);
    }

    // --- Métodos que tratam listas ---
    @Transactional
    public NutricaoAnimalLote criarComListas(NutricaoAnimalLoteRequest req) {
        NutricaoAnimalLote entity = fromRequest(req);
        NutricaoAnimalLote salvo = repository.save(entity);
        upsertListas(req, salvo.getId());
        return salvo;
    }

    @Transactional
    public Optional<NutricaoAnimalLote> atualizarComListas(Long id, NutricaoAnimalLoteRequest req) {
        return repository.findById(id).map(existing -> {
            existing.setLoteId(req.getLoteId());
            existing.setInserirDadosDieta(req.getInserirDadosDieta());
            existing.setSistemaProducao(req.getSistemaProducao());
            existing.setTempoPastoHorasDia(req.getTempoPastoHorasDia() != null ? req.getTempoPastoHorasDia() : BigDecimal.ZERO);
            existing.setTempoPastoDiasAno(req.getTempoPastoDiasAno() != null ? req.getTempoPastoDiasAno() : 0);
            NutricaoAnimalLote atualizado = repository.save(existing);
            upsertListas(req, atualizado.getId());
            return atualizado;
        });
    }

    public List<IngredienteDietaLote> listarIngredientes(Long nutricaoLoteId) {
        return ingredienteRepository.findByNutricaoLoteId(nutricaoLoteId);
    }

    public List<ConcentradoDietaLote> listarConcentrados(Long nutricaoLoteId) {
        return concentradoRepository.findByNutricaoLoteId(nutricaoLoteId);
    }

    public List<AditivoDietaLote> listarAditivos(Long nutricaoLoteId) {
        return aditivoRepository.findByNutricaoLoteId(nutricaoLoteId);
    }

    // Helpers
    private NutricaoAnimalLote fromRequest(NutricaoAnimalLoteRequest req) {
        NutricaoAnimalLote e = new NutricaoAnimalLote();
        e.setLoteId(req.getLoteId());
        e.setInserirDadosDieta(req.getInserirDadosDieta());
        e.setSistemaProducao(req.getSistemaProducao());
        e.setTempoPastoHorasDia(req.getTempoPastoHorasDia() != null ? req.getTempoPastoHorasDia() : BigDecimal.ZERO);
        e.setTempoPastoDiasAno(req.getTempoPastoDiasAno() != null ? req.getTempoPastoDiasAno() : 0);
        return e;
    }

    private void upsertListas(NutricaoAnimalLoteRequest req, Long nutricaoLoteId) {
        // Limpa e recria as listas para manter consistência
        ingredienteRepository.deleteByNutricaoLoteId(nutricaoLoteId);
        concentradoRepository.deleteByNutricaoLoteId(nutricaoLoteId);
        aditivoRepository.deleteByNutricaoLoteId(nutricaoLoteId);

        List<IngredienteDietaLote> ingredientes = new ArrayList<>();
        if (req.getIngredientes() != null) {
            for (IngredienteDietaLoteRequest i : req.getIngredientes()) {
                IngredienteDietaLote e = new IngredienteDietaLote();
                e.setNutricaoLoteId(nutricaoLoteId);
                e.setNomeIngrediente(i.getNomeIngrediente());
                e.setPercentual(i.getPercentual() != null ? i.getPercentual() : BigDecimal.ZERO);
                e.setQuantidadeKgCabDia(i.getQuantidadeKgCabDia());
                e.setOfertaDiasAno(i.getOfertaDiasAno());
                e.setProducao(i.getProducao());
                ingredientes.add(e);
            }
        }
        if (!ingredientes.isEmpty()) ingredienteRepository.saveAll(ingredientes);

        List<ConcentradoDietaLote> concentrados = new ArrayList<>();
        if (req.getConcentrados() != null) {
            for (ConcentradoDietaLoteRequest c : req.getConcentrados()) {
                ConcentradoDietaLote e = new ConcentradoDietaLote();
                e.setNutricaoLoteId(nutricaoLoteId);
                e.setNomeConcentrado(c.getNomeConcentrado());
                e.setPercentual(c.getPercentual() != null ? c.getPercentual() : BigDecimal.ZERO);
                e.setProteinaBrutaPercentual(c.getProteinaBrutaPercentual());
                e.setUreia(c.getUreia());
                e.setSubproduto(c.getSubproduto());
                e.setQuantidade(c.getQuantidade());
                e.setOferta(c.getOferta());
                e.setQuantidadeKgCabDia(c.getQuantidadeKgCabDia() != null ? c.getQuantidadeKgCabDia() : BigDecimal.ZERO);
                e.setOfertaDiasAno(c.getOfertaDiasAno() != null ? c.getOfertaDiasAno() : 0);
                e.setProducao(c.getProducao() != null ? c.getProducao() : OrigemProducao.INTERNA);
                concentrados.add(e);
            }
        }
        if (!concentrados.isEmpty()) concentradoRepository.saveAll(concentrados);

        List<AditivoDietaLote> aditivos = new ArrayList<>();
        if (req.getAditivos() != null) {
            for (AditivoDietaLoteRequest a : req.getAditivos()) {
                AditivoDietaLote e = new AditivoDietaLote();
                e.setNutricaoLoteId(nutricaoLoteId);
                e.setNomeAditivo(a.getNomeAditivo());
                e.setPercentual(a.getPercentual() != null ? a.getPercentual() : BigDecimal.ZERO);
                e.setTipo(a.getTipo());
                e.setDose(a.getDose());
                e.setOferta(a.getOferta());
                e.setPercentualAdicional(a.getPercentualAdicional());
                e.setQuantidadeKgCabDia(a.getQuantidadeKgCabDia() != null ? a.getQuantidadeKgCabDia() : BigDecimal.ZERO);
                e.setOfertaDiasAno(a.getOfertaDiasAno() != null ? a.getOfertaDiasAno() : 0);
                e.setProducao(a.getProducao() != null ? a.getProducao() : OrigemProducao.INTERNA);
                aditivos.add(e);
            }
        }
        if (!aditivos.isEmpty()) aditivoRepository.saveAll(aditivos);
    }
}