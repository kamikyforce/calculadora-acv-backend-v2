package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.model.FatorEnergiaEletrica;
import br.gov.serpro.calculadoraacv.model.BancoFatoresEnergia;
import br.gov.serpro.calculadoraacv.service.FatorEnergiaEletricaService;
import br.gov.serpro.calculadoraacv.service.BancoFatoresEnergiaService;
import br.gov.serpro.calculadoraacv.service.EnergiaDadosService;
import br.gov.serpro.calculadoraacv.dto.EnergiaComFatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fatores-energia")
@CrossOrigin(origins = "http://localhost:4200")
public class FatorEnergiaEletricaController {

    @Autowired
    private FatorEnergiaEletricaService fatorService;

    @Autowired
    private BancoFatoresEnergiaService bancoFatoresService;
    
    @Autowired
    private EnergiaDadosService energiaDadosService;

    @GetMapping
    public ResponseEntity<List<FatorEnergiaEletrica>> listarTodos() {
        List<FatorEnergiaEletrica> fatores = fatorService.listarTodos();
        return ResponseEntity.ok(fatores);
    }

    @GetMapping("/ano/{ano}")
    public ResponseEntity<Map<String, Object>> buscarFatorMedioAnualPorAno(@PathVariable Integer ano) {
        BigDecimal fator = bancoFatoresService.obterFatorMedioAnual(ano);
        Map<String, Object> response = new HashMap<>();
        response.put("fator_medio_anual", fator);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ano/{ano}/mes/{mes}")
    public ResponseEntity<FatorEnergiaEletrica> buscarPorAnoEMes(
            @PathVariable Integer ano, @PathVariable Integer mes) {
        return fatorService.buscarPorAnoEMes(ano, mes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipoDado}")
    public ResponseEntity<List<FatorEnergiaEletrica>> listarPorTipo(@PathVariable String tipoDado) {
        List<FatorEnergiaEletrica> fatores = fatorService.listarPorTipoDado(tipoDado);
        return ResponseEntity.ok(fatores);
    }

    @GetMapping("/fator-emissao/ano/{ano}/mes/{mes}")
    public ResponseEntity<BigDecimal> obterFatorEmissao(
            @PathVariable Integer ano, @PathVariable Integer mes) {
        BigDecimal fator = fatorService.obterFatorEmissao(ano, mes);
        return ResponseEntity.ok(fator);
    }

    @GetMapping("/anos")
    public ResponseEntity<List<Integer>> listarAnosDisponiveis() {
        List<Integer> anos = fatorService.listarAnosDisponiveis();
        return ResponseEntity.ok(anos);
    }

    // Endpoints para Banco de Fatores
    @GetMapping("/banco-fatores")
    public ResponseEntity<List<BancoFatoresEnergia>> listarBancoFatores() {
        List<BancoFatoresEnergia> fatores = bancoFatoresService.listarTodos();
        return ResponseEntity.ok(fatores);
    }

    @GetMapping("/banco-fatores/ano/{ano}")
    public ResponseEntity<BancoFatoresEnergia> buscarFatorAnual(@PathVariable Integer ano) {
        return bancoFatoresService.buscarPorAno(ano)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lista-fatores/ano/{ano}")
    public ResponseEntity<List<FatorEnergiaEletrica>> listarFatoresPorAno(@PathVariable Integer ano) {
        List<FatorEnergiaEletrica> fatores = fatorService.listarPorAno(ano);
        return ResponseEntity.ok(fatores);
    }
    
    // NOVO ENDPOINT PARA RESOLVER O ERRO 500 - USUÁRIO ESPECÍFICO
    @GetMapping("/usuario/{usuarioId}/com-fatores")
    public ResponseEntity<List<EnergiaComFatorResponse>> listarPorUsuarioComFatores(@PathVariable Long usuarioId) {
        List<EnergiaComFatorResponse> dados = energiaDadosService.listarPorUsuarioComFatores(usuarioId);
        return ResponseEntity.ok(dados);
    }
    
    // ENDPOINT PARA TODOS OS DADOS COM FATORES
    @GetMapping("/todos/com-fatores")
    public ResponseEntity<List<EnergiaComFatorResponse>> listarTodosComFatores() {
        List<EnergiaComFatorResponse> dados = energiaDadosService.listarTodosComFatores();
        return ResponseEntity.ok(dados);
    }
}