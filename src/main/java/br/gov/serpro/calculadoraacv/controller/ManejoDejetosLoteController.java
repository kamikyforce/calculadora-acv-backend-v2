package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.model.ManejoDejetosLote;
import br.gov.serpro.calculadoraacv.dto.ManejoDejetosLoteResponse;
import br.gov.serpro.calculadoraacv.service.ManejoDejetosLoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manejo-dejetos-lote")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class ManejoDejetosLoteController {

    private final ManejoDejetosLoteService service;

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<ManejoDejetosLoteResponse>> listarPorLote(@PathVariable Long loteId) {
        List<ManejoDejetosLoteResponse> result = service.listarPorLote(loteId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManejoDejetosLoteResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ManejoDejetosLoteResponse> criar(@RequestBody ManejoDejetosLote request) {
        ManejoDejetosLote criado = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManejoDejetosLoteResponse> atualizar(@PathVariable Long id, @RequestBody ManejoDejetosLote request) {
        return service.atualizar(id, request)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTiposManejo() {
        return ResponseEntity.ok(service.listarTiposManejo());
    }

    private ManejoDejetosLoteResponse toResponse(ManejoDejetosLote m) {
        ManejoDejetosLoteResponse r = new ManejoDejetosLoteResponse();
        r.setId(m.getId());
        r.setLoteId(m.getLoteId());
        r.setCategoriaAnimal(m.getCategoriaAnimal());
        r.setTipoManejo(m.getTipoManejo());
        r.setPercentualRebanho(m.getPercentualRebanho());
        r.setDataCriacao(m.getDataCriacao());
        r.setDataAtualizacao(m.getDataAtualizacao());
        return r;
    }
}