-- V47__set_default_escopo_for_existing_combustiveis.sql
-- Set default escopo values for existing combustiveis that have null escopo

-- Update existing combustiveis with null escopo to ESCOPO1 (default)
UPDATE combustiveis 
SET escopo = 'ESCOPO1' 
WHERE escopo IS NULL AND ativo = true;

-- Add comment
COMMENT ON COLUMN combustiveis.escopo IS 'Scope of the fuel data (ESCOPO1, ESCOPO3) - defaults to ESCOPO1 for existing records';