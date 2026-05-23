ALTER TABLE setor DROP COLUMN IF EXISTS id_func_responsavel;
ALTER TABLE setor ADD COLUMN id_contrato_responsavel INTEGER REFERENCES contrato(id) ON DELETE SET NULL;
