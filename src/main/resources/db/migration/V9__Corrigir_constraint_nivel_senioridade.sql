-- =============================================================================
-- V9__Corrigir_constraint_nivel_senioridade.sql
-- Corrige a constraint CHECK da coluna nivel_senioridade para aceitar
-- todos os valores do enum NivelSenioridade: JOVEM_APRENDIZ e ESTAGIARIO
-- que eram rejeitados pela constraint original (V6).
-- Amplia também o VARCHAR(10) para VARCHAR(20) pois JOVEM_APRENDIZ tem 15 chars.
-- =============================================================================

-- 1. Ampliar a coluna para comportar JOVEM_APRENDIZ (15 caracteres)
ALTER TABLE contrato ALTER COLUMN nivel_senioridade TYPE VARCHAR(20);

-- 2. Dropar a constraint antiga que só permitia JUNIOR, PLENO, SENIOR
ALTER TABLE contrato DROP CONSTRAINT IF EXISTS contrato_nivel_senioridade_check;

-- 3. Recriar com todos os valores válidos do enum
ALTER TABLE contrato ADD CONSTRAINT contrato_nivel_senioridade_check
    CHECK (nivel_senioridade IN ('JOVEM_APRENDIZ', 'ESTAGIARIO', 'JUNIOR', 'PLENO', 'SENIOR'));
