-- Migration V99: Alterar precisão do campo tamanho para suportar 7 casas decimais
-- Altera o campo tamanho de DECIMAL(15,3) para DECIMAL(15,7)

-- Alterar a coluna tamanho na tabela calculos_registrados
ALTER TABLE calculos_registrados 
ALTER COLUMN tamanho TYPE DECIMAL(15,7);