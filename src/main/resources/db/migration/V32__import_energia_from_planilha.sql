-- V32: Corrigir valores incorretos dos fatores de emissão
-- Dados extraídos do data.json com valores corretos por ano

-- Corrigir fatores de emissão na tabela fatores_energia_eletrica
UPDATE fatores_energia_eletrica 
SET fator_emissao = 0.0617,
    fator_medio_anual = 0.0617,
    data_atualizacao = NOW()
WHERE ano = 2020;

UPDATE fatores_energia_eletrica 
SET fator_emissao = 0.1264,
    fator_medio_anual = 0.1264,
    data_atualizacao = NOW()
WHERE ano = 2021;

UPDATE fatores_energia_eletrica 
SET fator_emissao = 0.0426,
    fator_medio_anual = 0.0426,
    data_atualizacao = NOW()
WHERE ano = 2022;

UPDATE fatores_energia_eletrica 
SET fator_emissao = 0.0385,
    fator_medio_anual = 0.0385,
    data_atualizacao = NOW()
WHERE ano = 2023;

UPDATE fatores_energia_eletrica 
SET fator_emissao = 0.0545,
    fator_medio_anual = 0.0545,
    data_atualizacao = NOW()
WHERE ano = 2024;

-- Corrigir APENAS fator_medio_anual na tabela banco_fatores_energia
UPDATE banco_fatores_energia 
SET fator_medio_anual = 0.0617,
    data_atualizacao = NOW()
WHERE ano = 2020;

UPDATE banco_fatores_energia 
SET fator_medio_anual = 0.1264,
    data_atualizacao = NOW()
WHERE ano = 2021;

UPDATE banco_fatores_energia 
SET fator_medio_anual = 0.0426,
    data_atualizacao = NOW()
WHERE ano = 2022;

UPDATE banco_fatores_energia 
SET fator_medio_anual = 0.0385,
    data_atualizacao = NOW()
WHERE ano = 2023;

UPDATE banco_fatores_energia 
SET fator_medio_anual = 0.0545,
    data_atualizacao = NOW()
WHERE ano = 2024;

-- Remover registros com valores incorretos (se existirem)
DELETE FROM fatores_energia_eletrica 
WHERE fator_emissao IN (2020.000000, 2021.000000, 2022.000000, 2023.000000, 2024.000000);

DELETE FROM banco_fatores_energia 
WHERE fator_medio_anual IN (2020.000000, 2021.000000, 2022.000000, 2023.000000, 2024.000000);

-- Inserir dados mensais para 2024 (extraídos do data.json)
INSERT INTO fatores_energia_eletrica (ano, mes, fator_emissao, unidade, referencia, data_criacao, data_atualizacao)
VALUES 
(2024, 1, 0.0421, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 2, 0.0376, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 3, 0.0278, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 4, 0.0195, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 5, 0.0283, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 6, 0.0365, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 7, 0.0571, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 8, 0.0739, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 9, 0.0917, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 10, 0.1127, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 11, 0.0701, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW()),
(2024, 12, 0.0545, 'tCO2/MWh', 'data.json - Fatores mensais 2024', NOW(), NOW())
ON CONFLICT (ano, mes) DO UPDATE SET
    fator_emissao = EXCLUDED.fator_emissao,
    data_atualizacao = NOW();

-- Inserir dados mensais para 2023 (extraídos do data.json)
INSERT INTO fatores_energia_eletrica (ano, mes, fator_emissao, unidade, referencia, data_criacao, data_atualizacao)
VALUES 
(2023, 1, 0.0292, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 2, 0.0238, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 3, 0.0296, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 4, 0.0340, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 5, 0.0295, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 6, 0.0528, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 7, 0.0495, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 8, 0.0419, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 9, 0.0343, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 10, 0.0387, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 11, 0.0529, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW()),
(2023, 12, 0.0459, 'tCO2/MWh', 'data.json - Fatores mensais 2023', NOW(), NOW())
ON CONFLICT (ano, mes) DO UPDATE SET
    fator_emissao = EXCLUDED.fator_emissao,
    data_atualizacao = NOW();

-- Inserir dados mensais para 2020 (extraídos do data.json)
INSERT INTO fatores_energia_eletrica (ano, mes, fator_emissao, unidade, referencia, data_criacao, data_atualizacao)
VALUES 
(2020, 1, 0.0916, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 2, 0.0558, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 3, 0.0384, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 4, 0.0296, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 5, 0.0358, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 6, 0.0491, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 7, 0.0400, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 8, 0.0414, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 9, 0.0565, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 10, 0.0892, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 11, 0.1018, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW()),
(2020, 12, 0.0912, 'tCO2/MWh', 'data.json - Fatores mensais 2020', NOW(), NOW())
ON CONFLICT (ano, mes) DO UPDATE SET
    fator_emissao = EXCLUDED.fator_emissao,
    data_atualizacao = NOW();