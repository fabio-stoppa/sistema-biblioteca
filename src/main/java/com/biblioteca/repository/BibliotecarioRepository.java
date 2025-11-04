package com.biblioteca.repository;

import com.biblioteca.domain.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Long> {

    // Query Methods personalizados
    Optional<Bibliotecario> findByCpf(String cpf);

    Optional<Bibliotecario> findByMatricula(String matricula);

    List<Bibliotecario> findByAtivoTrue();

    List<Bibliotecario> findByNomeContainingIgnoreCase(String nome);

    List<Bibliotecario> findBySalarioBetween(Double salarioMin, Double salarioMax);

    List<Bibliotecario> findByNomeContainingIgnoreCaseAndAtivo(String nome, Boolean ativo);
}

