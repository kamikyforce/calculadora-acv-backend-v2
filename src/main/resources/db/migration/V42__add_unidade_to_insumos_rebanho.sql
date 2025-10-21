-- V42__add_unidade_to_insumos_rebanho.sql
-- Adiciona campo unidade na tabela insumos_rebanho para o escopo 1

-- Adicionar a coluna unidade para armazenar a unidade do produto no escopo 1
ALTER TABLE insumos_rebanho 
    ADD COLUMN unidade VARCHAR(20);

-- Criar índice para melhor performance nas consultas por unidade
CREate INDEX IF NOT EXISTS idx_insumos_rebanho_unidade 
    ON insumos_rebanho (unidade);

-- Adicionar comentário na coluna para documentação
COMMENT ON COLUMN insumos_rebanho.unidade IS 'Unidade do produto para o escopo 1 (KG, T, G, L, etc.)';

-- Log da migration
DO $$
BEGIN
    RAISE NOTICE 'Migration V42 concluída: Campo unidade adicionado à tabela insumos_rebanho';
END $$;