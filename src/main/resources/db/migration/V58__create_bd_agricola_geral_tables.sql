-- V58__create_bd_agricola_geral_tables.sql
-- Criação das tabelas BD_AGRICOLA_GERAL baseadas nos dados da planilha BD_AGRICOLA_GERAL.csv
-- Contém matriz de dados para cálculo do módulo agrícola relacionados ao Escopo 1

-- Tabela principal para insumos agrícolas
CREATE TABLE bd_agricola_insumos (
    id SERIAL PRIMARY KEY,
    classe VARCHAR(50) NOT NULL,
    especificacao VARCHAR(200) NOT NULL,
    
    -- Teor de Macronutrientes
    teor_nitrogenio DECIMAL(8,2),
    teor_fosforo DECIMAL(8,2),
    teor_potassio DECIMAL(8,2),
    
    -- Fator de Conversão
    fator_conversao_valor DECIMAL(10,4),
    fator_conversao_unidade VARCHAR(100),
    
    -- Fatores de Emissão - Escopo 1
    fe_co2_biogenico DECIMAL(10,6),
    fe_co2 DECIMAL(10,6),
    fe_ch4 DECIMAL(10,6),
    fe_n2o_direto DECIMAL(10,6),
    
    -- Fatores N2O Volatilização
    frac_n2o_volatilizacao DECIMAL(8,4),
    fe_n2o_volatilizacao DECIMAL(8,4),
    
    -- Fatores N2O Lixiviação
    frac_n2o_lixiviacao DECIMAL(8,4),
    fe_n2o_lixiviacao DECIMAL(8,4),
    
    -- Outros fatores
    fe_n2o_composto DECIMAL(8,4),
    fe_co DECIMAL(8,2),
    fe_nox DECIMAL(8,2),
    
    -- Referência
    referencia VARCHAR(200),
    
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_agricola_insumos_classe ON bd_agricola_insumos (classe);
CREATE INDEX idx_bd_agricola_insumos_especificacao ON bd_agricola_insumos (especificacao);
CREATE INDEX idx_bd_agricola_insumos_classe_especificacao ON bd_agricola_insumos (classe, especificacao);

-- Tabela para fatores de manejo com queima
CREATE TABLE bd_agricola_manejo_queima (
    id SERIAL PRIMARY KEY,
    tipo_fator VARCHAR(20) NOT NULL CHECK (tipo_fator IN ('Cf', 'Mb', 'Queima')),
    cultura VARCHAR(100) NOT NULL,
    valor DECIMAL(10,4) NOT NULL,
    unidade VARCHAR(50),
    fe_co2_biogenico DECIMAL(10,2),
    fe_ch4 DECIMAL(8,2),
    fe_n2o DECIMAL(8,2),
    fe_co DECIMAL(8,2),
    fe_nox DECIMAL(8,2),
    referencia VARCHAR(200),
    
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_agricola_manejo_queima_tipo_fator ON bd_agricola_manejo_queima (tipo_fator);
CREATE INDEX idx_bd_agricola_manejo_queima_cultura ON bd_agricola_manejo_queima (cultura);
CREATE INDEX idx_bd_agricola_manejo_queima_tipo_cultura ON bd_agricola_manejo_queima (tipo_fator, cultura);

-- Tabela para decomposição dos restos culturais
CREATE TABLE bd_agricola_decomposicao (
    id SERIAL PRIMARY KEY,
    tipo_fator VARCHAR(20) NOT NULL CHECK (tipo_fator IN ('DRY', 'RAG', 'NAG', 'NBG', 'Rs', 'Restos')),
    cultura VARCHAR(100) NOT NULL,
    valor DECIMAL(10,6) NOT NULL,
    unidade VARCHAR(100),
    fe_n2o DECIMAL(8,4),
    frac_n2o_lixiviacao DECIMAL(8,4),
    fe_n2o_lixiviacao DECIMAL(8,4),
    referencia VARCHAR(200),
    
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_agricola_decomposicao_tipo_fator ON bd_agricola_decomposicao (tipo_fator);
CREATE INDEX idx_bd_agricola_decomposicao_cultura ON bd_agricola_decomposicao (cultura);
CREATE INDEX idx_bd_agricola_decomposicao_tipo_cultura ON bd_agricola_decomposicao (tipo_fator, cultura);

-- Tabela para GWP dos GEE
CREATE TABLE bd_agricola_gwp (
    id SERIAL PRIMARY KEY,
    gee VARCHAR(50) NOT NULL,
    detalhes VARCHAR(100) NOT NULL,
    pag DECIMAL(8,1) NOT NULL,
    referencia VARCHAR(200),
    
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_agricola_gwp_gee ON bd_agricola_gwp (gee);

-- Inserção dos dados de insumos agrícolas
INSERT INTO bd_agricola_insumos (
    classe, especificacao, teor_nitrogenio, teor_fosforo, teor_potassio,
    fe_n2o_direto, frac_n2o_volatilizacao, fe_n2o_volatilizacao, frac_n2o_lixiviacao, fe_n2o_lixiviacao, fe_n2o_composto,
    referencia
) VALUES
-- Fertilizantes NPK e sintéticos
('Fertilizante NPK sem especificação', 'FORMULADO SEM ESPECIFICAÇÃO', NULL, NULL, NULL, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Amônia Anidra', 82.00, 0.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Cloreto de Amônio', 25.00, 0.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Cloreto de Potássio', 0.00, 0.00, 59.00, NULL, NULL, NULL, NULL, NULL, NULL, '-'),
('Fertilizante sintético', 'Fosfato Monoamônico (MAP)', 11.00, 51.00, 0.00, 0.0007, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Fosfato Diamônico (DAP)', 18.00, 46.00, 0.00, 0.0007, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato de Amônio', 33.00, 0.00, 0.00, 0.0006, 0.05, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato de Amônio e Cálcio', 25.00, 0.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato de Cálcio', 15.00, 0.00, 0.00, 0.0004, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato Sulfato de Amônio', 26.00, 0.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato de Potássio', 13.50, 0.00, 44.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Nitrato Fosfato Amônio', 8.00, 52.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Solução de Nitrato de Amônio e Ureia', 32.00, 0.00, 0.00, 0.0113, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Sulfato de Amônio', 21.00, 0.00, 0.00, 0.0006, 0.11, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Fertilizante sintético', 'Sulfato de Potássio', 0.00, 0.00, 49.00, NULL, NULL, NULL, NULL, NULL, NULL, '-'),
('Fertilizante sintético', 'Superfosfato Simples', 0.00, 20.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, '-'),
('Fertilizante sintético', 'Superfosfato Triplo', 0.00, 46.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, '-'),

-- Ureia
('Ureia', 'Ureia', 45.00, 0.00, 0.00, 0.0088, 0.15, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Ureia', 'Ureia + DMPP (Fosfato de 3,4-dimetilpirazol)', 45.00, 0.00, 0.00, 0.0006, 0.15, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Ureia', 'Ureia + DCD (Dicianodiamida)', 45.00, 0.00, 0.00, 0.0010, 0.15, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Ureia', 'Ureia revestida de polímero e enxofre (UREP)', 45.00, 0.00, 0.00, 0.0157, 0.15, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Ureia', 'Ureia + NBPT (N-(n-butil) tiofosfórico triamida)', 45.00, 0.00, 0.00, 0.0013, 0.15, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),

-- Compostos orgânicos
('Composto orgânico', 'Esterco bovino', 1.60, 0.00, 0.00, 0.0002, 0.21, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Composto orgânico', 'Esterco avícola', 3.00, 0.00, 0.00, 0.0004, 0.21, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol'),
('Composto orgânico', 'Composto orgânico', 1.40, 0.00, 0.00, 0.0002, 0.21, 0.016, 0.24, 0.017, NULL, 'Ferramenta GHG Protocol');

-- Inserção de dados com FE CO2
INSERT INTO bd_agricola_insumos (
    classe, especificacao, fe_co2
) VALUES
('Corretivo agrícola', 'Calcário dolomítico', 0.48),
('Corretivo agrícola', 'Calcário calcítico', 0.44);

-- Inserção de dados com FE CO2 biogênico para Ureia
UPDATE bd_agricola_insumos 
SET fe_co2_biogenico = 1.63 
WHERE classe = 'Ureia';

-- Inserção dos dados de manejo com queima - Fatores Cf
INSERT INTO bd_agricola_manejo_queima (
    tipo_fator, cultura, valor, unidade, referencia
) VALUES
('Cf', 'Grãos sem especificação', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Milho', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Soja', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Sorgo', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Milheto', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Pastagem', 0.35, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Culturas sem especificação', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Cf', 'Cana-de-açúcar', 0.80, 'adimensional', 'IPCC 2019, Capítulo 2, tabela 2.6'),

-- Fatores Mb
('Mb', 'Grãos sem especificação', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Milho', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Soja', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Sorgo', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Milheto', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Pastagem Amazônia', 7.69, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Caatinga', 0.92, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Cerrado', 5.82, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Mata Atlântica', 3.25, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Pantanal', 5.82, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Pampa', 5.82, 'ton ha-1', '4ª Comunicação Nacional'),
('Mb', 'Pastagem Default', 23.70, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Culturas sem especificação', 10.00, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Mb', 'Cana-de-açúcar', 6.50, 'ton ha-1', 'IPCC 2019, Capítulo 2, tabela 2.6');

-- Fatores de queima
INSERT INTO bd_agricola_manejo_queima (
    tipo_fator, cultura, valor, unidade, fe_co2_biogenico, fe_ch4, fe_n2o, fe_co, fe_nox, referencia
) VALUES
('Queima', 'Resíduos agrícolas', 0.00, 'g/kg', 1515.00, 2.70, 0.07, 92.00, 2.50, 'IPCC 2019, Capítulo 2, tabela 2.6'),
('Queima', 'Pastagem', 0.00, 'g/kg', 1613.00, 2.30, 0.21, 65.00, 3.90, 'IPCC 2019, Capítulo 2, tabela 2.6');

-- Inserção dos dados de decomposição - Fatores DRY
INSERT INTO bd_agricola_decomposicao (
    tipo_fator, cultura, valor, unidade, referencia
) VALUES
('DRY', 'Grãos sem especificação', 0.88, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Milho', 0.87, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Soja', 0.91, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Sorgo', 0.89, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Milheto', 0.90, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Pastagem', 0.90, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),
('DRY', 'Culturas sem especificação', 0.85, 'kg d.m. (kg fresh weight)-1', 'IPCC 2019'),

-- Fatores RAG
('RAG', 'Grãos sem especificação', 1.30, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Milho', 1.00, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Soja', 2.10, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Sorgo', 1.40, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Milheto', 1.40, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Pastagem', 0.30, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('RAG', 'Culturas sem especificação', 1.00, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),

-- Fatores NAG
('NAG', 'Grãos sem especificação', 0.006, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NAG', 'Milho', 0.006, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NAG', 'Soja', 0.008, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NAG', 'Sorgo', 0.007, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NAG', 'Milheto', 0.007, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NAG', 'Culturas sem especificação', 0.008, 'kg N (kg d.m.)-1', 'IPCC 2019'),

-- Fatores NBG
('NBG', 'Grãos sem especificação', 0.009, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NBG', 'Milho', 0.007, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NBG', 'Soja', 0.008, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NBG', 'Sorgo', 0.006, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NBG', 'Milheto', 0.009, 'kg N (kg d.m.)-1', 'IPCC 2019'),
('NBG', 'Culturas sem especificação', 0.009, 'kg N (kg d.m.)-1', 'IPCC 2019'),

-- Fatores Rs
('Rs', 'Grãos sem especificação', 0.22, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('Rs', 'Milho', 0.22, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('Rs', 'Soja', 0.19, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('Rs', 'Sorgo', 0.22, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('Rs', 'Milheto', 0.22, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019'),
('Rs', 'Culturas sem especificação', 0.22, 'kg d.m. ha-1 (kg d.m. ha-1)-1', 'IPCC 2019');

-- Fator para restos culturais
INSERT INTO bd_agricola_decomposicao (
    tipo_fator, cultura, valor, unidade, fe_n2o, frac_n2o_lixiviacao, fe_n2o_lixiviacao, referencia
) VALUES
('Restos', 'Restos culturais', 0.00, '-', 0.02, 0.24, 0.017, 'IPCC 2019');

-- Inserção dos dados de GWP
INSERT INTO bd_agricola_gwp (
    gee, detalhes, pag, referencia
) VALUES
('Dióxido de carbono (CO2)', 'GWP-100', 1.0, 'IPCC, 2021'),
('Óxido nitroso (N2O)', 'GWP-100', 273.0, 'IPCC, 2021'),
('Metano (CH4)', 'GWP-100', 28.5, 'IPCC, 2021');

-- Comentários sobre as tabelas
COMMENT ON TABLE bd_agricola_insumos IS 'Insumos agrícolas com fatores de emissão para cálculo do módulo agrícola - Escopo 1';
COMMENT ON TABLE bd_agricola_manejo_queima IS 'Fatores para manejo com queima de resíduos agrícolas e pastagens';
COMMENT ON TABLE bd_agricola_decomposicao IS 'Fatores para decomposição dos restos culturais';
COMMENT ON TABLE bd_agricola_gwp IS 'Potencial de Aquecimento Global (GWP) dos gases de efeito estufa';