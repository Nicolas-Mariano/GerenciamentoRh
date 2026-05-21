# Sistema de Gerenciamento de RH

> Projeto desenvolvido para a avaliação M2 da disciplina **Padrões de Projeto (PP)** — Turma 5B Eng. Software

---

## Sobre o projeto

Sistema de gerenciamento de Recursos Humanos que contempla o ciclo de vida completo de um colaborador: contratação, gestão salarial, transferência entre setores e desligamento. O histórico de vínculos empregatícios é preservado por meio da entidade **Contrato**, que separa os dados pessoais do funcionário dos dados do vínculo empregatício.

---

## Integrantes

| Nome | GitHub |
|------|--------|
| Ana Raquel | [@Ana Raquel](https://github.com/anaraquelslv) |
| Felipe Pontes | [@usuario](https://github.com/usuario) |
| Larissa | [@Larissa Santo](https://github.com/Larissa-Holy) |
| Nicolas Mariano |[@Nicolas Mariano](https://github.com/Nicolas-Mariano) |

---

## Funcionalidades

### Módulo Funcionário
- Cadastro com geração automática de matrícula no formato `ANO-UUID5` (ex: `2026-B8D2F`)
- Validação de data de admissão (não permite datas futuras)
- Sanitização e formatação de CPF (`###.###.###-##`) e telefone (`(##) #####-####`)
- Edição de dados cadastrais e endereço residencial
- Exclusão definitiva

### Módulo Contrato
- Criação de contrato ao contratar um funcionário (cada vínculo é um registro independente)
- Registro de demissão com data e motivo de desligamento
- **Recontratar** gera um novo contrato — o histórico anterior é preservado
- Consulta de contrato ativo (`data_demissao IS NULL`)
- Histórico completo de vínculos por funcionário
- **Aplicação de aumento salarial** via padrão Decorator (percentual ou valor fixo, encadeáveis)

### Módulo Setor
- Cadastro de setores com nome obrigatório
- Vinculação de gerente: apenas funcionários com nível **Pleno** ou **Sênior** e contrato ativo no setor
- Bloqueio de exclusão quando há contratos ativos vinculados
- Ocultação automática do gerente ao ser demitido

### Módulo Endereço
- Endereço residencial vinculado ao funcionário em relação 1:1
- Formatação de CEP (`#####-###`)
- Persistência atômica com o funcionário (rollback automático em caso de falha)

---

## Modelo de dados

```
Funcionário (1) ──── (N) Contrato (N) ──── (1) Setor
     │
    (1)
     │
Endereço (1)
```

- **Funcionário** → dados pessoais permanentes (nome, CPF, telefone, e-mail)
- **Contrato** → vínculo empregatício (admissão, demissão, salário, setor, senioridade)
- **Setor** → gerente aponta para o contrato ativo do colaborador responsável
- **Endereço** → residencial, 1:1 com Funcionário

---

## Design Patterns aplicados

### Decorator — Aumento Salarial (obrigatório)

Implementado manualmente, sem uso de pacotes ou anotações da linguagem.

```
«interface» CalculadoraSalario
        calcular(contrato): double
              /           \
    SalarioBaseContrato   AumentoDecorator (abstrato)
    (retorna salário          /            \
     do contrato)    AumentoPercentual   AumentoPorBonus
                     base*(1+pct/100)    base + valor fixo
```

Exemplo de encadeamento:

```java
CalculadoraSalario calc = new AumentoPercentual(
    new AumentoPorBonus(
        new SalarioBaseContrato(), 500.0
    ), 10.0
);
double novoSalario = calc.calcular(contratoAtivo);
```

### Outros padrões utilizados

| Padrão | Onde foi aplicado |
|--------|------------------|
| Repository | Acesso e persistência de cada entidade |
| Strategy | Regras de cálculo de desligamento |
| Factory | Criação de novos Contratos |

---

## Princípios SOLID

| Princípio | Aplicação no projeto |
|-----------|---------------------|
| **SRP** — Responsabilidade única | Cada classe de serviço cuida de uma entidade (`FuncionarioService`, `ContratoService`, `SetorService`) |
| **OCP** — Aberto/fechado | Novos tipos de aumento são adicionados como novos Decorators, sem alterar os existentes |
| **LSP** — Liskov | Qualquer `CalculadoraSalario` pode ser substituída sem quebrar o sistema |
| **ISP** — Segregação de interfaces | Interfaces específicas por responsabilidade, sem forçar implementações desnecessárias |
| **DIP** — Inversão de dependência | Serviços dependem de abstrações (interfaces/repositórios), não de implementações concretas |

---

## Regras de negócio principais

- Data de admissão não pode ser futura
- Data de demissão não pode ser futura
- Recontratar cria um **novo Contrato** — nunca anula o anterior
- Gerente deve ter nível Pleno ou Sênior e contrato ativo no setor que irá liderar
- Setor não pode ser excluído com contratos ativos vinculados
- CPF e telefone são armazenados apenas com dígitos; a formatação é aplicada na exibição
- Matrícula é gerada automaticamente por contrato no formato `ANO-UUID5`

---

## Tecnologias

- **Linguagem:** Java 17+
- **Framework:** Spring Boot
- **Banco de dados:** PostgreSQL 

---

A aplicação sobe em `http://localhost:8080`.

---

## Principais endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/funcionarios` | Cadastrar funcionário |
| `GET` | `/funcionarios` | Listar todos |
| `GET` | `/funcionarios/{id}` | Buscar por ID |
| `PUT` | `/funcionarios/{id}` | Atualizar dados |
| `DELETE` | `/funcionarios/{id}` | Excluir |
| `POST` | `/contratos` | Criar contrato (contratar) |
| `GET` | `/contratos/ativo/{funcionarioId}` | Contrato ativo do funcionário |
| `GET` | `/contratos/historico/{funcionarioId}` | Histórico de contratos |
| `PATCH` | `/contratos/{id}/demitir` | Registrar demissão |
| `PATCH` | `/contratos/{id}/aumento` | Aplicar aumento salarial |
| `POST` | `/setores` | Cadastrar setor |
| `PUT` | `/setores/{id}/gerente` | Vincular gerente |
| `DELETE` | `/setores/{id}` | Excluir setor |

---

## Diagramas

Os diagramas de Classes e de Sequência estão disponíveis na pasta [`/docs`](./docs).

---

## Critérios de avaliação atendidos

- [x] Design Pattern Decorator (obrigatório, implementação manual)
- [x] Demais Design Patterns (Repository, Strategy, Factory)
- [x] Princípios SOLID e Calistenia de Objetos
- [x] Relacionamento 1:1 (Funcionário → Endereço)
- [x] Relacionamento 1:N (Funcionário → Contrato, Setor → Contrato)
- [x] Diagramas de Classes e Sequência
- [x] Hospedagem no GitHub com README
- [ ] Testes automatizados (em desenvolvimento)

---

> Disciplina: Padrões de Projeto (PP) · Turma 5B Eng. Software · Avaliação M2
