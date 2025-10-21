-- V37__set_default_escopo_for_existing_combustiveis.sql
-- Set default escopo values for existing combustiveis that have null escopo

-- First check if the column exists
DO $$
BEGIN
    -- Add escopo column if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'combustiveis' AND column_name = 'escopo') THEN
        ALTER TABLE combustiveis ADD COLUMN escopo VARCHAR(50);
    END IF;
    
    -- Then update existing combustiveis with null escopo to ESCOPO1 (default)
    UPDATE combustiveis 
    SET escopo = 'ESCOPO1' 
    WHERE escopo IS NULL AND ativo = true;
    
    -- Add comment if possible
    COMMENT ON COLUMN combustiveis.escopo IS 'Scope of the fuel data (ESCOPO1, ESCOPO3) - defaults to ESCOPO1 for existing records';
END $$;