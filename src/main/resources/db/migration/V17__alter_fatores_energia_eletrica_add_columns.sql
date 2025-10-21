ALTER TABLE fatores_energia_eletrica
    ADD COLUMN IF NOT EXISTS percentual_etanol_gasolina DECIMAL(5,2),
    ADD COLUMN IF NOT EXISTS percentual_biodiesel_diesel DECIMAL(5,2),
    ADD COLUMN IF NOT EXISTS fator_medio_anual DECIMAL(10,6),
    ADD COLUMN IF NOT EXISTS tipo_dado VARCHAR(20);