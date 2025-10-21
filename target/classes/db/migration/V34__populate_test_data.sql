-- V34__populate_test_data.sql
-- Migration para popular dados de teste nas tabelas de certificadoras, indústrias, usuários e administradores

-- Inserir usuários (todos os usuários necessários)
INSERT INTO usuario (id, nome, email, cpf, data_cadastro, ativo, origem_autenticacao, tipo) VALUES
(1, 'Administrador', 'admin@bndes.gov.br', '00000000000', '2025-09-02 16:24:38.998668', true, 'GOVBR', 'ADMINISTRADOR'),
(2, 'Admin', '01234567890@temp.gov.br', '01234567890', '2025-09-03 08:06:06.047467', true, 'GOVBR', 'ADMINISTRADOR'),
(3, 'Curador', '01234567891@temp.gov.br', '01234567891', '2025-09-03 08:06:55.001860', true, 'GOVBR', 'ADMINISTRADOR'),
(4, 'Admin Cert', '01234567892@temp.gov.br', '01234567892', '2025-09-03 08:06:55.001860', true, 'GOVBR', 'ADMINISTRADOR'),
(5, 'Visu Cert', '01234567893@temp.gov.br', '01234567893', '2025-09-03 08:07:16.128081', true, 'GOVBR', 'ADMINISTRADOR'),
(6, 'Adm Ind', '01234567894@temp.gov.br', '01234567894', '2025-09-03 08:07:52.355498', true, 'GOVBR', 'ADMINISTRADOR'),
(7, 'Visu Ind', '01234567895@temp.gov.br', '01234567895', '2025-09-03 08:08:28.058506', true, 'GOVBR', 'ADMINISTRADOR'),
(8, 'Visu Emissões', '01234567896@temp.gov.br', '01234567896', '2025-09-03 08:08:57.111544', true, 'GOVBR', 'ADMINISTRADOR')
ON CONFLICT (id) DO NOTHING;

-- Inserir certificadoras
INSERT INTO certificadora (id, nome, cnpj, data_cadastro, inventarios_tratados, estado, tipo, ativo) VALUES
(1, 'Cert 1', '99988888888880', '2025-09-03 00:00:00', 0, 'RJ', 'AMBOS', true),
(2, 'Cert 2', '99988888888881', '2025-09-03 00:00:00', 0, 'RS', 'LATICINIO', true)
ON CONFLICT (id) DO NOTHING;

-- Inserir indústrias
INSERT INTO industria (id, nome, cnpj, data_cadastro, inventarios_tratados, estado, tipo, ativo) VALUES
(1, 'Ind 1', '99988888888883', '2025-09-03 00:00:00', 0, 'MA', 'INDUSTRIA', true),
(2, 'Ind 2', '99988888888884', '2025-09-03 00:00:00', 0, 'SP', 'INDUSTRIA', true)
ON CONFLICT (id) DO NOTHING;

-- Inserir administradores (usando os perfis já existentes)
INSERT INTO administrador (id, usuario_id, orgao, perfil_id) VALUES
(1, 1, 'BNDES', (SELECT id FROM perfil WHERE nome = 'ADMINISTRADOR_SISTEMA')),
(2, 2, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'ADMINISTRADOR_SISTEMA')),
(3, 3, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'CURADOR')),
(4, 4, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'ADMINISTRADOR_CERTIFICADORAS')),
(5, 5, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'VISUALIZADOR_CERTIFICADORAS')),
(6, 6, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'ADMINISTRADOR_INDUSTRIAS')),
(7, 7, 'SERPRO', (SELECT id FROM perfil WHERE nome = 'VISUALIZADOR_INDUSTRIAS')),
(8, 8, 'SYSOPS', (SELECT id FROM perfil WHERE nome = 'VISUALIZADOR_EMISSOES'))
ON CONFLICT (id) DO NOTHING;

-- Inserir relacionamentos usuário-certificadora
INSERT INTO usuario_certificadora (usuario_id, certificadora_id) VALUES
(4, 1), -- Admin Cert -> Cert 1
(4, 2), -- Admin Cert -> Cert 2
(5, 1), -- Visu Cert -> Cert 1
(5, 2)  -- Visu Cert -> Cert 2
ON CONFLICT (usuario_id, certificadora_id) DO NOTHING;

-- Inserir relacionamentos usuário-indústria
INSERT INTO usuario_industria (usuario_id, industria_id) VALUES
(6, 1), -- Adm Ind -> Ind 1
(6, 2), -- Adm Ind -> Ind 2
(7, 1), -- Visu Ind -> Ind 1
(7, 2)  -- Visu Ind -> Ind 2
ON CONFLICT (usuario_id, industria_id) DO NOTHING;

-- Atualizar sequências para evitar conflitos futuros
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));
SELECT setval('certificadora_id_seq', (SELECT MAX(id) FROM certificadora));
SELECT setval('industria_id_seq', (SELECT MAX(id) FROM industria));
SELECT setval('administrador_id_seq', (SELECT MAX(id) FROM administrador));
SELECT setval('usuario_certificadora_id_seq', (SELECT MAX(id) FROM usuario_certificadora));
SELECT setval('usuario_industria_id_seq', (SELECT MAX(id) FROM usuario_industria));