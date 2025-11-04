package com.biblioteca.service;

import com.biblioteca.domain.Emprestimo;
import com.biblioteca.domain.Leitor;
import com.biblioteca.exception.DadosInvalidosException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço para gerenciar Empréstimos
 * Feature 4 - Relacionamento OneToMany
 */
@Service
@Transactional
public class EmprestimoService implements CrudService<Emprestimo, Long> {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private LeitorService leitorService;

    /**
     * Inclui um novo empréstimo no sistema
     * Feature 4 - Validações e associação com Leitor
     */
    @Override
    public Emprestimo incluir(Emprestimo emprestimo) {
        validarEmprestimo(emprestimo);

        // Verifica se o leitor existe
        if (emprestimo.getLeitor() != null && emprestimo.getLeitor().getId() != null) {
            Leitor leitor = leitorService.buscarPorId(emprestimo.getLeitor().getId());
            emprestimo.setLeitor(leitor);
        }

        return repository.save(emprestimo);
    }

    /**
     * Altera um empréstimo existente
     */
    @Override
    public Emprestimo alterar(Long id, Emprestimo emprestimo) {
        Emprestimo existente = buscarPorId(id);
        validarEmprestimo(emprestimo);

        // Mantém o ID original
        emprestimo.setId(existente.getId());

        // Verifica se o leitor existe
        if (emprestimo.getLeitor() != null && emprestimo.getLeitor().getId() != null) {
            Leitor leitor = leitorService.buscarPorId(emprestimo.getLeitor().getId());
            emprestimo.setLeitor(leitor);
        }

        return repository.save(emprestimo);
    }

    /**
     * Busca um empréstimo por ID
     */
    @Override
    @Transactional(readOnly = true)
    public Emprestimo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Empréstimo não encontrado com ID: " + id));
    }

    /**
     * Lista todos os empréstimos
     */
    @Override
    @Transactional(readOnly = true)
    public List<Emprestimo> listarTodos() {
        return repository.findAll();
    }

    /**
     * Exclui um empréstimo
     */
    @Override
    public void excluir(Long id) {
        buscarPorId(id); // Verifica se existe
        repository.deleteById(id);
    }

    // ========== MÉTODOS ESPECÍFICOS ==========

    /**
     * Registra a devolução de um livro
     * Feature 4 - Método específico (PATCH)
     */
    public Emprestimo registrarDevolucao(Long id) {
        Emprestimo emprestimo = buscarPorId(id);

        if (emprestimo.getDevolvido()) {
            throw new DadosInvalidosException("Este livro já foi devolvido anteriormente");
        }

        emprestimo.setDevolvido(true);
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        return repository.save(emprestimo);
    }

    /**
     * Renova o prazo de um empréstimo
     * Método específico adicional (PATCH)
     */
    public Emprestimo renovarEmprestimo(Long id, Integer diasAdicionais) {
        Emprestimo emprestimo = buscarPorId(id);

        if (emprestimo.getDevolvido()) {
            throw new DadosInvalidosException("Não é possível renovar um livro já devolvido");
        }

        if (diasAdicionais <= 0 || diasAdicionais > 30) {
            throw new DadosInvalidosException("Dias adicionais deve estar entre 1 e 30");
        }

        LocalDate novaData = emprestimo.getDataDevolucaoPrevista().plusDays(diasAdicionais);
        emprestimo.setDataDevolucaoPrevista(novaData);

        return repository.save(emprestimo);
    }

    // ========== QUERY METHODS ==========

    /**
     * Lista empréstimos de um leitor específico
     * Feature 4 - Query Method através de relacionamento
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> listarPorLeitor(Long leitorId) {
        return repository.findByLeitorId(leitorId);
    }

    /**
     * Lista empréstimos ativos (não devolvidos)
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> listarAtivos() {
        return repository.findByDevolvido(false);
    }

    /**
     * Lista empréstimos devolvidos
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> listarDevolvidos() {
        return repository.findByDevolvido(true);
    }

    /**
     * Busca empréstimos por título do livro (parcial, case insensitive)
     * Feature 4 - Query Method com Containing e IgnoreCase
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> buscarPorTitulo(String titulo) {
        return repository.findByTituloLivroContainingIgnoreCase(titulo);
    }

    /**
     * Busca empréstimos em um período
     * Feature 4 - Query Method com Between
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new DadosInvalidosException("Data inicial não pode ser posterior à data final");
        }
        return repository.findByDataEmprestimoBetween(inicio, fim);
    }

    /**
     * Lista empréstimos atrasados (não devolvidos e com prazo vencido)
     * Feature 4 - Query Method complexo com múltiplos critérios
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> listarAtrasados() {
        return repository.findByDataDevolucaoPrevistaBeforeAndDevolvidoFalse(LocalDate.now());
    }

    /**
     * Lista empréstimos de um leitor por status de devolução
     * Feature 4 - Query Method com múltiplos critérios através de relacionamento
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> listarPorLeitorEStatus(Long leitorId, Boolean devolvido) {
        return repository.findByLeitorIdAndDevolvido(leitorId, devolvido);
    }

    // ========== VALIDAÇÕES PRIVADAS ==========

    /**
     * Valida os dados de um empréstimo
     */
    private void validarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo.getTituloLivro() == null || emprestimo.getTituloLivro().trim().isEmpty()) {
            throw new DadosInvalidosException("Título do livro é obrigatório");
        }

        if (emprestimo.getDataEmprestimo() == null) {
            throw new DadosInvalidosException("Data de empréstimo é obrigatória");
        }

        if (emprestimo.getDataDevolucaoPrevista() == null) {
            throw new DadosInvalidosException("Data de devolução prevista é obrigatória");
        }

        if (emprestimo.getDataDevolucaoPrevista().isBefore(emprestimo.getDataEmprestimo())) {
            throw new DadosInvalidosException(
                    "Data de devolução prevista não pode ser anterior à data de empréstimo");
        }

        if (emprestimo.getLeitor() == null || emprestimo.getLeitor().getId() == null) {
            throw new DadosInvalidosException("Leitor é obrigatório");
        }

        if (emprestimo.getDevolvido() == null) {
            emprestimo.setDevolvido(false); // Define como false por padrão
        }
    }
}