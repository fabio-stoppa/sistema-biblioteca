package com.biblioteca.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Classe de tratamento global de exceções da aplicação
 * Feature 4 - Tratamento centralizado com @ControllerAdvice
 *
 * Esta classe intercepta todas as exceções lançadas pelos controllers
 * e retorna respostas HTTP apropriadas com mensagens de erro padronizadas
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de recurso não encontrado
     * Retorna: 404 NOT FOUND
     *
     * Feature 4 - Tratamento de NoSuchElementException
     */
    @ExceptionHandler({RecursoNaoEncontradoException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleRecursoNaoEncontrado(
            RuntimeException ex, WebRequest request) {

        ErrorResponse error = ErrorResponse.criar(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                extrairPath(request)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata exceções de dados inválidos ou regras de negócio violadas
     * Retorna: 400 BAD REQUEST
     *
     * Feature 4 - Tratamento de IllegalArgumentException
     */
    @ExceptionHandler({DadosInvalidosException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleDadosInvalidos(
            RuntimeException ex, WebRequest request) {

        ErrorResponse error = ErrorResponse.criar(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                extrairPath(request)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata erros de validação do Bean Validation (@Valid)
     * Retorna: 400 BAD REQUEST com detalhes dos campos inválidos
     *
     * Feature 4 - Tratamento de MethodArgumentNotValidException
     * Este é um dos principais requisitos da Feature 4
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Coleta todos os erros de validação
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(String.format("%s: %s",
                    error.getField(),
                    error.getDefaultMessage()));
        }

        ErrorResponse error = ErrorResponse.criarComDetalhes(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Erro de validação nos dados fornecidos",
                extrairPath(request),
                details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata violações de integridade de dados do banco
     * Retorna: 409 CONFLICT
     *
     * Feature 4 - Tratamento de DataIntegrityViolationException
     * Ocorre quando há tentativa de inserir dados duplicados (CPF, matrícula, etc)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        String mensagem = "Violação de integridade de dados";

        // Tenta identificar o tipo de violação
        String exceptionMessage = ex.getMessage();
        if (exceptionMessage != null) {
            if (exceptionMessage.contains("Unique") || exceptionMessage.contains("unique")) {
                mensagem = "Já existe um registro com esses dados únicos (CPF, Matrícula, Email, etc)";
            } else if (exceptionMessage.contains("foreign key") || exceptionMessage.contains("FK")) {
                mensagem = "Não é possível realizar esta operação devido a relacionamentos existentes";
            } else if (exceptionMessage.contains("not-null") || exceptionMessage.contains("NULL")) {
                mensagem = "Existem campos obrigatórios que não foram preenchidos";
            }
        }

        ErrorResponse error = ErrorResponse.criar(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                mensagem,
                extrairPath(request)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Trata exceções genéricas não capturadas pelos outros handlers
     * Retorna: 500 INTERNAL SERVER ERROR
     *
     * Feature 4 - Tratamento de Exception genérica
     * Este é o handler "catch-all" para qualquer erro inesperado
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        // Em produção, não devemos expor detalhes internos do erro
        // Por isso usamos uma mensagem genérica
        ErrorResponse error = ErrorResponse.criar(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.",
                extrairPath(request)
        );

        // Log do erro para debugging (apenas em desenvolvimento)
        System.err.println("Erro não tratado: " + ex.getClass().getName());
        System.err.println("Mensagem: " + ex.getMessage());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Método auxiliar para extrair o path da requisição
     */
    private String extrairPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}