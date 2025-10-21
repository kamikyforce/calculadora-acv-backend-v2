-- Migration V19: Limpar duplicatas e adicionar constraint UNIQUE
-- Baseado nas correções identificadas no arquivo correções.md

-- 1. Criar backup dos dados antes das alterações (opcional para auditoria)
-- CREATE TABLE energia_dados_backup AS SELECT * FROM energia_dados;

-- 2. Identificar e reportar duplicatas existentes
DO $$
DECLARE
    duplicate_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO duplicate_count
    FROM (
        SELECT usuario_id, ano_referencia, COUNT(*) as cnt
        FROM energia_dados
        WHERE ano_referencia IS NOT NULL
        GROUP BY usuario_id, ano_referencia
        HAVING COUNT(*) > 1
    ) duplicates;
    
    RAISE NOTICE 'Grupos de registros duplicados encontrados: %', duplicate_count;
END $$;

-- 3. Remover duplicatas mantendo o registro mais recente (maior ID)
-- Usar CTE para identificar registros a serem removidos
WITH duplicatas AS (
    SELECT id, 
           ROW_NUMBER() OVER (
               PARTITION BY usuario_id, ano_referencia 
               ORDER BY id DESC
           ) as rn
    FROM energia_dados
    WHERE ano_referencia IS NOT NULL
)
DELETE FROM energia_dados 
WHERE id IN (
    SELECT id FROM duplicatas WHERE rn > 1
);

-- 4. Verificar se ainda existem duplicatas
DO $$
DECLARE
    remaining_duplicates INTEGER;
BEGIN
    SELECT COUNT(*) INTO remaining_duplicates
    FROM (
        SELECT usuario_id, ano_referencia, COUNT(*) as cnt
        FROM energia_dados
        WHERE ano_referencia IS NOT NULL
        GROUP BY usuario_id, ano_referencia
        HAVING COUNT(*) > 1
    ) duplicates;
    
    RAISE NOTICE 'Duplicatas restantes após limpeza: %', remaining_duplicates;
    
    IF remaining_duplicates > 0 THEN
        RAISE EXCEPTION 'Ainda existem duplicatas. Não é seguro adicionar constraint UNIQUE.';
    END IF;
END $$;

-- 5. Adicionar constraint UNIQUE para prevenir futuras duplicatas
ALTER TABLE energia_dados 
ADD CONSTRAINT uk_energia_usuario_ano 
UNIQUE (usuario_id, ano_referencia);

-- 6. Verificar resultado final
DO $$
DECLARE
    total_records INTEGER;
    unique_combinations INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_records FROM energia_dados WHERE ano_referencia IS NOT NULL;
    
    SELECT COUNT(*) INTO unique_combinations
    FROM (
        SELECT DISTINCT usuario_id, ano_referencia
        FROM energia_dados
        WHERE ano_referencia IS NOT NULL
    ) unique_pairs;
    
    RAISE NOTICE 'Total de registros com ano_referencia: %', total_records;
    RAISE NOTICE 'Combinações únicas (usuario_id, ano_referencia): %', unique_combinations;
    
    IF total_records = unique_combinations THEN
        RAISE NOTICE 'SUCCESS: Constraint UNIQUE aplicada com sucesso!';
    ELSE
        RAISE WARNING 'ATENÇÃO: Diferença entre total e únicos detectada.';
    END IF;
END $$;