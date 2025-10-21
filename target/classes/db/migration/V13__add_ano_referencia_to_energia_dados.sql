-- Migration para adicionar coluna ano_referencia à tabela energia_dados
ALTER TABLE energia_dados ADD COLUMN ano_referencia INTEGER;

-- Criar índice para melhor performance nas consultas
CREATE INDEX IF NOT EXISTS idx_energia_dados_ano_referencia ON energia_dados(ano_referencia);

-- Adicionar constraint para validar anos válidos (2006-2030)
ALTER TABLE energia_dados ADD CONSTRAINT chk_ano_referencia_valido 
    CHECK (ano_referencia IS NULL OR (ano_referencia >= 2006 AND ano_referencia <= 2030));