-- Correção de colunas na tabela insumos_producao_agricola
-- Corrige nomes de colunas e adiciona campos faltantes

-- 1. Renomear colunas com nomes incorretos
ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN fator_conversao_valor TO fator_conversao;

ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN fator_conversao_unidade TO unidade_fator_conversao;

ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN quantidade_produto_referencia TO qtd_produto_referencia;

ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN grupo_ingrediente_alimentar TO grupo_ingrediente;

ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN gwp_100_transformacao_uso_solo TO gwp_100_transformacao;

ALTER TABLE insumos_producao_agricola 
    RENAME COLUMN data_atualizacao TO ultima_atualizacao;

-- 2. Adicionar coluna faltante
ALTER TABLE insumos_producao_agricola 
    ADD COLUMN unidade_produto VARCHAR(50);

-- 3. Remover colunas duplicadas/desnecessárias do Escopo 3
-- Estas colunas são redundantes pois já temos os campos principais
ALTER TABLE insumos_producao_agricola 
    DROP COLUMN IF EXISTS co2_fossil_escopo3;

ALTER TABLE insumos_producao_agricola 
    DROP COLUMN IF EXISTS ch4_fossil_escopo3;

ALTER TABLE insumos_producao_agricola 
    DROP COLUMN IF EXISTS ch4_biogenico_escopo3;

ALTER TABLE insumos_producao_agricola 
    DROP COLUMN IF EXISTS n2o_escopo3;

-- 4. Comentários atualizados
COMMENT ON COLUMN insumos_producao_agricola.fator_conversao IS 'Valor do fator de conversão (Escopo 1)';
COMMENT ON COLUMN insumos_producao_agricola.unidade_fator_conversao IS 'Unidade do fator de conversão (Escopo 1)';
COMMENT ON COLUMN insumos_producao_agricola.qtd_produto_referencia IS 'Quantidade do produto de referência (Escopo 3)';
COMMENT ON COLUMN insumos_producao_agricola.grupo_ingrediente IS 'Grupo do ingrediente alimentar (Escopo 3)';
COMMENT ON COLUMN insumos_producao_agricola.unidade_produto IS 'Unidade do produto (Escopo 3)';
COMMENT ON COLUMN insumos_producao_agricola.ultima_atualizacao IS 'Data da última atualização do registro';