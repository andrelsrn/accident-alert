# 🚨 Accident Alert API

API REST para gerenciamento de acidentes em ambiente ambulatorial, com foco em **resposta rápida, rastreabilidade e controle de acesso seguro**.

Este projeto foi inspirado em um cenário real de necessidade de comunicação rápida em ambientes ambulatoriais, onde falhas na notificação de incidentes podem impactar diretamente o tempo de resposta.

![CI](https://github.com/andrelsrn/accident-alert/actions/workflows/ci.yml/badge.svg)
---

## 📌 Visão Geral

O sistema permite registrar acidentes e notificar responsáveis, garantindo que **eventos críticos não passem despercebidos**.

A aplicação implementa regras reais de segurança utilizadas em ambientes corporativos, como:

- controle de acesso por hierarquia de roles
- desativação de usuários
- obrigatoriedade de troca de senha
- autenticação stateless com JWT

---

## 🧱 Arquitetura

A API segue uma arquitetura baseada em camadas:

Controller → Service → Repository → Database


- **Controller**: expõe endpoints REST
- **Service**: regras de negócio
- **Repository**: acesso a dados (JPA)
- **Security Layer**: autenticação e autorização (JWT + filtros)

---

## 🛠️ Stack Tecnológica

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Flyway (versionamento de banco)
- Docker
- Swagger / OpenAPI

---

## 🔐 Segurança

### Autenticação

- Baseada em JWT
- Token obrigatório para endpoints protegidos
- Sistema stateless

---

### Autorização (RBAC)

Hierarquia de roles:

ROLE_ADMIN > ROLE_MANAGER > ROLE_STAFF


| Role    | Permissões principais |
|--------|----------------------|
| ADMIN  | Controle total do sistema |
| MANAGER| Visualização de usuários e gestão operacional |
| STAFF  | Registro de acidentes |

---

## ⚙️ Regras de Negócio

### 👤 Gestão de Usuários

- Usuários podem ser **ativados/desativados**
- Usuários inativos:
    - ❌ não conseguem autenticar
    - ❌ não acessam endpoints protegidos

- Apenas ADMIN pode:
    - criar usuários
    - alterar roles
    - desativar contas

---

### 🔑 Autenticação

- Login gera token JWT contendo o email do usuário
- Token inválido ou ausente retorna erro padronizado:

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Token is missing or invalid"
}

```

🔄 Troca Obrigatória de Senha

Fluxo implementado:

Usuário é criado com senha inicial
mustChangePassword = true

Após login:
acesso é restrito
Só pode acessar:
/auth/login
/users/password

Qualquer outra tentativa:

``` json
{
"status": 403,
"error": "Forbidden",
"message": "You must change your password before accessing other resources"
}
```

---

## 🚑 Gestão de Acidentes

- Apenas usuários autenticados podem registrar acidentes
- Cada registro é associado ao usuário logado

Informações incluem:

- descrição
- localização
- severidade
- vítima

---

## 📡 Endpoints Principais

🔐 Auth

- POST /auth/login

👤 Usuários

- POST /users → criar usuário (ADMIN)
- GET /users → listar usuários (MANAGER+)
- PUT /users/{id}/role → alterar role (ADMIN)
- PUT /users/{id}/deactivate → desativar (ADMIN)
- PUT /users/password → trocar senha

🚑 Acidentes

- POST /accidents → registrar acidente
- GET /accidents → listar acidentes

---

## ⚠️ Tratamento de Erros

Todas as respostas seguem um padrão consistente:

```json
{
  "timestamp": "2026-05-05 14:43:00",
  "status": 403,
  "error": "Forbidden",
  "message": "User account is deactivated",
  "path": "/accidents"
}
```

---
## 🗄️ Banco de Dados
O PostgreSQL é iniciado automaticamente via Docker com as configurações definidas no docker-compose.yml.

As migrations são aplicadas automaticamente pelo Flyway ao iniciar a aplicação.
- PostgreSQL
- Versionamento com Flyway
- Estrutura baseada em:
    -  users
    -  accidents
    - notifications



---



## 🐳 Docker

O projeto pode ser executado utilizando Docker, facilitando a configuração do ambiente e eliminando dependências locais.

### 📦 Pré-requisitos

- Docker
- Docker Compose

---

### ▶️ Subindo a aplicação

```bash
docker-compose up --build
```

---

### 🗄️ Banco de Dados

O PostgreSQL é iniciado automaticamente via Docker com as configurações definidas no `docker-compose.yml`.

As migrations são aplicadas automaticamente pelo Flyway ao iniciar a aplicação.

---

### 🌐 Acessos

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

---

### ⛔ Parar containers

```bash
docker-compose down
```

---

### 🔄 Rebuild completo

```bash
docker-compose down -v
docker-compose up --build
```

---

### 📁 Estrutura Docker

- `Dockerfile` → build da aplicação Spring Boot
- `docker-compose.yml` → orquestração da API + PostgreSQL

---

### 📌 Observações

- O banco de dados roda em container isolado
- Os dados podem ser persistidos via volume Docker
- Ideal para desenvolvimento e testes locais


---
##  🧪 Roadmap
- Testes automatizados (JUnit + Mockito)
- Observabilidade (logs estruturados)
- Deploy em nuvem
- Sistema de notificações em tempo real

---
## 💡 Diferenciais do Projeto
- Implementação de regras reais de segurança
- Controle de acesso com hierarquia de roles
- Tratamento padronizado de erros
- Versionamento de banco com Flyway
- Estrutura organizada seguindo boas práticas

---
## 👨‍💻 Autor

Desenvolvido por André Nunes

Foco em backend com Java e Spring Boot

