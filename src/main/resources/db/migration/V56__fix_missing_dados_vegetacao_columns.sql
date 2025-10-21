-- V56: Fix missing columns in dados_vegetacao table
-- This migration ensures all required columns exist for DadosVegetacao model

-- Check if table exists first
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'dados_vegetacao') THEN
        RAISE EXCEPTION 'Table dados_vegetacao does not exist. Run previous migrations first.';
    END IF;
END $$;

-- Add missing columns with IF NOT EXISTS to avoid conflicts
ALTER TABLE dados_vegetacao 
ADD COLUMN IF NOT EXISTS especie_vegetacao VARCHAR(255),
ADD COLUMN IF NOT EXISTS altura_media DECIMAL(10,2),
ADD COLUMN IF NOT EXISTS biomassa_aerea DECIMAL(15,6),
ADD COLUMN IF NOT EXISTS biomassa_subterranea DECIMAL(15,6);

-- Add comments for documentation
COMMENT ON COLUMN dados_vegetacao.especie_vegetacao IS 'Espécie da vegetação';
COMMENT ON COLUMN dados_vegetacao.altura_media IS 'Altura média da vegetação em metros';
COMMENT ON COLUMN dados_vegetacao.biomassa_aerea IS 'Biomassa aérea da vegetação';
COMMENT ON COLUMN dados_vegetacao.biomassa_subterranea IS 'Biomassa subterrânea da vegetação';

-- Verification: Check that all columns now exist
DO $$ 
DECLARE
    missing_columns TEXT[] := ARRAY[]::TEXT[];
BEGIN
    -- Check each required column
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'dados_vegetacao' AND column_name = 'especie_vegetacao') THEN
        missing_columns := array_append(missing_columns, 'especie_vegetacao');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'dados_vegetacao' AND column_name = 'altura_media') THEN
        missing_columns := array_append(missing_columns, 'altura_media');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'dados_vegetacao' AND column_name = 'biomassa_aerea') THEN
        missing_columns := array_append(missing_columns, 'biomassa_aerea');
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'dados_vegetacao' AND column_name = 'biomassa_subterranea') THEN
        missing_columns := array_append(missing_columns, 'biomassa_subterranea');
    END IF;
    
    -- Report results
    IF array_length(missing_columns, 1) > 0 THEN
        RAISE EXCEPTION 'Migration V56 failed: Missing columns: %', array_to_string(missing_columns, ', ');
    ELSE
        RAISE NOTICE 'Migration V56 completed successfully: All dados_vegetacao columns are now present';
    END IF;
END $$;