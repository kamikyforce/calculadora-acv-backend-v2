package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.CombustivelRequest;
import br.gov.serpro.calculadoraacv.dto.CombustivelResponse;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import br.gov.serpro.calculadoraacv.exception.DuplicacaoRegistroException;
import br.gov.serpro.calculadoraacv.model.Combustivel;
import br.gov.serpro.calculadoraacv.service.CombustivelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/combustiveis")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br", "https://tes-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CombustivelController {

    @Autowired
    private CombustivelService combustivelService;

    @GetMapping
    public ResponseEntity<List<CombustivelResponse>> listar(@RequestParam(required = false) EscopoEnum escopo) {
        List<Combustivel> combustiveis;
        if (escopo != null) {
            combustiveis = combustivelService.listarPorEscopo(escopo);
        } else {
            combustiveis = combustivelService.listarTodos();
        }

        List<CombustivelResponse> response = combustiveis.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CombustivelResponse> buscarPorId(@PathVariable Long id) {
        Optional<Combustivel> combustivel = combustivelService.buscarPorId(id);
        return combustivel.map(c -> ResponseEntity.ok(convertToResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTipos() {
        List<String> tipos = combustivelService.listarTipos();
        return ResponseEntity.ok(tipos);
    }

    // 🔥 NOVO: endpoint para listar por tipo (alinha com o front /tipo/{tipo})
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CombustivelResponse>> listarPorTipo(@PathVariable String tipo) {
        List<Combustivel> combustiveis = combustivelService.listarPorTipo(tipo);
        List<CombustivelResponse> response = combustiveis.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CombustivelResponse>> buscarPorNome(
            @RequestParam String nome,
            @RequestParam(required = false) EscopoEnum escopo) {
        List<Combustivel> combustiveis;
        if (escopo != null) {
            combustiveis = combustivelService.buscarPorNomeEEscopo(nome, escopo);
        } else {
            combustiveis = combustivelService.buscarPorNome(nome);
        }

        List<CombustivelResponse> response = combustiveis.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CombustivelResponse> criar(@Valid @RequestBody CombustivelRequest request) {
        System.out.println("=== INÍCIO CRIAÇÃO COMBUSTÍVEL ===");
        System.out.println("Nome recebido: " + request.getNome());
        System.out.println("Escopo recebido: " + request.getEscopo());

        boolean existeNesseEscopo = combustivelService.existePorNomeEEscopo(request.getNome(), request.getEscopo());
        System.out.println("Combustível já existe neste escopo? " + existeNesseEscopo);

        if (existeNesseEscopo) {
            System.out.println("❌ ERRO: Tentativa de criar combustível duplicado no escopo: " + request.getNome() + " - " + request.getEscopo());
            throw new DuplicacaoRegistroException(
                String.format("Já existe um combustível com o nome '%s' no %s",
                    request.getNome(), request.getEscopo()),
                "COMBUSTIVEL_DUPLICADO"
            );
        }

        System.out.println("✅ Criando novo combustível: " + request.getNome() + " para escopo " + request.getEscopo());
        Combustivel combustivel = convertToEntity(request);
        Combustivel salvo = combustivelService.salvar(combustivel);
        System.out.println("✅ Combustível criado com ID: " + salvo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CombustivelResponse> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody CombustivelRequest request) {
        System.out.println("=== INÍCIO ATUALIZAÇÃO COMBUSTÍVEL ===");
        System.out.println("ID recebido: " + id);
        System.out.println("Nome recebido: " + request.getNome());
        System.out.println("Escopo recebido: " + request.getEscopo());

        try {
            Optional<Combustivel> combustivelExistente = combustivelService.buscarPorId(id);
            if (!combustivelExistente.isPresent()) {
                System.out.println("ERRO: Combustível com ID " + id + " não encontrado");
                return ResponseEntity.notFound().build();
            }

            final Combustivel existente = combustivelExistente.get();

            // Novo: permitir atualização cross-escopo (upsert no escopo solicitado)
            if (request.getEscopo() != null &&
                existente.getEscopo() != null &&
                request.getEscopo() != existente.getEscopo()) {
                final Long usuarioId = existente.getUsuarioId();
                System.out.println("Atualização cross-escopo solicitada. Base=" + existente.getEscopo()
                        + " -> Destino=" + request.getEscopo() + ", usuarioId=" + usuarioId);

                Combustivel alvo = combustivelService.upsertPorNomeEEscopoParaUsuario(convertToEntity(request), usuarioId);
                System.out.println("Cross-escopo atualizado/criado com sucesso! ID alvo: " + (alvo != null ? alvo.getId() : null));
                System.out.println("=== FIM ATUALIZAÇÃO COMBUSTÍVEL ===");
                return ResponseEntity.ok(convertToResponse(alvo));
            }

            final Long usuarioId = existente.getUsuarioId();
            boolean existeOutroComMesmoNomeEEscopo =
                combustivelService.existePorNomeEEscopoExcluindoIdPorUsuario(
                    request.getNome(), request.getEscopo(), id, usuarioId
                );

            if (existeOutroComMesmoNomeEEscopo) {
                System.out.println("ERRO: Já existe outro combustível com o nome: " + request.getNome() + " no escopo " + request.getEscopo());
                return ResponseEntity.badRequest().build();
            }

            System.out.println("Iniciando atualização...");
            Combustivel combustivelAtualizado = convertToEntity(request);
            Combustivel salvo = combustivelService.atualizar(id, combustivelAtualizado);

            System.out.println("Combustível atualizado com sucesso!");
            System.out.println("=== FIM ATUALIZAÇÃO COMBUSTÍVEL ===");

            return ResponseEntity.ok(convertToResponse(salvo));
        } catch (Exception e) {
            System.out.println("ERRO na atualização: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private CombustivelResponse convertToResponse(Combustivel combustivel) {
        CombustivelResponse response = new CombustivelResponse();
        response.setId(combustivel.getId());
        response.setNome(combustivel.getNome());
        response.setTipo(combustivel.getTipo());
        response.setFatorEmissaoCO2(combustivel.getFatorEmissaoCO2());
        response.setFatorEmissaoCH4(combustivel.getFatorEmissaoCH4());
        response.setFatorEmissaoN2O(combustivel.getFatorEmissaoN2O());
        response.setUnidade(combustivel.getUnidade());
        response.setEscopo(combustivel.getEscopo());
        response.setUsuarioId(combustivel.getUsuarioId());
        response.setDataCriacao(combustivel.getDataCriacao());
        response.setDataAtualizacao(combustivel.getDataAtualizacao());
        return response;
    }

    private Combustivel convertToEntity(CombustivelRequest request) {
        return new Combustivel(
            request.getNome(),
            request.getTipo(),
            request.getFatorEmissaoCO2(),
            request.getFatorEmissaoCH4(),
            request.getFatorEmissaoN2O(),
            request.getUnidade(),
            request.getEscopo(),
            null // usuarioId pode vir do contexto de segurança
        );
    }
}
