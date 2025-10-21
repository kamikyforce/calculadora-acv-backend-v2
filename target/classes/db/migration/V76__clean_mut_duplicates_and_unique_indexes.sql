-- Ensure required columns exist before using them (compatible with older schema versions)
ALTER TABLE dados_solo ADD COLUMN IF NOT EXISTS uso_anterior VARCHAR(255);
ALTER TABLE dados_solo ADD COLUMN IF NOT EXISTS uso_atual VARCHAR(255);

-- Clean duplicates for DESMATAMENTO (valor_unico = true)
WITH dup AS (
  SELECT fator_mut_id, bioma, MAX(id) AS keep_id, COUNT(*) AS cnt
  FROM dados_desmatamento
  WHERE valor_unico = TRUE
  GROUP BY fator_mut_id, bioma
  HAVING COUNT(*) > 1
)
DELETE FROM dados_desmatamento d
USING dup
WHERE d.valor_unico = TRUE
  AND d.fator_mut_id = dup.fator_mut_id
  AND d.bioma = dup.bioma
  AND d.id <> dup.keep_id;

-- Clean duplicates for SOLO (combinations fully specified)
WITH dup_solo AS (
  SELECT fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual, MAX(id) AS keep_id, COUNT(*) AS cnt
  FROM dados_solo
  WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL
  GROUP BY fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual
  HAVING COUNT(*) > 1
)
DELETE FROM dados_solo s
USING dup_solo ds
WHERE s.fator_mut_id = ds.fator_mut_id
  AND s.tipo_fator_solo = ds.tipo_fator_solo
  AND s.uso_anterior = ds.uso_anterior
  AND s.uso_atual = ds.uso_atual
  AND s.id <> ds.keep_id;

-- Ensure no duplicate UF in the same DESMATAMENTO record
CREATE UNIQUE INDEX IF NOT EXISTS uq_desmatamento_ufs_dados_uf
ON desmatamento_ufs(dados_desmatamento_id, uf);

-- Recreate RN008A indexes
CREATE UNIQUE INDEX IF NOT EXISTS uq_mut_solo_fator_uso
ON dados_solo (fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual)
WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_mut_desmat_valor_unico_por_fator
ON dados_desmatamento (fator_mut_id, bioma)
WHERE valor_unico = TRUE;