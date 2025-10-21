-- V49__add_bioma_to_dados_solo.sql
ALTER TABLE dados_solo 
ADD COLUMN bioma VARCHAR(50) CHECK (bioma IN ('AMAZONIA', 'CAATINGA', 'CERRADO', 'MATA_ATLANTICA', 'PAMPA', 'PANTANAL')),
ADD COLUMN fator_co2 DECIMAL(15,6),
ADD COLUMN fator_ch4 DECIMAL(15,6),
ADD COLUMN fator_n2o DECIMAL(15,6);

-- Adiciona colunas faltantes na tabela dados_desmatamento
ALTER TABLE dados_desmatamento 
ADD COLUMN fator_co2 DECIMAL(15,6),
ADD COLUMN fator_ch4 DECIMAL(15,6),
ADD COLUMN fator_n2o DECIMAL(15,6);

-- Adiciona índices para melhorar performance
CREATE INDEX idx_dados_solo_bioma ON dados_solo(bioma);
-- Nota: idx_dados_desmatamento_bioma já existe na V43, não precisa ser criado novamente

-- Comentários nas colunas - dados_solo
COMMENT ON COLUMN dados_solo.bioma IS 'Bioma relacionado aos dados de solo';
COMMENT ON COLUMN dados_solo.fator_co2 IS 'Fator de emissão de CO2';
COMMENT ON COLUMN dados_solo.fator_ch4 IS 'Fator de emissão de CH4';
COMMENT ON COLUMN dados_solo.fator_n2o IS 'Fator de emissão de N2O';

-- Comentários nas colunas - dados_desmatamento
COMMENT ON COLUMN dados_desmatamento.fator_co2 IS 'Fator de emissão de CO2';
COMMENT ON COLUMN dados_desmatamento.fator_ch4 IS 'Fator de emissão de CH4';
COMMENT ON COLUMN dados_desmatamento.fator_n2o IS 'Fator de emissão de N2O';