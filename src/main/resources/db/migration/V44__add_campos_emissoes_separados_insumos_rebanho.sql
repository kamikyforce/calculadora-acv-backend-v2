-- Migration para adicionar campos de emissões GEE separados por escopo na tabela insumos_rebanho
-- Resolve o problema onde campos de emissões do escopo 1 e escopo 3 eram confundidos

-- Adicionar campos específicos do Escopo 1
ALTER TABLE insumos_rebanho 
ADD COLUMN gee_total_escopo1 DECIMAL(15,6),
ADD COLUMN co2_fossil_escopo1 DECIMAL(15,6),
ADD COLUMN uso_terra_escopo1 DECIMAL(15,6),
ADD COLUMN ch4_fossil_escopo1 DECIMAL(15,6),
ADD COLUMN ch4_biogenico_escopo1 DECIMAL(15,6),
ADD COLUMN n2o_escopo1 DECIMAL(15,6),
ADD COLUMN outras_substancias_escopo1 DECIMAL(15,6);

-- Adicionar campos específicos do Escopo 3
ALTER TABLE insumos_rebanho 
ADD COLUMN gee_total_escopo3 DECIMAL(15,6),
ADD COLUMN gwp_100_fossil_escopo3 DECIMAL(15,6),
ADD COLUMN gwp_100_biogenico_escopo3 DECIMAL(15,6),
ADD COLUMN gwp_100_transformacao_escopo3 DECIMAL(15,6),
ADD COLUMN dioxido_carbono_fossil_escopo3 DECIMAL(15,6),
ADD COLUMN dioxido_carbono_metano_transformacao_escopo3 DECIMAL(15,6),
ADD COLUMN metano_fossil_escopo3 DECIMAL(15,6),
ADD COLUMN metano_biogenico_escopo3 DECIMAL(15,6),
ADD COLUMN oxido_nitroso_escopo3 DECIMAL(15,6),
ADD COLUMN outras_substancias_escopo3 DECIMAL(15,6);

-- Migrar dados existentes para os campos apropriados baseado no escopo
-- Para Escopo 1
UPDATE insumos_rebanho 
SET gee_total_escopo1 = gee_total,
    co2_fossil_escopo1 = co2_fossil,
    uso_terra_escopo1 = uso_terra,
    ch4_fossil_escopo1 = ch4_fossil,
    ch4_biogenico_escopo1 = ch4_biogenico,
    n2o_escopo1 = n2o,
    outras_substancias_escopo1 = outras_substancias
WHERE escopo = 'ESCOPO1';

-- Para Escopo 3
UPDATE insumos_rebanho 
SET gee_total_escopo3 = gee_total,
    gwp_100_fossil_escopo3 = gwp_100_fossil,
    gwp_100_biogenico_escopo3 = gwp_100_biogenico,
    gwp_100_transformacao_escopo3 = gwp_100_transformacao,
    dioxido_carbono_fossil_escopo3 = co2_fossil,
    dioxido_carbono_metano_transformacao_escopo3 = co2_ch4_transformacao,
    metano_fossil_escopo3 = ch4_fossil,
    metano_biogenico_escopo3 = ch4_biogenico,
    oxido_nitroso_escopo3 = n2o,
    outras_substancias_escopo3 = outras_substancias
WHERE escopo IN ('ESCOPO3_PRODUCAO', 'ESCOPO3_TRANSPORTE');

-- Comentários sobre a estratégia de migração:
-- Os campos originais serão mantidos temporariamente para compatibilidade
-- e serão removidos em uma migration futura após validação completa
-- Esta abordagem garante que cada escopo tenha seus próprios campos independentes