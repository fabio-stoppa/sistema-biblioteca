package com.biblioteca.loader;

import com.biblioteca.domain.Emprestimo;
import com.biblioteca.domain.Leitor;
import com.biblioteca.repository.LeitorRepository;
import com.biblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Loader para carregar empréstimos do arquivo emprestimos.txt
 * Feature 4 - População de dados via ApplicationRunner
 *
 * Formato do arquivo:
 * cpfLeitor;tituloLivro;autor;isbn;dataEmprestimo;dataDevolucaoPrevista;dataDevolucaoReal;devolvido
 */
@Component
@Order(3)
public class EmprestimoLoader implements ApplicationRunner {

    @Autowired
    private EmprestimoService service;

    @Autowired
    private LeitorRepository leitorRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n🔄 Carregando empréstimos...");

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("data/emprestimos.txt");

        if (is == null) {
            System.out.println("⚠️  Arquivo emprestimos.txt não encontrado!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linha;
            int contador = 0;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(";");

                // Buscar leitor pelo CPF
                String cpfLeitor = dados[0];
                Optional<Leitor> leitorOpt = leitorRepository.findByCpf(cpfLeitor);

                if (leitorOpt.isEmpty()) {
                    System.err.println("⚠️  Leitor com CPF " + cpfLeitor + " não encontrado. Pulando empréstimo.");
                    continue;
                }

                // Criar empréstimo
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setLeitor(leitorOpt.get());
                emprestimo.setTituloLivro(dados[1]);
                emprestimo.setAutor(dados[2]); // Autor adicionado
                emprestimo.setIsbn(dados[3]);
                emprestimo.setDataEmprestimo(LocalDate.parse(dados[4]));
                emprestimo.setDataDevolucaoPrevista(LocalDate.parse(dados[5]));

                // Data devolução real pode ser null
                if (dados.length > 6 && !dados[6].trim().isEmpty() && !dados[6].equalsIgnoreCase("null")) {
                    emprestimo.setDataDevolucaoEfetiva(LocalDate.parse(dados[6]));
                }

                emprestimo.setDevolvido(Boolean.parseBoolean(dados[7]));

                service.incluir(emprestimo);
                contador++;
            }

            System.out.println("✅ " + contador + " empréstimos carregados com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar empréstimos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}