# LogiTrack Pro

MVP de gestão de frota — backend Spring Boot + frontend Angular consumindo REST API, banco Postgres (Neon em produção), deploy do backend no Render e do frontend no Vercel.

## Setup local

O `docker-compose.yml` já sobe Postgres + backend com os valores de desenvolvimento embutidos (mesmos defaults documentados em `backend/.env.example`), então **não é necessário criar nenhum arquivo `.env` para rodar local**:

```bash
docker compose up
```

(Use `docker compose` com espaço — o plugin v2 do Docker Compose. O binário antigo `docker-compose` com hífen, v1, pode não estar instalado por padrão.)

Isso builda a imagem do backend, sobe o Postgres, aplica as migrations Flyway e expõe a API em `http://localhost:8080`.

Para o frontend:

```bash
cd frontend
npm install
ng serve
```

A aplicação abre em `http://localhost:4200` e já aponta para `http://localhost:8080/api` (ver `frontend/src/environments/environment.ts`).

### Variáveis de ambiente

- `backend/.env.example` documenta todas as variáveis lidas pelo backend (dev e prod) — útil como referência caso você rode a aplicação fora do `docker-compose` (ex: `./mvnw spring-boot:run` direto), quando pode ser necessário exportá-las manualmente no shell.
- O frontend não usa `.env` — ver a seção "Configuração de ambiente" em [`frontend/README.md`](frontend/README.md).
- Nenhum arquivo `.env` real deve ser commitado; o `.gitignore` da raiz já cobre `.env`, `.env.local` e variantes, mantendo apenas os arquivos `*.env.example` versionados.

### Produção (Render / Vercel / Neon)

Em produção, os valores reais (`DATABASE_URL`, `DATABASE_URL_DIRECT`, `PORT`, `CORS_ALLOWED_ORIGINS` no backend; nenhum no frontend) são configurados como variáveis de ambiente/secrets nativos de cada plataforma — nunca via arquivo `.env` no repositório.

O banco de produção é um projeto Neon (Postgres serverless), que expõe dois endpoints: um pooled (via PgBouncer) e um direto. `DATABASE_URL` deve apontar para o endpoint pooled — usado pela aplicação em runtime — e `DATABASE_URL_DIRECT` para o endpoint direto, usado exclusivamente pelo Flyway ao aplicar migrations (o PgBouncer em modo transaction não é compatível com o Flyway).

## CI/CD

O projeto separa claramente **CI** (integração contínua) de **CD** (entrega contínua), duas responsabilidades distintas que costumam ser confundidas:

- **CI — GitHub Actions (`.github/workflows/ci.yml`):** a cada `push` ou `pull_request`, dois jobs rodam em paralelo:
  - `backend`: `mvn -B test`, incluindo os testes de integração com Testcontainers (Postgres real via Docker, já disponível nativamente no runner `ubuntu-latest`).
  - `frontend`: `npm ci` + `npm run build`, garantindo que o build de produção do Angular compila sem erros.

  O CI funciona como gate de qualidade: se os testes ou o build falharem, o commit/PR fica sinalizado como quebrado antes de qualquer deploy.

- **CD — integração nativa Render/Vercel, sem workflow customizado:** o deploy em si não passa pelo GitHub Actions. Backend e frontend são publicados automaticamente a cada push na branch principal via integração nativa de cada plataforma com o GitHub:
  - **Render** (backend): serviço criado via Blueprint (`render.yaml`), com Auto-Deploy "On Commit" habilitado.
  - **Vercel** (frontend): projeto conectado via App do Vercel no GitHub, com deploy automático a cada push.

  Não existe (e propositalmente não deve existir) um `deploy.yml` no Actions — replicar em YAML customizado algo que a integração nativa das plataformas já resolve seria complexidade desnecessária para o escopo deste MVP.

**Nota de configuração:** para que o auto-deploy do Render funcione, o GitHub App da plataforma precisa ter acesso explícito ao repositório em Settings → Applications → Installed GitHub Apps → Render — sem isso, o serviço aparece "conectado" no dashboard do Render, mas nenhum deploy é disparado automaticamente após o push (o webhook do GitHub nunca chega).
