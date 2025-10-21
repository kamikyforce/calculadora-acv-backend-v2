-- Migration V24: Adicionar fatores médios anuais históricos (2006-2019)
-- Calculados com base nos dados mensais da tabela fatores_energia_eletrica

-- Verificar dados existentes antes da inserção
DO $$
DECLARE
    existing_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO existing_count 
    FROM banco_fatores_energia 
    WHERE ano BETWEEN 2006 AND 2019;
    
    RAISE NOTICE 'Registros existentes no banco_fatores_energia para anos 2006-2019: %', existing_count;
END $$;

-- Inserir fatores médios anuais calculados dos dados mensais
INSERT INTO banco_fatores_energia (ano, fator_medio_anual, fonte, observacoes, ativo)
SELECT 
    ano,
    ROUND(AVG(fator_emissao), 6) as fator_medio_anual,
    'SIN - Sistema Interligado Nacional' as fonte,
    'Fator médio anual calculado automaticamente com base nos dados mensais de ' || ano as observacoes,
    true as ativo
FROM fatores_energia_eletrica 
WHERE ativo = true 
AND ano BETWEEN 2006 AND 2019
AND ano NOT IN (
    SELECT ano FROM banco_fatores_energia WHERE ativo = true
)
GROUP BY ano
ORDER BY ano;

-- Verificar resultado da inserção
DO $$
DECLARE
    new_count INTEGER;
    total_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO new_count 
    FROM banco_fatores_energia 
    WHERE ano BETWEEN 2006 AND 2019;
    
    SELECT COUNT(*) INTO total_count 
    FROM banco_fatores_energia 
    WHERE ativo = true;
    
    RAISE NOTICE 'Registros no banco_fatores_energia para anos 2006-2019 após inserção: %', new_count;
    RAISE NOTICE 'Total de registros ativos no banco_fatores_energia: %', total_count;
END $$;

-- Mostrar os fatores calculados para verificação
SELECT 
    ano,
    fator_medio_anual,
    observacoes
FROM banco_fatores_energia 
WHERE ano BETWEEN 2006 AND 2019
ORDER BY ano;

-- Log da migration
DO $$
BEGIN
    RAISE NOTICE 'Migration V24 concluída: Fatores médios anuais históricos adicionados com sucesso!';
END $$;