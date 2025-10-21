package br.gov.serpro.calculadoraacv.config;

import br.gov.serpro.calculadoraacv.dto.ErrorResponse;
import br.gov.serpro.calculadoraacv.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Exceções de entidade não encontrada
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        log.error("Entidade não encontrada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("ENTIDADE_NAO_ENCONTRADA", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Recurso não encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("RECURSO_NAO_ENCONTRADO", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Exceções de validação e regras de negócio
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(ValidacaoException ex) {
        log.error("Erro de validação: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("ERRO_VALIDACAO", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorResponse> handleRegraDeNegocio(RegraDeNegocioException ex) {
        log.error("Violação de regra de negócio: {} - Código: {}", ex.getMessage(), ex.getCodigo());
        ErrorResponse error = new ErrorResponse(ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exceções de duplicação
    @ExceptionHandler(DuplicacaoRegistroException.class)
    public ResponseEntity<ErrorResponse> handleDuplicacaoRegistro(DuplicacaoRegistroException ex) {
        log.error("Tentativa de duplicação de registro: {} - Código: {}", ex.getMessage(), ex.getCodigo());
        ErrorResponse error = new ErrorResponse(ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Exceções de precisão decimal
    @ExceptionHandler(PrecisaoDecimalException.class)
    public ResponseEntity<ErrorResponse> handlePrecisaoDecimal(PrecisaoDecimalException ex) {
        log.error("Erro de precisão decimal: {} - Código: {}", ex.getMessage(), ex.getCodigo());
        ErrorResponse error = new ErrorResponse(ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exceções de tipo de dado
    @ExceptionHandler(TipoDadoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleTipoDadoInvalido(TipoDadoInvalidoException ex) {
        log.error("Tipo de dado inválido: {} - Código: {}", ex.getMessage(), ex.getCodigo());
        ErrorResponse error = new ErrorResponse(ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exceções de escopo
    @ExceptionHandler(EscopoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleEscopoInvalido(EscopoInvalidoException ex) {
        log.error("Escopo inválido: {} - Código: {}", ex.getMessage(), ex.getCodigo());
        ErrorResponse error = new ErrorResponse(ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exceções de integridade de dados
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Violação de integridade de dados: {}", ex.getMessage());
        
        String mensagem = "Violação de restrição de dados";
        String codigo = "INTEGRIDADE_DADOS";
        
        // Detectar tipos específicos de violação
        if (ex.getMessage().contains("uk_energia_usuario_ano")) {
            mensagem = "Já existe um registro para este usuário e ano. Para editar os dados existentes, utilize a opção 'Editar' na listagem.";
            codigo = "REGISTRO_DUPLICADO";
        } else if (ex.getMessage().contains("unique") || ex.getMessage().contains("duplicate")) {
            mensagem = "Dados duplicados detectados. Verifique se o registro já existe.";
            codigo = "DADOS_DUPLICADOS";
        }
        
        ErrorResponse error = new ErrorResponse(codigo, mensagem);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Exceções de validação de campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Erro de validação de campos: {}", ex.getMessage());
        
        StringBuilder mensagem = new StringBuilder("Erro de validação nos seguintes campos: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            mensagem.append(String.format("%s (%s); ", error.getField(), error.getDefaultMessage()))
        );
        
        ErrorResponse error = new ErrorResponse("CAMPOS_INVALIDOS", mensagem.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exceções de argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento inválido: {}", ex.getMessage());
        
        String mensagem = ex.getMessage();
        String codigo = "ARGUMENTO_INVALIDO";
        
        // Detectar tipos específicos de argumentos inválidos
        if (mensagem.contains("Escopo inválido")) {
            codigo = "ESCOPO_INVALIDO";
            mensagem = "Escopo informado não é válido. Escopos permitidos: ESCOPO1, ESCOPO2, ESCOPO3_PRODUCAO, ESCOPO3_TRANSPORTE.";
        }
        
        ErrorResponse error = new ErrorResponse(codigo, mensagem);
        return ResponseEntity.badRequest().body(error);
    }

    // Exceções de dados não legíveis
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Dados não legíveis na requisição: {}", ex.getMessage());

        String mensagem = "Dados inválidos no corpo da requisição";
        String codigo = "DADOS_INVALIDOS";

        // Detectar erros específicos de deserialização
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            if (ife.getTargetType().isEnum()) {
                mensagem = "Valor inválido para campo enum. Verifique os valores permitidos.";
                codigo = "ENUM_INVALIDO";
            } else if (ife.getTargetType().equals(java.math.BigDecimal.class)) {
                mensagem = "Formato numérico inválido. Use ponto como separador decimal.";
                codigo = "FORMATO_NUMERICO_INVALIDO";
            }
        }

        ErrorResponse error = new ErrorResponse(codigo, mensagem);
        return ResponseEntity.badRequest().body(error);
    }

    // Exceção genérica (deve ser a última)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Erro interno do servidor: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("ERRO_INTERNO", "Erro interno do servidor. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
