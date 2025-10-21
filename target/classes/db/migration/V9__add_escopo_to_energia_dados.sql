-- Adicionar campo escopo na tabela energia_dados
ALTER TABLE energia_dados 
ADD COLUMN escopo VARCHAR(50);

-- Criar índice para o campo escopo
CREATE INDEX idx_energia_dados_escopo ON energia_dados (escopo);

-- Criar índice composto para consultas por usuário e escopo
CREATE INDEX idx_energia_dados_usuario_escopo ON energia_dados (usuario_id, escopo);

-- Atualizar dados existentes com escopo padrão (opcional)
UPDATE energia_dados 
SET escopo = 'escopo2' 
WHERE tipo_energia IN ('ELETRICA', 'TERMICA', 'RENOVAVEL') AND escopo IS NULL;

UPDATE energia_dados 
SET escopo = 'escopo1' 
WHERE tipo_energia IN ('COMBUSTIVEL') AND escopo IS NULL;