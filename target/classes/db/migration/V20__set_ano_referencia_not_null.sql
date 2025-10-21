-- Migration V20: Tornar coluna ano_referencia NOT NULL
-- Aplicar após correção de valores NULL e limpeza de duplicatas

-- 1. Verificar se ainda existem valores NULL
DO $$
DECLARE
    null_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO null_count FROM energia_dados WHERE ano_referencia IS NULL;
    
    IF null_count > 0 THEN
        RAISE EXCEPTION 'Ainda existem % registros com ano_referencia NULL. Execute as migrations V18 e V19 primeiro.', null_count;
    END IF;
    
    RAISE NOTICE 'Verificação OK: Nenhum registro com ano_referencia NULL encontrado.';
END $$;

-- 2. Alterar coluna para NOT NULL
ALTER TABLE energia_dados 
ALTER COLUMN ano_referencia SET NOT NULL;

-- 3. Adicionar comentário na coluna
COMMENT ON COLUMN energia_dados.ano_referencia IS 'Ano de referência dos dados de energia (obrigatório, entre 1990-2030)';

-- 4. Verificar resultado final
DO $$
DECLARE
    column_nullable BOOLEAN;
BEGIN
    SELECT is_nullable = 'YES' INTO column_nullable
    FROM information_schema.columns 
    WHERE table_name = 'energia_dados' 
    AND column_name = 'ano_referencia';
    
    IF column_nullable THEN
        RAISE WARNING 'ATENÇÃO: Coluna ano_referencia ainda permite NULL';
    ELSE
        RAISE NOTICE 'SUCCESS: Coluna ano_referencia agora é NOT NULL';
    END IF;
END $$;

-- 5. Criar índice adicional para consultas por ano
CREATE INDEX IF NOT EXISTS idx_energia_dados_ano_referencia 
ON energia_dados (ano_referencia);

-- 6. Log final da migration
DO $$
BEGIN
    RAISE NOTICE 'Migration V20 concluída: ano_referencia agora é NOT NULL com índices otimizados';
END $$;