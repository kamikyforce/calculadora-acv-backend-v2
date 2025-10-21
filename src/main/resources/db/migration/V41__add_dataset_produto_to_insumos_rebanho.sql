-- V41__add_dataset_produto_to_insumos_rebanho.sql

-- Adiciona campo dataset_produto na tabela insumos_rebanho
ALTER TABLE insumos_rebanho 
    ADD COLUMN dataset_produto VARCHAR(255);