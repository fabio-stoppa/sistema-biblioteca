package com.biblioteca.repository;

import com.biblioteca.domain.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeitorRepository extends JpaRepository<Leitor, Long> {

    // Query Methods personalizados
    Optional<Leitor> findByCpf(String cpf);

    List<Leitor> findByFidelidade(String fidelidade);

    List<Leitor> findByNomeContainingIgnoreCase(String nome);

    List<Leitor> findByLimiteCreditoGreaterThanEqual(Double limiteMinimo);

    List<Leitor> findByDataUltimaLeituraAfter(LocalDate data);

    List<Leitor> findByFidelidadeAndLimiteCreditoGreaterThan(String fidelidade, Double limite);
}