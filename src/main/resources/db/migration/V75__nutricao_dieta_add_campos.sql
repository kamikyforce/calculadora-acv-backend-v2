-- Adiciona campos de quantidade, oferta e produção nas tabelas de dieta por lote
ALTER TABLE ingrediente_dieta_lote
    ADD COLUMN IF NOT EXISTS quantidade_kg_cab_dia NUMERIC(10,3) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS oferta_dias_ano INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS producao VARCHAR(20) NOT NULL DEFAULT 'INTERNA';

ALTER TABLE concentrado_dieta_lote
    ADD COLUMN IF NOT EXISTS quantidade_kg_cab_dia NUMERIC(10,3) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS oferta_dias_ano INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS producao VARCHAR(20) NOT NULL DEFAULT 'INTERNA';

ALTER TABLE aditivo_dieta_lote
    ADD COLUMN IF NOT EXISTS quantidade_kg_cab_dia NUMERIC(10,3) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS oferta_dias_ano INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS producao VARCHAR(20) NOT NULL DEFAULT 'INTERNA';