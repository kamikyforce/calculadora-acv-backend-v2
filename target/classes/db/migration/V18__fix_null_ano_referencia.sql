-- Migration V18: Corrigir registros com ano_referencia = NULL
-- Baseado nas correções identificadas no arquivo correções.md

-- 1. Verificar quantos registros têm ano_referencia = NULL antes da correção
DO $$
DECLARE
    null_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO null_count FROM energia_dados WHERE ano_referencia IS NULL;
    RAISE NOTICE 'Registros com ano_referencia NULL encontrados: %', null_count;
END $$;

-- 2. Atualizar registros com ano_referencia = NULL para 2024 (ano atual)
UPDATE energia_dados 
SET ano_referencia = 2024,
    data_atualizacao = CURRENT_TIMESTAMP
WHERE ano_referencia IS NULL;

-- 3. Verificar se a correção foi aplicada
DO $$
DECLARE
    remaining_null INTEGER;
    updated_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO remaining_null FROM energia_dados WHERE ano_referencia IS NULL;
    SELECT COUNT(*) INTO updated_count FROM energia_dados WHERE ano_referencia = 2024;
    
    RAISE NOTICE 'Registros com ano_referencia NULL restantes: %', remaining_null;
    RAISE NOTICE 'Registros atualizados para ano 2024: %', updated_count;
END $$;

-- 4. Criar índice composto para otimizar consultas por usuário e ano
CREATE INDEX IF NOT EXISTS idx_energia_dados_usuario_ano 
ON energia_dados (usuario_id, ano_referencia);

-- 5. Comentário para próximas etapas
-- NOTA: As próximas migrations devem implementar:
-- - Constraint UNIQUE (usuario_id, ano_referencia) após limpeza de duplicatas
-- - ALTER COLUMN ano_referencia SET NOT NULL após validação completa