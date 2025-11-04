package com.biblioteca.service;

import com.biblioteca.domain.Bibliotecario;
import com.biblioteca.exception.DadosInvalidosException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.BibliotecarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para gerenciar Bibliotecários
 * Feature 2 e 3 - Implementação completa de CRUD com JPA
 */
@Service
@Transactional
public class BibliotecarioService implements CrudService<Bibliotecario, Long> {

    @Autowired
    private BibliotecarioRepository repository;

    /**
     * Inclui um novo bibliotecário no sistema
     * Feature 2 - Validações e verificação de duplicidade
     */
    @Override
    public Bibliotecario incluir(Bibliotecario bibliotecario) {
        validarBibliotecario(bibliotecario);

        // Verifica duplicidade de CPF
        if (repository.findByCpf(bibliotecario.getCpf()).isPresent()) {
            throw new DadosInvalidosException("CPF já cadastrado: " + bibliotecario.getCpf());
        }

        // Verifica duplicidade de matrícula
        if (repository.findByMatricula(bibliotecario.getMatricula()).isPresent()) {
            throw new DadosInvalidosException("Matrícula já cadastrada: " + bibliotecario.getMatricula());
        }

        return repository.save(bibliotecario);
    }

    /**
     * Altera um bibliotecário existente
     * Feature 2 - Alteração integral (PUT)
     */
    @Override
    public Bibliotecario alterar(Long id, Bibliotecario bibliotecario) {
        Bibliotecario existente = buscarPorId(id);

        validarBibliotecario(bibliotecario);

        // Mantém o ID original
        bibliotecario.setId(existente.getId());

        return repository.save(bibliotecario);
    }

    /**
     * Busca um bibliotecário por ID
     * Feature 3 - Lança exceção se não encontrar
     */
    @Override
    @Transactional(readOnly = true)
    public Bibliotecario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Bibliotecário não encontrado com ID: " + id));
    }

    /**
     * Lista todos os bibliotecários
     * Feature 1 - Operação básica de listagem
     */
    @Override
    @Transactional(readOnly = true)
    public List<Bibliotecario> listarTodos() {
        return repository.findAll();
    }

    /**
     * Exclui um bibliotecário
     * Feature 2 - Operação DELETE
     */
    @Override
    public void excluir(Long id) {
        buscarPorId(id); // Verifica se existe
        repository.deleteById(id);
    }

    // ========== MÉTODOS ESPECÍFICOS (além do CRUD) ==========

    /**
     * Inativa um bibliotecário
     * Feature 2 - Método específico (PATCH)
     */
    public Bibliotecario inativar(Long id) {
        Bibliotecario bibliotecario = buscarPorId(id);
        bibliotecario.setAtivo(false);
        return repository.save(bibliotecario);
    }

    /**
     * Ativa um bibliotecário
     * Método específico adicional (PATCH)
     */
    public Bibliotecario ativar(Long id) {
        Bibliotecario bibliotecario = buscarPorId(id);
        bibliotecario.setAtivo(true);
        return repository.save(bibliotecario);
    }

    // ========== QUERY METHODS ==========

    /**
     * Busca bibliotecário por CPF
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public Bibliotecario buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Bibliotecário não encontrado com CPF: " + cpf));
    }

    /**
     * Lista apenas bibliotecários ativos
     * Feature 4 - Query Method
     */
    @Transactional(readOnly = true)
    public List<Bibliotecario> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    /**
     * Busca bibliotecários por nome (parcial, case insensitive)
     * Feature 4 - Query Method com Containing e IgnoreCase
     */
    @Transactional(readOnly = true)
    public List<Bibliotecario> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Busca bibliotecários por faixa salarial
     * Feature 4 - Query Method com Between
     */
    @Transactional(readOnly = true)
    public List<Bibliotecario> buscarPorFaixaSalarial(Double min, Double max) {
        if (min > max) {
            throw new DadosInvalidosException("Salário mínimo não pode ser maior que o máximo");
        }
        return repository.findBySalarioBetween(min, max);
    }

    /**
     * Busca bibliotecários por nome e status ativo
     * Feature 4 - Query Method com múltiplos critérios
     */
    @Transactional(readOnly = true)
    public List<Bibliotecario> buscarPorNomeEAtivo(String nome, Boolean ehAtivo) {
        return repository.findByNomeContainingIgnoreCaseAndAtivo(nome, ehAtivo);
    }

    // ========== VALIDAÇÕES PRIVADAS ==========

    /**
     * Valida os dados de um bibliotecário
     * Feature 2 - Validações de negócio
     */
    private void validarBibliotecario(Bibliotecario bibliotecario) {
        if (bibliotecario.getNome() == null || bibliotecario.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("Nome é obrigatório");
        }

        // Matrícula é String na entidade; validação por não vazio e formato opcional (apenas dígitos, mínimo 4)
        if (bibliotecario.getMatricula() == null || bibliotecario.getMatricula().trim().isEmpty()) {
            throw new DadosInvalidosException("Matrícula é obrigatória");
        }
        String matricula = bibliotecario.getMatricula().trim();
        if (!matricula.matches("\\d{4,}")) {
            throw new DadosInvalidosException("Matrícula deve conter ao menos 4 dígitos numéricos");
        }

        if (bibliotecario.getSalario() == null || bibliotecario.getSalario() < 1320.00) {
            throw new DadosInvalidosException("Salário não pode ser inferior ao salário mínimo (R$ 1.320,00)");
        }

        if (bibliotecario.getAtivo() == null) {
            throw new DadosInvalidosException("Status ativo é obrigatório");
        }
    }
}