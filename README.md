# Calculadora ACV Backend
Calculadora ACV 


## 💻 Executando em desenvolvimento
Para iniciar o servidor de desenvolvimento local, execute!

```bash
./mvnw spring-boot:run
```

Uma vez que o servidor esteja em execução, a aplicação estará disponível em http://localhost:8080/.

## ⛁ Banco de dados local

Para subir o banco usando docker, execute:
```
docker-compose -f src/docker/postgresql.yml up -d
```


## 🚀 Build
Para compilar o projeto, execute:

```bash
./mvnw clean package
```

Isso irá gerar um arquivo JAR no diretório target, que pode ser usado para executar a aplicação.

## 🧪 Executando testes unitários
Para executar os testes unitários, utilize o seguinte comando:

```bash
./mvnw test
```

Para executar os testes unitários e gerar o relatório de cobertura de código, utilize o seguinte comando:

```bash
./mvnw clean verify
```
O relatório de cobertura será gerado no diretório target/site/jacoco/. Abra o arquivo index.html no navegador para visualizar o relatório.

## Swagger
Para acessar o swagger, abra o link abaixo no seu navegador com o projeto em execução:
```
http://localhost:8080/calculadoraacv/backend/public/swagger-ui/index.html
```


## 🛠️ Tecnologias utilizadas
- Spring Boot: Framework principal para desenvolvimento da aplicação.
- Maven: Gerenciador de dependências e build.
- Java 21: Linguagem de programação utilizada.

## 🧭 Migrações (Flyway) e Perfis Maven/Spring

Os perfis Maven `local` e `prod` controlam as credenciais do Flyway (`flyway.url`, `flyway.user`, `flyway.password`). Por padrão, o perfil `local` fica ativo; para produção, use `-Pprod`. Você também pode sobrescrever com variáveis de ambiente (`FLYWAY_URL`, `FLYWAY_USER`, `FLYWAY_PASSWORD`).

Comandos para a base LOCAL:
```bash
.\mvnw -Plocal flyway:info
```
```bash
.\mvnw -Plocal flyway:repair
```
```bash
.\mvnw -Plocal flyway:migrate
```

Comandos para a base de PRODUÇÃO:
```bash
.\mvnw -Pprod flyway:info
```
```bash
.\mvnw -Pprod flyway:repair
```
```bash
.\mvnw -Pprod flyway:migrate
```

Executar a aplicação:
- Local (perfil Spring padrão):
```bash
.\mvnw spring-boot:run
```
- Produção (perfil Spring `prod`):
```bash
.\mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Dicas:
- Se não usar o wrapper, substitua `.\mvnw` por `mvn`.
- Não edite migrações já aplicadas; em erro de objeto existente, prefira ajustar novas migrações para serem idempotentes (ex.: `CREATE TABLE IF NOT EXISTS`).

## 🚀 Build
Para compilar o projeto, execute:

```bash
./mvnw clean package
```

Isso irá gerar um arquivo JAR no diretório target, que pode ser usado para executar a aplicação.

___
