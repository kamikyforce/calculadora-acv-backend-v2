-- V33: Inserir dados de teste na tabela energia_dados para usuário ID 1
-- Isso resolverá o problema de "Nenhuma energia cadastrada" no frontend
-- CORRIGIDO: Respeitando constraint uk_energia_usuario_ano (1 registro por usuário por ano)

-- Limpar dados existentes do usuário 1 para evitar duplicatas
DELETE FROM energia_dados WHERE usuario_id = 1;

-- Verificar se a limpeza foi bem-sucedida
DO $$
DECLARE
    existing_records INTEGER;
BEGIN
    SELECT COUNT(*) INTO existing_records FROM energia_dados WHERE usuario_id = 1;
    RAISE NOTICE 'Registros existentes para usuário 1 após DELETE: %', existing_records;
END $$;

-- Inserir dados de energia elétrica (anos 2020-2024)
INSERT INTO energia_dados (
    usuario_id, 
    tipo_energia,
    fonte_energia,
    consumo_anual, 
    unidade, 
    ano_referencia,
    data_criacao,
    data_atualizacao,
    escopo,
    ativo
) VALUES 
-- Energia elétrica para anos 2020-2024
(1, 'ELETRICA', 'REDE_ELETRICA', 2163.80, 'MWh', 2024, NOW(), NOW(), 'ESCOPO2', true),
(1, 'ELETRICA', 'REDE_ELETRICA', 1391.65, 'MWh', 2023, NOW(), NOW(), 'ESCOPO2', true),
(1, 'ELETRICA', 'REDE_ELETRICA', 996.50, 'MWh', 2022, NOW(), NOW(), 'ESCOPO2', true),
(1, 'ELETRICA', 'REDE_ELETRICA', 596.35, 'MWh', 2021, NOW(), NOW(), 'ESCOPO2', true),
(1, 'ELETRICA', 'REDE_ELETRICA', 695.85, 'MWh', 2020, NOW(), NOW(), 'ESCOPO2', true);

-- Inserir dados de combustíveis (anos 2015-2019 para evitar conflito)
INSERT INTO energia_dados (
    usuario_id, 
    tipo_energia,
    fonte_energia,
    consumo_anual, 
    unidade, 
    ano_referencia,
    data_criacao,
    data_atualizacao,
    escopo,
    ativo
) VALUES 
-- Combustíveis para anos 2015-2019 (sem conflito com energia elétrica)
(1, 'COMBUSTIVEL', 'COMBUSTIVEL_FOSSIL', 4201.60, 'L', 2019, NOW(), NOW(), 'ESCOPO1', true),
(1, 'COMBUSTIVEL', 'COMBUSTIVEL_FOSSIL', 3800.50, 'L', 2018, NOW(), NOW(), 'ESCOPO1', true),
(1, 'COMBUSTIVEL', 'COMBUSTIVEL_FOSSIL', 3650.25, 'L', 2017, NOW(), NOW(), 'ESCOPO1', true),
(1, 'COMBUSTIVEL', 'COMBUSTIVEL_FOSSIL', 3420.80, 'L', 2016, NOW(), NOW(), 'ESCOPO1', true),
(1, 'COMBUSTIVEL', 'COMBUSTIVEL_FOSSIL', 3250.40, 'L', 2015, NOW(), NOW(), 'ESCOPO1', true);

-- Verificar se os dados foram inseridos corretamente
DO $$
DECLARE
    total_records INTEGER;
    eletrica_records INTEGER;
    combustivel_records INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_records FROM energia_dados WHERE usuario_id = 1;
    SELECT COUNT(*) INTO eletrica_records FROM energia_dados WHERE usuario_id = 1 AND tipo_energia = 'ELETRICA';
    SELECT COUNT(*) INTO combustivel_records FROM energia_dados WHERE usuario_id = 1 AND tipo_energia = 'COMBUSTIVEL';
    
    RAISE NOTICE 'Total de registros inseridos para usuário 1: %', total_records;
    RAISE NOTICE 'Registros de energia elétrica: %', eletrica_records;
    RAISE NOTICE 'Registros de combustível: %', combustivel_records;
    
    IF total_records = 10 THEN
        RAISE NOTICE 'SUCCESS: Dados de teste inseridos corretamente!';
    ELSE
        RAISE WARNING 'ATENÇÃO: Número de registros diferente do esperado.';
    END IF;
END $$;