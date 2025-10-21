-- V40__add_quantidade_produto_referencia_column.sql
-- Adiciona a coluna quantidade_produto_referencia que está faltando na tabela insumos_rebanho

-- Adicionar a coluna quantidade_produto_referencia
ALTER TABLE insumos_rebanho 
    ADD COLUMN quantidade_produto_referencia DECIMAL(15,6);