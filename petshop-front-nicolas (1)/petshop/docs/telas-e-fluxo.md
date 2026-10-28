# Telas e fluxo de navegação - Front-end

Responsável pelo front-end e documentação: Nicolas
Tecnologias: Thymeleaf, Bootstrap 5.3, Bootstrap Icons e JavaScript puro.

## Fluxo geral

```
/login ──(usuário e senha)──► /dashboard
                                 │
              ┌──────────┬───────┴─────┬──────────────┐
          /tutores     /pets      /produtos     /agendamentos
              │
   listagem -> Novo/Editar -> Salvar -> volta pra listagem com mensagem
   listagem -> Excluir -> modal de confirmação -> POST -> volta com mensagem

Sem atividade por 30s -> aviso aos 25s -> /login?expirado
Botão "Sair" (POST /logout) -> /login?logout
```

## Telas

| Tela | Arquivo | O que faz |
|---|---|---|
| Login | `templates/login.html` | Formulário de usuário e senha, botão pra mostrar a senha e mensagens de erro, sessão expirada e logout |
| Painel | `templates/dashboard.html` | Cards com total de tutores, pets, produtos e agendamentos de hoje, mais a agenda do dia |
| Tutores | `templates/tutores/lista.html` e `form.html` | Lista com busca por nome, CPF mascarado na tabela e máscara de CPF/telefone no formulário |
| Pets | `templates/pets/lista.html` e `form.html` | Lista com busca, tutor escolhido em um `<select>` |
| Produtos | `templates/produtos/lista.html` e `form.html` | Preço em R$ e destaque de estoque baixo (amarelo abaixo de 5, vermelho zerado) |
| Agendamentos | `templates/agendamentos/lista.html` e `form.html` | Filtro por data, status com cores, cancelados riscados e erros de regra (horário ocupado, data no passado) |

### Partes reaproveitadas (`templates/fragments/layout.html`)

- `head(titulo)`: metas, Bootstrap, ícones e o nosso `style.css`
- `menu`: barra de navegação com o item ativo destacado, o usuário logado (`sec:authentication="name"`) e o botão Sair
- `mensagens`: alertas de sucesso e erro (flash attributes `sucesso` e `erro`)
- `modalExcluir`: um único modal de confirmação usado por todas as listagens
- `avisoSessao`: aviso com contagem regressiva antes da sessão expirar
- `scripts`: Bootstrap JS, `app.js` e `inatividade.js`

## Contrato front ↔ back

### Login e sessão
| Rota | Método | Observação |
|---|---|---|
| `/login` | GET | Mostra a tela. Parâmetros: `error`, `expirado`, `logout` |
| `/login` | POST | Campos `username` e `password` (tratado pelo Spring Security) |
| `/logout` | POST | Precisa do token CSRF; volta para `/login?logout` |
| `/sessao/ping` | GET | Usado pelo JS para avisar o servidor que o usuário está ativo (resposta 204) |

### Painel
| Rota | Atributos no model |
|---|---|
| `/dashboard` | `hoje`, `totalTutores`, `totalPets`, `totalProdutos`, `agendamentosHoje` |

### Cadastros (mesmo padrão para tutores, pets, produtos e agendamentos)
| Rota | Método | Atributos / campos |
|---|---|---|
| `/{entidade}` | GET | lista (`tutores`, `pets`, `produtos`, `agendamentos`) + busca `nome` (em agendamentos o filtro é `data`) |
| `/{entidade}/novo` | GET | objeto vazio do formulário |
| `/{entidade}/editar/{id}` | GET | objeto preenchido |
| `/{entidade}/salvar` | POST | campos do formulário; sem `id` cria, com `id` atualiza |
| `/{entidade}/excluir/{id}` | POST | redireciona com mensagem |

Campos dos formulários:
- Tutor: `nome`, `cpf`, `telefone`, `email`, `endereco`
- Pet: `nome`, `especie`, `raca`, `idade`, `tutorId` (o form recebe a lista `tutores`)
- Produto: `nome`, `preco`, `estoque`
- Agendamento: `petId`, `data`, `horario`, `servico`, `status` (o form recebe `pets`, `servicos` e `statusList`)

## Decisões de segurança no front

1. CSRF: todo formulário POST usa `th:action="@{...}"`, assim o Thymeleaf coloca o token sozinho. O modal de exclusão e o logout também enviam o token.
2. CPF na listagem: aparece mascarado (`*.*.789-00`). O número completo só aparece no formulário de edição.
3. Validação dupla: o navegador valida antes de enviar (campos obrigatórios, máscaras), mas a validação que vale é a do servidor (`@Valid` nos forms), porque o HTML pode ser alterado.
4. Inatividade de 30 segundos:
   - No navegador (`static/js/inatividade.js`): clique, tecla, rolagem, toque e movimento do mouse reiniciam o tempo. Existe só um timer (sempre `clearTimeout` antes de criar outro), então o logout não dispara várias vezes. Aos 25s aparece um aviso com contagem. Como o navegador "congela" timers de abas em segundo plano, o horário da última atividade fica guardado e é conferido no evento `visibilitychange`.
   - No servidor (`config/InatividadeFilter.java`): a cada requisição de um usuário logado, o filtro compara o horário atual com o último acesso guardado na sessão. Passou de 30s, a sessão é invalidada e o usuário vai para `/login?expirado`. Isso existe porque o JavaScript pode ser desligado.
   - Ligação entre os dois: quando o usuário mexe na tela sem trocar de página, o JS chama `/sessao/ping` (no máximo uma vez a cada 5s) para o servidor também saber que ele está ativo.
   - Não usamos só `server.servlet.session.timeout=30s` porque o Tomcat trabalha em minutos e arredonda para 1 minuto.

## Como testar o front

1. Entrar com usuário errado -> aparece "Usuário ou senha inválidos".
2. Entrar certo -> painel com o nome do usuário no menu.
3. Ficar 25s parado -> aviso amarelo com contagem. Mexer o mouse -> aviso some.
4. Ficar 30s parado -> volta para o login com "Sua sessão expirou".
5. Deixar a aba em segundo plano por mais de 30s e voltar -> expira na hora.
6. Desligar o JavaScript (DevTools -> Disable JavaScript), esperar 30s e clicar em um menu -> o servidor manda para `/login?expirado` do mesmo jeito.
7. Agendar dois atendimentos no mesmo dia e horário -> aparece a mensagem de horário ocupado.
8. Tentar excluir um tutor que tem pet -> aparece a mensagem explicando por que não dá.
