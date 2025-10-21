-- Criar tabela para fatores de emissão de energia elétrica
CREATE TABLE fatores_energia_eletrica (
    id BIGSERIAL PRIMARY KEY,
    ano INTEGER NOT NULL,
    mes INTEGER NOT NULL CHECK (mes >= 1 AND mes <= 12),
    fator_emissao DECIMAL(10, 6) NOT NULL,
    unidade VARCHAR(50) DEFAULT 'tCO2/MWh',
    referencia VARCHAR(100) DEFAULT 'MCT 2015',
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT NOW(),
    data_atualizacao TIMESTAMP DEFAULT NOW(),
    UNIQUE(ano, mes)
);

-- Criar índices
CREATE INDEX idx_fatores_energia_ano_mes ON fatores_energia_eletrica (ano, mes);
CREATE INDEX idx_fatores_energia_ativo ON fatores_energia_eletrica (ativo);