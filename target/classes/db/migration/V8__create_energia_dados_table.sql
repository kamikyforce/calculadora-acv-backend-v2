CREATE TABLE energia_dados (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_energia VARCHAR(50) NOT NULL,
    fonte_energia VARCHAR(100),
    consumo_anual DECIMAL(15,3),
    unidade VARCHAR(20),
    fator_emissao DECIMAL(10,6),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_energia_usuario ON energia_dados (usuario_id);
CREATE INDEX idx_energia_tipo ON energia_dados (tipo_energia);
CREATE INDEX idx_energia_ativo ON energia_dados (ativo);

-- Trigger para atualizar data_atualizacao automaticamente
CREATE OR REPLACE FUNCTION update_data_atualizacao_energia_dados()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_data_atualizacao_energia_dados
    BEFORE UPDATE ON energia_dados
    FOR EACH ROW
    EXECUTE FUNCTION update_data_atualizacao_energia_dados();

-- Foreign key constraint
ALTER TABLE energia_dados 
ADD CONSTRAINT fk_energia_dados_usuario 
FOREIGN KEY (usuario_id) REFERENCES usuario(id);