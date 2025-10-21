-- V5__insert_default_admin.sql
-- Insere usuário e administrador padrão para desenvolvimento

-- Inserir usuário padrão se não existir
INSERT INTO usuario (nome, email, cpf, tipo, ativo, origem_autenticacao)
SELECT 'Administrador', 'admin@bndes.gov.br', '00000000000', 'ADMINISTRADOR', true, 'GOVBR'
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE cpf = '00000000000'
);

-- Inserir administrador padrão se não existir
INSERT INTO administrador (usuario_id, orgao, perfil_id)
SELECT u.id, 'BNDES', 1
FROM usuario u
WHERE u.cpf = '00000000000'
AND NOT EXISTS (
    SELECT 1 FROM administrador a WHERE a.usuario_id = u.id
);