ALTER TABLE energia_dados 
ADD COLUMN fator_medio_anual DECIMAL(10,6);

COMMENT ON COLUMN energia_dados.fator_medio_anual IS 'Fator médio anual inserido manualmente pelo usuário';