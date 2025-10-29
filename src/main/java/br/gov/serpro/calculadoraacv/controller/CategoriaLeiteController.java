package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.model.CategoriaLeite;
import br.gov.serpro.calculadoraacv.service.CategoriaLeiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria-leite")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class CategoriaLeiteController {
    
    @Autowired
    private CategoriaLeiteService service;
    
    @GetMapping
    public ResponseEntity<List<CategoriaLeite>> listar() {
        List<CategoriaLeite> categorias = service.listar();
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaLeite> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}