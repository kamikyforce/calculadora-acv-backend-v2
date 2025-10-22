package br.gov.bndes.calculadoraacv.mut;

import br.gov.bndes.calculadoraacv.mut.exception.DadosDuplicadosException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MutExceptionHandler {

    @ExceptionHandler(DadosDuplicadosException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicados(DadosDuplicadosException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("codigo", "DADOS_DUPLICADOS");
        body.put("mensagem", "Dados duplicados detectados. Verifique se o registro já existe.");
        body.put("idExistente", ex.getIdExistente());
        body.put("escopo", ex.getEscopo());
        body.put("tipoFatorSolo", ex.getTipoFatorSolo());
        body.put("usoAnterior", ex.getUsoAnterior());
        body.put("usoAtual", ex.getUsoAtual());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}