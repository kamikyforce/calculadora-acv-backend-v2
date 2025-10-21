-- V75__add_columns_concentrado_aditivo_tables.sql
-- Migration para adicionar novas colunas nas tabelas de concentrado e aditivo
-- para suportar as alterações feitas no frontend da nutrição manual

-- Adicionar novas colunas na tabela concentrado_dieta_lote
ALTER TABLE concentrado_dieta_lote 
ADD COLUMN proteina_bruta_percentual DECIMAL(5,2),
ADD COLUMN ureia VARCHAR(10) CHECK (ureia IN ('Com', 'Sem')),
ADD COLUMN subproduto VARCHAR(10) CHECK (subproduto IN ('Com', 'Sem')),
ADD COLUMN quantidade DECIMAL(10,3),
ADD COLUMN oferta DECIMAL(10,3);

-- Adicionar novas colunas na tabela aditivo_dieta_lote
ALTER TABLE aditivo_dieta_lote 
ADD COLUMN tipo VARCHAR(50),
ADD COLUMN dose DECIMAL(10,3),
ADD COLUMN oferta DECIMAL(10,3),
ADD COLUMN percentual_adicional DECIMAL(5,2);

-- Comentários sobre as alterações
COMMENT ON COLUMN concentrado_dieta_lote.proteina_bruta_percentual IS 'Percentual de proteína bruta do concentrado';
COMMENT ON COLUMN concentrado_dieta_lote.ureia IS 'Indica se o concentrado contém ureia (Com/Sem)';
COMMENT ON COLUMN concentrado_dieta_lote.subproduto IS 'Indica se o concentrado contém subproduto (Com/Sem)';
COMMENT ON COLUMN concentrado_dieta_lote.quantidade IS 'Quantidade do concentrado';
COMMENT ON COLUMN concentrado_dieta_lote.oferta IS 'Oferta do concentrado';

COMMENT ON COLUMN aditivo_dieta_lote.tipo IS 'Tipo do aditivo (dropdown com opções específicas)';
COMMENT ON COLUMN aditivo_dieta_lote.dose IS 'Dose do aditivo';
COMMENT ON COLUMN aditivo_dieta_lote.oferta IS 'Oferta do aditivo';
COMMENT ON COLUMN aditivo_dieta_lote.percentual_adicional IS 'Percentual adicional do aditivo (diferente do percentual original)';