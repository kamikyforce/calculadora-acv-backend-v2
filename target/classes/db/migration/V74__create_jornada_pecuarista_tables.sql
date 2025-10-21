-- Migration V74: Criar tabelas para jornada pecuarista

-- Tabela principal do inventário da jornada pecuarista
CREATE TABLE inventario_jornada (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    tipo_rebanho VARCHAR(20) NOT NULL CHECK (tipo_rebanho IN ('CORTE', 'LEITE')),
    status VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO' CHECK (status IN ('RASCUNHO', 'EM_ANDAMENTO', 'CONCLUIDO')),
    fase_atual INTEGER NOT NULL DEFAULT 1 CHECK (fase_atual BETWEEN 1 AND 4),
    fase_rebanho_concluida BOOLEAN NOT NULL DEFAULT FALSE,
    fase_producao_agricola_concluida BOOLEAN NOT NULL DEFAULT FALSE,
    fase_mut_concluida BOOLEAN NOT NULL DEFAULT FALSE,
    fase_energia_concluida BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabela de lotes do rebanho
CREATE TABLE lote_rebanho (
    id BIGSERIAL PRIMARY KEY,
    inventario_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ordem INTEGER NOT NULL DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lote_inventario FOREIGN KEY (inventario_id) REFERENCES inventario_jornada(id) ON DELETE CASCADE,
    CONSTRAINT uk_lote_nome_inventario UNIQUE (inventario_id, nome)
);

-- Tabela de categorias por lote (informações gerais)
CREATE TABLE categoria_lote (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    categoria_corte_id BIGINT,
    categoria_leite_id BIGINT,
    animais_fazenda INTEGER NOT NULL DEFAULT 0,
    peso_medio_vivo DECIMAL(10,6) NOT NULL DEFAULT 0,
    animais_comprados INTEGER DEFAULT 0,
    peso_medio_comprados DECIMAL(10,6) DEFAULT 0,
    animais_vendidos INTEGER DEFAULT 0,
    peso_medio_vendidos DECIMAL(10,6) DEFAULT 0,
    permanencia_meses DECIMAL(10,6) DEFAULT 0,
    idade_desmame DECIMAL(10,6) DEFAULT 0,
    femeas_prenhas_percentual DECIMAL(5,2) DEFAULT 0,
    producao_leite_ano DECIMAL(10,2) DEFAULT 0,
    teor_gordura_leite DECIMAL(5,2) DEFAULT 0,
    teor_proteina_leite DECIMAL(5,2) DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_categoria_lote FOREIGN KEY (lote_id) REFERENCES lote_rebanho(id) ON DELETE CASCADE,
    CONSTRAINT fk_categoria_corte FOREIGN KEY (categoria_corte_id) REFERENCES bd_categorias_corte(id),
    CONSTRAINT fk_categoria_leite FOREIGN KEY (categoria_leite_id) REFERENCES bd_categorias_leite(id),
    CONSTRAINT chk_categoria_tipo CHECK (
        (categoria_corte_id IS NOT NULL AND categoria_leite_id IS NULL) OR
        (categoria_corte_id IS NULL AND categoria_leite_id IS NOT NULL)
    )
);

-- Tabela de CARs associados às categorias
CREATE TABLE car_categoria (
    id BIGSERIAL PRIMARY KEY,
    categoria_lote_id BIGINT NOT NULL,
    numero_car VARCHAR(50) NOT NULL,
    animais_comprados INTEGER NOT NULL DEFAULT 0,
    ordem INTEGER NOT NULL DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_car_categoria FOREIGN KEY (categoria_lote_id) REFERENCES categoria_lote(id) ON DELETE CASCADE
);

-- Tabela para dados de nutrição animal por lote
CREATE TABLE nutricao_animal_lote (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    inserir_dados_dieta BOOLEAN NOT NULL DEFAULT FALSE,
    sistema_producao VARCHAR(20) CHECK (sistema_producao IN ('PASTO', 'SEMI_CONFINADO', 'CONFINADO')),
    tempo_pasto_horas_dia DECIMAL(5,2) DEFAULT 0,
    tempo_pasto_dias_ano INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_nutricao_lote FOREIGN KEY (lote_id) REFERENCES lote_rebanho(id) ON DELETE CASCADE,
    CONSTRAINT uk_nutricao_lote UNIQUE (lote_id)
);

-- Tabela para ingredientes da dieta
CREATE TABLE ingrediente_dieta_lote (
    id BIGSERIAL PRIMARY KEY,
    nutricao_lote_id BIGINT NOT NULL,
    nome_ingrediente VARCHAR(255) NOT NULL,
    percentual DECIMAL(5,2) NOT NULL DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ingrediente_nutricao FOREIGN KEY (nutricao_lote_id) REFERENCES nutricao_animal_lote(id) ON DELETE CASCADE
);

-- Tabela para concentrados da dieta
CREATE TABLE concentrado_dieta_lote (
    id BIGSERIAL PRIMARY KEY,
    nutricao_lote_id BIGINT NOT NULL,
    nome_concentrado VARCHAR(255) NOT NULL,
    percentual DECIMAL(5,2) NOT NULL DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_concentrado_nutricao FOREIGN KEY (nutricao_lote_id) REFERENCES nutricao_animal_lote(id) ON DELETE CASCADE
);

-- Tabela para aditivos da dieta
CREATE TABLE aditivo_dieta_lote (
    id BIGSERIAL PRIMARY KEY,
    nutricao_lote_id BIGINT NOT NULL,
    nome_aditivo VARCHAR(255) NOT NULL,
    percentual DECIMAL(5,2) NOT NULL DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_aditivo_nutricao FOREIGN KEY (nutricao_lote_id) REFERENCES nutricao_animal_lote(id) ON DELETE CASCADE
);

-- Tabela para manejo de dejetos por lote
CREATE TABLE manejo_dejetos_lote (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    categoria_animal VARCHAR(100) NOT NULL,
    tipo_manejo VARCHAR(50) NOT NULL,
    percentual_rebanho DECIMAL(5,2) NOT NULL DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_manejo_lote FOREIGN KEY (lote_id) REFERENCES lote_rebanho(id) ON DELETE CASCADE
);

-- Índices para performance
CREATE INDEX idx_inventario_usuario ON inventario_jornada (usuario_id);
CREATE INDEX idx_inventario_status ON inventario_jornada (status);
CREATE INDEX idx_lote_inventario ON lote_rebanho (inventario_id);
CREATE INDEX idx_categoria_lote ON categoria_lote (lote_id);
CREATE INDEX idx_car_categoria ON car_categoria (categoria_lote_id);
CREATE INDEX idx_nutricao_lote ON nutricao_animal_lote (lote_id);
CREATE INDEX idx_manejo_lote ON manejo_dejetos_lote (lote_id);