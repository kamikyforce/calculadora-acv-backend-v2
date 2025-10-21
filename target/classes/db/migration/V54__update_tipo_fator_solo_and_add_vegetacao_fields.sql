-- V54__update_tipo_fator_solo_and_add_vegetacao_fields.sql
-- Atualizar TipoFatorSolo conforme especificação CAPW-8 e adicionar campos de vegetação

-- 1. Atualizar valores do TipoFatorSolo
UPDATE dados_solo 
SET tipo_fator_solo = 'USO_ANTERIOR_ATUAL' 
WHERE tipo_fator_solo = 'SOLO_LAC';

UPDATE dados_solo 
SET tipo_fator_solo = 'SOLO_USO_ANTERIOR_ATUAL' 
WHERE tipo_fator_solo = 'SOLO_ARENOSO';

-- 2. Atualizar constraint do TipoFatorSolo
ALTER TABLE dados_solo 
DROP CONSTRAINT IF EXISTS dados_solo_tipo_fator_solo_check;

ALTER TABLE dados_solo 
ADD CONSTRAINT dados_solo_tipo_fator_solo_check 
    CHECK (tipo_fator_solo IN ('USO_ANTERIOR_ATUAL', 'SOLO_USO_ANTERIOR_ATUAL'));

-- 3. Adicionar novos campos para dados_vegetacao
ALTER TABLE dados_vegetacao 
ADD COLUMN IF NOT EXISTS especie_vegetacao VARCHAR(255),
ADD COLUMN IF NOT EXISTS altura_media DECIMAL(10,2),
ADD COLUMN IF NOT EXISTS biomassa_aerea DECIMAL(15,6),
ADD COLUMN IF NOT EXISTS biomassa_subterranea DECIMAL(15,6);

-- 4. Comentários para documentar as mudanças
COMMENT ON COLUMN dados_solo.tipo_fator_solo IS 'Tipo de fator de solo: USO_ANTERIOR_ATUAL (Fatores que variam com uso anterior e atual) ou SOLO_USO_ANTERIOR_ATUAL (Fatores que variam com tipo de solo e uso anterior e atual)';
COMMENT ON COLUMN dados_vegetacao.especie_vegetacao IS 'Espécie da vegetação';
COMMENT ON COLUMN dados_vegetacao.altura_media IS 'Altura média da vegetação em metros';
COMMENT ON COLUMN dados_vegetacao.biomassa_aerea IS 'Biomassa aérea da vegetação';
COMMENT ON COLUMN dados_vegetacao.biomassa_subterranea IS 'Biomassa subterrânea da vegetação';