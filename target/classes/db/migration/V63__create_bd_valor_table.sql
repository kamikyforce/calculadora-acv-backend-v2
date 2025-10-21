-- V56__create_bd_valor_table.sql
-- Criação da tabela BD_VALOR baseada nos dados da planilha BD_VALOR.csv
-- Contém informações de ingredientes com análise bromatológica

CREATE TABLE bd_valor (
    id SERIAL PRIMARY KEY,
    ingrediente_padronizacao VARCHAR(100) NOT NULL,
    nome_alimento VARCHAR(200) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    representatividade_corte VARCHAR(20),
    representatividade_leite VARCHAR(20),
    
    -- Análise Bromatológica
    materia_seca DECIMAL(8,2),
    proteina_bruta DECIMAL(8,2),
    fibra_bruta DECIMAL(8,2),
    fibra_detergente_neutro DECIMAL(8,2),
    fibra_detergente_acido DECIMAL(8,2),
    extrato_etereo DECIMAL(8,2),
    materia_mineral DECIMAL(8,2),
    ndt DECIMAL(8,2),
    ndt_calculado DECIMAL(8,2),
    energia_bruta_total DECIMAL(8,2),
    energia_digestivel DECIMAL(8,2),
    degradabilidade_energia_bruta DECIMAL(8,2),
    degradabilidade_ms DECIMAL(8,2),
    degradabilidade_mo DECIMAL(8,2),
    edigestivel_ebruta VARCHAR(10),
    energia_bruta_mj DECIMAL(8,2),
    ms_g_kg DECIMAL(8,2),
    
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_valor_tipo ON bd_valor (tipo);
CREATE INDEX idx_bd_valor_ingrediente_padronizacao ON bd_valor (ingrediente_padronizacao);
CREATE INDEX idx_bd_valor_nome_alimento ON bd_valor (nome_alimento);
CREATE INDEX idx_bd_valor_representatividade_corte ON bd_valor (representatividade_corte);
CREATE INDEX idx_bd_valor_representatividade_leite ON bd_valor (representatividade_leite);

-- Inserção dos dados principais (linhas com dados válidos)
INSERT INTO bd_valor (
    ingrediente_padronizacao, nome_alimento, tipo, representatividade_corte, representatividade_leite,
    materia_seca, proteina_bruta, fibra_bruta, fibra_detergente_neutro, fibra_detergente_acido,
    extrato_etereo, materia_mineral, ndt, ndt_calculado, energia_bruta_total, energia_digestivel,
    degradabilidade_energia_bruta, degradabilidade_ms, degradabilidade_mo, edigestivel_ebruta,
    energia_bruta_mj, ms_g_kg
) VALUES
-- Volumosos (V)
('Feno alfafa', 'Alfafa, feno médio', 'V', '-', 'Alta', 89.32, 18.77, 29.36, 46.93, 37.52, 2.85, 9.11, 58.51, 55.24, 4.22, 2.55, 60.52, 57.51, 58.33, '60%', 17.66, 893.2),
('Algodão', 'Algodão casca', 'V', 'Alta', '-', 90.28, 5.27, 38.65, 88.49, 60.43, 1.54, 3.37, 48.65, 55.27, 4.58, 2.36, NULL, NULL, NULL, '52%', 19.16, 902.8),
('Algodão', 'Algodão, capulho', 'V', 'Alta', '-', 91.48, 12.62, NULL, 64.11, 55.00, 6.39, 3.24, 45.68, 56.77, 4.86, 2.52, NULL, NULL, NULL, '52%', 20.33, 914.8),
('Feno aveia', 'Aveia, feno', 'V', '-', 'Alta', 87.42, 11.96, 30.52, 67.74, 41.13, 1.77, 8.09, 54.29, 56.65, 4.05, 2.51, NULL, 54.09, 55.77, '62%', 16.95, 874.2),
('Cana', 'Cana forragem verde', 'V', 'Alta', '-', 21.06, 3.09, NULL, 59.59, 34.00, 1.79, NULL, 66.39, 61.53, NULL, 2.60, NULL, NULL, NULL, NULL, NULL, 210.6),
('Cana', 'Cana in natura', 'V', 'Alta', '-', 24.82, 2.97, NULL, 50.79, 30.00, 1.38, NULL, 68.43, 63.87, NULL, 2.69, NULL, NULL, NULL, NULL, NULL, 248.2),
('Cana', 'Cana in natura, pontas de', 'V', 'Alta', '-', 35.46, 4.93, 43.70, 54.64, 41.31, 2.24, 6.23, NULL, 31.04, NULL, 1.37, NULL, NULL, NULL, NULL, NULL, 354.6),
('Cana', 'Cana, bagaço hidrolisado', 'V', 'Alta', '-', 44.70, 1.82, 34.23, 58.78, 54.42, 1.74, 4.39, NULL, 55.67, NULL, 2.34, NULL, 51.86, NULL, NULL, NULL, 447.0),
('Cana', 'Cana, bagaço', 'V', 'Alta', '-', 57.17, 2.14, 44.14, 85.22, 58.02, 1.19, 4.30, 42.89, 53.35, 4.19, 2.24, 41.59, 33.23, NULL, '53%', 17.53, 571.7),
('Silagem cana', 'Cana, silagem', 'V', 'Alta', '-', 25.68, 3.47, NULL, 66.39, 42.71, 1.70, 4.05, 54.23, 58.75, 4.34, 2.49, NULL, 50.28, 54.25, '57%', 18.16, 256.8),

-- Concentrados Energéticos (CE)
('Farelo de arroz integral', 'Arroz, farelo integral', 'CE', '-', 'Alta', 88.95, 13.42, 8.76, 23.15, 11.96, 16.38, 9.66, 80.33, 89.80, 4.60, 3.91, NULL, 80.54, NULL, '85%', 19.25, 889.5),
('Farelo de arroz desfitinizado', 'Arroz, farelo desfitinizado', 'CE', '-', 'Alta', 90.80, 18.00, NULL, 44.89, NULL, 1.65, 14.29, NULL, 33.27, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 908.0),
('Melaço de cana', 'Cana, melaço em pó', 'CE', 'Alta', '-', 95.64, 2.70, 3.43, NULL, NULL, 0.35, 15.79, 70.00, 70.75, 3.78, 3.12, NULL, NULL, NULL, '83%', 15.82, 956.4),
('Melaço de cana', 'Cana, melaço líquido', 'CE', 'Alta', '-', 75.47, 3.22, NULL, NULL, NULL, 1.44, 9.13, 69.62, 78.80, 3.49, 3.47, 83.10, 82.60, NULL, '99%', 14.60, 754.7),
('Milho', 'Milho, Floculado', 'CE', 'Alta', '-', 88.23, 8.48, 2.50, 11.34, 3.52, 2.81, 0.98, NULL, 87.37, NULL, 3.76, NULL, NULL, NULL, NULL, NULL, 882.3),
('Silagem milho', 'Milho, grão úmido silagem', 'CE', 'Alta', 'Alta', 66.72, 9.19, 2.17, 10.02, 2.98, 4.59, 1.60, 89.73, 88.42, 4.27, 3.81, NULL, NULL, NULL, '89%', 17.87, 667.2),
('Milho', 'Milho, moído (nº4)', 'CE', 'Alta', 'Alta', 79.93, 10.04, NULL, 16.95, 2.95, 6.55, 0.98, 83.18, 90.06, NULL, 3.89, NULL, NULL, NULL, NULL, NULL, 799.3),

-- Concentrados Proteicos (CP)
('Torta de algodão', 'Algodão, torta', 'CP', 'Alta', '-', 90.48, 29.70, 48.00, 48.00, 35.00, 1.20, 6.10, 84.50, 84.50, NULL, 3.70, NULL, NULL, NULL, NULL, NULL, 904.8),
('Caroço de algodão', 'Algodão, caroço', 'CP', 'Alta', '-', 90.66, 22.90, 45.70, 45.70, 32.00, 20.00, 3.70, 96.80, 96.80, NULL, 4.27, NULL, NULL, NULL, NULL, NULL, 906.6),
('DDGS', 'DDGS', 'CP', 'Alta', 'Alta', 61.49, 30.70, 42.50, 42.50, 15.00, 10.00, 5.30, 86.20, 86.20, NULL, 3.80, NULL, NULL, NULL, NULL, NULL, 614.9),
('Farelo de milho, glúten 21 (Promil/Refinazil)', 'Farelo de milho, glúten 21', 'CP', 'Alta', 'Alta', 88.83, 24.00, 41.80, 41.80, 12.00, 2.50, 1.50, 78.10, 78.10, NULL, 3.44, NULL, NULL, NULL, NULL, NULL, 888.3),
('Farelo de milho, glúten 60 (Glutenose/Protenose)', 'Farelo de milho, glúten 60', 'CP', 'Alta', 'Alta', 90.55, 64.00, 6.90, 6.90, 3.00, 2.50, 1.80, 84.60, 84.60, NULL, 3.73, NULL, NULL, NULL, NULL, NULL, 905.5),
('Farelo de milho, glúten úmido (GoldenMill)', 'Farelo de milho, glúten úmido', 'CP', 'Alta', 'Alta', 37.30, 22.40, 48.40, 48.40, 15.00, 2.50, 1.50, 70.10, 70.10, NULL, 3.09, NULL, NULL, NULL, NULL, NULL, 373.0),
('Farelo soja', 'Farelo de soja', 'CP', 'Alta', 'Alta', 88.65, 48.80, 14.80, 14.80, 7.00, 1.50, 6.40, 80.40, 80.40, NULL, 3.55, NULL, NULL, NULL, NULL, NULL, 886.5),
('Soja', 'Soja (grão)', 'CP', 'Alta', 'Alta', 90.92, 38.50, 19.60, 19.60, 8.00, 18.00, 5.00, 97.00, 97.00, NULL, 4.28, NULL, NULL, NULL, NULL, NULL, 909.2),

-- Aditivos e Minerais (AM)
('Uréia Pecuária', 'Uréia Pecuária', 'AM', '-', 'Alta', 90.00, 281.00, NULL, 0.00, 0.00, 0.00, 0.20, NULL, 81.98, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 900.0),
('Gordura protegida', 'Gordura protegida (sabao de calcio) (megalac)', 'AM', '-', 'Alta', 96.10, NULL, NULL, NULL, NULL, 83.88, 23.81, 163.50, 163.50, NULL, 7.21, NULL, NULL, NULL, NULL, NULL, 961.0);

-- Comentários sobre a tabela
COMMENT ON TABLE bd_valor IS 'Tabela contendo valores nutricionais (análise bromatológica) de ingredientes para alimentação animal';