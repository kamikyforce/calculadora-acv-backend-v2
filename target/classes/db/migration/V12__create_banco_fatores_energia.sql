-- Criar tabela para banco de fatores de energia (dados consolidados)
CREATE TABLE banco_fatores_energia (
    id BIGSERIAL PRIMARY KEY,
    ano INTEGER NOT NULL,
    fator_medio_anual DECIMAL(10,6), -- Removed NOT NULL constraint to allow NULL for incomplete projections
    fonte VARCHAR(100) DEFAULT 'SIN - Sistema Interligado Nacional',
    observacoes TEXT,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT NOW(),
    data_atualizacao TIMESTAMP DEFAULT NOW(),
    UNIQUE(ano)
);

-- Inserir dados do banco de fatores
INSERT INTO banco_fatores_energia (ano, fator_medio_anual, observacoes) VALUES
(2020, 0.0617, 'Fator médio anual calculado com base nos dados mensais'),
(2021, 0.1264, 'Fator médio anual calculado com base nos dados mensais'),
(2022, 0.0426, 'Fator médio anual calculado com base nos dados mensais'),
(2023, 0.0385, 'Fator médio anual calculado com base nos dados mensais'),
(2024, 0.0545, 'Projeção - Fator médio anual estimado'),
(2025, 0.0545, 'Projeção - Fator médio anual estimado para 2025');

-- Criar índices
CREATE INDEX idx_banco_fatores_energia_ano ON banco_fatores_energia (ano);
CREATE INDEX idx_banco_fatores_energia_ativo ON banco_fatores_energia (ativo);