# Demo de uma API com Spring Boot e Sprint Security

## Funcionalidades

Este projeto é uma API desenvolvida com Spring Boot que oferece as seguintes funcionalidades:

### [Swagger UI](http://localhost:8080/swagger-ui.html) 
- **Cadastro de Usuários**: Endpoint `/users` com método POST permite cadastrar um novo usuário.
- **Listagem de Usuários**: Endpoint `/users` com método GET lista todos os usuários cadastrados. Este endpoint requer autenticação.
- **Autenticação**: Endpoint `/login` realiza a autenticação de um usuário e retorna um token JWT.

## Requisitos
- **Java 21**
- **Banco de Dados PostgreSQL** ou **Docker Compose** para executar o banco de dados em um contêiner.

## Segurança
A aplicação utiliza Spring Security para gerenciar a autenticação e autorização dos usuários. Para acessar os endpoints protegidos, é necessário incluir o token JWT no cabeçalho da requisição.

## Script de Inicialização
O script data.sql localizado em src/main/resources/data.sql é executado automaticamente no início da aplicação, criando a estrutura do banco de dados e registros de teste.

### Dados teste pré-cadastrados
   ```sh
   Usuário: admin com perfil ADMIN.
   Usuário: user com perfil USER.
   Usuário: master com perfils ADMIN e USER.
   A senha para todos são 01.
   ```

## Executando o Projeto

### Com PostgreSQL

1. Certifique-se de ter o PostgreSQL instalado e em execução.
2. Configure as credenciais do banco de dados no arquivo `application.properties`.
3. Execute a aplicação pela sua IDE de desenvolvimento ou compile e execute a aplicação com o Maven:
   ```sh
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

### Com Docker Compose
1. Certifique-se de ter o Docker e o Docker Compose instalados.
2. Execute o comando abaixo para iniciar os serviços:
   ```sh
   docker-compose up
   ```