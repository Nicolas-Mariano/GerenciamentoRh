# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**GerenciamentoRh** is an HR management system built with Java 23 using traditional servlet-based architecture with JSP views and PostgreSQL database. It also exposes a parallel JSON REST API (JAX-RS / Jersey). The project implements multiple design patterns (Decorator, Factory, Repository, Builder, Command) as part of a Design Patterns course (Padrões de Projeto - PP).

The system manages the complete employee lifecycle: hiring, promotions/salary raises, termination, and rehiring. Critically, **employee data is separated from employment contracts** — a single employee has many contracts, preserving employment history. This separation is **fully implemented**: `Funcionario` holds only personal data, while `Contrato` holds all employment data.

### Key Distinction
- **Funcionário** (Employee): Permanent personal data (name, CPF, phone, email, address)
- **Contrato** (Contract): Employment relationship data (hiring date, termination date, salary, sector, seniority level) — new contracts can be created for the same employee without overwriting old ones

## Build & Run

### Prerequisites
- Java 23 (Maven compiler `<release>` is 23)
- PostgreSQL 12+ running on `localhost:5433`
- Database: `gerenciamento_rh`
- DB credentials: copy `src/main/resources/db.properties.example` to `db.properties` and set `db.url` / `db.user` / `db.password`. `FabricaConexao` reads this file from the classpath; the Flyway plugin reads the same file via `properties-maven-plugin`. `db.properties` is gitignored (no credentials in source).

### Build Commands
```bash
# Clean and compile
mvn clean install

# Run migrations (Flyway)
mvn flyway:migrate

# Start embedded Tomcat 9 on http://localhost:8080
mvn cargo:run
```

### Database Migrations
Flyway migrations are in `src/main/resources/db/migration/`:
1. `V1__Criar_tabela_setor.sql` — Setor table
2. `V2__Criar_tabela_endereco.sql` — Endereco table
3. `V3__Criar_tabela_funcionario.sql` — Funcionario table
4. `V4__Alterar_tabela_funcionario_fks.sql` — FK constraints (Setor, Endereco)
5. `V5__Alterar_tabela_setor_fk.sql` — Setor manager FK
6. `V6__Criar_tabela_contrato.sql` — **Contrato table** (separates employment data from Funcionario)
7. `V7__Alterar_tabela_funcionario.sql` — strips employment columns from Funcionario (now personal data only)
8. `V8__Alterar_tabela_setor_gerente.sql` — Setor manager now references a contract (`id_contrato_responsavel`)
9. `V9__Corrigir_constraint_nivel_senioridade.sql` — fixes the seniority-level CHECK constraint
10. `V99__seed_dados_teste.sql` — seed/test data

Run migrations via: `mvn flyway:migrate` (Flyway reads credentials from `db.properties`).

## Architecture

### Layered Structure
```
controller/                 -- Servlet routing (controller.do)
  └─ br.com.commandfactory
     └─ controller/         -- Command pattern actions (ICommand implementations, 23 Actions)
com.mycompany.gerenciamentorh/
  ├─ JakartaRestConfiguration -- JAX-RS Application (@ApplicationPath "api")
  └─ resources/             -- REST endpoints (FuncionarioResource, ContratoResource, SetorResource)
service/                    -- Business logic & rule enforcement
  ├─ IFuncionarioService    -- Employee (personal data) operations
  ├─ IContratoService       -- Contract lifecycle (hire, terminate, rehire, promote)
  ├─ ISetorService          -- Sector operations
  ├─ FuncionarioServiceImpl, ContratoServiceImpl, SetorServiceImpl
  └─ ServiceFactory         -- Factory for service instances
salary/                     -- Salary calculation (Decorator pattern)
  ├─ CalculadoraSalario     -- Component interface
  ├─ SalarioBaseContrato    -- Concrete component (base salary)
  ├─ AumentoDecorator       -- Abstract decorator
  ├─ AumentoPercentual      -- Percentage raise decorator
  └─ AumentoPorBonus        -- Fixed-bonus raise decorator
dao/                        -- Data access objects (Repository pattern)
  ├─ IFuncionarioDAO, IContratoDAO, ISetorDAO, IEnderecoDAO
  ├─ FuncionarioDAO, ContratoDAO, SetorDAO, EnderecoDAO
  └─ DAOFactory             -- Factory for DAO instances
model/                      -- JPA-free POJOs with builders
  ├─ Funcionario            -- Personal data only
  ├─ Contrato               -- Employment data (1 Funcionario : N Contrato)
  ├─ Setor
  ├─ Endereco
  └─ NivelSenioridade       -- Seniority enum (Jovem Aprendiz → Senior, weighted)
util/                       -- Helper utilities
  └─ FabricaConexao         -- PostgreSQL connection (reads db.properties)
webapp/                     -- JSP views and static assets
  └─ WEB-INF/
```

### REST API (alternative entry point)
JAX-RS resources under `@ApplicationPath("api")` reuse the same `ServiceFactory`/`DAOFactory`:
- `GET/POST/PUT/DELETE /api/funcionarios` — employee CRUD
- `POST /api/contratos`, `/api/contratos/{id}/demitir`, `/api/contratos/{id}/promocao`, `GET /api/contratos/ativo/{funcId}`, `/api/contratos/historico/{funcId}`
- `POST/PUT/DELETE /api/setores`, `PUT /api/setores/{id}/gerente`

### Request Flow
1. `ControllerServlet` at `/controller.do` intercepts all requests
2. Extracts `acao` (action) parameter from query/form
3. Dynamically loads `br.com.commandfactory.controller.{acao}Action` class via reflection
4. Executes `ICommand.executar()` which returns JSP path
5. Forwards to JSP for rendering

### Key Patterns

#### Command Pattern (Controller Layer)
- 23 action classes (e.g., `CadastrarFuncionarioAction`, `DemitirFuncionarioAction`, `AplicarPromocaoAction`, `RecontratarFuncionarioAction`)
- All implement `ICommand` with `executar(HttpServletRequest, HttpServletResponse): String`
- Returns JSP path for forwarding
- Handles request parameter extraction and validation
- Actions with business rules call `ServiceFactory`; read-only/simple CRUD actions call `DAOFactory` directly

#### Factory Pattern
- `DAOFactory` — centralized DAO instantiation
- `ServiceFactory` — centralized service instantiation
- Both return concrete implementations via static methods

#### Repository Pattern
- DAO classes handle all database operations
- JDBC-based (no ORM)
- Prepared statements for SQL injection prevention
- Interfaces allow for dependency injection

#### Decorator Pattern (Salary Calculation) — `salary/`
- Implemented manually without annotation magic
- `CalculadoraSalario` is the component; `SalarioBaseContrato` is the base; `AumentoPercentual` and `AumentoPorBonus` are decorators wrapping a `CalculadoraSalario`
- **Actively used** in `ContratoServiceImpl.aplicarPromocao()`: a `SalarioBaseContrato` is wrapped by the chosen decorator (`PERCENTUAL` or `BONUS`) to compute the new salary

#### Builder Pattern (Model)
- `Funcionario`, `Contrato`, `Setor`, `Endereco` each expose a static `getBuilder()` returning a fluent inner builder (`.comX(...).constroi()`)

## Critical Business Rules

### Employee (Funcionario)
- Holds **personal data only** (name, CPF, phone, email, address) plus a `desligado` flag (derived: has no active contract)
- CPF stored as 11 digits only (no formatting) — sanitized in form actions
- Phone stored as 11 digits only — sanitized in form actions
- Formatted display methods: `getCpfFormatado()`, `getTelefoneFormatado()`

### Contract (Contrato) — fully implemented (1 Funcionario : N Contrato)
- Holds all employment data: `matricula`, `dataAdmissao`, `dataDemissao`, `motivoDesligamento`, `salarioBase`, `nivelSenioridade`, plus FKs to `Funcionario` and `Setor`
- All rules live in `ContratoServiceImpl`:
  - **Hire** (`contratar`): admission date cannot be in the future; employee must have no active contract; matricula auto-generated `{YEAR}-{5-char UUID}` (e.g., `2026-B8D2F`)
  - **Terminate** (`demitir`): termination date cannot be future; contract must not already be closed; sets `dataDemissao` + `motivoDesligamento` (never deletes)
  - **Rehire** (`recontratar`): no active contract allowed; new level must be ≥ previous; new salary must be ≥ 10% above previous
  - **Promote** (`aplicarPromocao`): new level must be **strictly higher** than current (`NivelSenioridade.ehInferiorA`); new salary computed via the salary **Decorator** (`PERCENTUAL` or `BONUS`)
- `NivelSenioridade` enum (weighted): Jovem Aprendiz(1) → Estagiário(2) → Junior(3) → Pleno(4) → Senior(5)

### Sector (Setor)
- Manager is a **contract** (`contratoResponsavel`), not a raw employee: the contract must be active, belong to that sector, and be Pleno or Senior (`SetorServiceImpl.vincularGerente`)
- Sector deletion blocked if active contracts exist (checked in `SetorDAO.deletar`)
- Manager FK is `id_contrato_responsavel`

### Address (Endereco)
- 1:1 relationship with Funcionario (UNIQUE FK constraint)
- Atomic persistence: if address save fails, employee save also rolls back
- CEP stored as 8 digits, formatted as `#####-###`
- Formatted method: `getEnderecoPadronizado()`, `getCepFormatado()`

## Common Development Tasks

### Add a New Operation (e.g., update sector name)
1. Create action class: `src/main/java/br/com/commandfactory/controller/AtualizarSetorAction.java`
   - Implement `ICommand`
   - Extract parameters, validate, call service
   - Return JSP path (success or error page)
2. Add service method: `SetorServiceImpl.atualizar(Setor)`
   - Enforce business rules
3. Add DAO method: `SetorDAO.atualizar(Setor)`
   - Execute SQL UPDATE via JDBC
4. Create/update JSP form in `src/main/webapp/`
5. Add link in `index.jsp` with `controller.do?acao=AtualizarSetor`

### Modify Database Schema
1. Create new migration file in `src/main/resources/db/migration/`
   - Follow naming: `V{N}__{Description}.sql`
   - Flyway auto-runs in version order
2. Update model classes if needed
3. Run `mvn flyway:migrate` to reset DB
4. Update DAO query strings

### Automated Tests
- Unit tests with **JUnit 5** (`junit-jupiter:5.10.2`) in `src/test/java/`
- `ContratoServiceImplTest` covers the contract lifecycle rules (hire/terminate/rehire/promote) using `ContratoDAOFake` (an in-memory `IContratoDAO`), so no database is required
- Run with `mvn test`

### Test a Feature End-to-End (manual)
1. Ensure PostgreSQL is running on port 5433 and `db.properties` is configured
2. Run `mvn clean flyway:migrate` to reset DB
3. Run `mvn cargo:run` to start server on :8080
4. Navigate to `http://localhost:8080/GerenciamentoRh/`
5. Use JSP forms (or the `/api` REST endpoints) to test the flow

## Known Limitations & Debt

- Test coverage is partial — only `ContratoServiceImpl` is unit-tested; other services/DAOs and the web layer are untested
- No transaction handling in DAOs — transactions only at service layer (e.g. `FuncionarioServiceImpl.cadastrar`)
- JDBC only — no ORM, repetitive boilerplate in DAO classes
- Limited input validation — mostly at action/service level
- No error logging — `printStackTrace()` only in ControllerServlet
- JSP without templating — no layout reuse, duplicated HTML across pages

## Dependencies

From `pom.xml`:
- jakarta.jakartaee-api:8.0.0 — Servlet/JSP API (provided by Tomcat)
- postgresql:42.7.3 — JDBC driver
- jstl:1.2 — JSP Standard Tag Library
- flyway-core & flyway-database-postgresql:10.10.0 — Schema migrations
- junit-jupiter:5.10.2 — Unit testing (test scope)
- jersey-container-servlet / jersey-hk2 / jersey-media-json-jackson:2.41 — JAX-RS REST runtime + JSON
- properties-maven-plugin:1.2.1 — loads `db.properties` for Flyway
- cargo-maven3-plugin:1.10.13 — Embedded Tomcat 9 runner

## IDE Notes

- Project uses NetBeans (`.idea/` and `nbproject/` directories present)
- `.vscode/` directory exists (VS Code integration possible)
- Code generated from NetBeans templates (visible in header comments)
- Encoding: UTF-8 throughout

## Git & Deployment

- Repository: https://github.com/Nicolas-Mariano/GerenciamentoRh.git
- Main branch: latest stable build
- Course assignment milestone: M2 evaluation (Design Patterns discipline)
- No CI/CD configured yet (local development only)
