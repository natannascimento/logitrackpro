# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 22.0.8.

## Arquitetura

O código em `src/app/` segue a separação `core/shared/features`:

- **`core/`** — infraestrutura transversal, sem UI própria: `auth/` (autenticação JWT), `interceptors/` (HTTP interceptors globais) e `route-loading/` (feedback visual de navegação entre rotas).
- **`shared/`** — components e models reutilizáveis entre módulos, como a sidebar (`shared/components/navbar/`) e os models de domínio (`shared/models/`).
- **`features/`** — uma pasta por módulo de negócio: `login/`, `viagens/` (CRUD completo) e `dashboard/` (somente leitura).

Todos os components são standalone (sem `NgModule`), e o estado local usa signals nativos do Angular — não há NgRx nem outra lib de state management no projeto.

## Autenticação (JWT)

O login integra com `POST /api/auth/login` do backend e mantém a sessão inteiramente no cliente, sem cookies:

- **`core/auth/auth-service.ts`** concentra o estado de autenticação: `login()` chama a API e guarda o `accessToken` retornado em `localStorage`; `isAuthenticated` é um signal computado a partir da presença do token; `logout()` chama `POST /api/auth/logout` (revogando o token no backend) e só então limpa o `localStorage` — se a chamada ao backend falhar (rede indisponível, token já expirado), a sessão local é limpa de qualquer forma, para o usuário nunca ficar "preso" logado.
- **`core/interceptors/auth.interceptor.ts`** anexa `Authorization: Bearer <token>` a toda requisição HTTP feita à API, quando há um token armazenado.
- **`core/auth/auth-guard.ts`** protege as rotas `viagens` e `dashboard` (`app.routes.ts`), redirecionando para `/login` quando não há sessão.
- **`core/interceptors/error.interceptor.ts`** trata 401 de forma centralizada: em qualquer endpoint protegido, um 401 dispara logout local e redireciona para `/login` — exceto nas próprias chamadas de `/auth/login` e `/auth/logout`, que tratam seus erros inline (mensagem no formulário e resiliência a falha, respectivamente) para evitar loop de chamadas repetidas.
- O botão **"Sair"** fica na sidebar (`shared/components/navbar/`), visível nas telas autenticadas.
- O botão "Entrar" mostra um spinner e o texto "Entrando…" enquanto aguarda a resposta do login — o backend roda no tier gratuito do Render, cujo cold start pode levar alguns segundos, e sem esse feedback o usuário não tinha como saber se o clique tinha sido registrado.

## Feedback de carregamento entre rotas

`core/route-loading/` mostra uma barra de progresso no topo (`route-progress-bar`) e um skeleton (`route-skeleton`) durante a transição entre rotas do Angular Router, com duração mínima de exibição para evitar "flash" em navegações muito rápidas. Isso cobre a troca de tela em si (ex.: `/viagens` → `/dashboard`); a espera pela resposta da API de login é tratada separadamente pelo spinner no próprio botão, já que a navegação para `/viagens` só começa depois que o login já respondeu.

## Configuração de ambiente

Este projeto **não usa arquivo `.env`** em build time. A configuração de ambiente (URL da API) é feita via os arquivos nativos do Angular em `src/environments/`:

- `environment.ts` — usado em desenvolvimento (`ng serve`), aponta para `http://localhost:8080/api`.
- `environment.prod.ts` — usado no build de produção (`ng build`), aponta para a URL do backend implantado no Render.

Não há nenhum segredo real nesses arquivos — a URL da API é informação pública (fica exposta no bundle de qualquer forma), por isso ambos são versionados normalmente. Se um novo valor de ambiente sensível surgir no futuro, reavalie a necessidade de um mecanismo de `.env` de build (ex: `@ngx-env/builder`) nesse momento.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
