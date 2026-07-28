# LogiTrack Pro

MVP de gestão de frota — backend Spring Boot + frontend Angular consumindo REST API, banco Postgres (Neon em produção), deploy do backend no Render e do frontend no Vercel.

## Decisões técnicas e arquitetura

### Backend em camadas

O backend segue uma separação clássica em camadas: `controller -> service (interface + impl) -> repository -> entity`. Os controllers não conhecem entities diretamente — trocam DTOs de request/response, desacoplados das entities JPA, o que evita expor detalhes de persistência na API e permite evoluir o modelo de dados sem quebrar o contrato HTTP. Erros de validação e de recurso não encontrado são tratados de forma centralizada por um `GlobalExceptionHandler`, que padroniza as respostas 400/404 em vez de deixar cada controller implementar seu próprio tratamento de exceção.

### Frontend: `core/shared/features`

O frontend é organizado em três pastas com responsabilidades distintas: `core/` concentra interceptors globais (ex.: tratamento de erro HTTP), `shared/` reúne components e models reutilizáveis entre módulos, e `features/` tem uma pasta por módulo de negócio (`dashboard/`, `viagens/`), mantendo cada área da aplicação isolada das demais. Os componentes são standalone (sem `NgModule`), como recomendado pelas versões recentes do Angular. A UI usa Angular Material com o tema padrão — sem customização visual — e Tailwind CSS apenas como utilitário de layout e espaçamento por cima do Material, nunca sobrescrevendo estilos internos dos componentes Material.

### Por que Viagens é o módulo de CRUD

Entre as entidades do domínio, **Viagens** foi escolhido como o módulo de CRUD completo (criar, listar, editar, excluir). **Manutenção** é somente leitura: não tem tela nem endpoint de criação/edição, e existe apenas para alimentar as métricas do dashboard. Essa divisão evita duplicar esforço de CRUD em duas entidades quando o desafio pede um módulo completo — Viagens foi o escolhido por ser a entidade mais central ao domínio de gestão de frota.

### Métricas do dashboard via SQL nativo

As 5 métricas do dashboard são implementadas com `@Query nativeQuery` (ou `JdbcTemplate`), executando a agregação diretamente no Postgres, em vez de carregar entidades para a JVM e agregar em memória Java. Isso evita trazer para a aplicação mais dados do que o necessário só para calcular uma soma, média ou contagem, e aproveita o próprio banco — que já é otimizado para esse tipo de operação — em vez de reimplementar agregação em código.

### Stack de deploy: Render, Vercel e Neon

O backend é implantado no **Render** via Docker, o que evita depender de um buildpack específico de Java e mantém o ambiente de execução idêntico ao usado localmente via `docker-compose`. O frontend é publicado no **Vercel** como build estático do Angular, aproveitando deploy automático a cada push e preview deployments por PR. O banco é um projeto **Neon** (Postgres serverless), escolhido pelo tier gratuito com endpoint pooled pronto para uso em produção — a aplicação usa o endpoint pooled (`DATABASE_URL`) em runtime e o endpoint direto (`DATABASE_URL_DIRECT`) exclusivamente para as migrations do Flyway, que não é compatível com o modo transaction do PgBouncer usado no pooling. Mais detalhes de configuração de ambiente estão na seção "Produção (Render / Vercel / Neon)" abaixo.

### Alterações no banco de dados

O banco de dados fornecido pelo desafio foi estendido. O script vigente não é um dump único, e sim o conjunto de migrations Flyway em [`backend/src/main/resources/db/migration/`](backend/src/main/resources/db/migration/), aplicadas automaticamente na subida da aplicação:

- **`V1__carga_inicial.sql`**: cria as tabelas `veiculos`, `viagens` e `manutencoes`, além da carga inicial de dados (seed) usada para popular o dashboard. Inclui a coluna `tipo` (`LEVE`/`PESADO`) em `veiculos` — necessária para a métrica "Volume por Categoria" do dashboard, que agrupa viagens pelo tipo do veículo.
- **`V2__viagens_constraints_e_auditoria.sql`**: adiciona colunas de auditoria `created_at`/`updated_at` em `viagens`, as constraints `CHECK (km_percorrida > 0)` e `CHECK (data_chegada >= data_saida)` para garantir integridade dos dados de viagem, e índices em `veiculo_id` (em `viagens` e `manutencoes`) para otimizar as consultas do dashboard.
- **`V3__seed_manutencoes_mes_corrente.sql`**: insere uma manutenção adicional com `data_inicio` calculada a partir de `CURRENT_DATE` (em vez de data fixa), garantindo que a métrica "Projeção Financeira" do dashboard sempre tenha dado no mês corrente em qualquer ambiente recém-provisionado — as manutenções de `V1` usam datas fixas de 2024 e nunca coincidem com o mês atual.

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
