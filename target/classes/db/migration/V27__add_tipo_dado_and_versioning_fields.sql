-- Migration V27: Adicionar campos para Dado Consolidado do Ano e Dado Mensal
-- Implementar lógica de versionamento e auditoria

-- Adicionar campos para controle de tipo de dado e versionamento
ALTER TABLE energia_dados 
ADD COLUMN IF NOT EXISTS tipo_dado VARCHAR(20) DEFAULT 'CONSOLIDADO_ANUAL' CHECK (tipo_dado IN ('CONSOLIDADO_ANUAL', 'MENSAL')),
ADD COLUMN IF NOT EXISTS versao INTEGER DEFAULT 1,
ADD COLUMN IF NOT EXISTS dados_mensais_json JSONB,
ADD COLUMN IF NOT EXISTS media_anual_calculada DECIMAL(15,3),
ADD COLUMN IF NOT EXISTS meses_preenchidos INTEGER DEFAULT 0,
ADD COLUMN IF NOT EXISTS status_calculo VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status_calculo IN ('PENDENTE', 'COMPLETO', 'PARCIAL')),
ADD COLUMN IF NOT EXISTS usuario_ultima_edicao BIGINT,
ADD COLUMN IF NOT EXISTS observacoes_auditoria TEXT;

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_energia_dados_tipo_dado ON energia_dados (tipo_dado);
CREATE INDEX IF NOT EXISTS idx_energia_dados_versao ON energia_dados (versao);
CREATE INDEX IF NOT EXISTS idx_energia_dados_status_calculo ON energia_dados (status_calculo);
CREATE INDEX IF NOT EXISTS idx_energia_dados_usuario_edicao ON energia_dados (usuario_ultima_edicao);

-- Criar tabela de histórico para auditoria
CREATE TABLE IF NOT EXISTS energia_dados_historico (
    id BIGSERIAL PRIMARY KEY,
    energia_dados_id BIGINT NOT NULL,
    versao_anterior INTEGER,
    versao_nova INTEGER,
    tipo_alteracao VARCHAR(50) NOT NULL, -- 'CRIACAO', 'EDICAO', 'CONVERSAO_MENSAL_ANUAL', 'CONVERSAO_ANUAL_MENSAL'
    dados_anteriores JSONB,
    dados_novos JSONB,
    usuario_id BIGINT NOT NULL,
    data_alteracao TIMESTAMP DEFAULT NOW(),
    observacoes TEXT,
    FOREIGN KEY (energia_dados_id) REFERENCES energia_dados(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Criar índices para tabela de histórico
CREATE INDEX IF NOT EXISTS idx_energia_historico_energia_id ON energia_dados_historico (energia_dados_id);
CREATE INDEX IF NOT EXISTS idx_energia_historico_usuario ON energia_dados_historico (usuario_id);
CREATE INDEX IF NOT EXISTS idx_energia_historico_data ON energia_dados_historico (data_alteracao);
CREATE INDEX IF NOT EXISTS idx_energia_historico_tipo ON energia_dados_historico (tipo_alteracao);

-- Atualizar registros existentes para tipo CONSOLIDADO_ANUAL
UPDATE energia_dados 
SET tipo_dado = 'CONSOLIDADO_ANUAL',
    status_calculo = 'COMPLETO'
WHERE tipo_dado IS NULL;

-- Função para registrar alterações no histórico
CREATE OR REPLACE FUNCTION registrar_alteracao_energia_dados()
RETURNS TRIGGER AS $$
BEGIN
    -- Registrar no histórico apenas se houve mudanças significativas
    IF TG_OP = 'UPDATE' AND (
        OLD.consumo_anual IS DISTINCT FROM NEW.consumo_anual OR
        OLD.dados_mensais_json IS DISTINCT FROM NEW.dados_mensais_json OR
        OLD.media_anual_calculada IS DISTINCT FROM NEW.media_anual_calculada OR
        OLD.tipo_dado IS DISTINCT FROM NEW.tipo_dado
    ) THEN
        INSERT INTO energia_dados_historico (
            energia_dados_id, versao_anterior, versao_nova, tipo_alteracao,
            dados_anteriores, dados_novos, usuario_id, observacoes
        ) VALUES (
            NEW.id, OLD.versao, NEW.versao, 'EDICAO',
            row_to_json(OLD), row_to_json(NEW), NEW.usuario_ultima_edicao,
            NEW.observacoes_auditoria
        );
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO energia_dados_historico (
            energia_dados_id, versao_anterior, versao_nova, tipo_alteracao,
            dados_novos, usuario_id, observacoes
        ) VALUES (
            NEW.id, 0, NEW.versao, 'CRIACAO',
            row_to_json(NEW), NEW.usuario_id, 'Registro criado'
        );
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Criar trigger para auditoria
DROP TRIGGER IF EXISTS trigger_auditoria_energia_dados ON energia_dados;
CREATE TRIGGER trigger_auditoria_energia_dados
    AFTER INSERT OR UPDATE ON energia_dados
    FOR EACH ROW
    EXECUTE FUNCTION registrar_alteracao_energia_dados();

-- Comentários para documentação
COMMENT ON COLUMN energia_dados.tipo_dado IS 'Tipo de dado: CONSOLIDADO_ANUAL (valor já calculado pelo gov) ou MENSAL (dados mensais para cálculo)';
COMMENT ON COLUMN energia_dados.versao IS 'Versão do registro para controle de auditoria (v1, v2, etc.)';
COMMENT ON COLUMN energia_dados.dados_mensais_json IS 'Dados mensais em formato JSON quando tipo_dado = MENSAL';
COMMENT ON COLUMN energia_dados.media_anual_calculada IS 'Média anual calculada automaticamente dos dados mensais';
COMMENT ON COLUMN energia_dados.meses_preenchidos IS 'Quantidade de meses preenchidos (0-12)';
COMMENT ON COLUMN energia_dados.status_calculo IS 'Status do cálculo: PENDENTE, PARCIAL (< 12 meses), COMPLETO (12 meses)';