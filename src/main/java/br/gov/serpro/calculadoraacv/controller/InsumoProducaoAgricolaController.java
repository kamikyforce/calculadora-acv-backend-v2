package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.InsumoProducaoAgricolaRequest;
import br.gov.serpro.calculadoraacv.dto.InsumoProducaoAgricolaResponse;

import br.gov.serpro.calculadoraacv.service.InsumoProducaoAgricolaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/insumos-producao-agricola")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class InsumoProducaoAgricolaController {

    private final InsumoProducaoAgricolaService service;

    @PostMapping
    public ResponseEntity<InsumoProducaoAgricolaResponse> criar(@Valid @RequestBody InsumoProducaoAgricolaRequest request) {
        InsumoProducaoAgricolaResponse response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InsumoProducaoAgricolaResponse>> listar(
            @RequestParam(required = false) Long usuarioId) {
        
        List<InsumoProducaoAgricolaResponse> insumos;
        
        if (usuarioId != null) {
            insumos = service.listarPorUsuario(usuarioId);
        } else {
            insumos = service.listar();
        }
        
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoProducaoAgricolaResponse> buscar(@PathVariable Long id) {
        InsumoProducaoAgricolaResponse insumo = service.buscarPorId(id);
        return ResponseEntity.ok(insumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumoProducaoAgricolaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InsumoProducaoAgricolaRequest request) {
        InsumoProducaoAgricolaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo) {
        service.alterarStatus(id, ativo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InsumoProducaoAgricolaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<InsumoProducaoAgricolaResponse> insumos = service.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(insumos);
    }



    @GetMapping("/usuario/{usuarioId}/produto")
    public ResponseEntity<List<InsumoProducaoAgricolaResponse>> buscarPorProduto(
            @PathVariable Long usuarioId,
            @RequestParam String produto) {
        List<InsumoProducaoAgricolaResponse> insumos = service.buscarPorProduto(usuarioId, produto);
        return ResponseEntity.ok(insumos);
    }



    @GetMapping("/usuario/{usuarioId}/count")
    public ResponseEntity<Map<String, Long>> contarPorUsuario(@PathVariable Long usuarioId) {
        long count = service.contarPorUsuario(usuarioId);
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/produto/exists")
    public ResponseEntity<Map<String, Boolean>> verificarExistenciaProduto(
            @RequestParam String nomeProduto,
            @RequestParam Long usuarioId) {
        boolean exists = service.existeProdutoParaUsuario(nomeProduto, usuarioId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verificar-nome/{nome}")
    public ResponseEntity<Boolean> verificarNomeExistente(
            @PathVariable String nome,
            @RequestParam Long usuarioId,
            @RequestParam(required = false) String classe,
            @RequestParam(required = false) Long id) {
        boolean exists = service.verificarNomeExistenteConsiderandoClasse(nome, usuarioId, classe, id);
        return ResponseEntity.ok(exists);
    }
}