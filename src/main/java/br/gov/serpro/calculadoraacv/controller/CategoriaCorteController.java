package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.model.CategoriaCorte;
import br.gov.serpro.calculadoraacv.service.CategoriaCorteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bd-categorias-corte")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CategoriaCorteController {

    private final CategoriaCorteService service;

    @GetMapping
    public ResponseEntity<List<CategoriaCorte>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }
}