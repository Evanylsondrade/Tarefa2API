# DevShowcase API

Backend da plataforma **DevShowcase** — portfólio colaborativo para desenvolvedores.

## 🛠️ Stack Utilizada

- **Java 21** + **Spring Boot 3.3.4**
- **Spring Data JPA** / **Hibernate**
- **SpringDoc OpenAPI (Swagger UI)**
- **PostgreSQL** (produção) | **H2** (testes e desenvolvimento local)
- **Bean Validation** (jakarta.validation)
- **Maven Wrapper** (mvnw)

---

## 📁 Estrutura do Projeto

```text
src/main/java/com/devshowcase/api/
├── config/          # Configurações (OpenApiConfig / Swagger)
├── controller/      # Controllers REST (Profile, Project, Technology)
├── dto/             # DTOs de Entrada (Request) e Saída (Response)
├── entity/          # Entidades JPA (Profile, Project, Technology, Feedback)
├── exception/       # Exceções customizadas e GlobalExceptionHandler
├── repository/      # Repositórios Spring Data JPA
└── service/         # Regras de negócio
```

---

## 🔗 Modelagem de Domínio

```text
Profile 1 ──────── N Project
                   Project N ──────── N Technology
                   Project 1 ──────── N Feedback
```

---

## 📌 Endpoints Disponíveis

| Método | Endpoint                      | Descrição                                                                      |
|--------|-------------------------------|--------------------------------------------------------------------------------|
| POST   | `/api/profiles`               | Cadastrar novo perfil                                                          |
| GET    | `/api/profiles/{id}`          | Buscar perfil por ID                                                           |
| DELETE | `/api/profiles/{id}`          | Excluir perfil por ID                                                          |
| POST   | `/api/technologies`           | Cadastrar tecnologia                                                           |
| GET    | `/api/technologies`           | Listar todas as tecnologias                                                    |
| POST   | `/api/projects`               | Cadastrar projeto associado a perfil e tecnologias                             |
| GET    | `/api/projects`               | Buscar projetos com filtragem por tecnologia e paginação (`page`, `size`)      |
| POST   | `/api/projects/{id}/feedbacks` | Cadastrar feedback (nota 1 a 5 e comentário) e recalcular nota média do projeto|
| PUT    | `/api/projects/{id}/upvote`   | Incrementar curtidas/estrelas (upvotes) do projeto                            |

---

## 📖 Documentação Interativa (Swagger / OpenAPI)

Após iniciar a aplicação, a documentação interativa estará acessível em:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ⚠️ Tratamento Global de Exceções

O `GlobalExceptionHandler` intercepta todas as exceções e retorna respostas JSON padronizadas:

- **`400 Bad Request`**: Dados de entrada inválidos, validações do Bean Validation (`@Valid`) ou parâmetros de rota incorretos.
- **`404 Not Found`**: Perfil, Tecnologia ou Projeto não localizado (`ResourceNotFoundException`).
- **`409 Conflict`**: Conflito de dados, como e-mail ou tecnologia duplicada (`ConflictException`).
- **`500 Internal Server Error`**: Erros genéricos não tratados.

---

## 🚀 Configuração e Execução Local

### Pré-requisitos
- Java 21+ instalado

### Opção A: Executar com Banco H2 em Memória (Recomendado para Testes Rápidos)

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Console do Banco H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:devshowcase`, Usuário: `sa`, Senha: em branco).

### Opção B: Executar com PostgreSQL Local

1. Crie o banco de dados no PostgreSQL:
   ```sql
   CREATE DATABASE devshowcase;
   ```
2. Inicie a aplicação:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

---

## 🧪 Executar Testes Automatizados

```powershell
.\mvnw.cmd test
```

> Os testes utilizam banco H2 isolado em memória. Não requer banco de dados externo rodando.

---

## 🌐 Deploy em Produção (Supabase / Render)

### 1. Provisionar Banco de Dados PostgreSQL (Supabase ou Render PostgreSQL)
1. Acesse o [Supabase](https://supabase.com) ou o [Render](https://render.com).
2. Crie uma nova instância de **PostgreSQL Database**.
3. Obtenha a URL de Conexão JDBC (`jdbc:postgresql://<HOST>:<PORT>/<DATABASE>`), Usuário e Senha.

### 2. Configurar Variáveis de Ambiente no Render
Na plataforma Render (ou PaaS escolhida), configure as seguintes **Environment Variables**:

| Variável      | Descrição / Exemplo                                                    |
|---------------|------------------------------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres` |
| `DB_USERNAME` | `postgres.seu_subdominio`                                              |
| `DB_PASSWORD` | `sua_senha_segura`                                                     |
| `PORT`        | `8080` (geralmente atribuído automaticamente pelo Render)              |

### 3. Deploy Contínuo a partir do GitHub
1. Faça o commit e push do projeto no GitHub.
2. No Render, crie um **New Web Service**, conectando o repositório GitHub.
3. Defina o **Environment** como `Docker`.
4. Defina o **Docker Context** como `./devshowcase-api` e o **Dockerfile Path** como `./devshowcase-api/Dockerfile`.
5. Clique em **Deploy Web Service**. A API estará publicada e conectada ao banco PostgreSQL na nuvem!
