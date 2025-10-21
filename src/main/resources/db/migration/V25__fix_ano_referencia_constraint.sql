-- Migration V22: Expandir constraint ano_referencia para aceitar 1990-2030
ALTER TABLE energia_dados DROP CONSTRAINT IF EXISTS chk_ano_referencia_valido;

ALTER TABLE energia_dados ADD CONSTRAINT chk_ano_referencia_valido 
    CHECK (ano_referencia IS NULL OR (ano_referencia >= 1990 AND ano_referencia <= 2030));

-- Verificar se a alteração foi aplicada corretamente
DO $$
BEGIN
    RAISE NOTICE 'Constraint chk_ano_referencia_valido atualizada para aceitar anos 1990-2030';
END $$;