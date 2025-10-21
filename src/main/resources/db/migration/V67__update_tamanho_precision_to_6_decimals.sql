-- Migration V67: Alterar precisão do campo tamanho para 6 casas decimais
-- Altera o campo tamanho de DECIMAL(15,7) para DECIMAL(15,6)

-- Alterar a coluna tamanho na tabela calculos_registrados
ALTER TABLE calculos_registrados 
ALTER COLUMN tamanho TYPE DECIMAL(15,6);