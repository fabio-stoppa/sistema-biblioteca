package com.biblioteca.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "bibliotecarios")
public class Bibliotecario extends Pessoa {

    @NotBlank(message = "Código de funcionário é obrigatório")
    @Column(name = "codigo_funcionario", nullable = false, unique = true)
    private String codigoFuncionario;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    private String turno;

    private Boolean ativo;

    @Column(name = "salario")
    private Double salario;

    @NotBlank(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true)
    private String matricula;

    // Constructors
    public Bibliotecario() {
        this.dataAdmissao = LocalDate.now();
        this.ativo = true;
    }

    public Bibliotecario(String nome, String cpf, String email, String telefone,
                         Endereco endereco, String codigoFuncionario, String turno) {
        super(nome, cpf, email, telefone, endereco);
        this.codigoFuncionario = codigoFuncionario;
        this.turno = turno;
        this.dataAdmissao = LocalDate.now();
        this.ativo = true;
    }

    // Getters and Setters
    public String getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(String codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}