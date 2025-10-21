-- 1) Dedup com base no que SERÁ extraído da descricao (evita violar uq_mut_solo_fator_uso)
WITH parsed AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim(split_part(descricao, '→', 1)) AS an,
         btrim(regexp_replace(regexp_replace(split_part(descricao, '→', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', '')) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao LIKE '%→%'

  UNION ALL

  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim(split_part(descricao, '->', 1)) AS an,
         btrim(regexp_replace(regexp_replace(split_part(descricao, '->', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', '')) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao LIKE '%->%'

  UNION ALL

  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim((regexp_matches(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[1]) AS an,
         btrim((regexp_matches(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[2]) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao ~* '\bde\s+.+\s+para\s+.+'
),
norm AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         NULLIF(an,'') AS uso_anterior,
         NULLIF(at,'') AS uso_atual
  FROM parsed
),
dups AS (
  SELECT *,
         MIN(id) OVER (PARTITION BY fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual) AS keep_id
  FROM norm
  WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL
)
DELETE FROM dados_solo s
USING dups d
WHERE s.id = d.id
  AND s.id <> d.keep_id;

-- 2) Backfill apenas nos "keepers"
WITH parsed AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim(split_part(descricao, '→', 1)) AS an,
         btrim(regexp_replace(regexp_replace(split_part(descricao, '→', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', '')) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao LIKE '%→%'

  UNION ALL

  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim(split_part(descricao, '->', 1)) AS an,
         btrim(regexp_replace(regexp_replace(split_part(descricao, '->', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', '')) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao LIKE '%->%'

  UNION ALL

  SELECT id, fator_mut_id, tipo_fator_solo,
         btrim((regexp_matches(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[1]) AS an,
         btrim((regexp_matches(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[2]) AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND (uso_anterior IS NULL OR uso_atual IS NULL)
    AND descricao ~* '\bde\s+.+\s+para\s+.+'
),
norm AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         NULLIF(an,'') AS uso_anterior,
         NULLIF(at,'') AS uso_atual
  FROM parsed
),
keepers AS (
  SELECT DISTINCT
         MIN(id) OVER (PARTITION BY fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual) AS id,
         uso_anterior, uso_atual
  FROM norm
  WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL
)
UPDATE dados_solo s
SET uso_anterior = COALESCE(s.uso_anterior, k.uso_anterior),
    uso_atual    = COALESCE(s.uso_atual,    k.uso_atual)
FROM keepers k
WHERE s.id = k.id;

-- 3) Ajustar tipo_fator_solo para “Tipos de Solo” (são classificações, não transições)
UPDATE dados_solo ds
SET tipo_fator_solo = 'SOLO_USO_ANTERIOR_ATUAL'
FROM fator_mut f
WHERE ds.fator_mut_id = f.id
  AND f.tipo_mudanca = 'SOLO'
  AND f.nome IN ('Tipos de Solo')
  AND ds.tipo_fator_solo <> 'SOLO_USO_ANTERIOR_ATUAL';

-- 4) Ocultar agrupadores que não devem aparecer no grid
UPDATE fator_mut
SET ativo = false
WHERE tipo_mudanca = 'SOLO'
  AND nome IN ('Tipos de Solo', 'Opções de Mudança de Uso - Solo', 'Anos de Mudança - Solo')
  AND ativo = true;

-- 5) Trigger para preencher uso_* automaticamente em inserts/updates futuros
DO $$
BEGIN
  CREATE OR REPLACE FUNCTION trg_set_uso_from_descricao() RETURNS trigger AS $BODY$
  BEGIN
    IF NEW.tipo_fator_solo = 'USO_ANTERIOR_ATUAL' THEN
      IF (NEW.uso_anterior IS NULL OR NEW.uso_atual IS NULL) AND NEW.descricao IS NOT NULL THEN
        IF position('→' in NEW.descricao) > 0 THEN
          NEW.uso_anterior := btrim(split_part(NEW.descricao, '→', 1));
          NEW.uso_atual    := btrim(regexp_replace(regexp_replace(split_part(NEW.descricao, '→', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''));
        ELSIF position('->' in NEW.descricao) > 0 THEN
          NEW.uso_anterior := btrim(split_part(NEW.descricao, '->', 1));
          NEW.uso_atual    := btrim(regexp_replace(regexp_replace(split_part(NEW.descricao, '->', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''));
        END IF;
      END IF;
    END IF;
    RETURN NEW;
  END
  $BODY$ LANGUAGE plpgsql;

  BEGIN
    CREATE TRIGGER before_dados_solo_ins_upd
    BEFORE INSERT OR UPDATE ON dados_solo
    FOR EACH ROW EXECUTE FUNCTION trg_set_uso_from_descricao();
  EXCEPTION WHEN duplicate_object THEN
    NULL;
  END;
END $$;

-- 6) (Opcional, mas forte) CHECK: se for USO_ANTERIOR_ATUAL, ambos os campos precisam existir
DO $$
BEGIN
  ALTER TABLE dados_solo
    ADD CONSTRAINT chk_dados_solo_uso_required
    CHECK (
      tipo_fator_solo <> 'USO_ANTERIOR_ATUAL'
      OR (uso_anterior IS NOT NULL AND uso_atual IS NOT NULL)
    );
EXCEPTION WHEN duplicate_object THEN
  NULL;
END $$;