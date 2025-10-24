-- V88__mut_add_columns_escopo_flags_hashes.sql
-- PostgreSQL

BEGIN;

-- ===== SOLO =====
ALTER TABLE dados_solo
  ADD COLUMN IF NOT EXISTS principal BOOLEAN NOT NULL DEFAULT TRUE,
  ADD COLUMN IF NOT EXISTS replicado_automatico BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS escopo VARCHAR(50);

-- main record = CO2/CH4 nulos
UPDATE dados_solo s
SET principal = (s.fator_co2 IS NULL AND s.fator_ch4 IS NULL);

-- escopo a partir do fator_mut
UPDATE dados_solo s
SET escopo = fm.escopo
FROM fator_mut fm
WHERE s.fator_mut_id = fm.id;

-- ===== DESMATAMENTO =====
ALTER TABLE dados_desmatamento
  ADD COLUMN IF NOT EXISTS replicado_automatico BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS escopo VARCHAR(50),
  ADD COLUMN IF NOT EXISTS ufs_hash VARCHAR(255);

UPDATE dados_desmatamento d
SET escopo = fm.escopo
FROM fator_mut fm
WHERE d.fator_mut_id = fm.id;

-- normaliza UFs associadas (ordenadas e concatenadas) quando não valor_unico
UPDATE dados_desmatamento d
SET ufs_hash = sub.ufs_hash
FROM (
  SELECT duf.dados_desmatamento_id AS id,
         string_agg(duf.uf, ',' ORDER BY duf.uf) AS ufs_hash
  FROM desmatamento_ufs duf
  GROUP BY duf.dados_desmatamento_id
) AS sub
WHERE d.id = sub.id
  AND COALESCE(d.valor_unico, FALSE) = FALSE;

-- ===== VEGETAÇÃO =====
ALTER TABLE dados_vegetacao
  ADD COLUMN IF NOT EXISTS replicado_automatico BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS escopo VARCHAR(50),
  ADD COLUMN IF NOT EXISTS categorias_hash VARCHAR(255);

UPDATE dados_vegetacao v
SET escopo = fm.escopo
FROM fator_mut fm
WHERE v.fator_mut_id = fm.id;

-- normaliza categorias associadas (ordenadas e concatenadas)
UPDATE dados_vegetacao v
SET categorias_hash = sub.categorias_hash
FROM (
  SELECT vc.dados_vegetacao_id AS id,
         string_agg(vc.categoria, ',' ORDER BY vc.categoria) AS categorias_hash
  FROM vegetacao_categorias vc
  GROUP BY vc.dados_vegetacao_id
) AS sub
WHERE v.id = sub.id;

-- torna escopo obrigatório após backfill
ALTER TABLE dados_solo         ALTER COLUMN escopo SET NOT NULL;
ALTER TABLE dados_desmatamento ALTER COLUMN escopo SET NOT NULL;
ALTER TABLE dados_vegetacao    ALTER COLUMN escopo SET NOT NULL;

COMMIT;