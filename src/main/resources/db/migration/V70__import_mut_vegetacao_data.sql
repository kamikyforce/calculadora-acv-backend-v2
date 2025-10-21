-- Flyway Migration: Importação dos dados MUT - Base | Vegetação
-- Baseado no arquivo 3.sql - dados de vegetação por bioma
-- Generated at 2025-01-27

-- Inserir fator_mut principal para dados de vegetação
INSERT INTO fator_mut (nome, descricao, tipo_mudanca, escopo, ativo, data_criacao)
SELECT 'Fatores de Vegetação por Bioma', 'Fatores de remoção e estoque de carbono por categoria fitofisionomica e bioma', 'VEGETACAO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Fatores de Vegetação por Bioma' 
    AND tipo_mudanca = 'VEGETACAO' 
    AND escopo = 'ESCOPO3'
);

-- Inserir dados de vegetação para AMAZONIA
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'AMAZONIA',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'AMAZONIA'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Inserir dados de vegetação para CERRADO
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'CERRADO',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'CERRADO'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Inserir dados de vegetação para CAATINGA
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'CAATINGA',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'CAATINGA'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Inserir dados de vegetação para MATA_ATLANTICA
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'MATA_ATLANTICA',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'MATA_ATLANTICA'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Inserir dados de vegetação para PAMPA
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'PAMPA',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'PAMPA'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Inserir dados de vegetação para PANTANAL
INSERT INTO dados_vegetacao (fator_mut_id, bioma, parametro, fator_ch4, fator_co2, fator_n2o, valor_amazonia, valor_caatinga, valor_cerrado, valor_mata_atlantica, valor_pampa, valor_pantanal)
SELECT 
    fm.id,
    'PANTANAL',
    'REMOÇÃO de floresta primária (tCO2e/ha/ano)',
    NULL,
    NULL,
    NULL,
    1.76,
    0.37,
    0.73,
    1.17,
    1.17,
    0.73
FROM fator_mut fm 
WHERE fm.nome = 'Fatores de Vegetação por Bioma'
AND NOT EXISTS (
    SELECT 1 FROM dados_vegetacao dv 
    WHERE dv.fator_mut_id = fm.id 
    AND dv.bioma = 'PANTANAL'
    AND dv.parametro = 'REMOÇÃO de floresta primária (tCO2e/ha/ano)'
);

-- Log da migration
DO $$
DECLARE
    total_records INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_records 
    FROM dados_vegetacao dv
    JOIN fator_mut fm ON dv.fator_mut_id = fm.id
    WHERE fm.nome = 'Fatores de Vegetação por Bioma';
    
    RAISE NOTICE 'Migration V69 concluída: % registros de vegetação importados com sucesso!', total_records;
END $$;