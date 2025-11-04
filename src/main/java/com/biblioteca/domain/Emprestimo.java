package com.biblioteca.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Leitor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "leitor_id", nullable = false)
    @JsonBackReference
    private Leitor leitor;

    @NotBlank(message = "Título do livro é obrigatório")
    @Column(name = "titulo_livro", nullable = false)
    private String tituloLivro;

    private String autor;

    private String isbn;

    @Column(name = "data_emprestimo")
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao_prevista")
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_efetiva")
    private LocalDate dataDevolucaoEfetiva;

    private Boolean devolvido;

    // Constructors
    public Emprestimo() {
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(14);
        this.devolvido = false;
    }

    public Emprestimo(Leitor leitor, String tituloLivro, String autor, String isbn) {
        this.leitor = leitor;
        this.tituloLivro = tituloLivro;
        this.autor = autor;
        this.isbn = isbn;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(14);
        this.devolvido = false;
    }

    // Business methods
    public void realizarDevolucao() {
        this.dataDevolucaoEfetiva = LocalDate.now();
        this.devolvido = true;
    }

    public boolean isAtrasado() {
        return !devolvido && LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public void setLeitor(Leitor leitor) {
        this.leitor = leitor;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }
    
    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoEfetiva;
    }
    
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoEfetiva = dataDevolucaoReal;
    }

    public Boolean getDevolvido() {
        return devolvido;
    }

    public void setDevolvido(Boolean devolvido) {
        this.devolvido = devolvido;
    }
}