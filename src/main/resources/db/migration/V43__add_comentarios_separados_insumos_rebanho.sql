-- Migration para adicionar campos de comentários separados por escopo na tabela insumos_rebanho
-- Resolve o problema onde comentários do escopo 1 e escopo 3 eram sobrescritos

ALTER TABLE insumos_rebanho 
ADD COLUMN comentarios_escopo1 TEXT,
ADD COLUMN comentarios_escopo3 TEXT;

-- Migra dados existentes do campo comentarios para o campo apropriado baseado no escopo
UPDATE insumos_rebanho 
SET comentarios_escopo1 = comentarios 
WHERE escopo = 'ESCOPO_1' AND comentarios IS NOT NULL;

UPDATE insumos_rebanho 
SET comentarios_escopo3 = comentarios 
WHERE escopo IN ('ESCOPO_3_PRODUCAO', 'ESCOPO_3_TRANSPORTE') AND comentarios IS NOT NULL;

-- Comentário sobre a estratégia de migração:
-- O campo 'comentarios' original será mantido temporariamente para compatibilidade
-- e será removido em uma migration futura após validação completa