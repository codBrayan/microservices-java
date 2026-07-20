# Microserviços com Spring Boot

Um projeto educacional de arquitetura de microserviços desenvolvido em Java com **Spring Boot 4** e **Spring Cloud**. O projeto demonstra padrões modernos de desenvolvimento distribuído, incluindo configuração centralizada, persistência com banco de dados e versionamento de schema.

## 🏗️ Arquitetura

O projeto é composto por 4 serviços independentes:

```
┌──────────────────────────────────────────────────────────┐
│                     Config Server                        │
│              (Configuração Centralizada)                 │
│                   Porta: 8888                            │
└──────────────────┬───────────────────────────────────────┘
                   │
        ┌──────────┼──────────┬──────────┐
        │          │          │          │
        ▼          ▼          ▼          ▼
   ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
   │Greeting│ │Product │ │Currency│ │  (API  │
   │Service │ │Service │ │Service │ │ Gateway)
   │ 8080   │ │ 8081   │ │ 8082   │ │
   └────────┘ └────────┘ └────────┘ └────────┘
              │          │
              ▼          ▼
           PostgreSQL Database
          (bd_product, bd_currency)
```

### Serviços

| Serviço | Porta | Descrição | Stack |
|---------|-------|-----------|-------|
| **Config Server** | 8888 | Centraliza configurações de todos os serviços | Spring Cloud Config Server |
| **Greeting Service** | 8080 | API REST que oferece endpoints de saudação com suporte a i18n | Spring Web MVC, Spring Cloud Config Client |
| **Product Service** | 8081 | API REST para gerenciar produtos com persistência | Spring Data JPA, PostgreSQL, Flyway |
| **Currency Service** | 8082 | API REST para gerenciar moedas com persistência | Spring Data JPA, PostgreSQL, Flyway |

## 🚀 Quick Start

### Pré-requisitos

- **Java 25+**
- **Maven 3.8+**
- **PostgreSQL 13+**
- **Docker & Docker Compose** (opcional, recomendado)

### Opção 1: Com Docker Compose (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/codBrayan/microservices-java
cd microservices-java

# Inicie todos os serviços
docker-compose up -d

# Aguarde 10-15 segundos para os serviços iniciarem
# Verifique o status
docker-compose ps
```

### Opção 2: Manualmente

#### 1. Configure o PostgreSQL

```bash
# Crie os bancos de dados
createdb bd_product
createdb bd_currency

# Ou via psql:
psql -U postgres
CREATE DATABASE bd_product;
CREATE DATABASE bd_currency;
```

#### 2. Inicie o Config Server

```bash
cd config-service
./mvnw spring-boot:run
```

Acesse: http://localhost:8888

#### 3. Inicie o Greeting Service (em outro terminal)

```bash
cd greeting-service
./mvnw spring-boot:run
```

Acesse: http://localhost:8080

#### 4. Inicie o Product Service (em outro terminal)

```bash
cd product-service
./mvnw spring-boot:run
```

Acesse: http://localhost:8081

#### 5. Inicie o Currency Service (em outro terminal)

```bash
cd currency-service
./mvnw spring-boot:run
```

Acesse: http://localhost:8082

## 📋 Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# PostgreSQL
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=password
DB_PRODUCT=bd_product
DB_CURRENCY=bd_currency

# Config Server
CONFIG_SERVER_URL=http://localhost:8888

# Services
GREETING_SERVICE_PORT=8080
PRODUCT_SERVICE_PORT=8081
CURRENCY_SERVICE_PORT=8082
```

### Perfis de Configuração (Greeting Service)

O Greeting Service suporta múltiplos locales através de perfis Spring:

- `application-en.properties` → Inglês
- `application-es.properties` → Espanhol
- `application-fr.properties` → Francês
- `application-it.properties` → Italiano

Para mudar o locale, edite `greeting-service/src/main/resources/application.properties`:

```properties
spring.profiles.active=en  # Mude para: en, es, fr, it
```

## 📡 Endpoints

### Greeting Service (http://localhost:8080)

```bash
# GET - Obter saudação
curl http://localhost:8080/greeting

# GET - Obter saudação com nome
curl http://localhost:8080/greeting?name=João

# GET - Health Check
curl http://localhost:8080/actuator/health
```

### Product Service (http://localhost:8081)

```bash
# GET - Listar todos os produtos
curl http://localhost:8081/api/products

# GET - Obter produto por ID
curl http://localhost:8081/api/products/1

# POST - Criar novo produto
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Notebook","price":2500.00}'

# PUT - Atualizar produto
curl -X PUT http://localhost:8081/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Notebook Dell","price":2800.00}'

# DELETE - Deletar produto
curl -X DELETE http://localhost:8081/api/products/1
```

### Currency Service (http://localhost:8082)

```bash
# GET - Listar moedas
curl http://localhost:8082/api/currencies

# GET - Obter moeda por ID
curl http://localhost:8082/api/currencies/1

# POST - Criar moeda
curl -X POST http://localhost:8082/api/currencies \
  -H "Content-Type: application/json" \
  -d '{"code":"USD","name":"Dólar Americano"}'
```

### Actuator Endpoints (Monitoramento)

Disponível em todos os serviços:

```bash
# Health Check
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics

# Informações da Aplicação
curl http://localhost:8080/actuator/info

# Variáveis de Ambiente
curl http://localhost:8080/actuator/env
```

## 🛠️ Desenvolvimento

### Estrutura do Projeto

```
microservices-java/
├── config-service/              # Config Server
│   ├── src/main/java
│   ├── pom.xml
│   └── mvnw
├── greeting-service/            # Greeting Microservice
│   ├── src/main/java
│   ├── src/main/resources
│   ├── src/test
│   ├── pom.xml
│   └── mvnw
├── product-service/             # Product Microservice
│   ├── src/main/java
│   ├── src/main/resources
│   ├── src/test
│   ├── pom.xml
│   └── mvnw
├── currency-service/            # Currency Microservice
│   ├── src/main/java
│   ├── src/main/resources
│   ├── src/test
│   ├── pom.xml
│   └── mvnw
├── configs/                      # Configurações Centralizadas
│   └── greeting-service/
│       ├── application-en.properties
│       ├── application-es.properties
│       ├── application-fr.properties
│       └── application-it.properties
├── docker-compose.yml           # Orquestração com Docker
└── README.md
```

### Stack Tecnológico

- **Java 25**
- **Spring Boot 4.0.5/4.0.6**
- **Spring Cloud 2025.1.1**
  - Spring Cloud Config Server
  - Spring Cloud Config Client
- **Spring Data JPA** (Product & Currency Services)
- **Flyway** (Versionamento de schema)
- **PostgreSQL** (Banco de dados)
- **Spring Web MVC** (APIs REST)
- **Spring Boot Actuator** (Monitoramento)
- **Maven** (Gerenciador de dependências)

### Build & Test

```bash
# Build individual de um serviço
cd greeting-service
./mvnw clean package

# Executar testes
./mvnw test

# Build sem testes
./mvnw clean package -DskipTests
```

## 📚 Conceitos Implementados

- ✅ **Arquitetura de Microserviços** - Serviços independentes com responsabilidades bem definidas
- ✅ **Configuração Centralizada** - Spring Cloud Config Server
- ✅ **Persistência com ORM** - Spring Data JPA
- ✅ **Versionamento de Schema** - Flyway migrations
- ✅ **APIs RESTful** - Controllers Spring Web MVC
- ✅ **Monitoramento** - Spring Boot Actuator
- ✅ **Internacionalização (i18n)** - Suporte a múltiplos idiomas
- ✅ **Containerização** - Docker & Docker Compose

## 🚨 Melhorias Futuras

- [ ] Implementar testes unitários e de integração
- [ ] Adicionar Spring Cloud Service Discovery (Eureka)
- [ ] Implementar API Gateway (Spring Cloud Gateway)
- [ ] Adicionar Circuit Breaker (Resilience4j)
- [ ] Implementar logging distribuído (ELK Stack)
- [ ] Adicionar autenticação/autorização (Spring Security)
- [ ] Implementar rate limiting
- [ ] Adicionar documentação Swagger/OpenAPI
- [ ] Implementar versionamento de APIs
- [ ] Adicionar observabilidade com Prometheus + Grafana

## 🐛 Solução de Problemas

### Config Server não inicia

```bash
# Verifique a porta 8888
lsof -i :8888

# Mude a porta em config-service/src/main/resources/application.properties
server.port=8889
```

### Erro de conexão com PostgreSQL

```bash
# Verifique se PostgreSQL está rodando
psql -U postgres -c "SELECT version();"

# Verifique as credenciais em application.properties:
# spring.datasource.url
# spring.datasource.username
# spring.datasource.password
```

### Serviço não consegue se conectar ao Config Server

```bash
# Verifique se Config Server está rodando
curl http://localhost:8888

# Verifique a URL em application.properties:
# spring.config.import=configserver:http://localhost:8888/
```

### Porta já em uso

```bash
# Linux/Mac
lsof -i :8080  # Substitua 8080 pela porta desejada

# Windows
netstat -ano | findstr :8080
```

## 📝 Licença

Este projeto é fornecido como-é para fins educacionais.

## 👤 Autor

Desenvolvido por **codBrayan**

---

**Última atualização:** 2026-04-27  
**Versão:** 1.0.0
