package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.IndustriaRequest;
import br.gov.serpro.calculadoraacv.dto.IndustriaResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioIndustriaResponse;
import br.gov.serpro.calculadoraacv.service.IndustriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/industrias")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class IndustriaController {

    private final IndustriaService service;

    @PostMapping
    public ResponseEntity<IndustriaResponse> criar(@Valid @RequestBody IndustriaRequest request) {
        IndustriaResponse response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IndustriaResponse>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo) {

        List<IndustriaResponse> industrias = service.listar(estado, ativo);
        return ResponseEntity.ok(industrias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndustriaResponse> buscar(@PathVariable Long id) {
        IndustriaResponse industria = service.buscarPorId(id);
        return ResponseEntity.ok(industria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndustriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody IndustriaRequest request) {
        IndustriaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.alterarStatus(id, true);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.alterarStatus(id, false);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/usuarios")
    public ResponseEntity<List<UsuarioIndustriaResponse>> buscarUsuarios(@PathVariable Long id) {
        List<UsuarioIndustriaResponse> usuarios = service.buscarUsuariosPorIndustria(id);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/cnpj/{cnpj}/existe")
    public ResponseEntity<Boolean> verificarCnpjExiste(@PathVariable String cnpj) {
        boolean existe = service.verificarCnpjExiste(cnpj);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cpf/{cpf}")
    public ResponseEntity<Boolean> verificarCpfExisteEmOutraIndustria(
            @PathVariable String cpf,
            @RequestParam(required = false) Long industriaId) {
        boolean existe = service.verificarCpfExisteEmOutraIndustria(cpf, industriaId);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cnpj-certificadora/{cnpj}")
    public ResponseEntity<Boolean> verificarCnpjExisteEmCertificadora(@PathVariable String cnpj) {
        boolean existe = service.verificarCnpjExisteEmCertificadora(cnpj);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cpf-certificadora/{cpf}")
    public ResponseEntity<Boolean> verificarCpfExisteEmCertificadora(@PathVariable String cpf) {
        boolean existe = service.verificarCpfExisteEmCertificadora(cpf);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-usuario-ativo/{cpf}")
    public ResponseEntity<IndustriaService.UsuarioAtivoInfo> verificarUsuarioAtivoEmOutroLocal(
            @PathVariable String cpf,
            @RequestParam(required = false) Long industriaId) {
        IndustriaService.UsuarioAtivoInfo info = service.verificarUsuarioAtivoEmOutroLocal(cpf, industriaId);
        return ResponseEntity.ok(info);
    }
}