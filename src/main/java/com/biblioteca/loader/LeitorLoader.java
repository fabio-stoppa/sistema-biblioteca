package com.biblioteca.loader;

import com.biblioteca.domain.Leitor;
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
 * Loader para carregar leitores do arquivo leitores.txt
 * Feature 4 - População de dados via ApplicationRunner
 */
@Component
@Order(2)
public class LeitorLoader implements ApplicationRunner {

    @Autowired
    private LeitorService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n🔄 Carregando leitores...");

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("data/leitores.txt");

        if (is == null) {
            System.out.println("⚠️  Arquivo leitores.txt não encontrado!");
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

                Leitor leitor = new Leitor();
                leitor.setNome(dados[0]);
                leitor.setEmail(dados[1]);
                leitor.setCpf(dados[2]);
                leitor.setTelefone(dados[3]);
                leitor.setFidelidade(dados[4]);
                leitor.setLimiteCredito(Double.parseDouble(dados[5]));

                if (!dados[6].equals("null")) {
                    leitor.setDataUltimaLeitura(LocalDate.parse(dados[6]));
                }

                service.incluir(leitor);
                contador++;
            }

            System.out.println("✅ " + contador + " leitores carregados com sucesso!");
            System.out.println("\n📋 Lista de leitores:");
            service.listarTodos().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar leitores: " + e.getMessage());
        }
    }
}