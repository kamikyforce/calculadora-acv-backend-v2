-- V51__update_tipo_fator_solo_values.sql
-- De: ALTO_CARBONO, BAIXO_CARBONO, MINERAL, ORGANICO
-- Para: SOLO_LAC, SOLO_ARENOSO

-- Primeiro, atualizar os dados existentes mapeando os valores antigos para os novos
UPDATE dados_solo 
SET tipo_fator_solo = 'SOLO_LAC' 
WHERE tipo_fator_solo IN ('ALTO_CARBONO', 'ORGANICO');

UPDATE dados_solo 
SET tipo_fator_solo = 'SOLO_ARENOSO' 
WHERE tipo_fator_solo IN ('BAIXO_CARBONO', 'MINERAL');

-- Remover o constraint antigo
ALTER TABLE dados_solo 
DROP CONSTRAINT IF EXISTS dados_solo_tipo_fator_solo_check;

-- Adicionar o novo constraint com os valores corretos
ALTER TABLE dados_solo 
ADD CONSTRAINT dados_solo_tipo_fator_solo_check 
    CHECK (tipo_fator_solo IN ('SOLO_LAC', 'SOLO_ARENOSO'));

-- Comentário para documentar a mudança
COMMENT ON COLUMN dados_solo.tipo_fator_solo IS 'Tipo de fator de solo: SOLO_LAC (Solo de baixa atividade argilosa - LAC) ou SOLO_ARENOSO (Solo arenoso)';