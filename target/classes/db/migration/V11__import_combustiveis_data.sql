-- Importação dos dados de combustíveis da planilha Excel
-- Aba: Combustíveis

INSERT INTO combustiveis (nome, tipo, fator_emissao_co2, fator_emissao_ch4, fator_emissao_n2o, unidade, ativo, data_criacao, data_atualizacao) VALUES
('Pure gasoline', 'FOSSIL', 2.212, 0.0008, 0.00026, 'liter', true, NOW(), NOW()),
('Diesel', 'FOSSIL', 2.603, 0.0001, 0.00014, 'liter', true, NOW(), NOW()),
('LPG', 'FOSSIL', 2.932, 0.0029, 0.00001, 'kg', true, NOW(), NOW()),
('Etanol Anidro', 'BIOFUEL', 1.526, 0.0002, 0.00001, 'liter', true, NOW(), NOW()),
('Etanol Hidratado', 'BIOFUEL', 1.457, 0.0004, 0.00001, 'liter', true, NOW(), NOW()),
('Biodiesel (B100)', 'BIOFUEL', 2.431, 0.0003, 0.00002, 'liter', true, NOW(), NOW());

-- Inserir dados de combustíveis brasileiros específicos
INSERT INTO combustiveis (nome, tipo, fator_emissao_co2, fator_emissao_ch4, fator_emissao_n2o, unidade, ativo, data_criacao, data_atualizacao) VALUES
('Gasolina Brasileira', 'FOSSIL', 1.6709365, 0.42189675, 0.0, 'liter', true, NOW(), NOW()),
('Diesel Brasileiro', 'FOSSIL', 2.379546, 0.244462, 0.0, 'liter', true, NOW(), NOW());

-- Inserir dados de óleos diesel
INSERT INTO combustiveis (nome, tipo, fator_emissao_co2, fator_emissao_ch4, fator_emissao_n2o, unidade, ativo, data_criacao, data_atualizacao) VALUES
('Óleo diesel', 'FOSSIL', 0.002651, 0.0000002, 0.0000003, 'tonelada CO2/litro', true, NOW(), NOW()),
('Gasolina A', 'FOSSIL', 0.002224, 0.0, 0.0, 'tonelada CO2/litro', true, NOW(), NOW());