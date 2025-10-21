-- V65__remove_nome_unique_constraint.sql
-- Remove a constraint única que impede nome null e permite múltiplos registros com nome null

-- Remove a constraint única existente
ALTER TABLE fator_mut DROP CONSTRAINT IF EXISTS uk_fator_mut_nome_tipo_escopo_ativo;

-- Permite que o campo nome seja null
ALTER TABLE fator_mut ALTER COLUMN nome DROP NOT NULL;

-- Cria uma nova constraint única que permite múltiplos valores null para nome
-- Usando uma constraint parcial que só se aplica quando nome não é null
CREATE UNIQUE INDEX uk_fator_mut_nome_tipo_escopo_not_null 
ON fator_mut (nome, tipo_mudanca, escopo) 
WHERE nome IS NOT NULL;