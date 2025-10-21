-- Migration para popular ano_referencia com base nos IDs existentes
-- Conforme especificado pelo usuário

UPDATE energia_dados SET ano_referencia = 2020 WHERE id = 1;
UPDATE energia_dados SET ano_referencia = 2021 WHERE id = 2;
UPDATE energia_dados SET ano_referencia = 2022 WHERE id = 3;
UPDATE energia_dados SET ano_referencia = 2023 WHERE id = 4;
UPDATE energia_dados SET ano_referencia = 2024 WHERE id = 5;
UPDATE energia_dados SET ano_referencia = 2025 WHERE id = 6;

-- Para registros futuros sem ano_referencia definido, 
-- a query usará EXTRACT(YEAR FROM data_criacao) como fallback