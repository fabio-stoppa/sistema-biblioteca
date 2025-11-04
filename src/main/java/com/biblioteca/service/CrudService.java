package com.biblioteca.service;

import java.util.List;

/**
 * Interface genérica para operações CRUD
 * Feature 1 e 2 - Contrato para serviços de domínio
 *
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador
 */
public interface CrudService<T, ID> {

    /**
     * Inclui uma nova entidade no sistema
     * @param entidade Entidade a ser incluída
     * @return Entidade incluída com ID gerado
     */
    T incluir(T entidade);

    /**
     * Altera uma entidade existente
     * @param id Identificador da entidade
     * @param entidade Novos dados da entidade
     * @return Entidade alterada
     */
    T alterar(ID id, T entidade);

    /**
     * Busca uma entidade por seu identificador
     * @param id Identificador da entidade
     * @return Entidade encontrada
     * @throws RecursoNaoEncontradoException se não encontrar
     */
    T buscarPorId(ID id);

    /**
     * Lista todas as entidades
     * @return Lista de todas as entidades
     */
    List<T> listarTodos();

    /**
     * Exclui uma entidade
     * @param id Identificador da entidade a ser excluída
     */
    void excluir(ID id);
}