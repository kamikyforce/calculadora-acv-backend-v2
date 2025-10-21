-- 1) Dedup com base no que SERÁ extraído da descricao (evita violar uq_mut_solo_fator_uso)
WITH extracted AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         CASE
           WHEN position('→' in descricao) > 0 THEN btrim(split_part(descricao, '→', 1))
           WHEN position('->' in descricao) > 0 THEN btrim(split_part(descricao, '->', 1))
           ELSE btrim((regexp_match(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[1])
         END AS an,
         CASE
           WHEN position('→' in descricao) > 0 THEN btrim(regexp_replace(regexp_replace(split_part(descricao, '→', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''))
           WHEN position('->' in descricao) > 0 THEN btrim(regexp_replace(regexp_replace(split_part(descricao, '->', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''))
           ELSE btrim((regexp_match(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[2])
         END AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND descricao IS NOT NULL
),
candidates AS (
  SELECT ds.id, ds.fator_mut_id, ds.tipo_fator_solo,
         COALESCE(NULLIF(ds.uso_anterior,''), NULLIF(ex.an,'')) AS uso_anterior,
         COALESCE(NULLIF(ds.uso_atual,''),    NULLIF(ex.at,'')) AS uso_atual
  FROM dados_solo ds
  LEFT JOIN extracted ex ON ex.id = ds.id
  WHERE ds.tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
),
keepers AS (
  SELECT fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual,
         MIN(id) AS keep_id, COUNT(*) AS cnt
  FROM candidates
  WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL
  GROUP BY fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual
)
DELETE FROM dados_solo s
USING candidates c, keepers k
WHERE s.id = c.id
  AND c.uso_anterior IS NOT NULL AND c.uso_atual IS NOT NULL
  AND c.fator_mut_id = k.fator_mut_id
  AND c.tipo_fator_solo = k.tipo_fator_solo
  AND c.uso_anterior = k.uso_anterior
  AND c.uso_atual = k.uso_atual
  AND s.id <> k.keep_id;

-- 2) Backfill apenas nos "keepers" (precisa de escopo próprio de CTEs)
WITH extracted AS (
  SELECT id, fator_mut_id, tipo_fator_solo,
         CASE
           WHEN position('→' in descricao) > 0 THEN btrim(split_part(descricao, '→', 1))
           WHEN position('->' in descricao) > 0 THEN btrim(split_part(descricao, '->', 1))
           ELSE btrim((regexp_match(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[1])
         END AS an,
         CASE
           WHEN position('→' in descricao) > 0 THEN btrim(regexp_replace(regexp_replace(split_part(descricao, '→', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''))
           WHEN position('->' in descricao) > 0 THEN btrim(regexp_replace(regexp_replace(split_part(descricao, '->', 2), '\s*-\s.*$', '', ''), '\s*\(.*$', '', ''))
           ELSE btrim((regexp_match(descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[2])
         END AS at
  FROM dados_solo
  WHERE tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
    AND descricao IS NOT NULL
),
candidates AS (
  SELECT ds.id, ds.fator_mut_id, ds.tipo_fator_solo,
         COALESCE(NULLIF(ds.uso_anterior,''), NULLIF(ex.an,'')) AS uso_anterior,
         COALESCE(NULLIF(ds.uso_atual,''),    NULLIF(ex.at,'')) AS uso_atual
  FROM dados_solo ds
  LEFT JOIN extracted ex ON ex.id = ds.id
  WHERE ds.tipo_fator_solo = 'USO_ANTERIOR_ATUAL'
),
keepers AS (
  SELECT fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual,
         MIN(id) AS keep_id, COUNT(*) AS cnt
  FROM candidates
  WHERE uso_anterior IS NOT NULL AND uso_atual IS NOT NULL
  GROUP BY fator_mut_id, tipo_fator_solo, uso_anterior, uso_atual
)
UPDATE dados_solo s
SET uso_anterior = COALESCE(s.uso_anterior, c.uso_anterior),
    uso_atual    = COALESCE(s.uso_atual,    c.uso_atual)
FROM candidates c
JOIN keepers k
  ON c.fator_mut_id = k.fator_mut_id
 AND c.tipo_fator_solo = k.tipo_fator_solo
 AND c.uso_anterior = k.uso_anterior
 AND c.uso_atual = k.uso_atual
WHERE s.id = k.keep_id;

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
-- Trigger section (trg_set_uso_from_descricao)
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
        ELSE
          -- Fallback para padrões 'de … para …' usando regexp_match (não SRF)
          NEW.uso_anterior := btrim((regexp_match(NEW.descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[1]);
          NEW.uso_atual    := btrim((regexp_match(NEW.descricao, '(?i)\bde\s+(.+?)\s+para\s+(.+?)(?:$|[.,;])'))[2]);
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