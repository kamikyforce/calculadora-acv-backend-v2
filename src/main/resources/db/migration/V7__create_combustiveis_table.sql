CREATE TABLE combustiveis (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fator_emissao_co2 DECIMAL(10,6),
    fator_emissao_ch4 DECIMAL(10,6),
    fator_emissao_n2o DECIMAL(10,6),
    unidade VARCHAR(20),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_combustivel_nome ON combustiveis (nome);
CREATE INDEX idx_combustivel_tipo ON combustiveis (tipo);
CREATE INDEX idx_combustivel_ativo ON combustiveis (ativo);

-- Trigger para atualizar data_atualizacao automaticamente
CREATE OR REPLACE FUNCTION update_data_atualizacao_combustiveis()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_data_atualizacao_combustiveis
    BEFORE UPDATE ON combustiveis
    FOR EACH ROW
    EXECUTE FUNCTION update_data_atualizacao_combustiveis();