INSERT INTO perfil (nome) VALUES
('VISUALIZADOR_CERTIFICADORAS'),
('ADMINISTRADOR_INDUSTRIAS');

-- Atualizar IDs para corresponder ao frontend
-- Como já temos ADMINISTRADOR_INDUSTRIAS duplicado, vamos remover o antigo
DELETE FROM perfil WHERE nome = 'VISUALIZADOR_INDUSTRIAS';

-- Inserir o perfil correto
INSERT INTO perfil (nome) VALUES ('VISUALIZADOR_INDUSTRIAS');