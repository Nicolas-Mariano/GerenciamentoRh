CREATE TABLE contrato (
    id SERIAL PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL UNIQUE,
    data_admissao DATE NOT NULL,
    data_demissao DATE,
    motivo_desligamento TEXT,
    salario_base DECIMAL(10,2) NOT NULL,
    nivel_senioridade VARCHAR(10) NOT NULL CHECK (nivel_senioridade IN ('JUNIOR', 'PLENO', 'SENIOR')),
    id_funcionario INTEGER NOT NULL REFERENCES funcionario(id),
    id_setor INTEGER NOT NULL REFERENCES setor(id),
    CONSTRAINT chk_contrato_admissao CHECK (data_admissao <= CURRENT_DATE),
    CONSTRAINT chk_contrato_datas CHECK (data_demissao IS NULL OR data_demissao >= data_admissao)
);
