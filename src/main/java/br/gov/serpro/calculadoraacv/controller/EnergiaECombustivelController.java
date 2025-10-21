package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.EnergiaECombustivelRequest;
import br.gov.serpro.calculadoraacv.dto.EnergiaECombustivelResponse;
import br.gov.serpro.calculadoraacv.dto.EnergiaEdicaoResponse;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import br.gov.serpro.calculadoraacv.enums.StatusCalculo;
import br.gov.serpro.calculadoraacv.enums.TipoDado;
import br.gov.serpro.calculadoraacv.dto.DadoMensalEdicao;
import br.gov.serpro.calculadoraacv.dto.EnergiaComFatorResponse;
import br.gov.serpro.calculadoraacv.model.EnergiaECombustivel;
import br.gov.serpro.calculadoraacv.service.EnergiaECombustivelService;
import br.gov.serpro.calculadoraacv.service.EnergiaDadosService;

import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/energia-combustiveis")
@CrossOrigin(origins = "http://localhost:4200")
public class EnergiaECombustivelController {
    
    private static final Logger logger = LoggerFactory.getLogger(EnergiaECombustivelController.class);
    
    @Autowired
    private EnergiaECombustivelService energiaService;
    
    @Autowired
    private EnergiaDadosService energiaDadosService;
    
    // ===================== LISTAGENS ===================== //
    @GetMapping("/usuario/{usuarioId}/com-fatores")
    public ResponseEntity<List<EnergiaComFatorResponse>> listarPorUsuarioComFatores(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(energiaDadosService.listarPorUsuarioComFatores(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnergiaECombustivelResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(
            energiaService.listarPorUsuario(usuarioId).stream().map(this::convertToResponse).collect(Collectors.toList())
        );
    }

    @GetMapping("/escopo/{escopo}")
    public ResponseEntity<List<EnergiaECombustivelResponse>> listarPorEscopo(@PathVariable String escopo) {
        return ResponseEntity.ok(
            energiaService.listarPorEscopo(escopo).stream().map(this::convertToResponse).collect(Collectors.toList())
        );
    }

    @GetMapping("/usuario/{usuarioId}/escopo/{escopo}")
    public ResponseEntity<List<EnergiaECombustivelResponse>> listarPorUsuarioEEscopo(
            @PathVariable Long usuarioId, @PathVariable String escopo) {
        return ResponseEntity.ok(
            energiaService.listarPorUsuarioEEscopo(usuarioId, escopo).stream().map(this::convertToResponse).collect(Collectors.toList())
        );
    }

    @GetMapping("/escopos")
    public ResponseEntity<List<String>> listarEscopos() {
        return ResponseEntity.ok(energiaService.listarEscoposDisponiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnergiaECombustivelResponse> buscarPorId(@PathVariable Long id) {
        return energiaService.buscarPorId(id)
                .map(energia -> ResponseEntity.ok(convertToResponse(energia)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTiposEnergia() {
        return ResponseEntity.ok(energiaService.listarTiposEnergia());
    }

    @GetMapping
    public ResponseEntity<List<EnergiaECombustivelResponse>> listarTodos() {
        return ResponseEntity.ok(
            energiaService.listarTodos().stream().map(this::convertToResponse).collect(Collectors.toList())
        );
    }

    // ===================== CRUD PRINCIPAL ===================== //
    @PostMapping
    public ResponseEntity<EnergiaECombustivelResponse> criar(@Valid @RequestBody EnergiaECombustivelRequest request) {
        logger.info("Criando novo registro: Usuario={}, Ano={}, Escopo={}, TipoDado={}", 
            request.getUsuarioId(), request.getAnoReferencia(), request.getEscopo(), request.getTipoDado());

        try {
            // 🚀 Agora não sobrescreve mais, sempre cria um novo registro
            EnergiaECombustivel energia = convertToEntity(request);
            EnergiaECombustivel salvo = energiaService.salvar(energia);

            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(salvo));
        } catch (Exception e) {
            logger.error("Erro ao criar energia - Usuario={}, Erro={}", request.getUsuarioId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergiaECombustivelResponse> atualizar(@PathVariable Long id, 
                                                               @Valid @RequestBody EnergiaECombustivelRequest request) {
        logger.info("Atualizando energia ID={} - Usuario={}, Ano={}, Tipo={}", 
            id, request.getUsuarioId(), request.getAnoReferencia(), request.getTipoDado());

        try {
            EnergiaECombustivel energiaAtualizada = convertToEntity(request);
            EnergiaECombustivel salvo = energiaService.atualizar(id, energiaAtualizada);
            return ResponseEntity.ok(convertToResponse(salvo));
        } catch (Exception e) {
            logger.error("Erro ao atualizar energia ID={} - Erro={}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            energiaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===================== UTILITÁRIOS ===================== //
    private EnergiaECombustivelResponse convertToResponse(EnergiaECombustivel energia) {
        EnergiaECombustivelResponse response = new EnergiaECombustivelResponse();
        response.setId(energia.getId());
        response.setUsuarioId(energia.getUsuarioId());
        response.setTipoEnergia(energia.getTipoEnergia());
        response.setFonteEnergia(energia.getFonteEnergia());
        response.setConsumoAnual(energia.getConsumoAnual());
        response.setUnidade(energia.getUnidade());
        response.setFatorEmissao(energia.getFatorEmissao());
        response.setFatorMedioAnual(energia.getFatorMedioAnual()); // ← ADICIONAR ESTA LINHA
        response.setEscopo(energia.getEscopo());
        response.setAnoReferencia(energia.getAnoReferencia());
        response.setDataCriacao(energia.getDataCriacao());
        response.setDataAtualizacao(energia.getDataAtualizacao());
        response.setDadosMensais(energia.getDadosMensaisJson()); // usa o campo JSON
        return response;
    }

    private EnergiaECombustivel convertToEntity(EnergiaECombustivelRequest request) {
        // Normaliza o escopo (aceita ESCOPO2, escopo2, ESCOPO3_PRODUCAO, etc.)
        String escopoCodigo = request.getEscopo() != null
            ? EscopoEnum.fromString(request.getEscopo()).getCodigo()
            : null;

        EnergiaECombustivel energia = new EnergiaECombustivel(
            request.getUsuarioId(),
            request.getTipoEnergia(),
            request.getFonteEnergia(),
            request.getConsumoAnual(),
            request.getUnidade(),
            request.getFatorEmissao(),
            escopoCodigo // <— usa o código normalizado
        );

        energia.setAnoReferencia(request.getAnoReferencia());
        energia.setFatorMedioAnual(request.getFatorMedioAnual());
        energia.setTipoDado(request.getTipoDado() != null ? request.getTipoDado() : TipoDado.CONSOLIDADO_ANUAL);
        energia.setVersao(request.getVersao() != null ? request.getVersao() : 1);
        energia.setStatusCalculo(StatusCalculo.PENDENTE);
        energia.setMesesPreenchidos(0);
        energia.setUsuarioUltimaEdicao(request.getUsuarioId());
        energia.setObservacoesAuditoria(request.getObservacoesAuditoria());

        if (request.getDadosMensais() != null && !request.getDadosMensais().isEmpty()) {
            energia.setTipoDado(TipoDado.MENSAL);
            try {
                StringBuilder jsonBuilder = new StringBuilder("[");
                for (int i = 0; i < request.getDadosMensais().size(); i++) {
                    var dado = request.getDadosMensais().get(i);
                    if (i > 0) jsonBuilder.append(",");
                    jsonBuilder.append(String.format("{\"mes\":%d,\"valor\":%s}", dado.getMes(), dado.getValor().toString()));
                }
                jsonBuilder.append("]");
                energia.setDadosMensaisJson(jsonBuilder.toString());
                energia.setMesesPreenchidos(request.getDadosMensais().size());
    
                // NOVA LÓGICA: Calcular média anual independente da quantidade de meses
                // Soma todos os valores (incluindo zeros) e divide por 12
                List<BigDecimal> valoresPreenchidos = request.getDadosMensais().stream()
                    .map(EnergiaECombustivelRequest.DadoMensal::getValor)
                    .filter(valor -> valor.compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
                
                if (!valoresPreenchidos.isEmpty()) {
                    // Soma TODOS os valores dos 12 meses (incluindo zeros)
                    BigDecimal somaTotal = request.getDadosMensais().stream()
                        .map(EnergiaECombustivelRequest.DadoMensal::getValor)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    // Calcula a média sempre dividindo por 12
                    energia.setMediaAnualCalculada(
                        somaTotal.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP)
                    );
                    
                    // Define status baseado na quantidade de meses preenchidos
                    if (valoresPreenchidos.size() == 12) {
                        energia.setStatusCalculo(StatusCalculo.COMPLETO);
                    } else {
                        energia.setStatusCalculo(StatusCalculo.PARCIAL);
                    }
                } else {
                    energia.setStatusCalculo(StatusCalculo.PENDENTE);
                }
            } catch (Exception e) {
                logger.error("Erro ao processar dados mensais: {}", e.getMessage());
                throw new RuntimeException("Erro ao processar dados mensais", e);
            }
        } else {
            energia.setTipoDado(TipoDado.CONSOLIDADO_ANUAL);
            energia.setStatusCalculo(StatusCalculo.COMPLETO);
        }

        return energia;
    }

    // ===================== OUTROS ENDPOINTS ===================== //
    @PutMapping("/{id}/ano-referencia")
    public ResponseEntity<EnergiaComFatorResponse> atualizarAnoReferencia(
            @PathVariable Long id, 
            @RequestParam @Min(1990) @Max(2030) @NotNull Integer anoReferencia) {
        try {
            Optional<EnergiaECombustivel> energiaOpt = energiaService.buscarPorId(id);
            if (energiaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            EnergiaECombustivel energia = energiaOpt.get();
            energia.setAnoReferencia(anoReferencia);
            energia.setDataAtualizacao(LocalDateTime.now());
            energiaService.salvar(energia);

            return energiaDadosService.listarPorUsuarioComFatores(energia.getUsuarioId()).stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/todos/com-fatores")
    public ResponseEntity<List<EnergiaComFatorResponse>> listarTodosComFatores() {
        return ResponseEntity.ok(energiaDadosService.listarTodosComFatores());
    }

    // ===================== ENDPOINT ESPECÍFICO PARA EDIÇÃO ===================== //
    /**
     * Endpoint específico para edição que direciona o usuário baseado no tipo de dado:
     * - CONSOLIDADO_ANUAL: retorna dados básicos para edição simples
     * - MENSAL: retorna dados mensais estruturados para edição detalhada
     */
    @GetMapping("/{id}/edicao")
    public ResponseEntity<EnergiaEdicaoResponse> buscarParaEdicao(@PathVariable Long id) {
        return energiaService.buscarPorId(id)
                .map(energia -> {
                    EnergiaEdicaoResponse response = new EnergiaEdicaoResponse();
                    
                    // Dados básicos
                    response.setId(energia.getId());
                    response.setUsuarioId(energia.getUsuarioId());
                    response.setTipoEnergia(energia.getTipoEnergia());
                    response.setFonteEnergia(energia.getFonteEnergia());
                    response.setUnidade(energia.getUnidade());
                    response.setFatorEmissao(energia.getFatorEmissao());
                    response.setEscopo(energia.getEscopo());
                    response.setAnoReferencia(energia.getAnoReferencia());
                    response.setTipoDado(energia.getTipoDado());
                    response.setVersao(energia.getVersao());
                    response.setObservacoesAuditoria(energia.getObservacoesAuditoria());
                    
                    // Direcionamento baseado no tipo de dado
                    if (energia.getTipoDado() == TipoDado.MENSAL) {
                        response.setModoEdicao("MENSAL");
                        response.setInstrucoes("Edite os valores mensais. O sistema calculará automaticamente a média anual quando todos os 12 meses estiverem preenchidos.");
                        
                        // Parse dos dados mensais JSON para estrutura editável
                        if (energia.getDadosMensaisJson() != null && !energia.getDadosMensaisJson().isEmpty()) {
                            try {
                                // Simples parsing do JSON para lista de dados mensais
                                String json = energia.getDadosMensaisJson();
                                List<DadoMensalEdicao> dadosMensais = parseDadosMensaisJson(json);
                                response.setDadosMensais(dadosMensais);
                            } catch (Exception e) {
                                logger.warn("Erro ao fazer parse dos dados mensais para ID {}: {}", id, e.getMessage());
                                response.setDadosMensais(new ArrayList<>());
                            }
                        } else {
                            response.setDadosMensais(new ArrayList<>());
                        }
                        
                        response.setStatusCalculo(energia.getStatusCalculo());
                        response.setMesesPreenchidos(energia.getMesesPreenchidos());
                        response.setMediaAnualCalculada(energia.getMediaAnualCalculada());
                        
                    } else {
                        response.setModoEdicao("CONSOLIDADO");
                        response.setInstrucoes("Edite o valor consolidado anual. Este valor representa o total do ano.");
                        response.setConsumoAnual(energia.getConsumoAnual());
                        response.setFatorMedioAnual(energia.getFatorMedioAnual());
                    }
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Método auxiliar para fazer parse dos dados mensais JSON
     */
    private List<DadoMensalEdicao> parseDadosMensaisJson(String json) {
        List<DadoMensalEdicao> dados = new ArrayList<>();
        
        // Remove colchetes e divide por vírgulas
        json = json.replace("[", "").replace("]", "");
        String[] items = json.split("},\\s*\\{");
        
        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            String[] parts = item.split(",");
            
            Integer mes = null;
            BigDecimal valor = null;
            
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].replace("\"", "").trim();
                    String value = keyValue[1].replace("\"", "").trim();
                    
                    if ("mes".equals(key)) {
                        mes = Integer.parseInt(value);
                    } else if ("valor".equals(key)) {
                        valor = new BigDecimal(value);
                    }
                }
            }
            
            if (mes != null && valor != null) {
                DadoMensalEdicao dado = new DadoMensalEdicao();
                dado.setMes(mes);
                dado.setValor(valor);
                dados.add(dado);
            }
        }
        
        return dados;
    }
}
