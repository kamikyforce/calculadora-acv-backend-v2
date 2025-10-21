-- Migration V59: Criação da tabela bd_fator_emissao_escopo_tres
-- Baseada na planilha BD_FATOR_EMISSAO_ESCOPO_TRES.csv
-- Contém fatores de emissão do escopo 3 para ingredientes alimentares

CREATE TABLE bd_fator_emissao_escopo_tres (
    id SERIAL PRIMARY KEY,
    grupo_ingrediente_alimentar VARCHAR(100),
    modulo_correspondente VARCHAR(100),
    prioridade VARCHAR(20),
    nome_produto VARCHAR(200) NOT NULL,
    tipo VARCHAR(10),
    quantidade_produto_referencia DECIMAL(10,3),
    unidade_produto_referencia VARCHAR(10),
    
    -- Matéria-prima + Transporte
    gwp100_total_mp_transporte DECIMAL(15,8),
    gwp100_fossil_mp_transporte DECIMAL(15,8),
    gwp100_biogenic_mp_transporte DECIMAL(15,8),
    gwp100_land_transformation_mp_transporte DECIMAL(15,8),
    carbon_dioxide_fossil_mp_transporte DECIMAL(15,8),
    carbon_dioxide_methane_land_transformation_mp_transporte DECIMAL(15,8),
    methane_fossil_mp_transporte DECIMAL(15,8),
    methane_biogenic_mp_transporte DECIMAL(15,8),
    nitrous_oxide_mp_transporte DECIMAL(15,8),
    outras_substancias_mp_transporte DECIMAL(15,8),
    
    -- Matéria-prima
    gwp100_total_mp DECIMAL(15,8),
    gwp100_fossil_mp DECIMAL(15,8),
    gwp100_biogenic_mp DECIMAL(15,8),
    gwp100_land_transformation_mp DECIMAL(15,8),
    carbon_dioxide_fossil_mp DECIMAL(15,8),
    carbon_dioxide_land_transformation_mp DECIMAL(15,8),
    co2_uptake_land_transformation_mp DECIMAL(15,8),
    methane_fossil_mp DECIMAL(15,8),
    methane_biogenic_mp DECIMAL(15,8),
    methane_land_transformation_mp DECIMAL(15,8),
    nitrous_oxide_mp DECIMAL(15,8),
    outras_substancias_mp DECIMAL(15,8),
    
    -- Transporte
    gwp100_total_transporte DECIMAL(15,8),
    gwp100_fossil_transporte DECIMAL(15,8),
    gwp100_biogenic_transporte DECIMAL(15,8),
    gwp100_land_transformation_transporte DECIMAL(15,8),
    carbon_dioxide_fossil_transporte DECIMAL(15,8),
    carbon_dioxide_land_transformation_transporte DECIMAL(15,8),
    co2_uptake_land_transformation_transporte DECIMAL(15,8),
    methane_fossil_transporte DECIMAL(15,8),
    methane_biogenic_transporte DECIMAL(15,8),
    methane_land_transformation_transporte DECIMAL(15,8),
    nitrous_oxide_transporte DECIMAL(15,8),
    outras_substancias_transporte DECIMAL(15,8),
    
    -- Parâmetros informativos
    distancia_transporte_rodoviario DECIMAL(15,10),
    quantidade_massa_seca DECIMAL(10,3),
    conteudo_carbono_fossil DECIMAL(10,3),
    conteudo_carbono_nao_fossil DECIMAL(10,4),
    produtividade DECIMAL(10,3),
    taxa_emissao_brluc DECIMAL(10,2),
    fonte_producao_materia_prima TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para otimização de consultas
CREATE INDEX idx_bd_fator_emissao_escopo_tres_nome_produto ON bd_fator_emissao_escopo_tres(nome_produto);
CREATE INDEX idx_bd_fator_emissao_escopo_tres_tipo ON bd_fator_emissao_escopo_tres(tipo);
CREATE INDEX idx_bd_fator_emissao_escopo_tres_grupo ON bd_fator_emissao_escopo_tres(grupo_ingrediente_alimentar);
CREATE INDEX idx_bd_fator_emissao_escopo_tres_prioridade ON bd_fator_emissao_escopo_tres(prioridade);

-- Inserção dos dados da planilha BD_FATOR_EMISSAO_ESCOPO_TRES.csv

-- Volumosos (V)
INSERT INTO bd_fator_emissao_escopo_tres (
    grupo_ingrediente_alimentar, modulo_correspondente, prioridade, nome_produto, tipo, 
    quantidade_produto_referencia, unidade_produto_referencia,
    gwp100_total_mp_transporte, gwp100_fossil_mp_transporte, gwp100_biogenic_mp_transporte, gwp100_land_transformation_mp_transporte,
    carbon_dioxide_fossil_mp_transporte, carbon_dioxide_methane_land_transformation_mp_transporte, methane_fossil_mp_transporte, methane_biogenic_mp_transporte,
    nitrous_oxide_mp_transporte, outras_substancias_mp_transporte,
    gwp100_total_mp, gwp100_fossil_mp, gwp100_biogenic_mp, gwp100_land_transformation_mp,
    carbon_dioxide_fossil_mp, carbon_dioxide_land_transformation_mp, co2_uptake_land_transformation_mp, methane_fossil_mp,
    methane_biogenic_mp, methane_land_transformation_mp, nitrous_oxide_mp, outras_substancias_mp,
    gwp100_total_transporte, gwp100_fossil_transporte, gwp100_biogenic_transporte, gwp100_land_transformation_transporte,
    carbon_dioxide_fossil_transporte, carbon_dioxide_land_transformation_transporte, co2_uptake_land_transformation_transporte, methane_fossil_transporte,
    methane_biogenic_transporte, methane_land_transformation_transporte, nitrous_oxide_transporte, outras_substancias_transporte,
    distancia_transporte_rodoviario, quantidade_massa_seca, conteudo_carbono_fossil, conteudo_carbono_nao_fossil,
    produtividade, taxa_emissao_brluc, fonte_producao_materia_prima
) VALUES
('Pastagem', 'Módulo animal, ingredientes alimentares', 'Alta', 'Alfafa, feno médio', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 1, 0, 0.4801, 5.918, 4.12, 'Hay {BR from CA-QC}| hay production | Cut-off, U (BNDES)'),

('Cana-de-açúcar', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cana in natura + 1% de Ureia', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.745, 0, 0.4196, 64.231, -0.21, 'Sugarcane {BR-GO,MG, MS, MT, PR, SP}| sugarcane production | Cut-off, U (BNDES)'),

('Cana-de-açúcar', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cana in natura Abril', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.745, 0, 0.4196, 64.231, -0.21, 'Sugarcane {BR-GO,MG, MS, MT, PR, SP}| sugarcane production | Cut-off, U (BNDES)'),

('Cana-de-açúcar', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cana in natura Setembro', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.745, 0, 0.4196, 64.231, -0.21, 'Sugarcane {BR-GO,MG, MS, MT, PR, SP}| sugarcane production | Cut-off, U (BNDES)'),

('Cana-de-açúcar', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cana, bagaço', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.005, 0.787, 0, 0.443, 0, 0, 'Bagasse, from sugarcane {BR}| sugarcane processing, traditional annexed plant | Cut-off, U (BNDES); Bagasse, from sugarcane {BR}| sugarcane processing, traditional autonomous plant | Cut-off, U (BNDES)'),

('Pastagem', 'Módulo animal, ingredientes alimentares', 'Alta', 'Capim, águas', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0, 0, 0, 13.5, 4.12, 'Grass, Swiss integrated production {BR from CH}| grass production, Swiss integrated production, intensive | Cut-off, U (BNDES)'),

('Pastagem', 'Módulo animal, ingredientes alimentares', 'Alta', 'Capim, silagem', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 1, 0, 0.484, 5, 4.12, 'Alfalfa-grass silage {BR from ZA}| alfalfa/grass silage production | Cut-off, U (BNDES)'),

('Milho', 'Módulo animal, ingredientes alimentares', 'Alta', 'Milho, silagem grão leitoso', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0, 0.35, 0, 0.4773, 50, 2.01, 'Maize silage {BR}| maize silage production | Cut-off, U (BNDES)'),

('Milho', 'Módulo animal, ingredientes alimentares', 'Alta', 'Milho, silagem 62%NDT', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0, 0.35, 0, 0.4773, 50, 2.01, 'Maize silage {BR}| maize silage production | Cut-off, U (BNDES)'),

('Milho', 'Módulo animal, ingredientes alimentares', 'Alta', 'Milho, silagem 65%NDT', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0, 0.35, 0, 0.4773, 50, 2.01, 'Maize silage {BR}| maize silage production | Cut-off, U (BNDES)'),

('Milho', 'Módulo animal, ingredientes alimentares', 'Alta', 'Milho, silagem 68%NDT', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0, 0.35, 0, 0.4773, 50, 2.01, 'Maize silage {BR}| maize silage production | Cut-off, U (BNDES)'),

('Sorgo', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sorgo, silagem boliviano AGRI 002e', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.27, 0, 0.425925926, 48.263, 2.13, 'Sweet sorghum stem {BR from RoW/CN}| sweet sorghum production | Cut-off, U (BNDES)'),

('Sorgo', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sorgo, silagem', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.27, 0, 0.425925926, 48.263, 2.13, 'Sweet sorghum stem {BR from RoW/CN}| sweet sorghum production | Cut-off, U (BNDES)'),

('Sorgo', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sorgo, silagem estágio avançado', 'Al', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.27, 0, 0.425925926, 48.263, 2.13, 'Sweet sorghum stem {BR from RoW/CN}| sweet sorghum production | Cut-off, U (BNDES)'),

('Trigo', 'Módulo animal, ingredientes alimentares', 'Alta', 'Trigo, palha', 'V', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 0.02546689, 0.85, 0, 0.437376667, 2.836654822, 1.49, 'Straw {BR from RoW}| wheat grain production | Cut-off, U (BNDES)');

-- Concentrados Energéticos (CE)
INSERT INTO bd_fator_emissao_escopo_tres (
    grupo_ingrediente_alimentar, modulo_correspondente, prioridade, nome_produto, tipo, 
    quantidade_produto_referencia, unidade_produto_referencia,
    gwp100_total_mp_transporte, gwp100_fossil_mp_transporte, gwp100_biogenic_mp_transporte, gwp100_land_transformation_mp_transporte,
    carbon_dioxide_fossil_mp_transporte, carbon_dioxide_methane_land_transformation_mp_transporte, methane_fossil_mp_transporte, methane_biogenic_mp_transporte,
    nitrous_oxide_mp_transporte, outras_substancias_mp_transporte,
    gwp100_total_mp, gwp100_fossil_mp, gwp100_biogenic_mp, gwp100_land_transformation_mp,
    carbon_dioxide_fossil_mp, carbon_dioxide_land_transformation_mp, co2_uptake_land_transformation_mp, methane_fossil_mp,
    methane_biogenic_mp, methane_land_transformation_mp, nitrous_oxide_mp, outras_substancias_mp,
    gwp100_total_transporte, gwp100_fossil_transporte, gwp100_biogenic_transporte, gwp100_land_transformation_transporte,
    carbon_dioxide_fossil_transporte, carbon_dioxide_land_transformation_transporte, co2_uptake_land_transformation_transporte, methane_fossil_transporte,
    methane_biogenic_transporte, methane_land_transformation_transporte, nitrous_oxide_transporte, outras_substancias_transporte,
    distancia_transporte_rodoviario, quantidade_massa_seca, conteudo_carbono_fossil, conteudo_carbono_nao_fossil,
    produtividade, taxa_emissao_brluc, fonte_producao_materia_prima
) VALUES
('Arroz', 'Módulo animal, ingredientes alimentares', 'Alta', 'Arroz, farelo integral', 'CE', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 48.2, 0.87, 0, 0.46412, 6.856, 2.69, 'Rice, non-basmati {BR from RoW}| rice production, non-basmati | Cut-off, U (BNDES)');

-- Concentrados Proteicos (CP) - Soja
INSERT INTO bd_fator_emissao_escopo_tres (
    grupo_ingrediente_alimentar, modulo_correspondente, prioridade, nome_produto, tipo, 
    quantidade_produto_referencia, unidade_produto_referencia,
    gwp100_total_mp_transporte, gwp100_fossil_mp_transporte, gwp100_biogenic_mp_transporte, gwp100_land_transformation_mp_transporte,
    carbon_dioxide_fossil_mp_transporte, carbon_dioxide_methane_land_transformation_mp_transporte, methane_fossil_mp_transporte, methane_biogenic_mp_transporte,
    nitrous_oxide_mp_transporte, outras_substancias_mp_transporte,
    gwp100_total_mp, gwp100_fossil_mp, gwp100_biogenic_mp, gwp100_land_transformation_mp,
    carbon_dioxide_fossil_mp, carbon_dioxide_land_transformation_mp, co2_uptake_land_transformation_mp, methane_fossil_mp,
    methane_biogenic_mp, methane_land_transformation_mp, nitrous_oxide_mp, outras_substancias_mp,
    gwp100_total_transporte, gwp100_fossil_transporte, gwp100_biogenic_transporte, gwp100_land_transformation_transporte,
    carbon_dioxide_fossil_transporte, carbon_dioxide_land_transformation_transporte, co2_uptake_land_transformation_transporte, methane_fossil_transporte,
    methane_biogenic_transporte, methane_land_transformation_transporte, nitrous_oxide_transporte, outras_substancias_transporte,
    distancia_transporte_rodoviario, quantidade_massa_seca, conteudo_carbono_fossil, conteudo_carbono_nao_fossil,
    produtividade, taxa_emissao_brluc, fonte_producao_materia_prima
) VALUES
('Soja', 'Módulo animal, ingredientes alimentares', 'Alta', 'Soja, farelo {BR-MS}', 'CP', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 10.4, 0.89, 0, 0.475626966, 0, 0, NULL),

('Soja', 'Módulo animal, ingredientes alimentares', 'Alta', 'Soja, grão', 'CP', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 440, 0.89, 0, 0.524507692, 0, 0, 'Soybean {BR-BA}| soybean production | Cut-off, U (BNDES)');

-- Aditivos e Minerais (AM)
INSERT INTO bd_fator_emissao_escopo_tres (
    grupo_ingrediente_alimentar, modulo_correspondente, prioridade, nome_produto, tipo, 
    quantidade_produto_referencia, unidade_produto_referencia,
    gwp100_total_mp_transporte, gwp100_fossil_mp_transporte, gwp100_biogenic_mp_transporte, gwp100_land_transformation_mp_transporte,
    carbon_dioxide_fossil_mp_transporte, carbon_dioxide_methane_land_transformation_mp_transporte, methane_fossil_mp_transporte, methane_biogenic_mp_transporte,
    nitrous_oxide_mp_transporte, outras_substancias_mp_transporte,
    gwp100_total_mp, gwp100_fossil_mp, gwp100_biogenic_mp, gwp100_land_transformation_mp,
    carbon_dioxide_fossil_mp, carbon_dioxide_land_transformation_mp, co2_uptake_land_transformation_mp, methane_fossil_mp,
    methane_biogenic_mp, methane_land_transformation_mp, nitrous_oxide_mp, outras_substancias_mp,
    gwp100_total_transporte, gwp100_fossil_transporte, gwp100_biogenic_transporte, gwp100_land_transformation_transporte,
    carbon_dioxide_fossil_transporte, carbon_dioxide_land_transformation_transporte, co2_uptake_land_transformation_transporte, methane_fossil_transporte,
    methane_biogenic_transporte, methane_land_transformation_transporte, nitrous_oxide_transporte, outras_substancias_transporte,
    distancia_transporte_rodoviario, quantidade_massa_seca, conteudo_carbono_fossil, conteudo_carbono_nao_fossil,
    produtividade, taxa_emissao_brluc, fonte_producao_materia_prima
) VALUES
('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Água', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Tap water {BR}| market for tap water | Cut-off, U'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Bicarbonato de sódio', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Sodium bicarbonate {RoW}| market for sodium bicarbonate | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Calcário Calcítico', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Lime {RoW}| market for lime | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cloreto de Potássio em pó', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Potassium chloride {RoW}| market for potassium chloride | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Cloreto de sódio (Sal Comum)', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Sodium chlorate, powder {RoW}| market for sodium chlorate, powder | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Óxido de magnésio', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Magnesium oxide {GLO}| market for magnesium oxide | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sulfato de cálcio', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Gypsum, mineral {RoW}| market for gypsum, mineral | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sulfato de cobalto 20', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Cobalt sulfate {RoW}| market for cobalt sulfate | Cut-off, U (BNDES)'),

('Minerais', 'Módulo animal, ingredientes alimentares', 'Alta', 'Sulfato de cubre 35', 'AM', 1, 'kg',
 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 6.00E+00, 2.00E+00, 2.00E+00, 2.00E+00, 2.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00, 1.00E+00,
 NULL, NULL, NULL, NULL, NULL, NULL, 'Matéria-prima, Copper sulfate {GLO}| market for copper sulfate | Cut-off, U (BNDES)');

-- Comentários sobre a tabela (PostgreSQL não suporta ALTER TABLE ... COMMENT)
COMMENT ON TABLE bd_fator_emissao_escopo_tres IS 'Tabela de fatores de emissão do escopo 3 para ingredientes alimentares - Baseada na planilha BD_FATOR_EMISSAO_ESCOPO_TRES.csv';