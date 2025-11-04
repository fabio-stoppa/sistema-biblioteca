package com.biblioteca.exception;

/**
 * Exceção customizada para recursos não encontrados
 * Usada quando uma busca por ID ou outro identificador não retorna resultados
 * Feature 2 - Exceções customizadas
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    // Métodos auxiliares para mensagens padronizadas
    public static RecursoNaoEncontradoException porId(String entidade, Long id) {
        return new RecursoNaoEncontradoException(
                String.format("%s não encontrado(a) com ID: %d", entidade, id)
        );
    }

    public static RecursoNaoEncontradoException porCpf(String entidade, String cpf) {
        return new RecursoNaoEncontradoException(
                String.format("%s não encontrado(a) com CPF: %s", entidade, cpf)
        );
    }
}