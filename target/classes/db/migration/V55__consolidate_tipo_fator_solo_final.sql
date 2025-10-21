-- V55: Consolidação final e definitiva do TipoFatorSolo
-- Garantindo que TODOS os valores antigos sejam mapeados corretamente

-- Maps ALL possible old values to new ones
UPDATE dados_solo SET tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
WHERE tipo_fator_solo IN ('SOLO_LAC', 'ALTO_CARBONO', 'BAIXO_CARBONO', 'ORGANICO');

UPDATE dados_solo SET tipo_fator_solo = 'SOLO_USO_ANTERIOR_ATUAL'  
WHERE tipo_fator_solo IN ('SOLO_ARENOSO', 'MINERAL');

-- Remover qualquer constraint antigo
ALTER TABLE dados_solo DROP CONSTRAINT IF EXISTS dados_solo_tipo_fator_solo_check;

-- Adicionar constraint final definitivo
ALTER TABLE dados_solo 
ADD CONSTRAINT dados_solo_tipo_fator_solo_check 
CHECK (tipo_fator_solo IN ('USO_ANTERIOR_ATUAL', 'SOLO_USO_ANTERIOR_ATUAL'));

-- Comentário final
COMMENT ON COLUMN dados_solo.tipo_fator_solo IS 'Tipo de fator de solo: USO_ANTERIOR_ATUAL (Uso anterior/atual) ou SOLO_USO_ANTERIOR_ATUAL (Solo uso anterior/atual)';

-- Includes integrity verification
DO $$ BEGIN
    IF EXISTS (SELECT 1 FROM dados_solo WHERE tipo_fator_solo NOT IN ('USO_ANTERIOR_ATUAL', 'SOLO_USO_ANTERIOR_ATUAL'))
    THEN RAISE EXCEPTION 'Still have invalid tipo_fator_solo values';
    END IF;
END $$;