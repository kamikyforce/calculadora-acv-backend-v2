-- V57__create_bd_manejo_tables.sql
-- Criação das tabelas BD_MANEJO baseadas nos dados da planilha BD_MANEJO.csv
-- Contém fatores de emissão para sistemas de manejo de dejetos conforme IPCC 2019

-- Tabela para fatores MCF (Methane Conversion Factor)
CREATE TABLE bd_manejo_mcf (
    id SERIAL PRIMARY KEY,
    sistema_manejo VARCHAR(50) NOT NULL,
    mcf_percentual DECIMAL(5,2) NOT NULL,
    fonte VARCHAR(200),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_manejo_mcf_sistema_manejo ON bd_manejo_mcf (sistema_manejo);

-- Tabela para fatores de emissão direta de N2O (EF3)
CREATE TABLE bd_manejo_ef3 (
    id SERIAL PRIMARY KEY,
    sistema_manejo VARCHAR(50) NOT NULL,
    ef3_valor DECIMAL(8,6) NOT NULL,
    fonte VARCHAR(200),
    observacao VARCHAR(100),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_manejo_ef3_sistema_manejo ON bd_manejo_ef3 (sistema_manejo);

-- Tabela para fatores de emissão indireta de N2O
CREATE TABLE bd_manejo_emissao_indireta (
    id SERIAL PRIMARY KEY,
    tipo_producao VARCHAR(10) NOT NULL CHECK (tipo_producao IN ('Leite', 'Corte')),
    sistema_manejo VARCHAR(50) NOT NULL,
    frac_gasm DECIMAL(8,6),
    frac_leach DECIMAL(8,6),
    fonte VARCHAR(200),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_manejo_emissao_indireta_tipo_producao ON bd_manejo_emissao_indireta (tipo_producao);
CREATE INDEX idx_bd_manejo_emissao_indireta_sistema_manejo ON bd_manejo_emissao_indireta (sistema_manejo);
CREATE INDEX idx_bd_manejo_emissao_indireta_tipo_sistema ON bd_manejo_emissao_indireta (tipo_producao, sistema_manejo);

-- Tabela para fatores EF4 e EF5
CREATE TABLE bd_manejo_ef4_ef5 (
    id SERIAL PRIMARY KEY,
    fator_tipo VARCHAR(10) NOT NULL CHECK (fator_tipo IN ('EF4', 'EF5')),
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(8,6) NOT NULL,
    unidade VARCHAR(100) NOT NULL,
    fonte VARCHAR(200),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_manejo_ef4_ef5_fator_tipo ON bd_manejo_ef4_ef5 (fator_tipo);

-- Inserção dos dados MCF
INSERT INTO bd_manejo_mcf (sistema_manejo, mcf_percentual, fonte) VALUES
('Lagoa anaeróbia', 78.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Esterqueira', 31.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Armazenamento sólido', 4.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Dry lot', 2.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Pastagem', 2.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Distribuição diária', 1.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Biodigestor', 1.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1'),
('Compostagem/Compost Barn', 10.00, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1');

-- Inserção dos dados EF3
INSERT INTO bd_manejo_ef3 (sistema_manejo, ef3_valor, fonte, observacao) VALUES
('Lagoa anaeróbia', 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Esterqueira', 0.005000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Armazenamento sólido', 0.005000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Dry lot', 0.020000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Pastagem', 0.004000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', 'verificar EF'),
('Distribuição diária', 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Biodigestor', 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL),
('Compostagem/Compost Barn', 0.005000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.21; IPCC 2019, Vol 4, Ch. 11, Table 11.1', NULL);

-- Inserção dos dados de emissão indireta - Leite
INSERT INTO bd_manejo_emissao_indireta (tipo_producao, sistema_manejo, frac_gasm, frac_leach, fonte) VALUES
('Leite', 'Lagoa anaeróbia', 0.350000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Esterqueira', 0.300000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Armazenamento sólido', 0.300000, 0.020000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Dry lot', 0.300000, 0.035000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Pastagem', 0.210000, 0.240000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Distribuição diária', 0.070000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Biodigestor', 0.050000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Leite', 'Compostagem/Compost Barn', 0.450000, 0.040000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),

-- Inserção dos dados de emissão indireta - Corte
('Corte', 'Lagoa anaeróbia', 0.350000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Esterqueira', 0.300000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Armazenamento sólido', 0.450000, 0.020000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Dry lot', 0.300000, 0.035000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Pastagem', 0.210000, 0.240000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Distribuição diária', 0.070000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Biodigestor', 0.050000, 0.000000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('Corte', 'Compostagem/Compost Barn', 0.450000, 0.040000, 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3');

-- Inserção dos fatores EF4 e EF5
INSERT INTO bd_manejo_ef4_ef5 (fator_tipo, descricao, valor, unidade, fonte) VALUES
('EF4', 'Volatilização e redeposição', 0.010000, 'kg N2O-N / (kg NH3-N + NOx-N volatilizado)', 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3'),
('EF5', 'Lixiviação/escoamento superficial', 0.011000, 'kg N2O-N / (kg N lixiviado/escoado)', 'IPCC 2019, Vol 4, Ch. 10, Table 10.22; IPCC 2019, Vol 4, Ch. 11, Table 11.3');

-- Comentários sobre as tabelas
COMMENT ON TABLE bd_manejo_mcf IS 'Fatores MCF para estimativa de emissões de metano em sistemas de manejo de dejetos';
COMMENT ON TABLE bd_manejo_ef3 IS 'Fatores de emissão direta de N2O para sistemas de manejo de dejetos';
COMMENT ON TABLE bd_manejo_emissao_indireta IS 'Fatores de emissão indireta de N2O para sistemas de manejo de dejetos por tipo de produção';
COMMENT ON TABLE bd_manejo_ef4_ef5 IS 'Fatores EF4 e EF5 para cálculo de emissões indiretas de N2O';