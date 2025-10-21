-- Migration V20: Otimização das queries com CTE para evitar subqueries por linha
-- Remove fallback problemático EXTRACT(YEAR FROM data_criacao) e otimiza performance

-- Verificar último ano disponível antes da correção
SELECT 
    'Último ano disponível:' as info,
    MAX(ano) as ultimo_ano
FROM banco_fatores_energia 
WHERE ativo = true;

-- Verificar registros que serão afetados
SELECT 
    'Registros sem ano_referencia:' as info,
    COUNT(*) as total_registros
FROM energia_dados 
WHERE ano_referencia IS NULL AND ativo = true;

-- Criar índices otimizados se não existirem
CREATE INDEX IF NOT EXISTS idx_banco_fatores_energia_ativo_ano 
    ON banco_fatores_energia (ativo, ano);

CREATE INDEX IF NOT EXISTS idx_energia_dados_usuario_ativo 
    ON energia_dados (usuario_id, ativo);

-- Documentar a correção implementada no repository
-- As queries nativas foram otimizadas com CTE para melhor performance