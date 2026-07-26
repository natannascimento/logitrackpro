# LogiTrack Pro

MVP de gestão de frota — backend Spring Boot + frontend Angular consumindo REST API, banco Postgres (Neon em produção), deploy do backend no Render e do frontend no Vercel.

## Setup local

O `docker-compose.yml` já sobe Postgres + backend com os valores de desenvolvimento embutidos (mesmos defaults documentados em `backend/.env.example`), então **não é necessário criar nenhum arquivo `.env` para rodar local**:

```bash
docker-compose up
```

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

### Produção (Render / Vercel)

Em produção, os valores reais (`DATABASE_URL`, `PORT`, `CORS_ALLOWED_ORIGINS` no backend; nenhum no frontend) são configurados como variáveis de ambiente/secrets nativos de cada plataforma — nunca via arquivo `.env` no repositório.
