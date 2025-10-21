-- V52__fix_energia_dados_index.sql
-- Migration para corrigir o índice na tabela energia_dados

-- Remover o índice se ele existir (para evitar conflitos)
DROP INDEX IF EXISTS idx_energia_dados_ano_referencia;

-- Recriar o índice corretamente
CREATE INDEX idx_energia_dados_ano_referencia ON energia_dados(ano_referencia);