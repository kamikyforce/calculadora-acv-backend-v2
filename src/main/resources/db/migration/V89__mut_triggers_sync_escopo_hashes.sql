-- V89__mut_triggers_sync_escopo_hashes.sql
-- PostgreSQL

BEGIN;

-- ===== Funções de escopo =====
CREATE OR REPLACE FUNCTION mut_set_escopo_dados_solo()
RETURNS trigger AS $$
BEGIN
  NEW.escopo := (SELECT escopo FROM fator_mut WHERE id = NEW.fator_mut_id);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION mut_set_escopo_dados_desmatamento()
RETURNS trigger AS $$
BEGIN
  NEW.escopo := (SELECT escopo FROM fator_mut WHERE id = NEW.fator_mut_id);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION mut_set_escopo_dados_vegetacao()
RETURNS trigger AS $$
BEGIN
  NEW.escopo := (SELECT escopo FROM fator_mut WHERE id = NEW.fator_mut_id);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ===== Funções de hash =====
CREATE OR REPLACE FUNCTION mut_recalc_ufs_hash(p_id BIGINT)
RETURNS void AS $$
UPDATE dados_desmatamento d
SET ufs_hash = sub.ufs_hash
FROM (
  SELECT duf.dados_desmatamento_id AS id,
         string_agg(duf.uf, ',' ORDER BY duf.uf) AS ufs_hash
  FROM desmatamento_ufs duf
  WHERE duf.dados_desmatamento_id = p_id
  GROUP BY duf.dados_desmatamento_id
) AS sub
WHERE d.id = sub.id;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION mut_recalc_categorias_hash(p_id BIGINT)
RETURNS void AS $$
UPDATE dados_vegetacao v
SET categorias_hash = sub.categorias_hash
FROM (
  SELECT vc.dados_vegetacao_id AS id,
         string_agg(vc.categoria, ',' ORDER BY vc.categoria) AS categorias_hash
  FROM vegetacao_categorias vc
  WHERE vc.dados_vegetacao_id = p_id
  GROUP BY vc.dados_vegetacao_id
) AS sub
WHERE v.id = sub.id;
$$ LANGUAGE sql;

-- ===== Triggers: set escopo nos filhos =====
DROP TRIGGER IF EXISTS trg_set_escopo_dados_solo        ON dados_solo;
DROP TRIGGER IF EXISTS trg_set_escopo_dados_desmatamento ON dados_desmatamento;
DROP TRIGGER IF EXISTS trg_set_escopo_dados_vegetacao    ON dados_vegetacao;

CREATE TRIGGER trg_set_escopo_dados_solo
BEFORE INSERT OR UPDATE OF fator_mut_id ON dados_solo
FOR EACH ROW EXECUTE FUNCTION mut_set_escopo_dados_solo();

CREATE TRIGGER trg_set_escopo_dados_desmatamento
BEFORE INSERT OR UPDATE OF fator_mut_id ON dados_desmatamento
FOR EACH ROW EXECUTE FUNCTION mut_set_escopo_dados_desmatamento();

CREATE TRIGGER trg_set_escopo_dados_vegetacao
BEFORE INSERT OR UPDATE OF fator_mut_id ON dados_vegetacao
FOR EACH ROW EXECUTE FUNCTION mut_set_escopo_dados_vegetacao();

-- ===== Triggers: recalcular hashes =====
DROP TRIGGER IF EXISTS trg_recalc_ufs_hash_aiud ON desmatamento_ufs;
DROP TRIGGER IF EXISTS trg_recalc_categorias_hash_aiud ON vegetacao_categorias;

CREATE OR REPLACE FUNCTION mut_trigger_recalc_ufs_hash()
RETURNS trigger AS $$
BEGIN
  PERFORM mut_recalc_ufs_hash(COALESCE(NEW.dados_desmatamento_id, OLD.dados_desmatamento_id));
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION mut_trigger_recalc_categorias_hash()
RETURNS trigger AS $$
BEGIN
  PERFORM mut_recalc_categorias_hash(COALESCE(NEW.dados_vegetacao_id, OLD.dados_vegetacao_id));
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_recalc_ufs_hash_aiud
AFTER INSERT OR UPDATE OR DELETE ON desmatamento_ufs
FOR EACH ROW EXECUTE FUNCTION mut_trigger_recalc_ufs_hash();

CREATE TRIGGER trg_recalc_categorias_hash_aiud
AFTER INSERT OR UPDATE OR DELETE ON vegetacao_categorias
FOR EACH ROW EXECUTE FUNCTION mut_trigger_recalc_categorias_hash();

COMMIT;