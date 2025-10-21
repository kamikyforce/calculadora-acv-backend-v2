package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.InventarioJornadaRequest;
import br.gov.serpro.calculadoraacv.dto.InventarioJornadaResponse;
import br.gov.serpro.calculadoraacv.model.InventarioJornada.StatusInventario;
import br.gov.serpro.calculadoraacv.service.InventarioJornadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventarios-jornada")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class InventarioJornadaController {

    private final InventarioJornadaService service;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<InventarioJornadaResponse> criar(
            @PathVariable Long usuarioId,
            @Valid @RequestBody InventarioJornadaRequest request) {
        InventarioJornadaResponse response = service.criar(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InventarioJornadaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<InventarioJornadaResponse> inventarios = service.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/usuario/{usuarioId}/status/{status}")
    public ResponseEntity<List<InventarioJornadaResponse>> listarPorUsuarioEStatus(
            @PathVariable Long usuarioId,
            @PathVariable StatusInventario status) {
        List<InventarioJornadaResponse> inventarios = service.listarPorUsuarioEStatus(usuarioId, status);
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioJornadaResponse> buscarPorId(@PathVariable Long id) {
        InventarioJornadaResponse inventario = service.buscarPorId(id);
        return ResponseEntity.ok(inventario);
    }

    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<InventarioJornadaResponse> atualizar(
            @PathVariable Long id,
            @PathVariable Long usuarioId,
            @Valid @RequestBody InventarioJornadaRequest request) {
        InventarioJornadaResponse response = service.atualizar(id, usuarioId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @PathVariable Long usuarioId) {
        service.deletar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/usuario/{usuarioId}/avancar-fase")
    public ResponseEntity<InventarioJornadaResponse> avancarFase(
            @PathVariable Long id,
            @PathVariable Long usuarioId) {
        InventarioJornadaResponse response = service.avancarFase(id, usuarioId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/usuario/{usuarioId}/concluir-fase/{fase}")
    public ResponseEntity<InventarioJornadaResponse> marcarFaseConcluida(
            @PathVariable Long id,
            @PathVariable Long usuarioId,
            @PathVariable int fase) {
        InventarioJornadaResponse response = service.marcarFaseConcluida(id, usuarioId, fase);
        return ResponseEntity.ok(response);
    }
}