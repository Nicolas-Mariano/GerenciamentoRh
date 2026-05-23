# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**GerenciamentoRh** is an HR management system built with Java 24 using traditional servlet-based architecture with JSP views and PostgreSQL database. The project implements multiple design patterns (Decorator, Factory, Repository, Strategy, Command) as part of a Design Patterns course (Padrões de Projeto - PP).

The system manages the complete employee lifecycle: hiring, salary management, sector transfers, and termination. Critically, **employee data is separated from employment contracts** — a single employee can have multiple contracts, preserving employment history.

### Key Distinction
- **Funcionário** (Employee): Permanent personal data (name, CPF, phone, email, address)
- **Contrato** (Contract): Employment relationship data (hiring date, termination date, salary, sector, seniority level) — new contracts can be created for the same employee without overwriting old ones

## Build & Run

### Prerequisites
- Java 24 (Maven compiler target is Java 24)
- PostgreSQL 12+ running on `localhost:5433`
- Database: `gerenciamento_rh`
- PostgreSQL credentials: hardcoded in `FabricaConexao.java` and `pom.xml` (check those files for current values)

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
5. `V5__Alterar_tabela_setor_fk.sql` — Setor.id_func_responsavel FK

Run migrations via: `mvn flyway:migrate` (pom.xml has hardcoded DB credentials for Flyway)

## Architecture

### Layered Structure
```
controller/                 -- Servlet routing (ControllerServlet.do)
  └─ br.com.commandfactory
     └─ controller/         -- Command pattern actions (ICommand implementations)
service/                    -- Business logic & rule enforcement
  ├─ IFuncionarioService    -- Employee operations
  ├─ ISetorService          -- Sector operations
  ├─ FuncionarioServiceImpl
  ├─ SetorServiceImpl
  └─ ServiceFactory         -- Factory for service instances
dao/                        -- Data access objects (Repository pattern)
  ├─ IFuncionarioDAO
  ├─ ISetorDAO
  ├─ IEnderecoDAO
  ├─ FuncionarioDAO
  ├─ SetorDAO
  ├─ EnderecoDAO
  └─ DAOFactory             -- Factory for DAO instances
model/                      -- JPA-free POJOs with builders
  ├─ Funcionario
  ├─ Setor
  └─ Endereco
util/                       -- Helper utilities
  └─ FabricaConexao         -- PostgreSQL connection pooling (hardcoded credentials)
webapp/                     -- JSP views and static assets
  └─ WEB-INF/
```

### Request Flow
1. `ControllerServlet` at `/controller.do` intercepts all requests
2. Extracts `acao` (action) parameter from query/form
3. Dynamically loads `br.com.commandfactory.controller.{acao}Action` class via reflection
4. Executes `ICommand.executar()` which returns JSP path
5. Forwards to JSP for rendering

### Key Patterns

#### Command Pattern (Controller Layer)
- 20+ action classes (e.g., `CadastrarFuncionarioAction`, `DemitirFuncionarioAction`)
- All implement `ICommand` with `executar(HttpServletRequest, HttpServletResponse): String`
- Returns JSP path for forwarding
- Handles request parameter extraction and validation

#### Factory Pattern
- `DAOFactory` — centralized DAO instantiation
- `ServiceFactory` — centralized service instantiation
- Both return concrete implementations via static methods

#### Repository Pattern
- DAO classes handle all database operations
- JDBC-based (no ORM)
- Prepared statements for SQL injection prevention
- Interfaces allow for dependency injection

#### Decorator Pattern (Salary Calculation)
- Implemented manually without annotation magic
- Salary raise system chains decorators: `AumentoPercentual` and `AumentoPorBonus`
- Wraps `SalarioBaseContrato` to calculate final salary
- Note: Current implementation in service layer is simplified (direct calculation, not full decorator chain in use)

#### Strategy Pattern
- Termination rules handled via different strategies (not explicitly segregated yet, but architecture supports it)

## Critical Business Rules

### Employee (Funcionario)
- Admission date **cannot be in the future** — enforced in `FuncionarioServiceImpl.cadastrar()`
- CPF stored as 11 digits only (no formatting) — sanitized in form actions
- Phone stored as 11 digits only — sanitized in form actions
- Formatted display methods: `getCpfFormatado()`, `getTelefoneFormatado()`
- Matricula auto-generated on hire: `{YEAR}-{5-char UUID}` (e.g., `2026-B8D2F`)

### Contract (Contrato)
- Not yet fully implemented as separate entity — currently salary/dates are stored in Funcionario
- Plan: Separate Contrato table with contract history
- Each new hiring creates a new contract record (preserves history)
- Termination date set via `demitirFuncionario()` — only registers demission, does not delete

### Sector (Setor)
- Manager must have Pleno or Sênior level AND active contract in that sector
- Sector deletion blocked if active contracts exist
- Manager field (`id_func_responsavel`) set to NULL on manager termination (FK: ON DELETE SET NULL)

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

### Test a Feature End-to-End
1. Ensure PostgreSQL is running on port 5433
2. Run `mvn clean flyway:migrate` to reset DB
3. Run `mvn cargo:run` to start server on :8080
4. Navigate to `http://localhost:8080/GerenciamentoRh/` 
5. Use JSP forms to test flow (no automated tests yet)

## Known Limitations & Debt

- No Contrato entity yet — currently all contract data stored in Funcionario (breaks 1:N design)
- No automated tests — README marks this "em desenvolvimento" (in development)
- Hardcoded DB credentials — in `FabricaConexao.java` and `pom.xml` (should use environment variables)
- No transaction handling in DAOs — transactions only at service layer
- JDBC only — no ORM, repetitive boilerplate in DAO classes
- Limited input validation — mostly at action/service level, minimal DB constraints beyond type checks
- No error logging — `printStackTrace()` only in ControllerServlet
- JSP without templating — no layout reuse, duplicated HTML across pages
- Java 24 compiler target — unusually high; consider downgrading to 17 for stability

## Dependencies

From `pom.xml`:
- jakarta.jakartaee-api:8.0.0 — Servlet/JSP API (provided by Tomcat)
- postgresql:42.7.3 — JDBC driver
- jstl:1.2 — JSP Standard Tag Library
- flyway-core & flyway-database-postgresql:10.10.0 — Schema migrations
- cargo-maven3-plugin:1.10.13 — Embedded Tomcat runner

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
