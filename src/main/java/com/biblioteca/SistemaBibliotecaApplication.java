package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Sistema de Biblioteca
 * Projeto de Disciplina - Arquitetura Java
 * 
 * @author Fabio Luis
 * @version 1.0
 */
@SpringBootApplication
public class SistemaBibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaBibliotecaApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("📚 SISTEMA DE BIBLIOTECA INICIADO COM SUCESSO!");
        System.out.println("==============================================");
        System.out.println("🌐 Acesse: http://localhost:8080");
        System.out.println("🗄️  Console H2: http://localhost:8080/h2-console");
        System.out.println("==============================================\n");
    }
}
