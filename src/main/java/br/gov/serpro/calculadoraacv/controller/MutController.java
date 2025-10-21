package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.*;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import br.gov.serpro.calculadoraacv.enums.TipoMudanca;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.service.MutService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mut")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class MutController {

    private final MutService mutService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CURADOR')")
    public ResponseEntity<MutResponse> criar(
            @Valid @RequestBody MutRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        MutResponse response = mutService.criar(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CURADOR')")
    public ResponseEntity<MutResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MutRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        MutResponse response = mutService.atualizar(id, request, usuario);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CURADOR')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        mutService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MutResponse> obterPorId(@PathVariable Long id) {
        MutResponse response = mutService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MutResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String termoBusca,
            @RequestParam(required = false) String tipoMudanca,
            @RequestParam(required = false) String escopo,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nomeUsuario,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "dataCriacao") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        MutFiltros filtros = new MutFiltros();
        filtros.setNome(nome);
        filtros.setTermoBusca(termoBusca);
        if (tipoMudanca != null && !tipoMudanca.trim().isEmpty()) {
            filtros.setTipoMudanca(TipoMudanca.fromString(tipoMudanca));
        }
        if (escopo != null) {
            filtros.setEscopo(EscopoEnum.fromString(escopo));
        }
        filtros.setAtivo(ativo);
        filtros.setNomeUsuario(nomeUsuario);
        filtros.setPage(page);
        filtros.setSize(size);
        filtros.setSort(sort);
        filtros.setDirection(direction);
        
        Page<MutResponse> response = mutService.listar(filtros);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<MutStats> obterEstatisticas() {
        MutStats stats = mutService.obterEstatisticas();
        return ResponseEntity.ok(stats);
    }

    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CURADOR')")
    public ResponseEntity<Map<String, Object>> importarExcel(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            mutService.importarExcel(arquivo);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Arquivo importado com sucesso");
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ValidacaoException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erro interno do servidor: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/verificar-existencia")
    public ResponseEntity<Boolean> verificarExistencia(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipoMudanca,
            @RequestParam(required = false) String escopo) {
        
        try {
            // Validar apenas tipoMudanca e escopo, nome é opcional
            if (tipoMudanca == null || tipoMudanca.trim().isEmpty() || 
                escopo == null || escopo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(false);
            }
            
            TipoMudanca tipo = TipoMudanca.fromString(tipoMudanca);
            EscopoEnum escopoEnum = EscopoEnum.fromString(escopo);
            
            boolean existe = mutService.verificarExistencia(tipo, escopoEnum);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}