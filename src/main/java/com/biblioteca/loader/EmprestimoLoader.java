package com.biblioteca.loader;

import com.biblioteca.domain.Emprestimo;
import com.biblioteca.domain.Leitor;
import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LeitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;

/**
 * Loader para carregar empréstimos do arquivo emprestimos.txt
 * Feature 4 - População com relacionamento OneToMany
 * Busca o Leitor pelo CPF antes de associar ao empréstimo
 */
@Component
@Order(3)
public class EmprestimoLoader implements ApplicationRunner {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LeitorService leitorService;

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

                // Feature 4: Buscar o Leitor pelo CPF usando Query Method
                String cpfLeitor = dados[0];
                Leitor leitor = leitorService.buscarPorCpf(cpfLeitor);

                // Criar empréstimo
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setTituloLivro(dados[1]);
                emprestimo.setIsbn(dados[2]);
                emprestimo.setDataEmprestimo(LocalDate.parse(dados[3]));
                emprestimo.setDataDevolucaoPrevista(LocalDate.parse(dados[4]));

                if (!dados[5].equals("null")) {
                    emprestimo.setDataDevolucaoReal(LocalDate.parse(dados[5]));
                }

                emprestimo.setDevolvido(Boolean.parseBoolean(dados[6]));
                emprestimo.setLeitor(leitor);

                emprestimoService.incluir(emprestimo);
                contador++;
            }

            System.out.println("✅ " + contador + " empréstimos carregados com sucesso!");
            System.out.println("\n📋 Lista de empréstimos:");
            emprestimoService.listarTodos().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar empréstimos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}