-- V45__add_versao_column_to_insumos_producao_agricola.sql
-- Adiciona coluna versao na tabela insumos_producao_agricola para controle de versionamento

ALTER TABLE insumos_producao_agricola 
    ADD COLUMN versao VARCHAR(10) NOT NULL DEFAULT 'v1';

-- Comentário sobre a estratégia:
-- A coluna versao armazenará valores no formato 'v1', 'v2', 'v3', etc.
-- Novos registros sempre começam com 'v1'
-- A cada atualização, a versão será incrementada automaticamente pelo service