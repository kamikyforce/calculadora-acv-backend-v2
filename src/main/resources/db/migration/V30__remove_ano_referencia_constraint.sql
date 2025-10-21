-- Migration V30: Remover constraint ano_referencia para permitir qualquer ano
-- Conforme regra de negócio: usuário pode inserir qualquer ano manualmente

ALTER TABLE energia_dados DROP CONSTRAINT IF EXISTS chk_ano_referencia_valido;

-- Verificar se a alteração foi aplicada corretamente
DO $$
BEGIN
    RAISE NOTICE 'Constraint chk_ano_referencia_valido removida - qualquer ano permitido';
END $$;