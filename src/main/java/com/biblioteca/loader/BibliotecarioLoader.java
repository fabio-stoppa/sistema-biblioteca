package com.biblioteca.loader;

import com.biblioteca.domain.Bibliotecario;
import com.biblioteca.domain.Endereco;
import com.biblioteca.service.BibliotecarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loader para carregar bibliotecários do arquivo bibliotecarios.txt
 * Feature 4 - População de dados via ApplicationRunner
 *
 * Formato do arquivo:
 * nome;email;cpf;telefone;matricula;codigoFuncionario;cep;logradouro;complemento;numero;bairro;cidade;uf;estadoNome;salario;ativo;turno
 */
@Component
@Order(1)
public class BibliotecarioLoader implements ApplicationRunner {

    @Autowired
    private BibliotecarioService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n🔄 Carregando bibliotecários...");

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("data/bibliotecarios.txt");

        if (is == null) {
            System.out.println("⚠️  Arquivo bibliotecarios.txt não encontrado!");
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
                endereco.setCep(dados[6]);
                endereco.setLogradouro(dados[7]);
                endereco.setComplemento(dados[8]);
                endereco.setNumero(dados[9]);
                endereco.setBairro(dados[10]);
                endereco.setCidade(dados[11]);
                endereco.setEstado(dados[12]); // UF

                // Criar bibliotecário
                Bibliotecario bibliotecario = new Bibliotecario();
                bibliotecario.setNome(dados[0]);
                bibliotecario.setEmail(dados[1]);
                bibliotecario.setCpf(dados[2]);
                bibliotecario.setTelefone(dados[3]);
                bibliotecario.setMatricula(dados[4]);
                bibliotecario.setCodigoFuncionario(dados[5]);
                bibliotecario.setSalario(Double.parseDouble(dados[14]));
                bibliotecario.setAtivo(Boolean.parseBoolean(dados[15]));
                if (dados.length > 16 && !dados[16].trim().isEmpty()) {
                    bibliotecario.setTurno(dados[16]);
                }
                bibliotecario.setEndereco(endereco);

                service.incluir(bibliotecario);
                contador++;
            }

            System.out.println("✅ " + contador + " bibliotecários carregados com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar bibliotecários: " + e.getMessage());
            e.printStackTrace();
        }
    }
}