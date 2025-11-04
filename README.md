# 📚 Sistema de Gestão de Biblioteca

## Projeto de Disciplina - Arquitetura Java

**Aluno:** Fabio Luis  
**Instituição:** Infnet  
**Curso:** Arquitetura de Software  
**Disciplina:** Arquitetura Java [25E4_2]  


---

## 📖 Sobre o Projeto

Sistema completo de gestão de biblioteca desenvolvido com Spring Boot, implementando todas as 4 features solicitadas no projeto de disciplina. O sistema gerencia bibliotecários, leitores e empréstimos de livros com persistência em banco de dados H2.

---

## 🎯 Features Implementadas

### ✅ Feature 1: Configuração Essencial e Gestão de Entidade Primária
- [x] Configuração do Spring Boot com Spring Initializr
- [x] Modelagem da entidade principal (Bibliotecario)
- [x] Implementação de camadas Controller e Service
- [x] Gestão em memória com Map
- [x] API REST com endpoints básicos
- [x] Loader para população inicial de dados

### ✅ Feature 2: Expansão do Modelo de Domínio e CRUD Completo
- [x] Herança: Classe abstrata Pessoa → Bibliotecario e Leitor
- [x] Associação: Relacionamento OneToOne (Bibliotecario ↔ Endereco)
- [x] CRUD completo para todas as entidades
- [x] Métodos específicos (inativar, atualizarFidelidade)
- [x] Loaders independentes para cada entidade
- [x] Exceções customizadas

### ✅ Feature 3: Persistência de Dados com BD e Refinamento de API
- [x] Migração de Map para JPA com H2 Database
- [x] Repositórios com Spring Data JPA
- [x] Mapeamento de entidades com @Entity
- [x] ResponseEntity com códigos HTTP apropriados
- [x] Console H2 habilitado
- [x] Relacionamentos JPA configurados

### ✅ Feature 4: Robustez, Validação Avançada e Relacionamentos Complexos
- [x] Bean Validation com anotações detalhadas
- [x] Tratamento global de exceções com @ControllerAdvice
- [x] Relacionamento OneToMany (Leitor → Emprestimos)
- [x] Query Methods avançados nos repositórios
- [x] Loader com associação dinâmica via CPF
- [x] Estrutura de erro padronizada (ErrorResponse)

---

## 🏗️ Arquitetura do Sistema

### Estrutura de Pacotes
```
com.biblioteca
├── SistemaBibliotecaApplication.java
├── domain/              # Entidades JPA
│   ├── Pessoa.java
│   ├── Bibliotecario.java
│   ├── Leitor.java
│   ├── Endereco.java
│   └── Emprestimo.java
├── repository/          # Camada de Persistência
│   ├── BibliotecarioRepository.java
│   ├── LeitorRepository.java
│   └── EmprestimoRepository.java
├── service/             # Camada de Negócio
│   ├── CrudService.java
│   ├── BibliotecarioService.java
│   ├── LeitorService.java
│   └── EmprestimoService.java
├── controller/          # Camada de API REST
│   ├── BibliotecarioController.java
│   ├── LeitorController.java
│   └── EmprestimoController.java
├── loader/              # Carga inicial de dados
│   ├── BibliotecarioLoader.java
│   ├── LeitorLoader.java
│   └── EmprestimoLoader.java
└── exception/           # Tratamento de Exceções
    ├── GlobalExceptionHandler.java
    ├── RecursoNaoEncontradoException.java
    ├── DadosInvalidosException.java
    └── ErrorResponse.java
```

### Modelo de Dados

```
Pessoa (Classe Abstrata)
├── Bibliotecario
│   └── Endereco (OneToOne)
└── Leitor
    └── Emprestimo[] (OneToMany)
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- IDE (Eclipse, IntelliJ ou VS Code)

### Passos para Execução

1. **Clone o repositório**
```bash
git clone [URL_DO_SEU_REPOSITORIO]
cd sistema-biblioteca
```

2. **Compile o projeto**
```bash
mvn clean install
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse os endpoints**
- API: `http://localhost:8080`
- Console H2: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:bibliotecadb`
    - Username: `sa`
    - Password: (deixe em branco)

---

## 🖥️ Como acessar o Console H2

1. Inicie a aplicação (veja a seção "Como Executar").
2. Acesse no navegador: `http://localhost:8080/h2-console`.
3. Na tela do H2, preencha os campos assim:
   - JDBC URL: `jdbc:h2:mem:bibliotecadb`
   - User Name: `sa`
   - Password: (deixe em branco)
4. Clique em "Connect".

Após conectar, você pode executar consultas SQL como:

```sql
SELECT * FROM BIBLIOTECARIO;
SELECT * FROM LEITOR;
SELECT * FROM EMPRESTIMO;
```


## 📡 Endpoints da API

### Bibliotecários (`/bibliotecarios`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/bibliotecarios` | Lista todos os bibliotecários |
| GET | `/bibliotecarios/{id}` | Busca por ID |
| GET | `/bibliotecarios/cpf/{cpf}` | Busca por CPF |
| GET | `/bibliotecarios/ativos` | Lista apenas ativos |
| GET | `/bibliotecarios/buscar?nome=` | Busca por nome |
| GET | `/bibliotecarios/salario?min=&max=` | Busca por faixa salarial |
| POST | `/bibliotecarios` | Cadastra novo bibliotecário |
| PUT | `/bibliotecarios/{id}` | Atualiza bibliotecário |
| PATCH | `/bibliotecarios/{id}/inativar` | Inativa bibliotecário |
| PATCH | `/bibliotecarios/{id}/ativar` | Ativa bibliotecário |
| DELETE | `/bibliotecarios/{id}` | Remove bibliotecário |

### Leitores (`/leitores`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/leitores` | Lista todos os leitores |
| GET | `/leitores/{id}` | Busca por ID |
| GET | `/leitores/cpf/{cpf}` | Busca por CPF |
| GET | `/leitores/fidelidade/{categoria}` | Busca por categoria |
| GET | `/leitores/buscar?nome=` | Busca por nome |
| POST | `/leitores` | Cadastra novo leitor |
| PUT | `/leitores/{id}` | Atualiza leitor |
| PATCH | `/leitores/{id}/fidelidade?novaFidelidade=` | Atualiza fidelidade |
| DELETE | `/leitores/{id}` | Remove leitor |

### Empréstimos (`/emprestimos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/emprestimos` | Lista todos os empréstimos |
| GET | `/emprestimos/{id}` | Busca por ID |
| GET | `/emprestimos/leitor/{leitorId}` | Lista por leitor |
| GET | `/emprestimos/ativos` | Lista empréstimos ativos |
| GET | `/emprestimos/atrasados` | Lista empréstimos atrasados |
| POST | `/emprestimos` | Cadastra novo empréstimo |
| PUT | `/emprestimos/{id}` | Atualiza empréstimo |
| PATCH | `/emprestimos/{id}/devolver` | Registra devolução |
| DELETE | `/emprestimos/{id}` | Remove empréstimo |

---

## 🧪 Testando com Postman

### Exemplo: Criar Bibliotecário

**POST** `http://localhost:8080/bibliotecarios`

```json
{
  "nome": "Teste Silva",
  "email": "teste@biblioteca.com",
  "cpf": "99988877766",
  "telefone": "11999887766",
  "matricula": 2001,
  "salario": 4000.00,
  "ehAtivo": true,
  "endereco": {
    "cep": "01310100",
    "logradouro": "Avenida Paulista",
    "complemento": "Apto 101",
    "unidade": "Torre B",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "estado": "São Paulo"
  }
}
```

### Exemplo: Criar Leitor

**POST** `http://localhost:8080/leitores`

```json
{
  "nome": "Teste Leitor",
  "email": "leitor@email.com",
  "cpf": "12312312399",
  "telefone": "11998765432",
  "fidelidade": "OURO",
  "limiteCredito": 2000.00,
  "dataUltimaLeitura": "2024-11-01"
}
```

### Exemplo: Criar Empréstimo

**POST** `http://localhost:8080/emprestimos`

```json
{
  "tituloLivro": "Clean Code",
  "isbn": "9780132350884",
  "dataEmprestimo": "2024-11-01",
  "dataDevolucaoPrevista": "2024-11-15",
  "devolvido": false,
  "leitor": {
    "id": 1
  }
}
```

---

## 📊 Validações Implementadas

### Bibliotecário
- Nome: 3-100 caracteres, obrigatório
- Email: formato válido, obrigatório
- CPF: 11 dígitos, obrigatório, único
- Telefone: 10-11 dígitos, obrigatório
- Matrícula: >= 1000, obrigatória, única
- Salário: >= R$ 1.320,00 e <= R$ 50.000,00

### Leitor
- Nome: 3-100 caracteres, obrigatório
- Email: formato válido, obrigatório
- CPF: 11 dígitos, obrigatório, único
- Telefone: 10-11 dígitos, obrigatório
- Fidelidade: BRONZE, PRATA, OURO ou DIAMANTE
- Limite de Crédito: 0 a R$ 10.000,00

### Empréstimo
- Título: 2-200 caracteres, obrigatório
- ISBN: 13 dígitos, obrigatório
- Data Empréstimo: não pode ser futura
- Data Devolução Prevista: deve ser futura
- Leitor: obrigatório (relacionamento)

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
- **H2 Database** (em memória)
- **Lombok** (redução de boilerplate)
- **Maven** (gerenciamento de dependências)
- **Jakarta EE** (Validation e Persistence)

---

## 📝 Conceitos Aplicados

### POO
- ✅ Herança (Pessoa → Bibliotecario, Leitor)
- ✅ Polimorfismo (CrudService interface)
- ✅ Encapsulamento (getters/setters com Lombok)
- ✅ Abstração (classe Pessoa abstrata)

### Spring Framework
- ✅ Injeção de Dependências (@Autowired)
- ✅ Arquitetura em Camadas (Controller → Service → Repository)
- ✅ Spring Data JPA
- ✅ Bean Validation
- ✅ Exception Handling (@ControllerAdvice)

### JPA/Hibernate
- ✅ @Entity, @Table, @Id, @GeneratedValue
- ✅ @OneToOne, @ManyToOne, @OneToMany
- ✅ @MappedSuperclass (herança)
- ✅ Cascade e Orphan Removal
- ✅ FetchType (LAZY/EAGER)

### API REST
- ✅ HTTP Methods (GET, POST, PUT, PATCH, DELETE)
- ✅ Status Codes apropriados (200, 201, 204, 400, 404, 409, 500)
- ✅ ResponseEntity
- ✅ @RequestParam, @PathVariable, @RequestBody
- ✅ Tratamento de erros estruturado

---

## 📦 Estrutura de Arquivos de Dados

Os arquivos de carga inicial estão em `src/main/resources/data/`:

- `bibliotecarios.txt` - 5 registros
- `leitores.txt` - 8 registros
- `emprestimos.txt` - 12 registros

Os loaders são executados na ordem: Bibliotecarios → Leitores → Emprestimos

---

## 🎓 Avaliação das Features

| Feature | Status | Observações |
|---------|--------|-------------|
| Feature 1 | ✅ Completa | Todas as configurações e entidade base |
| Feature 2 | ✅ Completa | Herança, associações e CRUD completo |
| Feature 3 | ✅ Completa | JPA/H2 e ResponseEntity implementados |
| Feature 4 | ✅ Completa | Validações, exceções e OneToMany |