# API Financial Management

Projeto backend baseado em **arquitetura de microsserviços**, voltado para o gerenciamento financeiro de usuários, incluindo cadastro, transações e processamento assíncrono de eventos.

O sistema utiliza **Spring Boot, Kafka e PostgreSQL**, seguindo boas práticas de arquitetura, mensageria e documentação técnica.

---

## 🎯 Objetivo do Projeto

O objetivo deste projeto é simular um **sistema financeiro real**, aplicando conceitos como:

- Arquitetura de microsserviços
- Comunicação assíncrona com Kafka
- Separação de responsabilidades
- Persistência de dados
- Processamento de eventos
- Dead Letter Queue (DLQ)
- Documentação com OpenAPI
- Boas práticas de código e organização

---

## 🧱 Arquitetura Geral

O sistema é composto por múltiplos microsserviços independentes, que se comunicam entre si por meio de **eventos Kafka**.

### Microsserviços

| Microsserviço | Responsabilidade |
|--------------|------------------|
| **ms-user** | Cadastro de usuários (individual e via upload) |
| **ms-transaction** | Registro e validação de transações financeiras |
| **worker** | Processamento assíncrono de eventos de transação |

---

## 🔄 Comunicação entre Serviços

- Comunicação **síncrona**: REST
- Comunicação **assíncrona**: Apache Kafka

### Fluxo principal de transação

1. Usuário é cadastrado no **ms-user**
2. Uma transação é criada no **ms-transaction**
3. A transação é persistida no banco
4. Evento `transaction.requested` é publicado no Kafka
5. O **worker** consome o evento
6. O worker realiza validações/processamentos adicionais
7. Em caso de falha crítica, o evento é enviado para `transaction.dlq`

---

## 📡 Mensageria (Kafka)

### Tópicos Utilizados

| Tópico | Descrição |
|------|----------|
| `transaction.requested` | Evento publicado após criação da transação |
| `transaction.dlq` | Dead Letter Queue para erros críticos |

- `ms-transaction`: **Producer**
- `worker`: **Consumer**

---

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot
- Spring Web (MVC)
- Spring Data JPA
- Spring Validation
- Spring for Apache Kafka
- Flyway
- JWT (autenticação)

### Infraestrutura
- Apache Kafka
- Zookeeper
- PostgreSQL
- Docker
- Docker Compose

### Documentação
- OpenAPI / Swagger

---

## 📂 Estrutura do Repositório

```text
api-financial-management
 ├── ms-user
 ├── ms-transaction
 ├── worker
 ├── docker-compose.yml
 └── README.md
