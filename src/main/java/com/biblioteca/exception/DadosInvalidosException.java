package com.biblioteca.exception;

/**
 * Exceção customizada para dados inválidos ou regras de negócio violadas
 * Usada quando os dados fornecidos não atendem aos requisitos do sistema
 * Feature 2 - Exceções customizadas
 */
public class DadosInvalidosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }

    public DadosInvalidosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    // Métodos auxiliares para validações comuns
    public static DadosInvalidosException campoObrigatorio(String campo) {
        return new DadosInvalidosException(
                String.format("O campo '%s' é obrigatório", campo)
        );
    }

    public static DadosInvalidosException valorInvalido(String campo, String valor) {
        return new DadosInvalidosException(
                String.format("Valor inválido para o campo '%s': %s", campo, valor)
        );
    }

    public static DadosInvalidosException valorDuplicado(String campo, String valor) {
        return new DadosInvalidosException(
                String.format("Já existe um registro com %s: %s", campo, valor)
        );
    }
}