package com.biblioteca.loader;

import com.biblioteca.domain.Endereco;
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
 *
 * Formato do arquivo:
 * nome;email;cpf;telefone;matricula;fidelidade;limiteCredito;dataUltimaLeitura;cep;logradouro;complemento;numero;bairro;cidade;uf
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

                // Criar endereço
                Endereco endereco = new Endereco();
                endereco.setCep(dados[8]);
                endereco.setLogradouro(dados[9]);
                endereco.setComplemento(dados[10]);
                endereco.setNumero(dados[11]);
                endereco.setBairro(dados[12]);
                endereco.setCidade(dados[13]);
                endereco.setEstado(dados[14]); // UF

                // Criar leitor
                Leitor leitor = new Leitor();
                leitor.setNome(dados[0]);
                leitor.setEmail(dados[1]);
                leitor.setCpf(dados[2]);
                leitor.setTelefone(dados[3]);
                leitor.setMatricula(dados[4]);
                leitor.setFidelidade(dados[5]);
                leitor.setLimiteCredito(Double.parseDouble(dados[6]));

                // Data ultima leitura pode ser null
                if (dados.length > 7 && !dados[7].trim().isEmpty() && !dados[7].equalsIgnoreCase("null")) {
                    leitor.setDataUltimaLeitura(LocalDate.parse(dados[7]));
                }

                leitor.setEndereco(endereco);
                leitor.setAtivo(true);

                service.incluir(leitor);
                contador++;
            }

            System.out.println("✅ " + contador + " leitores carregados com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar leitores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}