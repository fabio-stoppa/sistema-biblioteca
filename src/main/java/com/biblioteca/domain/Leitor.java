package com.biblioteca.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "leitores")
public class Leitor extends Pessoa {

    @NotBlank(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    private Boolean ativo;

    private String fidelidade;
    
    @Column(name = "limite_credito")
    private Double limiteCredito;

    @OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private LocalDate dataUltimaLeitura;

    // Constructors
    public Leitor() {
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
    }

    public Leitor(String nome, String cpf, String email, String telefone,
                  Endereco endereco, String matricula) {
        super(nome, cpf, email, telefone, endereco);
        this.matricula = matricula;
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
    }

    // Getters and Setters
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public String getFidelidade() {
        return fidelidade;
    }
    
    public void setFidelidade(String fidelidade) {
        this.fidelidade = fidelidade;
    }
    
    public Double getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public LocalDate getDataUltimaLeitura() {
        return dataUltimaLeitura;
    }

    public void setDataUltimaLeitura(LocalDate dataUltimaLeitura) {
        this.dataUltimaLeitura = dataUltimaLeitura;
    }
}