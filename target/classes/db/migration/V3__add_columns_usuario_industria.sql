-- V3__add_columns_usuario_industria.sql
-- Adiciona colunas data_cadastro e ativo na tabela usuario_industria

ALTER TABLE usuario_industria 
ADD COLUMN data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

-- Atualiza registros existentes com valores padrão
UPDATE usuario_industria 
SET data_cadastro = CURRENT_TIMESTAMP, ativo = TRUE 
WHERE data_cadastro IS NULL OR ativo IS NULL;

-- Adiciona índice para performance
CREATE INDEX idx_usuario_industria_ativo ON usuario_industria(ativo);
CREATE INDEX idx_usuario_industria_data_cadastro ON usuario_industria(data_cadastro);