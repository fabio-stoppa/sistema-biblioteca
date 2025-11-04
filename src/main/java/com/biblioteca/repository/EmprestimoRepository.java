package com.biblioteca.repository;

import com.biblioteca.domain.Emprestimo;
import com.biblioteca.domain.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Query Methods personalizados
    List<Emprestimo> findByLeitor(Leitor leitor);

    List<Emprestimo> findByLeitorId(Long leitorId);

    List<Emprestimo> findByDevolvido(Boolean devolvido);

    List<Emprestimo> findByTituloLivroContainingIgnoreCase(String titulo);

    List<Emprestimo> findByDataEmprestimoBetween(LocalDate inicio, LocalDate fim);

    List<Emprestimo> findByLeitorIdAndDevolvido(Long leitorId, Boolean devolvido);

    List<Emprestimo> findByDataDevolucaoPrevistaBeforeAndDevolvidoFalse(LocalDate data);
}