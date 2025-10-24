-- V91__mut_dedup_by_escopo_and_unique_indexes.sql
-- PostgreSQL

BEGIN;

-- ===== LIMPEZA DE DUPLICATAS POR ESCOPO =====

-- SOLO: remover duplicatas por (escopo, tipo_fator_solo, uso_anterior, uso_atual) quando principal = TRUE
WITH solo_dup AS (
  SELECT escopo, tipo_fator_solo, uso_anterior, uso_atual, MAX(id) AS keep_id
  FROM dados_solo
  WHERE principal = TRUE
    AND uso_anterior IS NOT NULL
    AND uso_atual IS NOT NULL
  GROUP BY escopo, tipo_fator_solo, uso_anterior, uso_atual
  HAVING COUNT(*) > 1
)
DELETE FROM dados_solo s
USING solo_dup d
WHERE s.principal = TRUE
  AND s.escopo = d.escopo
  AND s.tipo_fator_solo = d.tipo_fator_solo
  AND s.uso_anterior = d.uso_anterior
  AND s.uso_atual = d.uso_atual
  AND s.id <> d.keep_id;

-- DESMATAMENTO: remover duplicatas por (escopo, bioma) quando valor_unico = TRUE
WITH desm_uni_dup AS (
  SELECT escopo, bioma, MAX(id) AS keep_id
  FROM dados_desmatamento
  WHERE valor_unico = TRUE
    AND bioma IS NOT NULL
  GROUP BY escopo, bioma
  HAVING COUNT(*) > 1
)
DELETE FROM dados_desmatamento d
USING desm_uni_dup u
WHERE d.valor_unico = TRUE
  AND d.escopo = u.escopo
  AND d.bioma = u.bioma
  AND d.id <> u.keep_id;

-- DESMATAMENTO: remover duplicatas por (escopo, bioma, ufs_hash) quando não for valor_unico
WITH desm_hash_dup AS (
  SELECT escopo, bioma, ufs_hash, MAX(id) AS keep_id
  FROM dados_desmatamento
  WHERE COALESCE(valor_unico, FALSE) = FALSE
    AND bioma IS NOT NULL
    AND ufs_hash IS NOT NULL
  GROUP BY escopo, bioma, ufs_hash
  HAVING COUNT(*) > 1
)
DELETE FROM dados_desmatamento d
USING desm_hash_dup h
WHERE COALESCE(d.valor_unico, FALSE) = FALSE
  AND d.escopo = h.escopo
  AND d.bioma = h.bioma
  AND d.ufs_hash = h.ufs_hash
  AND d.id <> h.keep_id;

-- VEGETAÇÃO: remover duplicatas por (escopo, categorias_hash, parametro)
WITH veg_dup AS (
  SELECT escopo, categorias_hash, parametro, MAX(id) AS keep_id
  FROM dados_vegetacao
  WHERE categorias_hash IS NOT NULL
    AND parametro IS NOT NULL
  GROUP BY escopo, categorias_hash, parametro
  HAVING COUNT(*) > 1
)
DELETE FROM dados_vegetacao v
USING veg_dup dv
WHERE v.escopo = dv.escopo
  AND v.categorias_hash = dv.categorias_hash
  AND v.parametro = dv.parametro
  AND v.id <> dv.keep_id;

-- ===== ÍNDICES ÚNICOS POR ESCOPO =====

-- SOLO: chave comum por escopo (principal)
CREATE UNIQUE INDEX IF NOT EXISTS ux_solo_chave_comum_escopo
  ON dados_solo (escopo, tipo_fator_solo, uso_anterior, uso_atual)
  WHERE principal = TRUE
    AND uso_anterior IS NOT NULL
    AND uso_atual IS NOT NULL;

-- DESMATAMENTO: bioma + valor_unico por escopo
CREATE UNIQUE INDEX IF NOT EXISTS ux_desm_valor_unico_escopo
  ON dados_desmatamento (escopo, bioma)
  WHERE valor_unico = TRUE;

-- DESMATAMENTO: bioma + ufs_hash por escopo (quando não valor_unico)
CREATE UNIQUE INDEX IF NOT EXISTS ux_desm_ufs_escopo
  ON dados_desmatamento (escopo, bioma, ufs_hash)
  WHERE COALESCE(valor_unico, FALSE) = FALSE;

-- VEGETAÇÃO: categorias + parâmetro por escopo
CREATE UNIQUE INDEX IF NOT EXISTS ux_veg_categoria_param_escopo
  ON dados_vegetacao (escopo, categorias_hash, parametro);

COMMIT;