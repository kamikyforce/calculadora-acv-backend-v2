-- V60__create_bd_fator_emissao_dieta_tables.sql
-- Criação das tabelas BD_FATOR_EMISSAO_DIETA baseadas nos dados da planilha BD_FATOR_EMISSAO_DIETA.csv
-- Dividido em 3 tabelas: composição das rações DSM, NDT dos ingredientes e composição bromatológica dos ingredientes

-- Tabela 1: Composição das rações fornecidas pela DSM (Kg/1000Kg MN)
CREATE TABLE bd_fator_emissao_dieta_racoes_dsm (
    id SERIAL PRIMARY KEY,
    inserir_dados_dieta VARCHAR(10) NOT NULL,
    sistema_producao VARCHAR(20) NOT NULL,
    proteina_bruta_percentual DECIMAL(5,2) NULL,
    ureia VARCHAR(10) NULL,
    subproduto VARCHAR(10) NULL,
    codigo_racao VARCHAR(20) NOT NULL,
    proteina_bruta_racao DECIMAL(5,2) NOT NULL,
    ureia_racao VARCHAR(10) NOT NULL,
    subproduto_racao VARCHAR(10) NOT NULL,
    milho DECIMAL(6,3) NOT NULL,
    farelo_soja DECIMAL(6,3) NOT NULL,
    casquinha_soja DECIMAL(6,3) NOT NULL,
    ddgs DECIMAL(6,3) NOT NULL,
    farelo_algodao_38 DECIMAL(6,3) NOT NULL,
    ureia_pecuaria DECIMAL(6,3) NOT NULL,
    minerais DECIMAL(6,3) NOT NULL,
    total_proporcao DECIMAL(6,3) NOT NULL,
    ndt DECIMAL(6,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_fator_emissao_dieta_racoes_dsm_sistema_producao ON bd_fator_emissao_dieta_racoes_dsm (sistema_producao);
CREATE INDEX idx_bd_fator_emissao_dieta_racoes_dsm_codigo_racao ON bd_fator_emissao_dieta_racoes_dsm (codigo_racao);
CREATE INDEX idx_bd_fator_emissao_dieta_racoes_dsm_inserir_dados_dieta ON bd_fator_emissao_dieta_racoes_dsm (inserir_dados_dieta);

-- Tabela 2: NDT dos ingredientes
CREATE TABLE bd_fator_emissao_dieta_ndt_ingredientes (
    id SERIAL PRIMARY KEY,
    tipo_ingrediente VARCHAR(10) NOT NULL,
    nome_ingrediente VARCHAR(100) NOT NULL,
    ndt_percentual DECIMAL(6,2) NULL,
    energia_bruta DECIMAL(6,2) NULL,
    materia_seca DECIMAL(8,2) NULL,
    proteina_bruta_ms DECIMAL(8,2) NULL,
    fatores_emissoes_calculados VARCHAR(10) NULL,
    observacoes TEXT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_fator_emissao_dieta_ndt_ingredientes_tipo_ingrediente ON bd_fator_emissao_dieta_ndt_ingredientes (tipo_ingrediente);
CREATE INDEX idx_bd_fator_emissao_dieta_ndt_ingredientes_nome_ingrediente ON bd_fator_emissao_dieta_ndt_ingredientes (nome_ingrediente);
CREATE INDEX idx_bd_fator_emissao_dieta_ndt_ingredientes_fatores_emissoes_calculados ON bd_fator_emissao_dieta_ndt_ingredientes (fatores_emissoes_calculados);

-- Tabela 3: Composição bromatológica dos ingredientes (dados da parte inferior da planilha)
CREATE TABLE bd_fator_emissao_dieta_composicao_ingredientes (
    id SERIAL PRIMARY KEY,
    nome_ingrediente VARCHAR(100) NOT NULL,
    materia_seca_percentual DECIMAL(5,2) NOT NULL,
    proteina_bruta_percentual DECIMAL(5,2) NOT NULL,
    fibra_detergente_neutro DECIMAL(5,2) NOT NULL,
    extrato_etereo DECIMAL(5,2) NOT NULL,
    cinzas DECIMAL(5,2) NOT NULL,
    carboidratos_nao_fibrosos DECIMAL(5,2) NOT NULL,
    ndt_calculado DECIMAL(5,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_fator_emissao_dieta_composicao_ingredientes_nome_ingrediente ON bd_fator_emissao_dieta_composicao_ingredientes (nome_ingrediente);

-- Inserção dos dados das rações DSM
INSERT INTO bd_fator_emissao_dieta_racoes_dsm (
    inserir_dados_dieta, sistema_producao, proteina_bruta_percentual, ureia, subproduto, codigo_racao,
    proteina_bruta_racao, ureia_racao, subproduto_racao, milho, farelo_soja, casquinha_soja, ddgs, farelo_algodao_38, ureia_pecuaria, minerais, total_proporcao, ndt
) VALUES
-- Dados de entrada do usuário
('Sim', 'Extensivo', 15.00, 'Sem', 'Sem', 'Corte 1', 15.00, 'Sem', 'Sem', 0.508, 0.242, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.53),
('Não', 'Semi Intensivo', 17.00, 'Com', 'Com', 'Corte 2', 15.00, 'Com', 'Sem', 0.562, 0.178, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.99),
('Não', 'Intensivo', 19.00, NULL, NULL, 'Corte 3', 15.00, 'Sem', 'Com', 0.445, 0.155, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.87),
('Não', 'Intensivo', 18.00, NULL, NULL, 'Corte 4', 15.00, 'Com', 'Com', 0.498, 0.092, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 65.33),
('Não', 'Intensivo', 20.00, NULL, NULL, 'Corte 5', 17.00, 'Sem', 'Sem', 0.455, 0.295, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.09),
('Não', 'Intensivo', 22.00, NULL, NULL, 'Corte 6', 17.00, 'Com', 'Sem', 0.510, 0.230, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.56),
('Não', 'Intensivo', 24.00, NULL, NULL, 'Corte 7', 17.00, 'Sem', 'Com', 0.392, 0.208, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.43),
('Não', 'Intensivo', 27.00, NULL, NULL, 'Corte 8', 17.00, 'Com', 'Com', 0.446, 0.144, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 64.89),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 9', 19.00, 'Sem', 'Sem', 0.403, 0.347, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.65),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 10', 19.00, 'Com', 'Sem', 0.457, 0.283, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.12),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 11', 19.00, 'Sem', 'Com', 0.340, 0.260, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.00),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 12', 19.00, 'Com', 'Com', 0.394, 0.196, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 64.46),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 13', 30.00, 'com', 'sem', 0.350, 0.340, 0.000, 0.000, 0.000, 0.060, 0.250, 1.000, 63.31),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 14', 30.00, 'com', 'com', 0.140, 0.000, 0.150, 0.250, 0.150, 0.060, 0.250, 1.000, 50.11),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 15', 35.00, 'com', 'sem', 0.430, 0.220, 0.000, 0.000, 0.000, 0.100, 0.250, 1.000, 64.04),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 16', 35.00, 'com', 'com', 0.280, 0.000, 0.100, 0.050, 0.220, 0.100, 0.250, 1.000, 44.83),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 17', 40.00, 'com', 'sem', 0.385, 0.245, 0.000, 0.000, 0.000, 0.120, 0.250, 1.000, 63.69),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 18', 40.00, 'com', 'com', 0.190, 0.000, 0.150, 0.100, 0.190, 0.120, 0.250, 1.000, 46.53),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 19', 45.00, 'com', 'sem', 0.325, 0.290, 0.000, 0.000, 0.000, 0.135, 0.250, 1.000, 63.22),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 20', 45.00, 'com', 'com', 0.165, 0.000, 0.100, 0.150, 0.200, 0.135, 0.250, 1.000, 46.11),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 21', 20.00, 'com', 'sem', 0.600, 0.220, 0.000, 0.000, 0.000, 0.030, 0.150, 1.000, 73.38),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 22', 20.00, 'com', 'com', 0.410, 0.000, 0.150, 0.060, 0.200, 0.030, 0.150, 1.000, 55.23),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 23', 25.00, 'com', 'sem', 0.570, 0.230, 0.000, 0.000, 0.000, 0.050, 0.150, 1.000, 73.16),
('Não', 'Intensivo', NULL, NULL, NULL, 'Corte 24', 25.00, 'com', 'com', 0.375, 0.000, 0.150, 0.075, 0.200, 0.050, 0.150, 1.000, 55.05),
-- Rações de Leite
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 1', 15.00, 'Sem', 'Sem', 0.500, 0.250, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.46),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 2', 15.00, 'Com', 'Sem', 0.560, 0.180, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.97),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 3', 15.00, 'Sem', 'Com', 0.410, 0.240, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 63.15),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 4', 15.00, 'Com', 'Com', 0.470, 0.170, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 63.67),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 5', 18.00, 'Sem', 'Sem', 0.420, 0.330, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.80),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 6', 18.00, 'Com', 'Sem', 0.480, 0.260, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.31),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 7', 18.00, 'Sem', 'Com', 0.340, 0.310, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 62.57),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 8', 18.00, 'Com', 'Com', 0.480, 0.260, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.31),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 9', 20.00, 'Sem', 'Sem', 0.370, 0.380, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.38),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 10', 20.00, 'Com', 'Sem', 0.430, 0.310, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 63.89),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 11', 20.00, 'Sem', 'Com', 0.280, 0.370, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 62.07),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 12', 20.00, 'Com', 'Com', 0.330, 0.310, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 62.50),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 13', 22.00, 'Sem', 'Sem', 0.320, 0.430, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 62.97),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 14', 22.00, 'Com', 'Sem', 0.370, 0.370, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 63.40),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 15', 22.00, 'Sem', 'Com', 0.230, 0.420, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 61.66),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 16', 22.00, 'Com', 'Com', 0.290, 0.350, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 62.17),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 17', 24.00, 'Sem', 'Sem', 0.270, 0.480, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 62.55),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 18', 24.00, 'Com', 'Sem', 0.320, 0.420, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 62.98),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 19', 24.00, 'Sem', 'Com', 0.180, 0.470, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 61.24),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 20', 24.00, 'Com', 'Com', 0.230, 0.410, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 61.67),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 21', 27.00, 'Sem', 'Sem', 0.190, 0.560, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 61.89),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 22', 27.00, 'Com', 'Sem', 0.240, 0.500, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 62.32),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 23', 27.00, 'Sem', 'Com', 0.100, 0.550, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 60.58),
('Não', 'Intensivo', NULL, NULL, NULL, 'Leite 24', 27.00, 'Com', 'Com', 0.150, 0.490, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 61.01);

-- Inserção dos dados NDT dos ingredientes
INSERT INTO bd_fator_emissao_dieta_ndt_ingredientes (
    tipo_ingrediente, nome_ingrediente, ndt_percentual, energia_bruta, materia_seca, proteina_bruta_ms, fatores_emissoes_calculados, observacoes
) VALUES
-- Volumosos (V)
('V', 'VOLUMOSO', NULL, NULL, NULL, NULL, NULL, NULL),
('V', 'Algodão', 56.02, 19.75, 908.8, 8.9, 'Não', 'Casca ou capulho. Modelos prontos para caroço e farelo de algodão'),
('V', 'Aveia forrageira', 68.61, 17.15, 177.0, 11.6, 'Sim', 'Palha de aveia adaptado de outras regiões {FI}'),
('V', 'Azevem pré-secado', 66.85, 18.10, 622.0, 6.7, 'Não', 'ryegrass silage {GLO}'),
('V', 'Cana', 53.09, 18.00, 366.4, 3.0, 'Sim', 'Disponível para Brasil'),
('V', 'Capim', 56.74, 18.20, 292.2, 8.6, 'Sim', 'Datasets CA, CH e ZA disponíveis. Verificar se é necessário regionalização'),
('V', 'Feno alfafa', 55.24, 17.66, 893.2, 18.8, 'Sim', 'Hay {BR from CA-QC}| hay production | Cut-off, U'),
('V', 'Feno aveia', 56.65, 16.95, 874.2, 12.0, 'Sim', 'Palha de aveia adaptado de outras regiões {FI}'),
('V', 'Feno azevem', 73.72, 18.10, 929.8, 14.7, 'Não', 'ryegrass silage {GLO}'),
('V', 'Feno braquiária', 54.26, 18.12, 891.4, 7.0, 'Sim', 'Hay {BR from CA-QC}| hay production | Cut-off, U'),
('V', 'Feno cynodon', 55.57, 18.30, 887.9, 8.9, 'Sim', 'Hay {BR from CA-QC}| hay production | Cut-off, U'),
('V', 'Milho (palha)', 53.30, 18.28, 904.4, 2.6, 'Não', 'Apenas grão e silagem'),
('V', 'Milho (planta)', 64.61, 17.87, 331.4, 7.1, 'Não', 'Apenas grão e silagem'),
('V', 'Palha de aveia', 57.52, 18.40, 888.1, 4.6, 'Sim', 'Palha de aveia adaptado de outras regiões {FI}'),
('V', 'Palma forrageira', 63.43, 13.18, 117.8, 4.3, 'Não', 'Não disponível. LUC estimado calculado com BRLUC'),
('V', 'Silagem cana', 58.75, 18.16, 256.8, 3.5, 'Não', 'Não disponível. LUC estimado calculado com BRLUC'),
('V', 'Silagem milheto', 55.96, 17.15, 264.9, 8.1, 'Não', 'Não disponível. Apenas inventário do grão existe no ecoinvent'),
('V', 'Silagem milho', 75.67, 19.10, 452.3, 8.2, 'Sim', 'Disponível para Brasil'),
('V', 'Silagem sorgo', 65.12, 18.10, 310.3, 6.2, 'Sim', 'Produtos são caule e grão'),

-- Concentrados Energéticos (CE)
('CE', 'CONCENTRADO ENERGÉTICO', NULL, NULL, NULL, NULL, NULL, NULL),
('CE', 'Casquinha de soja', 74.81, 17.11, 901.1, 12.6, 'Não', 'Apenas farelo e grão disponíveis'),
('CE', 'Farelo de arroz desfitinizado', 33.27, 17.00, 908.0, 18.0, 'Proxy', 'Dataset informa produção do grão sem processamento para farelo'),
('CE', 'Farelo de arroz integral', 89.80, 19.25, 889.5, 13.4, 'Proxy', 'Dataset informa produção do grão sem processamento para farelo'),
('CE', 'Gérmen de milho desengordurado', 77.77, 15.86, 897.6, 11.2, 'Proxy', 'Apenas grão e silagem'),
('CE', 'Gérmen de milho farelo', 101.08, 22.05, 899.1, 11.0, 'Proxy', 'Apenas grão e silagem'),
('CE', 'Melaço de cana', 74.78, 15.21, 855.6, 3.0, 'Não', 'Apenas cana in natura. Maleão disponível na AF6'),
('CE', 'Milho', 88.72, 18.70, 840.8, 9.1, 'Sim', 'Disponível para Brasil'),
('CE', 'Milho (quebradinho)', 12.48, 17.66, 884.9, 6.4, 'Proxy', 'Apenas grão e silagem'),
('CE', 'Polpa cítrica', 78.52, 18.30, 895.9, 7.2, 'Não', 'Dataset para produção de laranja disponível, falta o processamento para polpa (alocação e energia), disponível da Agribalyse'),
('CE', 'Sorgo', 87.87, 18.80, 652.7, 9.1, 'Sim', 'Disponível, adaptado de outra regiões'),
('CE', 'Trigo', 81.76, 17.38, 881.1, 15.7, 'Sim', 'Disponível, adaptado de outra regiões'),

-- Concentrados Proteicos (CP)
('CP', 'CONCENTRADO PROTEICO', NULL, NULL, NULL, NULL, NULL, NULL),
('CP', 'Caroço de algodão', 96.81, 23.47, 906.6, 22.9, 'Sim', 'Disponível, adaptado de outra regiões. Ou dados brasileiros no SICV (necessário calcular alocação)'),
('CP', 'DDGS', 86.19, 10.04, 614.9, 30.7, 'Não', 'Apenas grão e silagem. Resíduo de destilaria de etanol, verificar abordagem'),
('CP', 'Farelo de milho, glúten 21 (Promil/Refinazil)', 78.10, 15.94, 888.3, 24.0, 'Proxy', 'Processamento para milho picado e farinha adaptados de outra regiões'),
('CP', 'Farelo de milho, glúten 60 (Glutenose/Protenose)', 84.57, 21.84, 905.5, 64.0, 'Proxy', 'Processamento para milho picado e farinha adaptados de outra regiões'),
('CP', 'Farelo de milho, glúten úmido (GoldenMill)', 70.10, 18.50, 373.0, 22.4, 'Proxy', 'Processamento para milho picado e farinha adaptados de outra regiões'),
('CP', 'Farelo de soja', 80.41, 18.79, 886.5, 48.8, 'Sim', 'Disponível para Brasil'),
('CP', 'Soja (grão)', 97.03, 22.09, 909.2, 38.5, 'Sim', 'Disponível para Brasil'),
('CP', 'Torta de algodão', 84.54, 20.84, 904.8, 29.7, 'Não', 'Modelos prontos para caroço e farelo de algodão'),

-- Aditivos e Minerais (AM)
('AM', 'ADITIVOS E MINERAIS', NULL, NULL, NULL, NULL, NULL, NULL),
('AM', 'Sal mineral', NULL, NULL, NULL, NULL, 'Proxy', 'Disponível, porém rota de produção é diferente. Por isso, Embrapa não considerou em seus datasets de gado de corte'),
('AM', 'Uréia Pecuária', 81.98, NULL, 900.0, 281.0, 'Proxy', 'Disponível, porém não representa o grade pecuária (proxy)'),
('AM', 'Gordura protegida', 163.50, NULL, 961.0, NULL, 'Não', 'Não disponível. Disponível apenas na AF6');

-- Inserção dos dados de composição bromatológica dos ingredientes
INSERT INTO bd_fator_emissao_dieta_composicao_ingredientes (
    nome_ingrediente, materia_seca_percentual, proteina_bruta_percentual, fibra_detergente_neutro, extrato_etereo, cinzas, carboidratos_nao_fibrosos, ndt_calculado
) VALUES
('Milho moido', 90.0, 8.5, 15.5, 4.2, 2.5, 69.3, 81.8),
('Farelo de Soja', 90.0, 51.0, 15.5, 2.5, 2.4, 28.6, 74.5),
('Casca de Soja', 90.0, 13.0, 35.7, 2.7, 3.5, 45.1, 69.0),
('DDG 40', 90.0, 44.0, 20.0, 6.5, 4.0, 25.5, 76.2),
('Farelo Algodao 38', 90.0, 41.0, 20.7, 5.5, 3.5, 29.3, 75.6),
('Caroço de algodão', 90.0, 23.0, 35.4, 24.0, 3.3, 14.3, 91.0),
('Ureia', 90.0, 281.0, 0.0, 0.0, 0.2, 54.84, 82.0),
('Gordura Protegida', 90.0, 0.0, 0.0, 88.0, 0.5, 11.5, 182.4),
('Minerais', 95.0, 0.0, 0.0, 0.0, 100.0, 0.0, 0.0);

-- Comentários sobre as tabelas
COMMENT ON TABLE bd_fator_emissao_dieta_racoes_dsm IS 'Tabela contendo composição das rações fornecidas pela DSM (Kg/1000Kg MN) com dados de entrada do usuário';
COMMENT ON TABLE bd_fator_emissao_dieta_ndt_ingredientes IS 'Tabela contendo NDT dos ingredientes com informações sobre fatores de emissão calculados';
COMMENT ON TABLE bd_fator_emissao_dieta_composicao_ingredientes IS 'Tabela contendo composição bromatológica detalhada dos ingredientes (MS, PB, FDN, EE, CZ, CNF, NDT)';