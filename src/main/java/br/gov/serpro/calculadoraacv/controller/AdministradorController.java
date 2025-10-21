package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.AdministradorRequest;
import br.gov.serpro.calculadoraacv.dto.AdministradorResponse;
import br.gov.serpro.calculadoraacv.service.AdministradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class AdministradorController {

    private final AdministradorService service;

    @PostMapping
    public ResponseEntity<AdministradorResponse> criar(@Valid @RequestBody AdministradorRequest request) {
        AdministradorResponse response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResponse>> listar() {
        List<AdministradorResponse> administradores = service.listar();
        return ResponseEntity.ok(administradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponse> buscar(@PathVariable Long id) {
        AdministradorResponse administrador = service.buscarPorId(id);
        return ResponseEntity.ok(administrador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AdministradorRequest request) {
        AdministradorResponse response = service.atualizar(id, request);
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

    @GetMapping("/verificar-cpf-vinculado/{cpf}")
    public ResponseEntity<Map<String, Object>> verificarCpfVinculado(@PathVariable String cpf) {
        Map<String, Object> resultado = service.verificarCpfVinculado(cpf);
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/verificar-usuario-inativo/{cpf}")
    public ResponseEntity<Boolean> verificarUsuarioInativoAdministrador(@PathVariable String cpf) {
        boolean usuarioInativo = service.verificarUsuarioInativoAdministrador(cpf);
        return ResponseEntity.ok(usuarioInativo);
    }
}