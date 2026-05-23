ALTER TABLE funcionario ADD COLUMN email VARCHAR(150);

ALTER TABLE funcionario DROP COLUMN IF EXISTS matricula;
ALTER TABLE funcionario DROP COLUMN IF EXISTS funcao;
ALTER TABLE funcionario DROP COLUMN IF EXISTS salario_base;
ALTER TABLE funcionario DROP COLUMN IF EXISTS nivel;
ALTER TABLE funcionario DROP COLUMN IF EXISTS data_admissao;
ALTER TABLE funcionario DROP COLUMN IF EXISTS data_demissao;
ALTER TABLE funcionario DROP COLUMN IF EXISTS id_setor;
