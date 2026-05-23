-- =============================================================================
-- V99__seed_dados_teste.sql
-- Seed de dados de teste para o sistema GerenciamentoRh
-- Banco: PostgreSQL | Gerenciador de migrations: Flyway
--
-- Cenários cobertos:
--   - 3 setores (TI, RH, Financeiro)
--   - 5 funcionários com endereços 1:1 (transação atômica via DO block)
--   - 5 contratos ativos + 1 contrato histórico (Fernanda Lima)
--   - 2 gerentes vinculados (TI e RH); Financeiro sem gerente
--   - Fernanda Lima: demissão + recontratação (testa buscarHistorico)
--
-- Usa PL/pgSQL (DO $$) para capturar IDs gerados pelo SERIAL via RETURNING,
-- garantindo integridade referencial sem depender de IDs sequenciais fixos.
-- =============================================================================

BEGIN;

DO $$
DECLARE
    -- IDs dos setores
    v_id_ti          INTEGER;
    v_id_rh          INTEGER;
    v_id_fin         INTEGER;

    -- IDs dos endereços
    v_end_carlos     INTEGER;
    v_end_mariana    INTEGER;
    v_end_fernanda   INTEGER;
    v_end_roberto    INTEGER;
    v_end_juliana    INTEGER;

    -- IDs dos funcionários
    v_func_carlos    INTEGER;
    v_func_mariana   INTEGER;
    v_func_fernanda  INTEGER;
    v_func_roberto   INTEGER;
    v_func_juliana   INTEGER;

    -- IDs dos contratos (apenas os que viram gerente)
    v_cont_carlos    INTEGER;
    v_cont_juliana   INTEGER;

BEGIN

    -- =========================================================================
    -- 1. SETORES
    -- Inseridos antes dos contratos pois contrato.id_setor referencia setor(id).
    -- id_contrato_responsavel permanece NULL e é atualizado na etapa 5.
    -- =========================================================================
    INSERT INTO setor (nome) VALUES ('TI')
        RETURNING id INTO v_id_ti;

    INSERT INTO setor (nome) VALUES ('Recursos Humanos')
        RETURNING id INTO v_id_rh;

    INSERT INTO setor (nome) VALUES ('Financeiro')
        RETURNING id INTO v_id_fin;


    -- =========================================================================
    -- 2. ENDEREÇOS (um por funcionário)
    -- Inseridos antes dos funcionários pois funcionario.id_endereco NOT NULL.
    -- =========================================================================
    INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento)
    VALUES ('Rua das Flores', 'Jardim Europa', 'São Paulo', 'SP', '01310100', '101', 'Apto 12')
        RETURNING id INTO v_end_carlos;

    INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento)
    VALUES ('Avenida Paulista', 'Bela Vista', 'São Paulo', 'SP', '01311000', '1578', NULL)
        RETURNING id INTO v_end_mariana;

    INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento)
    VALUES ('Rua das Acacias', 'Centro', 'Belo Horizonte', 'MG', '30130050', '45', 'Casa')
        RETURNING id INTO v_end_fernanda;

    INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento)
    VALUES ('Rua Sete de Setembro', 'Batista Campos', 'Belem', 'PA', '66015150', '200', 'Bloco B Ap 3')
        RETURNING id INTO v_end_roberto;

    INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento)
    VALUES ('Rua da Paz', 'Meireles', 'Fortaleza', 'CE', '60165070', '320', 'Ap. 5')
        RETURNING id INTO v_end_juliana;


    -- =========================================================================
    -- 3. FUNCIONÁRIOS
    -- CPF e telefone: apenas dígitos (11 caracteres cada), sem máscara.
    -- =========================================================================
    INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco)
    VALUES ('Carlos Silva', '11122233344', '11987654321', 'carlos.silva@empresa.com', v_end_carlos)
        RETURNING id INTO v_func_carlos;

    INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco)
    VALUES ('Mariana Costa', '55566677788', '21987654321', 'mariana.costa@empresa.com', v_end_mariana)
        RETURNING id INTO v_func_mariana;

    INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco)
    VALUES ('Fernanda Lima', '99900011122', '31987654321', 'fernanda.lima@empresa.com', v_end_fernanda)
        RETURNING id INTO v_func_fernanda;

    INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco)
    VALUES ('Roberto Alves', '33344455566', '41987654321', 'roberto.alves@empresa.com', v_end_roberto)
        RETURNING id INTO v_func_roberto;

    INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco)
    VALUES ('Juliana Souza', '77788899900', '51987654321', 'juliana.souza@empresa.com', v_end_juliana)
        RETURNING id INTO v_func_juliana;


    -- =========================================================================
    -- 4. CONTRATOS
    -- Matrículas no formato ANO-UUID5 maiúsculo (ex: 2022-A1B2C).
    -- Inseridos na ordem: ativos primeiro, depois histórico de Fernanda.
    -- =========================================================================

    -- Carlos Silva: SENIOR, setor TI — contrato ativo desde 2022
    INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2022-A1B2C', '2022-03-01', 12000.00, 'SENIOR', v_func_carlos, v_id_ti)
        RETURNING id INTO v_cont_carlos;

    -- Mariana Costa: PLENO, setor TI — contrato ativo desde 2023
    INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2023-D4E5F', '2023-06-15', 8000.00, 'PLENO', v_func_mariana, v_id_ti);

    -- Fernanda Lima: contrato 1 — ENCERRADO (demissão 2023-04-30)
    --   Testa: buscarHistorico retorna 2 contratos; buscarAtivo retorna apenas o 2o.
    INSERT INTO contrato (matricula, data_admissao, data_demissao, motivo_desligamento,
                          salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2022-G6H7I', '2022-01-10', '2023-04-30', 'Pedido de demissao',
            3500.00, 'JUNIOR', v_func_fernanda, v_id_rh);

    -- Fernanda Lima: contrato 2 — ATIVO (recontratação em 2024)
    INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2024-J8K9L', '2024-02-01', 4000.00, 'JUNIOR', v_func_fernanda, v_id_rh);

    -- Roberto Alves: PLENO, setor Financeiro — contrato ativo desde 2023
    INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2023-M0N1O', '2023-01-10', 9000.00, 'PLENO', v_func_roberto, v_id_fin);

    -- Juliana Souza: SENIOR, setor RH — contrato ativo desde 2022
    INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor)
    VALUES ('2022-P2Q3R', '2022-07-20', 11000.00, 'SENIOR', v_func_juliana, v_id_rh)
        RETURNING id INTO v_cont_juliana;


    -- =========================================================================
    -- 5. GERENTES DOS SETORES
    -- Contrato do gerente: ativo (data_demissao IS NULL), nível PLENO ou SENIOR,
    -- pertencente ao próprio setor — regra validada por SetorServiceImpl.
    --
    --   Setor TI        → Carlos Silva  (SENIOR, contrato '2022-A1B2C')
    --   Setor RH        → Juliana Souza (SENIOR, contrato '2022-P2Q3R')
    --   Setor Financeiro → sem gerente  (caso de teste: nomeResponsavel = null)
    -- =========================================================================
    UPDATE setor
       SET id_contrato_responsavel = v_cont_carlos
     WHERE id = v_id_ti;

    UPDATE setor
       SET id_contrato_responsavel = v_cont_juliana
     WHERE id = v_id_rh;

END;
$$;

COMMIT;
