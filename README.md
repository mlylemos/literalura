# 📚 LiterAlura — Catálogo de Livros

![Java](https://img.shields.io/badge/Java-17+-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue?logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-4+-red?logo=apachemaven)
![Status](https://img.shields.io/badge/Status-Concluído-success)

> Challenge desenvolvido no programa **Alura ONE (Oracle Next Education)** — trilha de especialização em Java Back-End.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Como Executar](#-como-executar)
- [Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Autor](#-autor)

---

## 📖 Sobre o Projeto

O **LiterAlura** é uma aplicação de linha de comando (CLI) desenvolvida em **Java + Spring Boot** que consome a [API Gutendex](https://gutendex.com/) para buscar e catalogar livros do [Projeto Gutenberg](https://www.gutenberg.org/) — uma biblioteca com mais de 70 mil obras do domínio público.

Os dados são persistidos em um banco de dados **PostgreSQL**, permitindo consultas ricas sobre livros, autores, idiomas e estatísticas de downloads.

### Objetivo de Aprendizado

- Consumo de API REST com `java.net.http.HttpClient`
- Desserialização de JSON com **Jackson** (`@JsonAlias`, `@JsonIgnoreProperties`)
- Persistência com **Spring Data JPA** (derived queries, JPQL)
- Mapeamento objeto-relacional com **Hibernate**
- Uso de Streams, `DoubleSummaryStatistics` e Lambdas
- Boas práticas: separação de camadas, DTOs, Records, Enums

---

## ✨ Funcionalidades

| # | Funcionalidade | Fonte de dados |
|---|----------------|---------------|
| 1 | 🔍 Buscar livro por título | API Gutendex |
| 2 | 📖 Listar todos os livros cadastrados | Banco de dados |
| 3 | 👤 Listar todos os autores cadastrados | Banco de dados |
| 4 | 🗓 Listar autores vivos em determinado ano | Banco de dados |
| 5 | 🌐 Listar livros por idioma | Banco de dados |
| 6 | 📊 Estatísticas de downloads (média, max, min) | Banco de dados |
| 7 | 🏆 Top 10 livros mais baixados | Banco de dados |
| 8 | 🔎 Buscar autor por nome | Banco de dados |
| 9 | 🗺 Contagem de livros por idioma | Banco de dados |

---

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|-----------|
| Java | 17+ | Linguagem principal |
| Spring Boot | 3.2.3 | Framework de aplicação |
| Spring Data JPA | 3.2.3 | Camada de persistência |
| Hibernate | (via Spring) | ORM |
| PostgreSQL | 16+ | Banco de dados relacional |
| Jackson Databind | 2.16.1 | Desserialização de JSON |
| Maven | 4+ | Gerenciamento de dependências |

---

## 🏛 Arquitetura

O projeto segue uma arquitetura **em camadas** bem definida:

```
┌──────────────────────────────────────────┐
│            Principal (CLI)               │  ← Interação com o usuário
├──────────────────────────────────────────┤
│            LivroService                  │  ← Regras de negócio
├──────────────────────────────────────────┤
│   ConsumoApiService │ ConversorJsonService│  ← Consumo de API / JSON
├──────────────────────────────────────────┤
│  LivroRepository  │  AutorRepository     │  ← Acesso ao banco de dados
├──────────────────────────────────────────┤
│         Livro  │  Autor  │  Idioma       │  ← Entidades / Modelo
└──────────────────────────────────────────┘
                     ↕
              PostgreSQL Database
```

### Relacionamento entre Entidades

```
Autor (1) ──────── (N) Livro
  - id                 - id
  - nome               - titulo
  - anoNascimento      - idioma (Enum)
  - anoFalecimento     - numeroDownloads
                       - autor_id (FK)
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java JDK 17 ou superior
- Maven 4+
- PostgreSQL 16+

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/literalura.git
cd literalura
```

### 2. Configure o banco de dados

Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE literalura;
```

### 3. Configure as variáveis de ambiente

Você pode exportar as variáveis ou editar `application.properties`:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=literalura
export DB_USER=postgres
export DB_PASSWORD=sua_senha
```

### 4. Compile e execute

```bash
mvn clean package -DskipTests
java -jar target/literalura-0.0.1-SNAPSHOT.jar
```

Ou diretamente pelo Maven:

```bash
mvn spring-boot:run
```

---

## 🗄 Configuração do Banco de Dados

O Spring Boot com `ddl-auto=update` cria automaticamente as tabelas ao iniciar. A estrutura gerada é:

```sql
-- Tabela de autores
CREATE TABLE autores (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(255) NOT NULL,
    ano_nascimento   INTEGER,
    ano_falecimento  INTEGER
);

-- Tabela de livros
CREATE TABLE livros (
    id                BIGSERIAL PRIMARY KEY,
    titulo            VARCHAR(255) NOT NULL UNIQUE,
    idioma            VARCHAR(50)  NOT NULL,
    numero_downloads  INTEGER,
    autor_id          BIGINT REFERENCES autores(id)
);
```

---

## 💻 Exemplos de Uso

```
╔══════════════════════════════════╗
║         📚  L I T E R A L U R A  ║
╚══════════════════════════════════╝
  Escolha uma opção:
  1 - 🔍 Buscar livro por título
  2 - 📖 Listar todos os livros
  ...

▶ Opção: 1

🔍 Digite o título do livro: hamlet

🌐 Consultando a API Gutendex...

✅ Livro encontrado e salvo no catálogo:

┌─ Livro ──────────────────────────────────────────────┐
│ Título:     Hamlet                                   │
│ Autor:      Shakespeare, William                     │
│ Idioma:     Inglês (en)                              │
│ Downloads:  280,437                                  │
└──────────────────────────────────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
literalura/
├── src/
│   ├── main/
│   │   ├── java/com/alura/literalura/
│   │   │   ├── LiteraluraApplication.java     # Entry point + CommandLineRunner
│   │   │   ├── principal/
│   │   │   │   └── Principal.java             # Menu interativo
│   │   │   ├── model/
│   │   │   │   ├── Livro.java                 # Entidade JPA
│   │   │   │   ├── Autor.java                 # Entidade JPA
│   │   │   │   └── Idioma.java                # Enum de idiomas
│   │   │   ├── dto/
│   │   │   │   ├── DadosLivroDTO.java         # Record para JSON
│   │   │   │   ├── DadosAutorDTO.java         # Record para JSON
│   │   │   │   └── RespostaGutendexDTO.java   # Record para resposta paginada
│   │   │   ├── repository/
│   │   │   │   ├── LivroRepository.java       # Spring Data JPA
│   │   │   │   └── AutorRepository.java       # Spring Data JPA
│   │   │   └── service/
│   │   │       ├── LivroService.java          # Regras de negócio
│   │   │       ├── ConsumoApiService.java     # HttpClient
│   │   │       └── ConversorJsonService.java  # ObjectMapper
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/alura/literalura/
│           ├── ConversorJsonServiceTest.java
│           └── IdiomaTest.java
└── pom.xml
```

---

## 👨‍💻 Autor

Desenvolvido como parte do **Programa ONE — Oracle Next Education + Alura**.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?logo=linkedin)](https://linkedin.com/in/seu-perfil)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?logo=github)](https://github.com/seu-usuario)

---

*"A leitura torna o homem completo." — Francis Bacon*
