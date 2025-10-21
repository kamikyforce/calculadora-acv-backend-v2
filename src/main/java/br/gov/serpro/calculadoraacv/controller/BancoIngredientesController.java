package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.IngredienteResponse;
import br.gov.serpro.calculadoraacv.service.BancoIngredientesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banco-ingredientes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BancoIngredientesController {

    private final BancoIngredientesService bancoIngredientesService;

    @GetMapping
    public ResponseEntity<List<IngredienteResponse>> listarTodosIngredientes() {
        log.info("Requisição para listar todos os ingredientes do banco de fatores");
        try {
            List<IngredienteResponse> ingredientes = bancoIngredientesService.listarTodosIngredientes();
            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            log.error("Erro ao buscar ingredientes: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTiposIngredientes() {
        log.info("Requisição para listar tipos de ingredientes");
        try {
            List<String> tipos = bancoIngredientesService.listarTiposIngredientes();
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            log.error("Erro ao buscar tipos de ingredientes: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/por-tipo")
    public ResponseEntity<List<IngredienteResponse>> buscarIngredientesPorTipo(@RequestParam String tipo) {
        log.info("Requisição para buscar ingredientes por tipo: {}", tipo);
        try {
            List<IngredienteResponse> ingredientes = bancoIngredientesService.buscarIngredientesPorTipo(tipo);
            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            log.error("Erro ao buscar ingredientes por tipo: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/por-nome")
    public ResponseEntity<List<IngredienteResponse>> buscarIngredientesPorNome(@RequestParam String nome) {
        log.info("Requisição para buscar ingredientes por nome: {}", nome);
        try {
            List<IngredienteResponse> ingredientes = bancoIngredientesService.buscarIngredientesPorNome(nome);
            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            log.error("Erro ao buscar ingredientes por nome: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponse> buscarIngredientePorId(@PathVariable Long id, @RequestParam String fonte) {
        log.info("Requisição para buscar ingrediente por ID: {} da fonte: {}", id, fonte);
        try {
            // Este endpoint pode ser implementado posteriormente se necessário
            // Por enquanto, retorna not implemented
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao buscar ingrediente por ID: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}