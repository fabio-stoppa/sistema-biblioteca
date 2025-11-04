package com.biblioteca.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe para padronizar as respostas de erro da API
 * Feature 4 - Estrutura de erro padronizada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp do momento em que o erro ocorreu
     */
    private LocalDateTime timestamp;

    /**
     * Código de status HTTP (400, 404, 500, etc.)
     */
    private Integer status;

    /**
     * Nome do erro HTTP (Bad Request, Not Found, etc.)
     */
    private String error;

    /**
     * Mensagem descritiva do erro
     */
    private String message;

    /**
     * Caminho da requisição que gerou o erro
     */
    private String path;

    /**
     * Lista de detalhes adicionais do erro (opcional)
     * Usado principalmente para erros de validação
     */
    private List<String> details;

    /**
     * Construtor para erros simples sem detalhes
     */
    public ErrorResponse(LocalDateTime timestamp, Integer status, String error,
                         String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Método auxiliar para criar resposta de erro
     */
    public static ErrorResponse criar(Integer status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    /**
     * Método auxiliar para criar resposta de erro com detalhes
     */
    public static ErrorResponse criarComDetalhes(Integer status, String error,
                                                 String message, String path,
                                                 List<String> details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .details(details)
                .build();
    }
}