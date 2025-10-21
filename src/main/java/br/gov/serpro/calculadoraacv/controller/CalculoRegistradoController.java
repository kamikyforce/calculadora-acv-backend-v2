package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.CalculoRegistradoRequest;
import br.gov.serpro.calculadoraacv.dto.CalculoRegistradoResponse;
import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import br.gov.serpro.calculadoraacv.service.CalculoRegistradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculos-registrados")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CalculoRegistradoController {

    private final CalculoRegistradoService service;

    @GetMapping
    public ResponseEntity<Page<CalculoRegistradoResponse>> listar(
            @RequestParam(required = false) String car,
            @RequestParam(required = false) String fazenda,
            @RequestParam(required = false) StatusCalculoRegistrado status,
            @RequestParam(required = false) TipoCertificacao certificacao,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer ano,
            Pageable pageable) {
        
        if (car != null || fazenda != null || status != null || certificacao != null || estado != null || ano != null) {
            Page<CalculoRegistradoResponse> page = service.listarComFiltros(car, fazenda, status, certificacao, estado, ano, pageable);
            return ResponseEntity.ok(page);
        }
        
        Page<CalculoRegistradoResponse> page = service.listarComPaginacao(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalculoRegistradoResponse> buscarPorId(@PathVariable Long id) {
        CalculoRegistradoResponse calculo = service.buscarPorId(id);
        return ResponseEntity.ok(calculo);
    }

    @PostMapping
    public ResponseEntity<CalculoRegistradoResponse> criar(@Valid @RequestBody CalculoRegistradoRequest request,
                                                          @RequestParam Long usuarioId) {
        CalculoRegistradoResponse response = service.criar(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalculoRegistradoResponse> atualizar(@PathVariable Long id,
                                                              @Valid @RequestBody CalculoRegistradoRequest request,
                                                              @RequestParam Long usuarioId) {
        CalculoRegistradoResponse response = service.atualizar(id, request, usuarioId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/verificar-fazenda/{fazenda}")
    public ResponseEntity<Boolean> verificarFazendaExistente(
            @PathVariable String fazenda,
            @RequestParam Integer ano,
            @RequestParam Long usuarioId,
            @RequestParam(required = false) Long id) {
        boolean exists = service.verificarFazendaExistente(fazenda, ano, usuarioId, id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/status")
    public ResponseEntity<List<StatusCalculoRegistrado>> listarStatus() {
        List<StatusCalculoRegistrado> status = Arrays.asList(StatusCalculoRegistrado.values());
        return ResponseEntity.ok(status);
    }

    @GetMapping("/certificacoes")
    public ResponseEntity<List<TipoCertificacao>> listarCertificacoes() {
        List<TipoCertificacao> certificacoes = Arrays.asList(TipoCertificacao.values());
        return ResponseEntity.ok(certificacoes);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(@RequestParam Long usuarioId) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Contadores por status
        estatisticas.put("totalCalculos", service.contarPorStatus(null));
        estatisticas.put("calculosConcluidos", service.contarPorStatus(StatusCalculoRegistrado.CONCLUIDO));
        estatisticas.put("calculosRascunho", service.contarPorStatus(StatusCalculoRegistrado.RASCUNHO));
        
        // Contadores por certificação
        estatisticas.put("certificados", service.contarPorCertificacao(TipoCertificacao.CERTIFICADO));
        estatisticas.put("emCertificacao", service.contarPorCertificacao(TipoCertificacao.EM_CERTIFICACAO));
        estatisticas.put("naoCertificados", service.contarPorCertificacao(TipoCertificacao.NAO_CERTIFICADO));
        
        return ResponseEntity.ok(estatisticas);
    }
}