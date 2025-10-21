package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.LoteRebanhoRequest;
import br.gov.serpro.calculadoraacv.dto.LoteRebanhoResponse;
import br.gov.serpro.calculadoraacv.service.LoteRebanhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotes-rebanho")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class LoteRebanhoController {

    private final LoteRebanhoService service;

    @PostMapping
    public ResponseEntity<LoteRebanhoResponse> criar(@Valid @RequestBody LoteRebanhoRequest request) {
        LoteRebanhoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<LoteRebanhoResponse>> listarPorInventario(@PathVariable Long inventarioId) {
        List<LoteRebanhoResponse> lotes = service.listarPorInventario(inventarioId);
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}/inventario/{inventarioId}")
    public ResponseEntity<LoteRebanhoResponse> buscarPorId(
            @PathVariable Long id,
            @PathVariable Long inventarioId) {
        LoteRebanhoResponse lote = service.buscarPorId(id, inventarioId);
        return ResponseEntity.ok(lote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteRebanhoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteRebanhoRequest request) {
        LoteRebanhoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/inventario/{inventarioId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @PathVariable Long inventarioId) {
        service.deletar(id, inventarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/inventario/{inventarioId}")
    public ResponseEntity<Void> deletarTodosPorInventario(@PathVariable Long inventarioId) {
        service.deletarTodosPorInventario(inventarioId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/inventario/{inventarioId}/reordenar")
    public ResponseEntity<Void> reordenarLotes(
            @PathVariable Long inventarioId,
            @RequestBody List<Long> idsOrdenados) {
        service.reordenarLotes(inventarioId, idsOrdenados);
        return ResponseEntity.ok().build();
    }
}