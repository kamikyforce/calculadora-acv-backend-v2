-- V64__create_calculos_registrados_table.sql
-- Migration para criar a tabela de cálculos registrados

-- Criar tabela calculos_registrados
CREATE TABLE calculos_registrados (
    id BIGSERIAL PRIMARY KEY,
    car VARCHAR(50) NOT NULL,
    fazenda VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    municipio VARCHAR(100),
    tamanho DECIMAL(15,3),
    ano INTEGER NOT NULL,
    versao VARCHAR(10) NOT NULL,
    status VARCHAR(50) NOT NULL,
    emissao_total DECIMAL(15,3),
    certificacao VARCHAR(50) NOT NULL,
    usuario_id BIGINT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT true
);

-- Criar índices para otimizar consultas
CREATE INDEX idx_calculos_registrados_car ON calculos_registrados (car);
CREATE INDEX idx_calculos_registrados_fazenda ON calculos_registrados (fazenda);
CREATE INDEX idx_calculos_registrados_status ON calculos_registrados (status);
CREATE INDEX idx_calculos_registrados_certificacao ON calculos_registrados (certificacao);
CREATE INDEX idx_calculos_registrados_estado ON calculos_registrados (estado);
CREATE INDEX idx_calculos_registrados_municipio ON calculos_registrados (municipio);
CREATE INDEX idx_calculos_registrados_ano ON calculos_registrados (ano);
CREATE INDEX idx_calculos_registrados_usuario_id ON calculos_registrados (usuario_id);
CREATE INDEX idx_calculos_registrados_ativo ON calculos_registrados (ativo);
CREATE INDEX idx_calculos_registrados_data_atualizacao ON calculos_registrados (data_atualizacao DESC);

-- Criar índice composto para consultas com filtros múltiplos
CREATE INDEX idx_calculos_registrados_filtros ON calculos_registrados (ativo, status, certificacao, estado, ano);

-- Adicionar constraint de chave estrangeira para usuario_id (se a tabela usuario existir)
-- ALTER TABLE calculos_registrados ADD CONSTRAINT fk_calculos_registrados_usuario 
-- FOREIGN KEY (usuario_id) REFERENCES usuario(id);

-- Comentários na tabela e colunas
COMMENT ON TABLE calculos_registrados IS 'Tabela para armazenar os cálculos de ACV registrados pelos usuários';
COMMENT ON COLUMN calculos_registrados.car IS 'Código de Área Rural (CAR)';
COMMENT ON COLUMN calculos_registrados.fazenda IS 'Nome da fazenda';
COMMENT ON COLUMN calculos_registrados.tipo IS 'Tipo de produção (Leite, Corte, etc.)';
COMMENT ON COLUMN calculos_registrados.estado IS 'Estado da fazenda (sigla)';
COMMENT ON COLUMN calculos_registrados.municipio IS 'Município da fazenda';
COMMENT ON COLUMN calculos_registrados.tamanho IS 'Tamanho da propriedade em hectares';
COMMENT ON COLUMN calculos_registrados.ano IS 'Ano de referência do cálculo';
COMMENT ON COLUMN calculos_registrados.versao IS 'Versão do cálculo';
COMMENT ON COLUMN calculos_registrados.status IS 'Status do cálculo (CONCLUIDO, RASCUNHO, EM_CERTIFICACAO)';
COMMENT ON COLUMN calculos_registrados.emissao_total IS 'Total de emissões calculadas';
COMMENT ON COLUMN calculos_registrados.certificacao IS 'Status da certificação (CERTIFICADO, NAO_CERTIFICADO, NAO_INICIADO, EM_CERTIFICACAO)';
COMMENT ON COLUMN calculos_registrados.usuario_id IS 'ID do usuário que criou o cálculo';
COMMENT ON COLUMN calculos_registrados.data_criacao IS 'Data de criação do registro';
COMMENT ON COLUMN calculos_registrados.data_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN calculos_registrados.ativo IS 'Indica se o registro está ativo (soft delete)';

-- Log da migration
DO $$
BEGIN
    RAISE NOTICE 'Migration V64 concluída: Tabela calculos_registrados criada com sucesso!';
END $$;