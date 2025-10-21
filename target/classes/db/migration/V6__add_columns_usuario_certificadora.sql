-- V6__add_columns_usuario_certificadora.sql
-- Adiciona colunas data_cadastro e ativo na tabela usuario_certificadora

ALTER TABLE usuario_certificadora 
ADD COLUMN data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

-- Atualiza registros existentes com valores padrão
UPDATE usuario_certificadora 
SET data_cadastro = CURRENT_TIMESTAMP, ativo = TRUE 
WHERE data_cadastro IS NULL OR ativo IS NULL;

-- Adiciona índice para performance
CREATE INDEX idx_usuario_certificadora_ativo ON usuario_certificadora(ativo);
CREATE INDEX idx_usuario_certificadora_data_cadastro ON usuario_certificadora(data_cadastro);