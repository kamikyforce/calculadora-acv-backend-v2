package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.CertificadoraRequest;
import br.gov.serpro.calculadoraacv.dto.CertificadoraResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioCertificadoraResponse;
import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import br.gov.serpro.calculadoraacv.service.CertificadoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificadoras")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CertificadoraController {

    private final CertificadoraService service;

    @PostMapping
    public ResponseEntity<CertificadoraResponse> criar(@Valid @RequestBody CertificadoraRequest request) {
        CertificadoraResponse response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CertificadoraResponse>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) TipoCertificadora tipo,
            @RequestParam(required = false) Boolean ativo) {

        List<CertificadoraResponse> certificadoras = service.listar(estado, tipo, ativo);
        return ResponseEntity.ok(certificadoras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificadoraResponse> buscar(@PathVariable Long id) {
        CertificadoraResponse certificadora = service.buscarPorId(id);
        return ResponseEntity.ok(certificadora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificadoraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CertificadoraRequest request) {
        CertificadoraResponse response = service.atualizar(id, request);
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
    public ResponseEntity<List<UsuarioCertificadoraResponse>> buscarUsuarios(@PathVariable Long id) {
        List<UsuarioCertificadoraResponse> usuarios = service.buscarUsuariosPorCertificadora(id);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/cnpj/{cnpj}/existe")
    public ResponseEntity<Boolean> verificarCnpjExiste(@PathVariable String cnpj) {
        boolean existe = service.verificarCnpjExiste(cnpj);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/cpf/{cpf}/existe")
    public ResponseEntity<Boolean> verificarCpfExisteEmOutraCertificadora(
            @PathVariable String cpf,
            @RequestParam(required = false) Long certificadoraId) {
        boolean existe = service.verificarCpfExisteEmOutraCertificadora(cpf, certificadoraId);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cnpj-industria/{cnpj}")
    public ResponseEntity<Boolean> verificarCnpjExisteEmIndustria(@PathVariable String cnpj) {
        boolean existe = service.verificarCnpjExisteEmIndustria(cnpj);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cpf-industria/{cpf}")
    public ResponseEntity<Boolean> verificarCpfExisteEmIndustria(@PathVariable String cpf) {
        boolean existe = service.verificarCpfExisteEmIndustria(cpf);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-usuario-ativo/{cpf}")
    public ResponseEntity<CertificadoraService.UsuarioAtivoInfo> verificarUsuarioAtivoEmOutroLocal(
            @PathVariable String cpf,
            @RequestParam(required = false) Long certificadoraId) {
        CertificadoraService.UsuarioAtivoInfo info = service.verificarUsuarioAtivoEmOutroLocal(cpf, certificadoraId);
        return ResponseEntity.ok(info);
    }
}