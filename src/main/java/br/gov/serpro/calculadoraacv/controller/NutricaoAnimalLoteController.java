package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.NutricaoAnimalLoteRequest;
import br.gov.serpro.calculadoraacv.dto.NutricaoAnimalLoteResponse;
import br.gov.serpro.calculadoraacv.dto.ConcentradoDietaLoteResponse;
import br.gov.serpro.calculadoraacv.dto.AditivoDietaLoteResponse;
import br.gov.serpro.calculadoraacv.dto.IngredienteDietaLoteResponse;
import br.gov.serpro.calculadoraacv.model.NutricaoAnimalLote;
import br.gov.serpro.calculadoraacv.model.IngredienteDietaLote;
import br.gov.serpro.calculadoraacv.model.ConcentradoDietaLote;
import br.gov.serpro.calculadoraacv.model.AditivoDietaLote;
import br.gov.serpro.calculadoraacv.service.NutricaoAnimalLoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/nutricao-animal-lote")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class NutricaoAnimalLoteController {

    private final NutricaoAnimalLoteService service;

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<NutricaoAnimalLoteResponse>> listarPorLote(@PathVariable Long loteId) {
        List<NutricaoAnimalLoteResponse> result = new ArrayList<>();
        service.buscarPorLote(loteId).ifPresent(entity -> result.add(toResponse(entity)));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutricaoAnimalLoteResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(entity -> ResponseEntity.ok(toResponse(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NutricaoAnimalLoteResponse> criar(@RequestBody NutricaoAnimalLoteRequest request) {
        NutricaoAnimalLote criado = service.criarComListas(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutricaoAnimalLoteResponse> atualizar(@PathVariable Long id, @RequestBody NutricaoAnimalLoteRequest request) {
        return service.atualizarComListas(id, request)
                .map(entity -> ResponseEntity.ok(toResponse(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mapping helpers ---
    private NutricaoAnimalLote fromRequest(NutricaoAnimalLoteRequest req) {
        NutricaoAnimalLote e = new NutricaoAnimalLote();
        e.setLoteId(req.getLoteId());
        e.setInserirDadosDieta(req.getInserirDadosDieta());
        e.setSistemaProducao(req.getSistemaProducao());
        e.setTempoPastoHorasDia(req.getTempoPastoHorasDia() != null ? req.getTempoPastoHorasDia() : BigDecimal.ZERO);
        e.setTempoPastoDiasAno(req.getTempoPastoDiasAno() != null ? req.getTempoPastoDiasAno() : 0);
        return e;
    }

    private NutricaoAnimalLoteResponse toResponse(NutricaoAnimalLote e) {
        NutricaoAnimalLoteResponse r = new NutricaoAnimalLoteResponse();
        r.setId(e.getId());
        r.setLoteId(e.getLoteId());
        r.setInserirDadosDieta(e.getInserirDadosDieta());
        r.setSistemaProducao(e.getSistemaProducao());
        r.setTempoPastoHorasDia(e.getTempoPastoHorasDia());
        r.setTempoPastoDiasAno(e.getTempoPastoDiasAno());
        r.setDataCriacao(e.getDataCriacao());
        r.setDataAtualizacao(e.getDataAtualizacao());
        // Buscar listas via repositórios para evitar proxies lazy
        List<IngredienteDietaLoteResponse> ingredientes = new ArrayList<>();
        for (IngredienteDietaLote i : service.listarIngredientes(e.getId())) {
            IngredienteDietaLoteResponse ir = new IngredienteDietaLoteResponse();
            ir.setId(i.getId());
            ir.setNutricaoLoteId(i.getNutricaoLoteId());
            ir.setNomeIngrediente(i.getNomeIngrediente());
            ir.setPercentual(i.getPercentual() != null ? i.getPercentual() : BigDecimal.ZERO);
            ir.setQuantidadeKgCabDia(i.getQuantidadeKgCabDia());
            ir.setOfertaDiasAno(i.getOfertaDiasAno());
            ir.setProducao(i.getProducao());
            ir.setDataCriacao(i.getDataCriacao());
            ingredientes.add(ir);
        }
        r.setIngredientes(ingredientes);

        List<ConcentradoDietaLoteResponse> concentrados = new ArrayList<>();
        for (ConcentradoDietaLote c : service.listarConcentrados(e.getId())) {
            ConcentradoDietaLoteResponse cr = new ConcentradoDietaLoteResponse();
            cr.setId(c.getId());
            cr.setNutricaoLoteId(c.getNutricaoLoteId());
            cr.setNomeConcentrado(c.getNomeConcentrado());
            cr.setPercentual(c.getPercentual());
            cr.setProteinaBrutaPercentual(c.getProteinaBrutaPercentual());
            cr.setUreia(c.getUreia());
            cr.setSubproduto(c.getSubproduto());
            cr.setQuantidade(c.getQuantidade());
            cr.setOferta(c.getOferta());
            cr.setQuantidadeKgCabDia(c.getQuantidadeKgCabDia());
            cr.setOfertaDiasAno(c.getOfertaDiasAno() != null ? c.getOfertaDiasAno() : 0);
            cr.setProducao(c.getProducao());
            cr.setDataCriacao(c.getDataCriacao());
            concentrados.add(cr);
        }
        r.setConcentrados(concentrados);

        List<AditivoDietaLoteResponse> aditivos = new ArrayList<>();
        for (AditivoDietaLote a : service.listarAditivos(e.getId())) {
            AditivoDietaLoteResponse ar = new AditivoDietaLoteResponse();
            ar.setId(a.getId());
            ar.setNutricaoLoteId(a.getNutricaoLoteId());
            ar.setNomeAditivo(a.getNomeAditivo());
            ar.setPercentual(a.getPercentual());
            ar.setTipo(a.getTipo());
            ar.setDose(a.getDose());
            ar.setOferta(a.getOferta());
            ar.setPercentualAdicional(a.getPercentualAdicional());
            ar.setQuantidadeKgCabDia(a.getQuantidadeKgCabDia());
            ar.setOfertaDiasAno(a.getOfertaDiasAno());
            ar.setProducao(a.getProducao());
            ar.setDataCriacao(a.getDataCriacao());
            aditivos.add(ar);
        }
        r.setAditivos(aditivos);
        return r;
    }
}