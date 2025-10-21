-- Migration V69: Modificar tabela bd_rebanho para usar referências às tabelas de categorias

-- Adicionar colunas de referência
ALTER TABLE bd_rebanho 
ADD COLUMN categoria_corte_id BIGINT,
ADD COLUMN categoria_leite_id BIGINT,
ADD COLUMN codigo_4cn_id BIGINT;

-- Criar foreign keys
ALTER TABLE bd_rebanho 
ADD CONSTRAINT fk_bd_rebanho_categoria_corte 
    FOREIGN KEY (categoria_corte_id) REFERENCES bd_categorias_corte(id),
ADD CONSTRAINT fk_bd_rebanho_categoria_leite 
    FOREIGN KEY (categoria_leite_id) REFERENCES bd_categorias_leite(id),
ADD CONSTRAINT fk_bd_rebanho_codigo_4cn 
    FOREIGN KEY (codigo_4cn_id) REFERENCES bd_codigos_4cn(id);

-- Atualizar registros existentes para referenciar as tabelas de categorias de corte
UPDATE bd_rebanho r
SET categoria_corte_id = (
    SELECT c.id 
    FROM bd_categorias_corte c 
    WHERE c.categoria = r.categoria AND c.idade = r.idade
)
WHERE r.tipo = 'Corte';

-- Atualizar registros existentes para referenciar as tabelas de categorias de leite
UPDATE bd_rebanho r
SET categoria_leite_id = (
    SELECT l.id 
    FROM bd_categorias_leite l 
    WHERE l.categoria = r.categoria AND l.idade = r.idade
)
WHERE r.tipo = 'Leite';

-- Atualizar registros existentes para referenciar os códigos 4CN
UPDATE bd_rebanho r
SET codigo_4cn_id = (
    SELECT cn.id 
    FROM bd_codigos_4cn cn 
    WHERE cn.categoria_calculadora = r.categoria
);

-- Remover colunas antigas que agora são referenciadas
ALTER TABLE bd_rebanho 
DROP COLUMN categoria,
DROP COLUMN idade,
DROP COLUMN codigo_4o_in;

-- Criar índices para as novas foreign keys
CREATE INDEX idx_bd_rebanho_categoria_corte ON bd_rebanho (categoria_corte_id);
CREATE INDEX idx_bd_rebanho_categoria_leite ON bd_rebanho (categoria_leite_id);
CREATE INDEX idx_bd_rebanho_codigo_4cn ON bd_rebanho (codigo_4cn_id);