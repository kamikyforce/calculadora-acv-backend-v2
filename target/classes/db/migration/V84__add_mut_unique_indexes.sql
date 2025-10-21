-- RN008A — Clean duplicates then add unique indexes

-- 1) DESMATAMENTO (valor_unico = TRUE): clean duplicates, keep newest id per (fator_mut_id, bioma)
WITH dup AS (
  SELECT fator_mut_id, bioma, MAX(id) AS keep_id
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

-- 2) SOLO: clean duplicates where combo is fully specified (uso_anterior & uso_atual not null)
WITH dup_solo AS (
  SELECT fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual, MAX(id) AS keep_id
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

-- 3) RN008A indexes
-- SOLO: Tipo de fator + Uso anterior + Uso atual (partial index avoids null conflicts)
CREATE UNIQUE INDEX IF NOT EXISTS uq_mut_solo_fator_uso
ON dados_solo (fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual)
WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL;

-- DESMATAMENTO (valor único): Bioma + Valor único (partial index)
CREATE UNIQUE INDEX IF NOT EXISTS uq_mut_desmat_valor_unico_por_fator
ON dados_desmatamento (fator_mut_id, bioma)
WHERE valor_unico = TRUE;