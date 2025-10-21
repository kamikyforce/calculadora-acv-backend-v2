package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.CategoriaLoteRequest;
import br.gov.serpro.calculadoraacv.dto.CategoriaLoteResponse;
import br.gov.serpro.calculadoraacv.model.CategoriaLote;
import br.gov.serpro.calculadoraacv.service.CategoriaLoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categoria-lote")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CategoriaLoteController {

    private final CategoriaLoteService service;

    private CategoriaLoteResponse toResponse(CategoriaLote entity) {
        CategoriaLoteResponse r = new CategoriaLoteResponse();
        r.setId(entity.getId());
        r.setLoteId(entity.getLoteId());
        r.setQuantidadeAnimais(entity.getAnimaisFazenda());
        r.setPesoMedio(entity.getPesoMedioVivo());
        r.setCategoriaCorteId(entity.getCategoriaCorteId());
        r.setCategoriaLeiteId(entity.getCategoriaLeiteId());
        r.setObservacoes(null); // não mapeado no modelo atual
        r.setAnimaisComprados(entity.getAnimaisComprados());
        r.setPesoMedioComprados(entity.getPesoMedioComprados());
        r.setAnimaisVendidos(entity.getAnimaisVendidos());
        r.setPesoMedioVendidos(entity.getPesoMedioVendidos());
        r.setPermanenciaMeses(entity.getPermanenciaMeses());
        r.setIdadeDesmame(entity.getIdadeDesmame());
        r.setFemeasPrenhasPercentual(entity.getFemeasPrenhasPercentual());
        r.setProducaoLeiteAno(entity.getProducaoLeiteAno());
        r.setTeorGorduraLeite(entity.getTeorGorduraLeite());
        r.setTeorProteinaLeite(entity.getTeorProteinaLeite());
        r.setDataCriacao(entity.getDataCriacao());
        r.setDataAtualizacao(entity.getDataAtualizacao());
        return r;
    }

    private CategoriaLote fromRequest(CategoriaLoteRequest req) {
        CategoriaLote e = new CategoriaLote();
        e.setLoteId(req.getLoteId());
        e.setCategoriaCorteId(req.getCategoriaCorteId());
        e.setCategoriaLeiteId(req.getCategoriaLeiteId());
        e.setAnimaisFazenda(req.getQuantidadeAnimais() != null ? req.getQuantidadeAnimais() : 0);
        e.setPesoMedioVivo(req.getPesoMedio() != null ? req.getPesoMedio() : java.math.BigDecimal.ZERO);
        e.setAnimaisComprados(req.getAnimaisComprados() != null ? req.getAnimaisComprados() : 0);
        e.setPesoMedioComprados(req.getPesoMedioComprados() != null ? req.getPesoMedioComprados() : java.math.BigDecimal.ZERO);
        e.setAnimaisVendidos(req.getAnimaisVendidos() != null ? req.getAnimaisVendidos() : 0);
        e.setPesoMedioVendidos(req.getPesoMedioVendidos() != null ? req.getPesoMedioVendidos() : java.math.BigDecimal.ZERO);
        e.setPermanenciaMeses(req.getPermanenciaMeses() != null ? req.getPermanenciaMeses() : java.math.BigDecimal.ZERO);
        e.setIdadeDesmame(req.getIdadeDesmame() != null ? req.getIdadeDesmame() : java.math.BigDecimal.ZERO);
        e.setFemeasPrenhasPercentual(req.getFemeasPrenhasPercentual() != null ? req.getFemeasPrenhasPercentual() : java.math.BigDecimal.ZERO);
        e.setProducaoLeiteAno(req.getProducaoLeiteAno() != null ? req.getProducaoLeiteAno() : java.math.BigDecimal.ZERO);
        e.setTeorGorduraLeite(req.getTeorGorduraLeite() != null ? req.getTeorGorduraLeite() : java.math.BigDecimal.ZERO);
        e.setTeorProteinaLeite(req.getTeorProteinaLeite() != null ? req.getTeorProteinaLeite() : java.math.BigDecimal.ZERO);
        // Observações não existe no modelo; ignorado
        return e;
    }

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<CategoriaLoteResponse>> listarPorLote(@PathVariable Long loteId) {
        List<CategoriaLoteResponse> out = service.listarPorLote(loteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaLoteResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(e -> ResponseEntity.ok(toResponse(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaLoteResponse> criar(@RequestBody CategoriaLoteRequest request) {
        CategoriaLote criado = service.criar(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaLoteResponse> atualizar(@PathVariable Long id, @RequestBody CategoriaLoteRequest request) {
        return service.atualizar(id, fromRequest(request))
                .map(e -> ResponseEntity.ok(toResponse(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}