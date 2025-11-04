package com.biblioteca.service;

import com.biblioteca.domain.Leitor;
import com.biblioteca.exception.DadosInvalidosException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.LeitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço para gerenciar Leitores
 * Feature 2 - Segunda entidade filha com CRUD completo
 */
@Service
@Transactional
public class LeitorService implements CrudService<Leitor, Long> {

    @Autowired
    private LeitorRepository repository;

    /**
     * Inclui um novo leitor no sistema
     */
    @Override
    public Leitor incluir(Leitor leitor) {
        validarLeitor(leitor);

        // Verifica duplicidade de CPF
        if (repository.findByCpf(leitor.getCpf()).isPresent()) {
            throw new DadosInvalidosException("CPF já cadastrado: " + leitor.getCpf());
        }

        return repository.save(leitor);
    }

    /**
     * Altera um leitor existente
     */
    @Override
    public Leitor alterar(Long id, Leitor leitor) {
        Leitor existente = buscarPorId(id);
        validarLeitor(leitor);

        // Mantém o ID original
        leitor.setId(existente.getId());

        return repository.save(leitor);
    }

    /**
     * Busca um leitor por ID
     */
    @Override
    @Transactional(readOnly = true)
    public Leitor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Leitor não encontrado com ID: " + id));
    }

    /**
     * Lista todos os leitores
     */
    @Override
    @Transactional(readOnly = true)
    public List<Leitor> listarTodos() {
        return repository.findAll();
    }

    /**
     * Exclui um leitor
     */
    @Override
    public void excluir(Long id) {
        buscarPorId(id); // Verifica se existe
        repository.deleteById(id);
    }

    // ========== MÉTODOS ESPECÍFICOS ==========

    /**
     * Atualiza a categoria de fidelidade de um leitor
     * Feature 2 - Método específico (PATCH)
     */
    public Leitor atualizarFidelidade(Long id, String novaFidelidade) {
        Leitor leitor = buscarPorId(id);

        if (!novaFidelidade.matches("BRONZE|PRATA|OURO|DIAMANTE")) {
            throw new DadosInvalidosException(
                    "Categoria de fidelidade inválida. Valores aceitos: BRONZE, PRATA, OURO, DIAMANTE");
        }

        leitor.setFidelidade(novaFidelidade);
        return repository.save(leitor);
    }

    /**
     * Atualiza o limite de crédito de um leitor
     * Método específico adicional (PATCH)
     */
    public Leitor atualizarLimiteCredito(Long id, Double novoLimite) {
        Leitor leitor = buscarPorId(id);

        if (novoLimite < 0 || novoLimite > 10000) {
            throw new DadosInvalidosException(
                    "Limite de crédito deve estar entre R$ 0,00 e R$ 10.000,00");
        }

        leitor.setLimiteCredito(novoLimite);
        return repository.save(leitor);
    }

    // ========== QUERY METHODS ==========

    /**
     * Busca leitor por CPF
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public Leitor buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Leitor não encontrado com CPF: " + cpf));
    }

    /**
     * Busca leitores por categoria de fidelidade
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public List<Leitor> buscarPorFidelidade(String fidelidade) {
        return repository.findByFidelidade(fidelidade);
    }

    /**
     * Busca leitores por nome (parcial, case insensitive)
     * Feature 4 - Query Method com Containing e IgnoreCase
     */
    @Transactional(readOnly = true)
    public List<Leitor> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Busca leitores com limite de crédito maior ou igual ao especificado
     * Feature 4 - Query Method com GreaterThanEqual
     */
    @Transactional(readOnly = true)
    public List<Leitor> buscarPorLimiteMinimo(Double limiteMinimo) {
        return repository.findByLimiteCreditoGreaterThanEqual(limiteMinimo);
    }

    /**
     * Busca leitores que leram após determinada data
     * Feature 4 - Query Method com After
     */
    @Transactional(readOnly = true)
    public List<Leitor> buscarLeitoresAtivosDesde(LocalDate data) {
        return repository.findByDataUltimaLeituraAfter(data);
    }

    /**
     * Busca leitores por fidelidade e limite mínimo
     * Feature 4 - Query Method com múltiplos critérios
     */
    @Transactional(readOnly = true)
    public List<Leitor> buscarPorFidelidadeELimite(String fidelidade, Double limiteMinimo) {
        return repository.findByFidelidadeAndLimiteCreditoGreaterThan(fidelidade, limiteMinimo);
    }

    // ========== VALIDAÇÕES PRIVADAS ==========

    /**
     * Valida os dados de um leitor
     */
    private void validarLeitor(Leitor leitor) {
        if (leitor.getNome() == null || leitor.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("Nome é obrigatório");
        }

        if (leitor.getFidelidade() == null ||
                !leitor.getFidelidade().matches("BRONZE|PRATA|OURO|DIAMANTE")) {
            throw new DadosInvalidosException(
                    "Categoria de fidelidade inválida. Valores aceitos: BRONZE, PRATA, OURO, DIAMANTE");
        }

        if (leitor.getLimiteCredito() == null ||
                leitor.getLimiteCredito() < 0 ||
                leitor.getLimiteCredito() > 10000) {
            throw new DadosInvalidosException(
                    "Limite de crédito deve estar entre R$ 0,00 e R$ 10.000,00");
        }
    }
}