-- V53__add_descricao_to_fator_mut.sql
-- Migration para adicionar coluna descricao à tabela fator_mut

-- Adicionar a coluna descricao à tabela fator_mut
ALTER TABLE fator_mut 
ADD COLUMN IF NOT EXISTS descricao VARCHAR(500);

-- Adicionar comentário para documentar a coluna
COMMENT ON COLUMN fator_mut.descricao IS 'Descrição detalhada do fator MUT (máximo 500 caracteres)';

-- Criar índice para melhorar performance em buscas por descrição
CREATE INDEX IF NOT EXISTS idx_fator_mut_descricao ON fator_mut(descricao);