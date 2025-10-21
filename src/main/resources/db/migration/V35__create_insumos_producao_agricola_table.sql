-- Criação da tabela insumos_producao_agricola com todos os campos necessários
CREATE TABLE insumos_producao_agricola (
    -- Campos de controle
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    
    -- ESCOPO 1 - Classificação
    classe VARCHAR(100),
    especificacao VARCHAR(255),
    
    -- ESCOPO 1 - Teor de macronutrientes
    teor_nitrogenio DECIMAL(5,2),
    teor_fosforo DECIMAL(5,2),
    teor_potassio DECIMAL(5,2),
    
    -- ESCOPO 1 - Fator de conversão
    fator_conversao_valor DECIMAL(15,6),
    fator_conversao_unidade VARCHAR(50),
    
    -- ESCOPO 1 - Quantidade e unidade de referência
    quantidade DECIMAL(15,6),
    unidade_referencia VARCHAR(50),
    
    -- ESCOPO 1 - Fatores de emissão
    fe_co2_biogenico DECIMAL(15,6),
    ref_fe_co2_biogenico VARCHAR(500),
    fe_co2 DECIMAL(15,6),
    ref_fe_co2 VARCHAR(500),
    fe_ch4 DECIMAL(15,6),
    ref_fe_ch4 VARCHAR(500),
    fe_n2o_direto DECIMAL(15,6),
    ref_fe_n2o_direto VARCHAR(500),
    frac_n2o_volatilizacao DECIMAL(15,6),
    ref_frac_n2o_volatilizacao VARCHAR(500),
    frac_n2o_lixiviacao DECIMAL(15,6),
    ref_frac_n2o_lixiviacao VARCHAR(500),
    fe_n2o_composto DECIMAL(15,6),
    ref_fe_n2o_composto VARCHAR(500),
    fe_co DECIMAL(15,6),
    ref_fe_co VARCHAR(500),
    fe_nox DECIMAL(15,6),
    ref_fe_nox VARCHAR(500),
    
    -- ESCOPO 3 - Identificação e classificação
    grupo_ingrediente_alimentar VARCHAR(100),
    nome_produto VARCHAR(200),
    tipo_produto VARCHAR(100),
    
    -- ESCOPO 3 - Quantidade e unidade de referência
    quantidade_produto_referencia DECIMAL(15,6),
    unidade_produto_referencia VARCHAR(50),
    
    -- ESCOPO 3 - Quantidade e unidade
    quantidade_produto DECIMAL(15,6),
    
    -- ESCOPO 3 - Valores de emissões (GEE)
    gee_total DECIMAL(15,6),
    gwp_100_total DECIMAL(15,6),
    gwp_100_fossil DECIMAL(15,6),
    gwp_100_biogenico DECIMAL(15,6),
    gwp_100_transformacao_uso_solo DECIMAL(15,6),
    dioxido_carbono_fossil DECIMAL(15,6),
    dioxido_carbono_metano_transformacao DECIMAL(15,6),
    metano_fossil DECIMAL(15,6),
    metano_biogenico DECIMAL(15,6),
    oxido_nitroso DECIMAL(15,6),
    outras_substancias DECIMAL(15,6),
    co2_fossil_escopo3 DECIMAL(15,6),
    co2_ch4_transformacao DECIMAL(15,6),
    ch4_fossil_escopo3 DECIMAL(15,6),
    ch4_biogenico_escopo3 DECIMAL(15,6),
    n2o_escopo3 DECIMAL(15,6),
    
    -- ESCOPO 3 - Observações
    comentarios TEXT,
    
    -- Campos de auditoria
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT true
);

-- Comentários da tabela
COMMENT ON TABLE insumos_producao_agricola IS 'Tabela para armazenar dados de insumos de produção agrícola com campos de Escopo 1 e Escopo 3';

-- Comentários dos campos principais
COMMENT ON COLUMN insumos_producao_agricola.usuario_id IS 'ID do usuário proprietário do registro';
COMMENT ON COLUMN insumos_producao_agricola.classe IS 'Classificação do insumo (Escopo 1)';
COMMENT ON COLUMN insumos_producao_agricola.nome_produto IS 'Nome do produto (Escopo 3)';
COMMENT ON COLUMN insumos_producao_agricola.gee_total IS 'Total de gases de efeito estufa';
COMMENT ON COLUMN insumos_producao_agricola.gwp_100_total IS 'Potencial de aquecimento global total em 100 anos';
COMMENT ON COLUMN insumos_producao_agricola.ativo IS 'Indica se o registro está ativo';