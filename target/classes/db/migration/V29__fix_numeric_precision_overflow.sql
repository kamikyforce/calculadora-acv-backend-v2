-- Migration V29: Corrigir overflow numérico - Alterar precisão de DECIMAL(10,6) para DECIMAL(20,6)
-- Soluciona erro: "numeric field overflow - A field with precision 10, scale 6 must round to an absolute value less than 10^4"

-- Verificar dados existentes antes da alteração
DO $$
DECLARE
    max_fator_emissao DECIMAL;
    max_fator_medio DECIMAL;
    max_consumo DECIMAL;
BEGIN
    -- Verificar valores máximos atuais
    SELECT MAX(fator_emissao) INTO max_fator_emissao FROM energia_dados WHERE fator_emissao IS NOT NULL;
    SELECT MAX(fator_medio_anual) INTO max_fator_medio FROM energia_dados WHERE fator_medio_anual IS NOT NULL;
    SELECT MAX(consumo_anual) INTO max_consumo FROM energia_dados WHERE consumo_anual IS NOT NULL;
    
    RAISE NOTICE 'Valores máximos antes da alteração:';
    RAISE NOTICE '  - fator_emissao: %', COALESCE(max_fator_emissao, 0);
    RAISE NOTICE '  - fator_medio_anual: %', COALESCE(max_fator_medio, 0);
    RAISE NOTICE '  - consumo_anual: %', COALESCE(max_consumo, 0);
    
    RAISE NOTICE 'Iniciando alteração das colunas...';
END $$;

-- 1. ALTERAR TABELA energia_dados
-- Alterar fator_emissao de DECIMAL(10,6) para DECIMAL(20,6)
ALTER TABLE energia_dados 
ALTER COLUMN fator_emissao TYPE DECIMAL(20,6);

-- Alterar fator_medio_anual de DECIMAL(10,6) para DECIMAL(20,6)
ALTER TABLE energia_dados 
ALTER COLUMN fator_medio_anual TYPE DECIMAL(20,6);

-- 2. ALTERAR TABELA fatores_energia_eletrica
-- Alterar fator_emissao de DECIMAL(10,6) para DECIMAL(20,6)
ALTER TABLE fatores_energia_eletrica 
ALTER COLUMN fator_emissao TYPE DECIMAL(20,6);

-- Alterar fator_medio_anual de DECIMAL(10,6) para DECIMAL(20,6)
ALTER TABLE fatores_energia_eletrica 
ALTER COLUMN fator_medio_anual TYPE DECIMAL(20,6);

-- 3. ALTERAR TABELA banco_fatores_energia
-- Alterar fator_medio_anual de DECIMAL(10,6) para DECIMAL(20,6)
ALTER TABLE banco_fatores_energia 
ALTER COLUMN fator_medio_anual TYPE DECIMAL(20,6);

-- Verificar resultado final
DO $$
DECLARE
    energia_fator_emissao_type TEXT;
    energia_fator_medio_type TEXT;
    fatores_fator_emissao_type TEXT;
    fatores_fator_medio_type TEXT;
    banco_fator_medio_type TEXT;
BEGIN
    -- Verificar tipos das colunas após alteração
    SELECT data_type || '(' || numeric_precision || ',' || numeric_scale || ')' 
    INTO energia_fator_emissao_type
    FROM information_schema.columns 
    WHERE table_name = 'energia_dados' AND column_name = 'fator_emissao';
    
    SELECT data_type || '(' || numeric_precision || ',' || numeric_scale || ')' 
    INTO energia_fator_medio_type
    FROM information_schema.columns 
    WHERE table_name = 'energia_dados' AND column_name = 'fator_medio_anual';
    
    SELECT data_type || '(' || numeric_precision || ',' || numeric_scale || ')' 
    INTO fatores_fator_emissao_type
    FROM information_schema.columns 
    WHERE table_name = 'fatores_energia_eletrica' AND column_name = 'fator_emissao';
    
    SELECT data_type || '(' || numeric_precision || ',' || numeric_scale || ')' 
    INTO fatores_fator_medio_type
    FROM information_schema.columns 
    WHERE table_name = 'fatores_energia_eletrica' AND column_name = 'fator_medio_anual';
    
    SELECT data_type || '(' || numeric_precision || ',' || numeric_scale || ')' 
    INTO banco_fator_medio_type
    FROM information_schema.columns 
    WHERE table_name = 'banco_fatores_energia' AND column_name = 'fator_medio_anual';
    
    RAISE NOTICE 'Tipos das colunas após alteração:';
    RAISE NOTICE '  - energia_dados.fator_emissao: %', energia_fator_emissao_type;
    RAISE NOTICE '  - energia_dados.fator_medio_anual: %', energia_fator_medio_type;
    RAISE NOTICE '  - fatores_energia_eletrica.fator_emissao: %', fatores_fator_emissao_type;
    RAISE NOTICE '  - fatores_energia_eletrica.fator_medio_anual: %', fatores_fator_medio_type;
    RAISE NOTICE '  - banco_fatores_energia.fator_medio_anual: %', banco_fator_medio_type;
    
    RAISE NOTICE '✅ Migration V29 concluída com sucesso!';
    RAISE NOTICE '📊 Todas as colunas DECIMAL(10,6) foram alteradas para DECIMAL(20,6)';
    RAISE NOTICE '🎯 Problema de overflow numérico resolvido!';
    RAISE NOTICE '💡 Agora suporta valores até 99.999.999.999.999,999999';
END $$;

-- Atualizar comentários das colunas
COMMENT ON COLUMN energia_dados.fator_emissao IS 'Fator de emissão DECIMAL(20,6) - suporta valores até 10^14 com 6 casas decimais';
COMMENT ON COLUMN energia_dados.fator_medio_anual IS 'Fator médio anual DECIMAL(20,6) - suporta valores até 10^14 com 6 casas decimais';
COMMENT ON COLUMN fatores_energia_eletrica.fator_emissao IS 'Fator de emissão DECIMAL(20,6) - suporta valores até 10^14 com 6 casas decimais';
COMMENT ON COLUMN fatores_energia_eletrica.fator_medio_anual IS 'Fator médio anual DECIMAL(20,6) - suporta valores até 10^14 com 6 casas decimais';
COMMENT ON COLUMN banco_fatores_energia.fator_medio_anual IS 'Fator médio anual DECIMAL(20,6) - suporta valores até 10^14 com 6 casas decimais';