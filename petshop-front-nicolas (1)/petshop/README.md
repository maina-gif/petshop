# Pet Shop Backend

Base de back-end em Spring Boot 3.5, Java 21, MySQL, Spring Security, BCrypt e AES/GCM. O projeto expõe uma API REST para o front-end Thymeleaf/Bootstrap integrar depois.

## 1. Criar o banco

```sql
CREATE DATABASE petshop_db;
```

## 2. Configurar o PowerShell

Na mesma janela usada para executar o Maven:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/petshop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="SUA_SENHA_DO_MYSQL"
$env:AES_SECRET_KEY="01234567890123456789012345678901"
$env:ADMIN_USERNAME="admin"
$env:ADMIN_PASSWORD="admin123"
```

A chave AES precisa ter exatamente 32 caracteres. Não publique senhas nem a chave.

## 3. Executar

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Se ainda não houver `mvnw.cmd`, use o Maven instalado:

```powershell
mvn spring-boot:run
```

## 4. Testar

A API inicia em `http://localhost:8080`. O usuário inicial é o valor de `ADMIN_USERNAME` e a senha é o valor de `ADMIN_PASSWORD`.

A autenticação atual usa HTTP Basic para facilitar os primeiros testes. Exemplos:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/dashboard -Headers @{Authorization="Basic SEU_BASE64"}
```

Rotas principais:

- `GET/POST/DELETE /api/tutores`
- `GET/POST/DELETE /api/pets`
- `GET/POST/DELETE /api/produtos`
- `GET/POST/DELETE /api/agendamentos`
- `GET /api/dashboard`

CPF e telefone são armazenados criptografados no banco. A busca por nome funciona em campos não criptografados. A sessão de servidor está configurada para 30 segundos.

## 5. Telas (front-end)

Com a aplicação rodando, acesse `http://localhost:8080` no navegador. Ele leva para a tela de login, e depois do login abre o painel com o menu para Tutores, Pets, Produtos e Agendamentos.

- As telas usam Thymeleaf + Bootstrap 5 e ficam em `src/main/resources/templates`.
- CSS e JavaScript ficam em `src/main/resources/static`.
- A API REST (`/api/**`) continua funcionando com HTTP Basic, separada das telas, que usam login por formulário com CSRF.
- A sessão expira após 30 segundos sem atividade, tanto no navegador (`inatividade.js`) quanto no servidor (`InatividadeFilter`).

O mapa das telas, o contrato entre front e back e o roteiro de testes estão em [`docs/telas-e-fluxo.md`](docs/telas-e-fluxo.md).

## Observação

Esta é uma base inicial para testar e integrar com os templates do Nicolas. Pelas telas os CRUDs já têm cadastro, edição, exclusão e busca. Ainda devem ser acrescentados testes automatizados.
