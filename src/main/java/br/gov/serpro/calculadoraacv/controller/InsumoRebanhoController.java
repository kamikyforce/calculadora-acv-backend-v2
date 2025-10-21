package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.InsumoRebanhoRequest;
import br.gov.serpro.calculadoraacv.dto.InsumoRebanhoResponse;
import br.gov.serpro.calculadoraacv.enums.*;
import br.gov.serpro.calculadoraacv.service.InsumoRebanhoService;
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
@RequestMapping("/insumos-rebanho")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class InsumoRebanhoController {

    private final InsumoRebanhoService service;

    @PostMapping
    public ResponseEntity<InsumoRebanhoResponse> criar(@Valid @RequestBody InsumoRebanhoRequest request) {
        InsumoRebanhoResponse response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InsumoRebanhoResponse>> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) TipoInsumo tipo,
            @RequestParam(required = false) EscopoEnum escopo,
            @RequestParam(required = false) GrupoIngredienteAlimentar grupoIngrediente,
            @RequestParam(required = false) FazParteDieta fazParteDieta) {
        
        List<InsumoRebanhoResponse> insumos;
        
        if (usuarioId != null || tipo != null || escopo != null || grupoIngrediente != null || fazParteDieta != null) {
            insumos = service.listarComFiltros(usuarioId, tipo, escopo, grupoIngrediente, fazParteDieta);
        } else {
            insumos = service.listar();
        }
        
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoRebanhoResponse> buscar(@PathVariable Long id) {
        InsumoRebanhoResponse insumo = service.buscarPorId(id);
        return ResponseEntity.ok(insumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumoRebanhoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InsumoRebanhoRequest request) {
        InsumoRebanhoResponse response = service.atualizar(id, request);
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
    public ResponseEntity<List<InsumoRebanhoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<InsumoRebanhoResponse> insumos = service.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<InsumoRebanhoResponse>> buscarPorTipo(@PathVariable TipoInsumo tipo) {
        List<InsumoRebanhoResponse> insumos = service.buscarPorTipo(tipo);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/escopo/{escopo}")
    public ResponseEntity<List<InsumoRebanhoResponse>> buscarPorEscopo(@PathVariable EscopoEnum escopo) {
        List<InsumoRebanhoResponse> insumos = service.buscarPorEscopo(escopo);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/usuario/{usuarioId}/produto")
    public ResponseEntity<List<InsumoRebanhoResponse>> buscarPorProduto(
            @PathVariable Long usuarioId,
            @RequestParam String produto) {
        List<InsumoRebanhoResponse> insumos = service.buscarPorProduto(usuarioId, produto);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/modulos")
    public ResponseEntity<List<String>> listarModulosDistintos() {
        List<String> modulos = service.listarModulosDistintos();
        return ResponseEntity.ok(modulos);
    }

    @GetMapping("/escopos")
    public ResponseEntity<List<EscopoEnum>> listarEscoposDistintos() {
        List<EscopoEnum> escopos = service.listarEscoposDistintos();
        return ResponseEntity.ok(escopos);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoInsumo>> listarTiposDistintos() {
        List<TipoInsumo> tipos = service.listarTiposDistintos();
        return ResponseEntity.ok(tipos);
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
            @RequestParam String identificacao,
            @RequestParam Long usuarioId) {
        boolean exists = service.existeProdutoParaUsuario(identificacao, usuarioId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/busca-avancada")
    public ResponseEntity<List<InsumoRebanhoResponse>> buscaAvancada(
            @RequestBody Map<String, Object> criterios) {
        
        Long usuarioId = criterios.get("usuarioId") != null ? 
            Long.valueOf(criterios.get("usuarioId").toString()) : null;
        
        TipoInsumo tipo = criterios.get("tipo") != null ? 
            TipoInsumo.valueOf(criterios.get("tipo").toString()) : null;
        
        EscopoEnum escopo = criterios.get("escopo") != null ? 
            EscopoEnum.valueOf(criterios.get("escopo").toString()) : null;
        
        GrupoIngredienteAlimentar grupoIngrediente = criterios.get("grupoIngrediente") != null ? 
            GrupoIngredienteAlimentar.valueOf(criterios.get("grupoIngrediente").toString()) : null;
        
        FazParteDieta fazParteDieta = criterios.get("fazParteDieta") != null ? 
            FazParteDieta.valueOf(criterios.get("fazParteDieta").toString()) : null;
        
        List<InsumoRebanhoResponse> insumos = service.listarComFiltros(
            usuarioId, tipo, escopo, grupoIngrediente, fazParteDieta);
        
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/ingredientes-alimentares")
    public ResponseEntity<List<String>> listarIngredientesAlimentares() {
        List<String> ingredientes = service.listarIngredientesAlimentares();
        return ResponseEntity.ok(ingredientes);
    }
}