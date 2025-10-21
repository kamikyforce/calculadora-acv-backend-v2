-- V21__create_insumos_rebanho_table.sql

-- Tabela de insumos do rebanho
CREATE TABLE insumos_rebanho (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    
    -- Campos básicos
    modulo VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('INGREDIENTES_ALIMENTARES', 'ANIMAIS_COMPRADOS', 'FERTILIZANTES')),
    escopo VARCHAR(20) NOT NULL CHECK (escopo IN ('ESCOPO1', 'ESCOPO2', 'ESCOPO3_PRODUCAO', 'ESCOPO3_TRANSPORTE')),
    identificacao_produto VARCHAR(255) NOT NULL,
    fonte_dataset VARCHAR(255),
    unidade_produto VARCHAR(20),
    metodo_avaliacao_gwp VARCHAR(100),
    
    -- Campos para Escopo 1
    gee_total DECIMAL(15,6),
    co2_fossil DECIMAL(15,6),
    uso_terra DECIMAL(15,6),
    ch4_fossil DECIMAL(15,6),
    ch4_biogenico DECIMAL(15,6),
    n2o DECIMAL(15,6),
    outras_substancias DECIMAL(15,6),
    
    -- Campos para Escopo 3
    grupo_ingrediente VARCHAR(50) CHECK (grupo_ingrediente IN ('CEREAIS_E_GRAOS', 'LEGUMINOSAS', 'OLEAGINOSAS')),
    nome_produto VARCHAR(255),
    unidade_produto_referencia VARCHAR(10) CHECK (unidade_produto_referencia IN ('KG', 'T', 'G')),
    
    -- Campos de emissões Escopo 3
    gwp_fossil DECIMAL(15,6),
    gwp_biogenico DECIMAL(15,6),
    gwp_transformacao DECIMAL(15,6),
    dioxido_carbono_fossil DECIMAL(15,6),
    dioxido_carbono_metano DECIMAL(15,6),
    metano_fossil DECIMAL(15,6),
    metano_biogenico DECIMAL(15,6),
    oxido_nitroso DECIMAL(15,6),
    outras_substancias_escopo3 DECIMAL(15,6),
    
    -- Campos de dieta
    faz_parte_dieta VARCHAR(10) CHECK (faz_parte_dieta IN ('SIM', 'NAO')),
    ingrediente VARCHAR(255),
    not_eu VARCHAR(100),
    energia_bruta DECIMAL(10,2),
    ms_kg DECIMAL(10,2),
    proteina_bruta DECIMAL(10,2),
    fatores_emissao VARCHAR(20) CHECK (fatores_emissao IN ('CALCULADO', 'ESTIMADO')),
    
    -- Campos de auditoria
    comentarios TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Índices para performance
CREATE INDEX idx_insumos_rebanho_usuario ON insumos_rebanho (usuario_id);
CREATE INDEX idx_insumos_rebanho_tipo ON insumos_rebanho (tipo);
CREATE INDEX idx_insumos_rebanho_escopo ON insumos_rebanho (escopo);
CREATE INDEX idx_insumos_rebanho_ativo ON insumos_rebanho (ativo);
CREATE INDEX idx_insumos_rebanho_modulo ON insumos_rebanho (modulo);

-- Trigger para atualizar data_atualizacao automaticamente
CREATE OR REPLACE FUNCTION update_data_atualizacao_insumos_rebanho()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_data_atualizacao_insumos_rebanho
    BEFORE UPDATE ON insumos_rebanho
    FOR EACH ROW
    EXECUTE FUNCTION update_data_atualizacao_insumos_rebanho();

-- Foreign key constraint
ALTER TABLE insumos_rebanho 
ADD CONSTRAINT fk_insumos_rebanho_usuario 
FOREIGN KEY (usuario_id) REFERENCES usuario(id);