-- Add unique constraint to prevent duplicate combustiveis by name and scope
ALTER TABLE combustiveis 
ADD CONSTRAINT uk_combustivel_nome_escopo 
UNIQUE (nome, escopo, usuario_id);

-- Add index for better performance
CREATE INDEX IF NOT EXISTS idx_combustivel_nome_escopo_usuario 
ON combustiveis (nome, escopo, usuario_id);