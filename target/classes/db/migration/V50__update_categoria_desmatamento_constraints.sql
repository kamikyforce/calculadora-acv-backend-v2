-- V50__update_categoria_desmatamento_constraints.sql
UPDATE dados_desmatamento 
SET categoria_desmatamento = CASE 
    WHEN categoria_desmatamento = 'FLORESTA_PRIMARIA' THEN 'F'
    WHEN categoria_desmatamento = 'FLORESTA_SECUNDARIA' THEN 'F'
    WHEN categoria_desmatamento = 'VEGETACAO_NATIVA' THEN 'OFL'
    WHEN categoria_desmatamento = 'AREA_ANTROPIZADA' THEN 'G'
    ELSE categoria_desmatamento
END;

UPDATE vegetacao_categorias 
SET categoria = CASE 
    WHEN categoria = 'FLORESTA_PRIMARIA' THEN 'F'
    WHEN categoria = 'FLORESTA_SECUNDARIA' THEN 'F'
    WHEN categoria = 'VEGETACAO_NATIVA' THEN 'OFL'
    WHEN categoria = 'AREA_ANTROPIZADA' THEN 'G'
    ELSE categoria
END;

-- Drop old constraints
ALTER TABLE dados_desmatamento DROP CONSTRAINT IF EXISTS dados_desmatamento_categoria_desmatamento_check;
ALTER TABLE vegetacao_categorias DROP CONSTRAINT IF EXISTS vegetacao_categorias_categoria_check;

-- Add new constraints with correct values
ALTER TABLE dados_desmatamento 
ADD CONSTRAINT dados_desmatamento_categoria_desmatamento_check 
CHECK (categoria_desmatamento IN ('O', 'F', 'OFL', 'G'));

ALTER TABLE vegetacao_categorias 
ADD CONSTRAINT vegetacao_categorias_categoria_check 
CHECK (categoria IN ('O', 'F', 'OFL', 'G'));