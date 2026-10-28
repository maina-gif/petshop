# Sistema de Gestão para Pet Shop

Aplicação acadêmica para gestão de tutores, pets, produtos e agendamentos de banho e tosa. O projeto reúne back-end em Spring Boot, páginas server-side com Thymeleaf e uma camada REST em `/api/**`.

## Status do projeto

O repositório já contém:

- Login com Spring Security e senha armazenada com BCrypt;
- Sessão com verificação de inatividade de 30 segundos no navegador e no servidor;
- CRUD de tutores, pets, produtos e agendamentos;
- Dashboard com indicadores do dia;
- Criptografia AES/GCM de CPF e telefone antes da gravação no banco;
- Chave AES e credenciais do banco fornecidas por variáveis de ambiente;
- Regra que impede dois agendamentos na mesma data e horário;
- Interface Thymeleaf com Bootstrap 5, busca, validações e confirmação de exclusão;
- Documentação de telas, fluxo e contrato front-end/back-end em [`docs/telas-e-fluxo.md`](docs/telas-e-fluxo.md).

## Tecnologias

- Java 21;
- Spring Boot 3.5.6;
- Spring MVC e Thymeleaf;
- Spring Data JPA/Hibernate;
- Spring Security 6;
- MySQL 8+;
- Bootstrap 5.3 e Bootstrap Icons;
- Maven.

## Pré-requisitos

Instale e confirme:

```powershell
java -version
mvn -version
```

Também é necessário ter o MySQL Server em execução. O MySQL Workbench pode ser usado para executar os comandos SQL, mas ele sozinho não substitui o MySQL Server.

## Como obter o projeto

```powershell
git clone https://github.com/maina-gif/petshop.git
cd petshop
```

## 1. Criar o banco de dados

No MySQL Workbench ou em outro cliente MySQL, execute:

```sql
CREATE DATABASE IF NOT EXISTS petshop_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

O Hibernate cria e atualiza as tabelas durante o desenvolvimento usando `spring.jpa.hibernate.ddl-auto=update`.

## 2. Configurar as variáveis de ambiente

Copie `.env.example` apenas como referência. O Spring não carrega esse arquivo automaticamente; no Windows, defina as variáveis no PowerShell ou na configuração de execução do IntelliJ.

### PowerShell

Na mesma janela usada para iniciar a aplicação:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/petshop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="SUA_SENHA_REAL_DO_MYSQL"
$env:AES_SECRET_KEY="01234567890123456789012345678901"
$env:ADMIN_USERNAME="admin"
$env:ADMIN_PASSWORD="admin123"
```

Regras importantes:

- `DB_PASSWORD` deve ser a senha real do MySQL;
- `AES_SECRET_KEY` deve ter exatamente 32 caracteres;
- nunca publique senhas, a chave AES ou um arquivo `.env`;
- se a chave AES for perdida, os campos criptografados não poderão ser recuperados.

### IntelliJ IDEA

Se executar pelo botão Run:

1. Abra **Run > Edit Configurations**;
2. Selecione a configuração da aplicação Spring Boot;
3. Preencha **Environment variables** com as mesmas variáveis acima;
4. Salve e execute novamente.

## 3. Executar

Com Maven instalado:

```powershell
mvn spring-boot:run
```

Ou execute a classe principal no IntelliJ:

```text
src/main/java/br/com/petshop/PetshopApplication.java
```

Quando iniciar corretamente, acesse:

```text
http://localhost:8080
```

## Usuário inicial

Na primeira inicialização, `DataInitializer` cria um usuário administrador se ele ainda não existir:

```text
usuário: admin
senha: admin123
```

Esses valores podem ser alterados pelas variáveis `ADMIN_USERNAME` e `ADMIN_PASSWORD` antes da primeira execução.

## Rotas da interface

| Rota | Função |
|---|---|
| `/login` | Login |
| `/dashboard` | Painel inicial |
| `/tutores` | Listagem, busca e CRUD de tutores |
| `/pets` | Listagem, busca e CRUD de pets |
| `/produtos` | Listagem, busca e CRUD de produtos |
| `/agendamentos` | Agenda, filtro por data e CRUD de agendamentos |
| `/sessao/ping` | Atualização da atividade da sessão |

Os formulários usam POST com CSRF e as rotas protegidas exigem autenticação.

## API REST

A API continua disponível para integração futura ou testes com ferramentas como Postman. Ela usa HTTP Basic e não deve ser confundida com o login por formulário das telas.

| Método | Rota | Função |
|---|---|---|
| GET/POST/DELETE | `/api/tutores` | CRUD básico de tutores |
| GET/POST/DELETE | `/api/pets` | CRUD básico de pets |
| GET/POST/DELETE | `/api/produtos` | CRUD básico de produtos |
| GET/POST/DELETE | `/api/agendamentos` | CRUD básico de agendamentos |
| GET | `/api/dashboard` | Indicadores do dia |

Exemplo de teste autenticado no PowerShell:

```powershell
$credencial = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:admin123"))
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/dashboard" `
  -Headers @{ Authorization = "Basic $credencial" }
```

## Estrutura principal

```text
src/main/java/br/com/petshop/
├── config/        # segurança e filtro de inatividade
├── controller/    # endpoints REST e tratamento de erros
├── entity/        # entidades JPA
├── repository/    # consultas ao banco
├── security/      # conversor AES
├── service/       # regras de negócio
└── web/           # controllers e forms das páginas Thymeleaf

src/main/resources/
├── static/        # CSS e JavaScript
├── templates/     # páginas e fragments Thymeleaf
└── application.properties
```

## Decisões de segurança

- Senhas não são armazenadas em texto puro: o projeto usa `BCryptPasswordEncoder`;
- CPF e telefone usam `AesAttributeConverter` com AES/GCM/NoPadding;
- a chave AES é lida de `AES_SECRET_KEY`, fora do código-fonte;
- formulários web usam CSRF;
- `/api/**` usa HTTP Basic e desativa CSRF apenas para essa cadeia REST;
- o servidor confere a última atividade em `InatividadeFilter`;
- o JavaScript registra clique, teclado, rolagem, toque e movimento do mouse;
- CPF aparece mascarado nas listagens;
- validações do navegador não substituem as validações do servidor.

## Testes manuais recomendados

1. Acesse `/login` com usuário inválido e confirme a mensagem de erro.
2. Entre com o administrador e confirme o redirecionamento para `/dashboard`.
3. Cadastre, edite, busque e exclua um tutor.
4. Cadastre um pet vinculando-o a um tutor.
5. Tente excluir um tutor que ainda possui pets.
6. Cadastre um produto com preço e estoque válidos.
7. Crie um agendamento e tente criar outro no mesmo dia e horário.
8. Tente salvar uma data passada.
9. Fique 25 segundos sem atividade e confirme o aviso visual.
10. Fique 30 segundos sem atividade e confirme o retorno ao login.
11. Desative o JavaScript e faça uma requisição depois de 30 segundos para verificar a proteção do servidor.
12. Abra o banco e confirme que CPF, telefone e senha não estão legíveis.

## Divisão da equipe

- **Maina:** banco, entidades, repositories, services, segurança, criptografia, regras de negócio e integração do back-end;
- **Nicolas:** templates Thymeleaf, CSS, JavaScript da interface, fluxo de navegação e documentação visual.

## Próximas melhorias

- Adicionar testes automatizados para regras de agendamento e validações;
- Separar os controllers REST em arquivos menores;
- Criar DTOs para evitar expor entidades JPA diretamente na API;
- Adicionar auditoria de alterações;
- Criar perfis de acesso, como administrador e atendente;
- Adicionar migrations com Flyway ou Liquibase antes de produção;
- Criar o Maven Wrapper (`mvnw`/`mvnw.cmd`) para padronizar a execução em qualquer máquina.

## Segurança do repositório

Antes de cada commit, confirme:

```powershell
git status
git diff --cached
```

Nunca faça commit de:

- senhas reais;
- `DB_PASSWORD` preenchido;
- `AES_SECRET_KEY` real;
- arquivos `.env`;
- dumps do banco com dados pessoais.
