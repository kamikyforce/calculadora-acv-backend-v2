-- V22__fix_insumos_rebanho_column_names.sql
-- Corrige os nomes das colunas para coincidir com o mapeamento JPA da entidade InsumoRebanho

-- Renomear colunas que não coincidem com o mapeamento JPA
ALTER TABLE insumos_rebanho 
    RENAME COLUMN dioxido_carbono_metano TO co2_ch4_transformacao;

ALTER TABLE insumos_rebanho 
    RENAME COLUMN gwp_fossil TO gwp_100_fossil;

ALTER TABLE insumos_rebanho 
    RENAME COLUMN gwp_biogenico TO gwp_100_biogenico;

ALTER TABLE insumos_rebanho 
    RENAME COLUMN gwp_transformacao TO gwp_100_transformacao;

ALTER TABLE insumos_rebanho 
    RENAME COLUMN ms_kg TO ms;

-- Adicionar colunas que estão na entidade mas não existem na tabela
ALTER TABLE insumos_rebanho 
    ADD COLUMN uuid VARCHAR(100);

ALTER TABLE insumos_rebanho 
    ADD COLUMN quantidade DECIMAL(15,6);

-- Remover colunas que existem na tabela mas não na entidade
ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS unidade_produto_referencia;

ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS dioxido_carbono_fossil;

ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS metano_fossil;

ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS metano_biogenico;

ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS oxido_nitroso;

ALTER TABLE insumos_rebanho 
    DROP COLUMN IF EXISTS outras_substancias_escopo3;

-- Comentário explicativo
COMMENT ON TABLE insumos_rebanho IS 'Tabela de insumos do rebanho - colunas alinhadas com entidade JPA';