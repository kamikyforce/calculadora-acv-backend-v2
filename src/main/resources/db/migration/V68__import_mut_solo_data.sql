-- Flyway Migration: Importação completa dos dados MUT - Solo
-- Importa todos os dados de solo do arquivo 1.sql nas tabelas existentes do projeto
-- Generated at 2025-01-27

-- Inserir fatores MUT principais para dados de solo
INSERT INTO fator_mut (nome, tipo_mudanca, escopo, ativo, data_criacao)
SELECT 'Fatores de Emissão por Mudança de Uso do Solo', 'SOLO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Fatores de Emissão por Mudança de Uso do Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
UNION ALL
SELECT 'Fatores de Emissão por Tipo de Solo', 'SOLO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Fatores de Emissão por Tipo de Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
UNION ALL
SELECT 'Tipos de Solo', 'SOLO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Tipos de Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
UNION ALL
SELECT 'Opções de Mudança de Uso - Solo', 'SOLO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Opções de Mudança de Uso - Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
UNION ALL
SELECT 'Anos de Mudança - Solo', 'SOLO', 'ESCOPO3', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut 
    WHERE nome = 'Anos de Mudança - Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
);

-- Inserir dados específicos de solo - Fatores de emissão por mudança de uso (Tabela 1)
WITH fator_solo AS (
    SELECT id FROM fator_mut 
    WHERE nome = 'Fatores de Emissão por Mudança de Uso do Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
INSERT INTO dados_solo (fator_mut_id, tipo_fator_solo, valor_fator, descricao)
SELECT 
    fs.id,
    'USO_ANTERIOR_ATUAL',  -- Changed from 'ALTO_CARBONO'
    -2.25,
    'Cana-de-açúcar com queima → Cana-de-açúcar sem queima (GALDOS et al., 2011)'
FROM fator_solo fs
WHERE NOT EXISTS (
    SELECT 1 FROM dados_solo ds 
    WHERE ds.fator_mut_id = fs.id 
    AND ds.descricao = 'Cana-de-açúcar com queima → Cana-de-açúcar sem queima (GALDOS et al., 2011)'
)
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -6.23, 'Cultivo convencional → Integração lavoura-pecuária(-floresta)' FROM fator_solo fs
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional → Integração lavoura-pecuária(-floresta)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -3.67, 'Cultivo convencional → Pastagem/pastagem melhorada (ASSAD e MARTINS, 2015)' FROM fator_solo fs
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional → Pastagem/pastagem melhorada (ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -3.67, 'Cultivo convencional → Pastagem melhorada com insumos (ASSAD e MARTINS, 2015)' FROM fator_solo fs
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional → Pastagem melhorada com insumos (ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -1.06, 'Cultivo convencional (Demais regiões) → Plantio direto (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional (Demais regiões) → Plantio direto (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -1.28, 'Cultivo convencional (Região sul) → Plantio direto (BAYER et al., 2006)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional (Região sul) → Plantio direto (BAYER et al., 2006)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -5.39, 'Cultivo convencional → Sistema agroflorestal (Madari et al. (2017))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Cultivo convencional → Sistema agroflorestal (Madari et al. (2017))')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 1.72, 'Integração lavoura-pecuária → Cultivo convencional (Adaptado de ASSAD e MARTINS, 2015)' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Integração lavoura-pecuária → Cultivo convencional (Adaptado de ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -2.56, 'Integração lavoura-pecuária → Pastagem/pastagem melhorada (Adaptado de ASSAD e MARTINS, 2015)' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Integração lavoura-pecuária → Pastagem/pastagem melhorada (Adaptado de ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -2.56, 'Integração lavoura-pecuária → Pastagem melhorada com insumos (Adaptado de ASSAD e MARTINS, 2015)' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Integração lavoura-pecuária → Pastagem melhorada com insumos (Adaptado de ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -1.76, 'Integração lavoura-pecuária → Plantio direto (ASSAD e MARTINS, 2015)' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Integração lavoura-pecuária → Plantio direto (ASSAD e MARTINS, 2015)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 0.92, 'Pastagem degradada → Cultivo convencional (Adaptado de MAIA et al., 2009)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem degradada → Cultivo convencional (Adaptado de MAIA et al., 2009)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -3.01, 'Pastagem degradada → Integração lavoura-pecuária(-floresta) (Carvalho et al. (2010))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem degradada → Integração lavoura-pecuária(-floresta) (Carvalho et al. (2010))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -0.99, 'Pastagem degradada → Plantio direto (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem degradada → Plantio direto (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -2.24, 'Pastagem/pastagem melhorada → Sistema agroflorestal (Madari et al. (2017); Maia et al. (2009))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem/pastagem melhorada → Sistema agroflorestal (Madari et al. (2017); Maia et al. (2009))')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -1.69, 'Pastagem/pastagem melhorada → Integração lavoura-pecuária(-floresta) (Carvalho et al. (2010))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem/pastagem melhorada → Integração lavoura-pecuária(-floresta) (Carvalho et al. (2010))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', 1.22, 'Pastagem → Floresta plantada - eucalipto (Lima et al, 2008)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem → Floresta plantada - eucalipto (Lima et al, 2008)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 0.92, 'Plantio direto → Cultivo convencional (Adaptado de MAIA et al., 2009)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Plantio direto → Cultivo convencional (Adaptado de MAIA et al., 2009)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -2.93, 'Plantio direto → Integração lavoura-pecuária(-floresta) (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Plantio direto → Integração lavoura-pecuária(-floresta) (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 5.28, 'Pastagem não degradada → Agricultura convencional (Carvalho et al. (2010))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem não degradada → Agricultura convencional (Carvalho et al. (2010))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -1.95, 'Pastagem → Café (Imaflora (2021))' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem → Café (Imaflora (2021))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -1.95, 'Lavoura → Café (Imaflora (2021))' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Lavoura → Café (Imaflora (2021))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -1.80, 'Pastagem → Regeneração Natural (Bayer et al. (2006) e Carvalho et al. (2009))' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Pastagem → Regeneração Natural (Bayer et al. (2006) e Carvalho et al. (2009))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', -0.37, 'Vegetação nativa (Cerrado) → Plantio direto (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) → Plantio direto (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', 0.95, 'Vegetação nativa (Floresta) → Plantio direto (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Floresta) → Plantio direto (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 4.99, 'Vegetação nativa (Floresta) → Plantio direto (Carvalho et al, 2010)' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Floresta) → Plantio direto (Carvalho et al, 2010)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', 1.72, 'Vegetação nativa (geral) → Lavoura convencional (Maia et al, 2013)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (geral) → Lavoura convencional (Maia et al, 2013)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 4.58, 'Vegetação nativa (Cerrado) solo argiloso → Lavoura convencional (Jantalia et al. (2007))' FROM fator_solo fs  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) solo argiloso → Lavoura convencional (Jantalia et al. (2007))')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', 1.10, 'Vegetação nativa (Cerrado) solo médio → Lavoura convencional (Maia et al, 2010)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) solo médio → Lavoura convencional (Maia et al, 2010)')
UNION ALL
SELECT fs.id, 'SOLO_USO_ANTERIOR_ATUAL', 1.58, 'Vegetação nativa (Cerradão) solo médio → Lavoura convencional (Maia et al, 2010)' FROM fator_solo fs  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerradão) solo médio → Lavoura convencional (Maia et al, 2010)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 3.85, 'Vegetação nativa (Cerradão) → Pastagem degradada (Maia et al, 2009)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerradão) → Pastagem degradada (Maia et al, 2009)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 0.70, 'Vegetação nativa (Cerradão) → Pastagem nominal (Maia et al, 2009)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerradão) → Pastagem nominal (Maia et al, 2009)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', 1.06, 'Vegetação nativa (Cerrado) → Pastagem degradada (Lilienfein et al. 2003)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) → Pastagem degradada (Lilienfein et al. 2003)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -0.40, 'Vegetação nativa (Cerrado) → Pastagem nominal (Maquere et al. 2008)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) → Pastagem nominal (Maquere et al. 2008)')
UNION ALL
SELECT fs.id, 'USO_ANTERIOR_ATUAL', -1.72, 'Vegetação nativa (Cerrado) → Pastagem melhorada (Lilienfein et al. 2003)' FROM fator_solo fs  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fs.id AND ds.descricao = 'Vegetação nativa (Cerrado) → Pastagem melhorada (Lilienfein et al. 2003)');

-- Inserir dados específicos de solo - Fatores por tipo de solo (Tabela 2)
WITH fator_tipo_solo AS (
    SELECT id FROM fator_mut 
    WHERE nome = 'Fatores de Emissão por Tipo de Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
INSERT INTO dados_solo (fator_mut_id, tipo_fator_solo, valor_fator, descricao)
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Severamente Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.39, 'Severamente Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -2.09, 'Severamente Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -3.28, 'Severamente Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -4.17, 'Severamente Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Severamente Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.99, 'Severamente Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.49, 'Severamente Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -2.33, 'Severamente Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -2.97, 'Severamente Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Severamente Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.39, 'Moderadamente Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Moderadamente Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.70, 'Moderadamente Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.88, 'Moderadamente Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -2.78, 'Moderadamente Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.99, 'Moderadamente Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Moderadamente Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.50, 'Moderadamente Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.34, 'Moderadamente Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.98, 'Moderadamente Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Moderadamente Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 2.09, 'Não Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Severamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.70, 'Não Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Não Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Não Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.19, 'Não Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -2.08, 'Não Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.49, 'Não Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Severamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.50, 'Não Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Não Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Não Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.84, 'Não Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -1.48, 'Não Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Não Degradado → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 3.28, 'Melhorado sem uso de insumos → Severamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Severamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.88, 'Melhorado sem uso de insumos → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.19, 'Melhorado sem uso de insumos → Não Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Não Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Melhorado sem uso de insumos → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.90, 'Melhorado sem uso de insumos → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 2.33, 'Melhorado sem uso de insumos → Severamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Severamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.34, 'Melhorado sem uso de insumos → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.84, 'Melhorado sem uso de insumos → Não Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Não Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Melhorado sem uso de insumos → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', -0.64, 'Melhorado sem uso de insumos → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado sem uso de insumos → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 4.17, 'Melhorado com uso de insumos → Severamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Severamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 2.78, 'Melhorado com uso de insumos → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Moderadamente Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 2.08, 'Melhorado com uso de insumos → Não Degradado - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Não Degradado - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.90, 'Melhorado com uso de insumos → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Melhorado sem uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Melhorado com uso de insumos → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Melhorado com uso de insumos - Solo Argiloso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 2.97, 'Melhorado com uso de insumos → Severamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Severamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.98, 'Melhorado com uso de insumos → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Moderadamente Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 1.48, 'Melhorado com uso de insumos → Não Degradado - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Não Degradado - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.64, 'Melhorado com uso de insumos → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Melhorado sem uso de insumos - Solo Arenoso (IPCC, 2019)')
UNION ALL
SELECT fts.id, 'USO_ANTERIOR_ATUAL', 0.00, 'Melhorado com uso de insumos → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)' FROM fator_tipo_solo fts  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fts.id AND ds.descricao = 'Melhorado com uso de insumos → Melhorado com uso de insumos - Solo Arenoso (IPCC, 2019)');

-- Inserir dados específicos de solo - Tipos de solo (Tabela 3)
WITH fator_tipos AS (
    SELECT id FROM fator_mut 
    WHERE nome = 'Tipos de Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
INSERT INTO dados_solo (fator_mut_id, tipo_fator_solo, valor_fator, descricao)
SELECT ft.id, 'USO_ANTERIOR_ATUAL', 1, 'Argiloso' FROM fator_tipos ft  -- Changed from 'ALTO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = ft.id AND ds.descricao = 'Argiloso')
UNION ALL
SELECT ft.id, 'USO_ANTERIOR_ATUAL', 2, 'Arenoso' FROM fator_tipos ft  -- Changed from 'BAIXO_CARBONO'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = ft.id AND ds.descricao = 'Arenoso');

-- Inserir dados específicos de solo - Opções de mudança de uso (Tabela 4)
WITH fator_opcoes AS (
    SELECT id FROM fator_mut 
    WHERE nome = 'Opções de Mudança de Uso - Solo' 
    AND tipo_mudanca = 'SOLO' 
    AND escopo = 'ESCOPO3'
)
INSERT INTO dados_solo (fator_mut_id, tipo_fator_solo, valor_fator, descricao)
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 1, 'Cana-de-açúcar com queima → Cana-de-açúcar sem queima' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cana-de-açúcar com queima → Cana-de-açúcar sem queima')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 2, 'Cultivo convencional → Integração lavoura-pecuária(-floresta)' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional → Integração lavoura-pecuária(-floresta)')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 3, 'Cultivo convencional → Pastagem melhorada com insumos' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional → Pastagem melhorada com insumos')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 4, 'Cultivo convencional → Pastagem/pastagem melhorada' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional → Pastagem/pastagem melhorada')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 5, 'Cultivo convencional → Sistema agroflorestal' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional → Sistema agroflorestal')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 6, 'Cultivo convencional (Demais regiões) → Plantio direto' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional (Demais regiões) → Plantio direto')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 7, 'Cultivo convencional (Região sul) → Plantio direto' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Cultivo convencional (Região sul) → Plantio direto')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 8, 'Integração lavoura-pecuária → Cultivo convencional' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Integração lavoura-pecuária → Cultivo convencional')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 9, 'Integração lavoura-pecuária → Pastagem melhorada com insumos' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Integração lavoura-pecuária → Pastagem melhorada com insumos')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 10, 'Integração lavoura-pecuária → Pastagem/pastagem melhorada' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Integração lavoura-pecuária → Pastagem/pastagem melhorada')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 11, 'Integração lavoura-pecuária → Plantio direto' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Integração lavoura-pecuária → Plantio direto')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 12, 'Lavoura → Café' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Lavoura → Café')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 13, 'Pastagem → Café' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem → Café')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 14, 'Pastagem → Floresta plantada - eucalipto' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem → Floresta plantada - eucalipto')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 15, 'Pastagem → Regeneração Natural' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem → Regeneração Natural')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 16, 'Pastagem degradada → Cultivo convencional' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem degradada → Cultivo convencional')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 17, 'Pastagem degradada → Integração lavoura-pecuária(-floresta)' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem degradada → Integração lavoura-pecuária(-floresta)')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 18, 'Pastagem degradada → Plantio direto' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem degradada → Plantio direto')
UNION ALL
SELECT fo.id, 'SOLO_USO_ANTERIOR_ATUAL', 19, 'Pastagem não degradada → Agricultura convencional' FROM fator_opcoes fo  -- Changed from 'MINERAL'
WHERE NOT EXISTS (SELECT 1 FROM dados_solo ds WHERE ds.fator_mut_id = fo.id AND ds.descricao = 'Pastagem não degradada → Agricultura convencional');