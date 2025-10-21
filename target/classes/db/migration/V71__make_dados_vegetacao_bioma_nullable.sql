-- V70__make_dados_vegetacao_bioma_nullable.sql
-- Tornar o campo bioma opcional na tabela dados_vegetacao
-- Resolve o erro 409 Conflict quando bioma é null no request

-- Remover a restrição NOT NULL do campo bioma
ALTER TABLE dados_vegetacao 
ALTER COLUMN bioma DROP NOT NULL;

-- Atualizar o comentário da coluna para refletir que agora é opcional
COMMENT ON COLUMN dados_vegetacao.bioma IS 'Bioma relacionado aos dados de vegetação (opcional)';

-- Verificar se a alteração foi aplicada corretamente
-- (Esta query é apenas informativa e será executada durante a migração)
DO $$
BEGIN
    RAISE NOTICE 'Campo bioma na tabela dados_vegetacao agora permite valores NULL';
END $$;