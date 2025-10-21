-- V55__create_bd_dieta_tables.sql
-- Criação das tabelas BD_DIETA baseadas nos dados da planilha BD_DIETA.csv
-- Dividido em 4 tabelas: rações, ingredientes individuais, forragens e fatores de conversão de metano

-- Tabela 1: Rações (Corte e Leite)
CREATE TABLE bd_dieta_racoes (
    id SERIAL PRIMARY KEY,
    tipo_racao VARCHAR(20) NOT NULL,
    numero_racao INTEGER NOT NULL,
    proteina_bruta_percentual DECIMAL(5,2) NOT NULL,
    ureia VARCHAR(10) NOT NULL,
    subproduto VARCHAR(10) NOT NULL,
    codigo_racao VARCHAR(20) NOT NULL,
    milho DECIMAL(6,3) NOT NULL,
    farelo_soja DECIMAL(6,3) NOT NULL,
    casquinha_soja DECIMAL(6,3) NOT NULL,
    ddgs DECIMAL(6,3) NOT NULL,
    farelo_algodao_38 DECIMAL(6,3) NOT NULL,
    ureia_pecuaria DECIMAL(6,3) NOT NULL,
    minerais DECIMAL(6,3) NOT NULL,
    total_proporcao DECIMAL(6,3) NOT NULL,
    ndt DECIMAL(6,2) NOT NULL,
    energia_bruta DECIMAL(6,2) NOT NULL,
    materia_seca DECIMAL(8,2) NOT NULL,
    proteina_bruta_ms DECIMAL(6,2) NOT NULL,
    fibra_detergente_neutro DECIMAL(6,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_dieta_racoes_tipo_racao ON bd_dieta_racoes (tipo_racao);
CREATE INDEX idx_bd_dieta_racoes_numero_racao ON bd_dieta_racoes (numero_racao);
CREATE INDEX idx_bd_dieta_racoes_codigo_racao ON bd_dieta_racoes (codigo_racao);
CREATE UNIQUE INDEX uk_bd_dieta_racoes_codigo_racao ON bd_dieta_racoes (codigo_racao);

-- Tabela 2: Ingredientes Individuais
CREATE TABLE bd_dieta_ingredientes (
    id SERIAL PRIMARY KEY,
    tipo_ingrediente VARCHAR(10) NOT NULL,
    nome_ingrediente VARCHAR(100) NOT NULL,
    ndt_percentual DECIMAL(6,2) NULL,
    energia_bruta DECIMAL(6,2) NULL,
    materia_seca DECIMAL(8,2) NULL,
    proteina_bruta_ms DECIMAL(8,2) NULL,
    fibra_detergente_neutro DECIMAL(6,2) NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_dieta_ingredientes_tipo_ingrediente ON bd_dieta_ingredientes (tipo_ingrediente);
CREATE INDEX idx_bd_dieta_ingredientes_nome_ingrediente ON bd_dieta_ingredientes (nome_ingrediente);

-- Tabela 3: Forragens (Pasto)
CREATE TABLE bd_dieta_forragens (
    id SERIAL PRIMARY KEY,
    tipo_forragem VARCHAR(50) NOT NULL,
    nome_forragem VARCHAR(100) NOT NULL,
    ndt_percentual DECIMAL(6,2) NULL,
    energia_bruta DECIMAL(6,2) NULL,
    materia_seca DECIMAL(8,2) NULL,
    proteina_bruta_ms DECIMAL(8,2) NULL,
    fibra_detergente_neutro DECIMAL(6,2) NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_dieta_forragens_tipo_forragem ON bd_dieta_forragens (tipo_forragem);
CREATE INDEX idx_bd_dieta_forragens_nome_forragem ON bd_dieta_forragens (nome_forragem);

-- Tabela 4: Fatores de Conversão de Metano (Ym)
CREATE TABLE bd_dieta_fatores_metano (
    id SERIAL PRIMARY KEY,
    tipo_producao VARCHAR(20) NOT NULL,
    de_min DECIMAL(6,2) NULL,
    de_max DECIMAL(6,2) NULL,
    ndf_min DECIMAL(6,2) NULL,
    ndf_max DECIMAL(6,2) NULL,
    ym DECIMAL(4,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_dieta_fatores_metano_tipo_producao ON bd_dieta_fatores_metano (tipo_producao);

-- Inserção dos dados das rações
INSERT INTO bd_dieta_racoes (
    tipo_racao, numero_racao, proteina_bruta_percentual, ureia, subproduto, codigo_racao,
    milho, farelo_soja, casquinha_soja, ddgs, farelo_algodao_38, ureia_pecuaria, minerais, total_proporcao,
    ndt, energia_bruta, materia_seca, proteina_bruta_ms, fibra_detergente_neutro
) VALUES
-- Rações de Corte
('Corte', 1, 15.00, 'Sem', 'Sem', '15SemSem', 0.508, 0.242, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.53, 14.05, 641.66, 16.46, 10.76),
('Corte', 2, 15.00, 'Com', 'Sem', '15ComSem', 0.562, 0.178, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.99, 13.85, 639.33, 16.64, 10.58),
('Corte', 3, 15.00, 'Sem', 'Com', '15SemCom', 0.445, 0.155, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.87, 12.74, 603.79, 16.25, 14.96),
('Corte', 4, 15.00, 'Com', 'Com', '15ComCom', 0.498, 0.092, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 65.33, 12.55, 601.50, 16.46, 14.78),
('Corte', 5, 17.00, 'Sem', 'Sem', '17SemSem', 0.455, 0.295, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.09, 14.05, 644.08, 18.56, 10.80),
('Corte', 6, 17.00, 'Com', 'Sem', '17ComSem', 0.510, 0.230, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.56, 13.86, 641.70, 18.70, 10.61),
('Corte', 7, 17.00, 'Sem', 'Com', '17SemCom', 0.392, 0.208, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.43, 12.74, 606.21, 18.35, 15.00),
('Corte', 8, 17.00, 'Com', 'Com', '17ComCom', 0.446, 0.144, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 64.89, 12.55, 603.88, 18.53, 14.81),
('Corte', 9, 19.00, 'Sem', 'Sem', '19SemSem', 0.403, 0.347, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.65, 14.05, 646.46, 20.63, 10.83),
('Corte', 10, 19.00, 'Com', 'Sem', '19ComSem', 0.457, 0.283, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.12, 13.86, 644.13, 20.81, 10.65),
('Corte', 11, 19.00, 'Sem', 'Com', '19SemCom', 0.340, 0.260, 0.000, 0.150, 0.000, 0.000, 0.250, 1.000, 64.00, 12.75, 608.59, 20.42, 15.03),
('Corte', 12, 19.00, 'Com', 'Com', '19ComCom', 0.394, 0.196, 0.000, 0.150, 0.000, 0.010, 0.250, 1.000, 64.46, 12.56, 606.26, 20.59, 14.85),
('Corte', 13, 30.00, 'Com', 'Sem', '30ComSem', 0.350, 0.340, 0.000, 0.000, 0.000, 0.060, 0.250, 1.000, 63.31, 12.93, 649.69, 36.66, 9.98),
('Corte', 14, 30.00, 'Com', 'Com', '30ComCom', 0.140, 0.000, 0.150, 0.250, 0.150, 0.060, 0.250, 1.000, 50.11, 10.55, 595.20, 33.66, 27.47),
('Corte', 15, 35.00, 'Com', 'Sem', '35ComSem', 0.430, 0.220, 0.000, 0.000, 0.000, 0.100, 0.250, 1.000, 64.04, 12.17, 646.57, 42.77, 9.33),
('Corte', 16, 35.00, 'Com', 'Com', '35ComCom', 0.280, 0.000, 0.100, 0.050, 0.220, 0.100, 0.250, 1.000, 44.83, 11.64, 643.70, 42.17, 19.92),
('Corte', 17, 40.00, 'Com', 'Sem', '40ComSem', 0.385, 0.245, 0.000, 0.000, 0.000, 0.120, 0.250, 1.000, 63.69, 11.80, 648.90, 49.20, 9.07),
('Corte', 18, 40.00, 'Com', 'Com', '40ComCom', 0.190, 0.000, 0.150, 0.100, 0.190, 0.120, 0.250, 1.000, 46.53, 10.74, 634.91, 47.95, 23.11),
('Corte', 19, 45.00, 'Com', 'Sem', '45ComSem', 0.325, 0.290, 0.000, 0.000, 0.000, 0.135, 0.250, 1.000, 63.22, 11.53, 651.85, 55.07, 8.88),
('Corte', 20, 45.00, 'Com', 'Com', '45ComCom', 0.165, 0.000, 0.100, 0.150, 0.200, 0.135, 0.250, 1.000, 46.11, 10.11, 622.05, 53.24, 21.89),
('Corte', 21, 20.00, 'Com', 'Sem', '20ComSem', 0.600, 0.220, 0.000, 0.000, 0.000, 0.030, 0.150, 1.000, 73.38, 15.35, 726.51, 24.66, 11.74),
('Corte', 22, 20.00, 'Com', 'Com', '20ComCom', 0.410, 0.000, 0.150, 0.060, 0.200, 0.030, 0.150, 1.000, 55.23, 14.64, 723.26, 23.83, 24.85),
('Corte', 23, 25.00, 'Com', 'Sem', '25ComSem', 0.570, 0.230, 0.000, 0.000, 0.000, 0.050, 0.150, 1.000, 73.16, 14.98, 728.15, 30.49, 11.46),
('Corte', 24, 25.00, 'Com', 'Com', '25ComCom', 0.375, 0.000, 0.150, 0.075, 0.200, 0.050, 0.150, 1.000, 55.05, 14.14, 721.06, 29.60, 24.99),

-- Rações de Leite (códigos prefixados com 'L' para evitar duplicação)
('Leite', 1, 15.00, 'Sem', 'Sem', 'L15SemSem', 0.500, 0.250, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 64.46, 14.05, 642.03, 16.78, 10.77),
('Leite', 2, 15.00, 'Com', 'Sem', 'L15ComSem', 0.560, 0.180, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.97, 13.85, 639.42, 16.72, 10.58),
('Leite', 3, 15.00, 'Sem', 'Com', 'L15SemCom', 0.410, 0.240, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 63.15, 13.89, 647.60, 16.73, 15.99),
('Leite', 4, 15.00, 'Com', 'Com', 'L15ComCom', 0.470, 0.170, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 63.67, 13.69, 644.99, 16.67, 15.80),
('Leite', 5, 18.00, 'Sem', 'Sem', 'L18SemSem', 0.420, 0.330, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.80, 14.05, 645.68, 19.95, 10.82),
('Leite', 6, 18.00, 'Com', 'Sem', 'L18ComSem', 0.480, 0.260, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.31, 13.86, 643.07, 19.89, 10.63),
('Leite', 7, 18.00, 'Sem', 'Com', 'L18SemCom', 0.340, 0.310, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 62.57, 13.89, 650.80, 19.51, 16.03),
('Leite', 8, 18.00, 'Com', 'Com', 'L18ComCom', 0.480, 0.260, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 64.31, 13.86, 643.07, 19.89, 10.63),
('Leite', 9, 20.00, 'Sem', 'Sem', 'L20SemSem', 0.370, 0.380, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 63.38, 14.06, 647.97, 21.94, 10.85),
('Leite', 10, 20.00, 'Com', 'Sem', 'L20ComSem', 0.430, 0.310, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 63.89, 13.86, 645.36, 21.88, 10.66),
('Leite', 11, 20.00, 'Sem', 'Com', 'L20SemCom', 0.280, 0.370, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 62.07, 13.90, 653.54, 21.89, 16.07),
('Leite', 12, 20.00, 'Com', 'Com', 'L20ComCom', 0.330, 0.310, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 62.50, 13.71, 651.39, 22.22, 15.89),
('Leite', 13, 22.00, 'Sem', 'Sem', 'L22SemSem', 0.320, 0.430, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 62.97, 14.06, 650.25, 23.92, 10.88),
('Leite', 14, 22.00, 'Com', 'Sem', 'L22ComSem', 0.370, 0.370, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 63.40, 13.87, 648.10, 24.26, 10.70),
('Leite', 15, 22.00, 'Sem', 'Com', 'L22SemCom', 0.230, 0.420, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 61.66, 13.90, 655.82, 23.87, 16.10),
('Leite', 16, 22.00, 'Com', 'Com', 'L22ComCom', 0.290, 0.350, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 62.17, 13.71, 653.22, 23.81, 15.92),
('Leite', 17, 24.00, 'Sem', 'Sem', 'L24SemSem', 0.270, 0.480, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 62.55, 14.07, 652.54, 25.91, 10.91),
('Leite', 18, 24.00, 'Com', 'Sem', 'L24ComSem', 0.320, 0.420, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 62.98, 13.87, 650.39, 26.25, 10.73),
('Leite', 19, 24.00, 'Sem', 'Com', 'L24SemCom', 0.180, 0.470, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 61.24, 13.91, 658.11, 25.86, 16.13),
('Leite', 20, 24.00, 'Com', 'Com', 'L24ComCom', 0.230, 0.410, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 61.67, 13.71, 655.96, 26.19, 15.95),
('Leite', 21, 27.00, 'Sem', 'Sem', 'L27SemSem', 0.190, 0.560, 0.000, 0.000, 0.000, 0.000, 0.250, 1.000, 61.89, 14.07, 656.19, 29.09, 10.96),
('Leite', 22, 27.00, 'Com', 'Sem', 'L27ComSem', 0.240, 0.500, 0.000, 0.000, 0.000, 0.010, 0.250, 1.000, 62.32, 13.88, 654.04, 29.42, 10.78),
('Leite', 23, 27.00, 'Sem', 'Com', 'L27SemCom', 0.100, 0.550, 0.100, 0.000, 0.000, 0.000, 0.250, 1.000, 60.58, 13.91, 661.77, 29.03, 16.18),
('Leite', 24, 27.00, 'Com', 'Com', 'L27ComCom', 0.150, 0.490, 0.100, 0.000, 0.000, 0.010, 0.250, 1.000, 61.01, 13.72, 659.62, 29.37, 16.00);

-- Inserção dos dados dos ingredientes individuais
INSERT INTO bd_dieta_ingredientes (
    tipo_ingrediente, nome_ingrediente, ndt_percentual, energia_bruta, materia_seca, proteina_bruta_ms, fibra_detergente_neutro
) VALUES
-- Volumosos (V)
('V', 'Algodão', 56.0, 19.75, 908.8, 8.9, 76.3),
('V', 'Aveia forrageira', 68.6, 17.15, 177.0, 11.6, 49.2),
('V', 'Azevem pré-secado', 66.9, 18.10, 622.0, 6.7, 47.6),
('V', 'Cana', 53.1, 18.00, 366.4, 3.0, 61.8),
('V', 'Capim', 56.7, 18.20, 292.2, 8.6, 71.0),
('V', 'Feno alfafa', 55.2, 17.66, 893.2, 18.8, 46.9),
('V', 'Feno aveia', 56.7, 16.95, 874.2, 12.0, 67.7),
('V', 'Feno azevem', 73.7, 18.10, 929.8, 14.7, 50.2),
('V', 'Feno braquiária', 54.3, 18.12, 891.4, 7.0, 81.5),
('V', 'Feno cynodon', 55.6, 18.30, 887.9, 8.9, 78.0),
('V', 'Milho (palha)', 53.3, 18.28, 904.4, 2.6, 84.3),
('V', 'Milho (planta)', 64.6, 17.87, 331.4, 7.1, 55.3),
('V', 'Palha de aveia', 57.5, 18.40, 888.1, 4.6, 77.3),
('V', 'Palma forrageira', 63.4, 13.18, 117.8, 4.3, 30.7),
('V', 'Silagem cana', 58.8, 18.16, 256.8, 3.5, 66.4),
('V', 'Silagem milheto', 56.0, 17.15, 264.9, 8.1, 68.5),
('V', 'Silagem milho', 75.7, 19.10, 452.3, 8.2, 35.5),
('V', 'Silagem sorgo', 65.1, 18.10, 310.3, 6.2, 58.6),

-- Concentrados Energéticos (CE)
('CE', 'Casquinha de soja', 74.8, 17.11, 901.1, 12.6, 66.4),
('CE', 'Farelo de arroz desfitinizado', 33.3, 17.00, 908.0, 18.0, 44.9),
('CE', 'Farelo de arroz integral', 89.8, 19.25, 889.5, 13.4, 23.2),
('CE', 'Gérmen de milho desengordurado', 77.8, 15.86, 897.6, 11.2, 29.3),
('CE', 'Gérmen de milho farelo', 101.1, 22.05, 899.1, 11.0, 32.5),
('CE', 'Melaço de cana', 74.8, 15.21, 855.6, 3.0, NULL),
('CE', 'Milho', 88.7, 18.70, 840.8, 9.1, 14.1),
('CE', 'Milho (quebradinho)', 12.5, 17.66, 884.9, 6.4, 82.3),
('CE', 'Polpa cítrica', 78.5, 18.30, 895.9, 7.2, 26.1),
('CE', 'Sorgo', 87.9, 18.80, 652.7, 9.1, 7.2),
('CE', 'Trigo', 81.8, 17.38, 881.1, 15.7, 32.2),

-- Concentrados Proteicos (CP)
('CP', 'Caroço de algodão', 96.8, 23.47, 906.6, 22.9, 45.7),
('CP', 'DDGS', 86.2, 10.04, 614.9, 30.7, 42.5),
('CP', 'Farelo de algodão 38', 77.0, 19.04, 897.4, 39.6, 32.7),
('CP', 'Farelo de milho, glúten 21 (Promil/Refinazil)', 78.1, 15.94, 888.3, 24.0, 41.8),
('CP', 'Farelo de milho, glúten 60 (Glutenose/Protenose)', 84.6, 21.84, 905.5, 64.0, 6.9),
('CP', 'Farelo de milho, glúten úmido (GoldenMill)', 70.1, 18.50, 373.0, 22.4, 48.4),
('CP', 'Farelo de soja', 80.4, 18.79, 886.5, 48.8, 14.8),
('CP', 'Soja (grão)', 97.0, 22.09, 909.2, 38.5, 19.6),
('CP', 'Torta de algodão', 84.5, 20.84, 904.8, 29.7, 48.0),

-- Aditivos e Minerais (AM)
('AM', 'Uréia Pecuária', 82.0, NULL, 900.0, 281.0, NULL),
('AM', 'Gordura protegida', 163.5, NULL, 961.0, NULL, NULL);

-- Inserção dos dados das forragens (pasto)
INSERT INTO bd_dieta_forragens (
    tipo_forragem, nome_forragem
) VALUES
('V', 'Pasto sem manejo'),
('V', 'Pasto manejado');

-- Inserção dos dados dos fatores de conversão de metano
INSERT INTO bd_dieta_fatores_metano (
    tipo_producao, de_min, de_max, ndf_min, ndf_max, ym
) VALUES
('Leite', 70.0, 100.0, 0.0, 35.0, 5.7),
('Leite', 70.0, 100.0, 35.0, 100.0, 6.0),
('Leite', 63.0, 69.0, 37.0, 100.0, 6.3),
('Leite', 0.0, 63.0, 38.0, 100.0, 6.5),
('Corte', 0.0, 62.0, 0.0, 100.0, 7.0),
('Corte', 62.0, 72.0, NULL, NULL, 6.3),
('Corte', 72.0, 100.0, NULL, NULL, 4.0),
('Corte', 76.0, 100.0, NULL, NULL, 3.0);

-- Comentários sobre as tabelas
COMMENT ON TABLE bd_dieta_racoes IS 'Tabela contendo composição e bromatologia das rações fornecidas (Corte e Leite)';
COMMENT ON TABLE bd_dieta_ingredientes IS 'Tabela contendo composição e bromatologia dos ingredientes ofertados individualmente na ração';
COMMENT ON TABLE bd_dieta_forragens IS 'Tabela contendo composição e bromatologia de forragem (pasto) sob diferentes qualidades';
COMMENT ON TABLE bd_dieta_fatores_metano IS 'Tabela contendo fatores de conversão de metano (Ym) por tipo de produção';