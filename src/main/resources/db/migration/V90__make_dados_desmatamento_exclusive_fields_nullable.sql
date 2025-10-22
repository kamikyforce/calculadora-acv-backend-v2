-- Tornar campos exclusivos de DESMATAMENTO opcionais para suportar replicação entre escopos
ALTER TABLE dados_desmatamento
    ALTER COLUMN nome_fitofisionomia DROP NOT NULL,
    ALTER COLUMN sigla_fitofisionomia DROP NOT NULL,
    ALTER COLUMN categoria_desmatamento DROP NOT NULL;

COMMENT ON COLUMN dados_desmatamento.nome_fitofisionomia IS 'Nome da fitofisionomia (opcional, exclusivo por escopo)';
COMMENT ON COLUMN dados_desmatamento.sigla_fitofisionomia IS 'Sigla da fitofisionomia (opcional, exclusivo por escopo)';
COMMENT ON COLUMN dados_desmatamento.categoria_desmatamento IS 'Categoria de desmatamento (opcional, exclusivo por escopo)';