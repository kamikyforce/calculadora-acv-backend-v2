-- Migration V31: Ajustar precisão de media_anual_calculada para DECIMAL(20,6)
-- Alinha com cálculo do backend (6 casas) e previne overflow

ALTER TABLE energia_dados
ALTER COLUMN media_anual_calculada TYPE DECIMAL(20,6)
USING media_anual_calculada::DECIMAL(20,6);

COMMENT ON COLUMN energia_dados.media_anual_calculada IS 'Média anual calculada automaticamente dos dados mensais (DECIMAL(20,6))';