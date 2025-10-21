-- Unicidade por usuário, escopo e nome (case-insensível), apenas ativos.
-- Limpeza de duplicatas antes de criar índice único.

DROP INDEX IF EXISTS ux_combustiveis_nome_escopo_usuario;

-- 1) Normaliza nomes removendo espaços
UPDATE combustiveis
SET nome = TRIM(nome)
WHERE nome IS NOT NULL AND nome <> TRIM(nome);

-- 2) Desativa duplicatas mantendo o menor id ativo por (LOWER(TRIM(nome)), escopo, COALESCE(usuario_id, -1))
WITH grupos AS (
    SELECT
        LOWER(TRIM(nome)) AS nome_norm,
        escopo,
        COALESCE(usuario_id, -1) AS usuario_norm,
        MIN(id) AS keep_id,
        COUNT(*) AS cnt
    FROM combustiveis
    WHERE ativo IS TRUE
    GROUP BY LOWER(TRIM(nome)), escopo, COALESCE(usuario_id, -1)
    HAVING COUNT(*) > 1
)
UPDATE combustiveis c
SET ativo = FALSE
FROM grupos g
WHERE c.ativo IS TRUE
  AND LOWER(TRIM(c.nome)) = g.nome_norm
  AND c.escopo = g.escopo
  AND COALESCE(c.usuario_id, -1) = g.usuario_norm
  AND c.id <> g.keep_id;

-- 3) Cria o índice único (case-insensível e ignorando espaços)
CREATE UNIQUE INDEX IF NOT EXISTS ux_combustiveis_nome_escopo_usuario
ON combustiveis (LOWER(TRIM(nome)), escopo, COALESCE(usuario_id, -1))
WHERE ativo IS TRUE;