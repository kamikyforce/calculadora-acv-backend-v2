-- V35__add_escopo_usuario_id_to_combustiveis_fixed.sql
-- Add escopo and usuario_id columns to combustiveis table (idempotent version)

-- Check if columns don't exist before adding them
DO $$
BEGIN
    -- Add escopo column if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'combustiveis' AND column_name = 'escopo') THEN
        ALTER TABLE combustiveis ADD COLUMN escopo VARCHAR(50);
    END IF;
    
    -- Add usuario_id column if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'combustiveis' AND column_name = 'usuario_id') THEN
        ALTER TABLE combustiveis ADD COLUMN usuario_id BIGINT;
    END IF;
END $$;

-- Add foreign key constraint if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'fk_combustivel_usuario') THEN
        ALTER TABLE combustiveis 
        ADD CONSTRAINT fk_combustivel_usuario 
        FOREIGN KEY (usuario_id) REFERENCES usuario(id);
    END IF;
END $$;

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_combustivel_escopo ON combustiveis (escopo);
CREATE INDEX IF NOT EXISTS idx_combustivel_usuario_id ON combustiveis (usuario_id);
CREATE INDEX IF NOT EXISTS idx_combustivel_nome_escopo ON combustiveis (nome, escopo);
CREATE INDEX IF NOT EXISTS idx_combustivel_usuario_escopo ON combustiveis (usuario_id, escopo);

-- Add comments
COMMENT ON COLUMN combustiveis.escopo IS 'Scope of the fuel data (ESCOPO1, ESCOPO2, ESCOPO3, etc.)';
COMMENT ON COLUMN combustiveis.usuario_id IS 'User ID for user-specific fuel data';