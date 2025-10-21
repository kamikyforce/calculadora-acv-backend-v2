-- V48__create_mut_tables.sql

-- Tabela principal de fatores MUT
CREATE TABLE fator_mut (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo_mudanca VARCHAR(50) NOT NULL CHECK (tipo_mudanca IN ('DESMATAMENTO', 'VEGETACAO', 'SOLO')),
    escopo VARCHAR(50) NOT NULL CHECK (escopo IN ('ESCOPO1', 'ESCOPO2', 'ESCOPO3', 'ESCOPO3_PRODUCAO', 'ESCOPO3_TRANSPORTE')),
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    usuario_id BIGINT REFERENCES usuario(id),
    CONSTRAINT uk_fator_mut_nome_tipo_escopo_ativo UNIQUE (nome, tipo_mudanca, escopo)
);

-- Tabela para dados específicos de desmatamento
CREATE TABLE dados_desmatamento (
    id BIGSERIAL PRIMARY KEY,
    fator_mut_id BIGINT NOT NULL REFERENCES fator_mut(id) ON DELETE CASCADE,
    bioma VARCHAR(50) NOT NULL CHECK (bioma IN ('AMAZONIA', 'CERRADO', 'CAATINGA', 'MATA_ATLANTICA', 'PAMPA', 'PANTANAL')),
    valor_unico BOOLEAN NOT NULL,
    nome_fitofisionomia VARCHAR(255) NOT NULL,
    sigla_fitofisionomia VARCHAR(50) NOT NULL,
    categoria_desmatamento VARCHAR(50) NOT NULL CHECK (categoria_desmatamento IN ('FLORESTA_PRIMARIA', 'FLORESTA_SECUNDARIA', 'VEGETACAO_NATIVA', 'AREA_ANTROPIZADA')),
    estoque_carbono DECIMAL(15,6)
);

-- Tabela para UFs relacionadas ao desmatamento
CREATE TABLE desmatamento_ufs (
    dados_desmatamento_id BIGINT NOT NULL REFERENCES dados_desmatamento(id) ON DELETE CASCADE,
    uf VARCHAR(2) NOT NULL CHECK (uf IN ('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO')),
    PRIMARY KEY (dados_desmatamento_id, uf)
);

-- Tabela para dados específicos de vegetação
CREATE TABLE dados_vegetacao (
    id BIGSERIAL PRIMARY KEY,
    fator_mut_id BIGINT NOT NULL REFERENCES fator_mut(id) ON DELETE CASCADE,
    bioma VARCHAR(50) NOT NULL CHECK (bioma IN ('AMAZONIA', 'CERRADO', 'CAATINGA', 'MATA_ATLANTICA', 'PAMPA', 'PANTANAL')),
    parametro VARCHAR(500),
    fator_ch4 DECIMAL(15,6),
    fator_co2 DECIMAL(15,6),
    fator_n2o DECIMAL(15,6),
    valor_amazonia DECIMAL(15,6),
    valor_caatinga DECIMAL(15,6),
    valor_cerrado DECIMAL(15,6),
    valor_mata_atlantica DECIMAL(15,6),
    valor_pampa DECIMAL(15,6),
    valor_pantanal DECIMAL(15,6)
);

-- Tabela para categorias de fitofisionomia da vegetação
CREATE TABLE vegetacao_categorias (
    dados_vegetacao_id BIGINT NOT NULL REFERENCES dados_vegetacao(id) ON DELETE CASCADE,
    categoria VARCHAR(50) NOT NULL CHECK (categoria IN ('FLORESTA_PRIMARIA', 'FLORESTA_SECUNDARIA', 'VEGETACAO_NATIVA', 'AREA_ANTROPIZADA')),
    PRIMARY KEY (dados_vegetacao_id, categoria)
);

-- Tabela para dados específicos de solo
CREATE TABLE dados_solo (
    id BIGSERIAL PRIMARY KEY,
    fator_mut_id BIGINT NOT NULL REFERENCES fator_mut(id) ON DELETE CASCADE,
    tipo_fator_solo VARCHAR(50) NOT NULL CHECK (tipo_fator_solo IN ('ALTO_CARBONO', 'BAIXO_CARBONO', 'MINERAL', 'ORGANICO')),
    valor_fator DECIMAL(15,6) NOT NULL,
    descricao VARCHAR(500)
);

-- Índices para melhorar performance
CREATE INDEX idx_fator_mut_tipo_escopo ON fator_mut(tipo_mudanca, escopo);
CREATE INDEX idx_fator_mut_ativo ON fator_mut(ativo);
CREATE INDEX idx_fator_mut_usuario ON fator_mut(usuario_id);
CREATE INDEX idx_fator_mut_data_criacao ON fator_mut(data_criacao);
CREATE INDEX idx_dados_desmatamento_bioma ON dados_desmatamento(bioma);
CREATE INDEX idx_dados_desmatamento_fator ON dados_desmatamento(fator_mut_id);
CREATE INDEX idx_dados_vegetacao_fator ON dados_vegetacao(fator_mut_id);
CREATE INDEX idx_dados_solo_fator ON dados_solo(fator_mut_id);

-- Comentários nas tabelas
COMMENT ON TABLE fator_mut IS 'Tabela principal para fatores de Mudança do Uso da Terra (MUT)';
COMMENT ON TABLE dados_desmatamento IS 'Dados específicos para fatores MUT do tipo Desmatamento';
COMMENT ON TABLE dados_vegetacao IS 'Dados específicos para fatores MUT do tipo Vegetação';
COMMENT ON TABLE dados_solo IS 'Dados específicos para fatores MUT do tipo Solo';
COMMENT ON TABLE desmatamento_ufs IS 'Unidades Federativas relacionadas aos dados de desmatamento';
COMMENT ON TABLE vegetacao_categorias IS 'Categorias de fitofisionomia para dados de vegetação';