-- Flyway Migration: Importação dos dados MUT - Base | Desmatamento (Tabela 6)
-- Baseado na planilha oficial extraída conforme regras de negócio
-- Generated at 2025-01-27

-- Inserir fator_mut principal para dados de desmatamento
INSERT INTO fator_mut (nome, descricao, tipo_mudanca, escopo, ativo)
SELECT 'Estoque de Carbono por Desmatamento', 'Estoques de carbono por bioma e fitofisionomia para cálculo de emissões por desmatamento', 'DESMATAMENTO', 'ESCOPO3', true
WHERE NOT EXISTS (
    SELECT 1 FROM fator_mut WHERE nome = 'Estoque de Carbono por Desmatamento'
);

-- Inserir dados de desmatamento para AMAZONIA
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Aa/Floresta Ombrófila Aberta Aluvial',
    'F',
    532.77,
    true,
    'Aa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Aa/Floresta Ombrófila Aberta Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Ab/Floresta Ombrófila Aberta Terras Baixas',
    'F',
    608.30,
    true,
    'Ab'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Ab/Floresta Ombrófila Aberta Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'As/Floresta Ombrófila Aberta Submontana',
    'F',
    484.00,
    true,
    'As'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'As/Floresta Ombrófila Aberta Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Cb/Floresta Estacional Decidual Terras Baixas',
    'F',
    216.70,
    true,
    'Cb'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Cb/Floresta Estacional Decidual Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Cs/Floresta Estacional Decidual Submontana',
    'F',
    475.93,
    true,
    'Cs'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Cs/Floresta Estacional Decidual Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Da/Floresta Ombrófila Densa Aluvial',
    'F',
    479.23,
    true,
    'Da'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Da/Floresta Ombrófila Densa Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Db/Floresta Ombrófila Densa de Terras Baixas',
    'F',
    679.43,
    true,
    'Db'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Db/Floresta Ombrófila Densa de Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Dm/Floresta Ombrófila Densa Montana',
    'F',
    508.57,
    true,
    'Dm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Dm/Floresta Ombrófila Densa Montana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Ds/Floresta Ombrófila Densa Submontana',
    'F',
    737.37,
    true,
    'Ds'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Ds/Floresta Ombrófila Densa Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Fa/Floresta Estacional Semidecidual aluvial',
    'F',
    240.53,
    true,
    'Fa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Fa/Floresta Estacional Semidecidual aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Fb/Floresta Estacional Semidecidual de terras baixas',
    'F',
    299.57,
    true,
    'Fb'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Fb/Floresta Estacional Semidecidual de terras baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Fm/Floresta Estacional Semidecidual montana',
    'F',
    461.27,
    true,
    'Fm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Fm/Floresta Estacional Semidecidual montana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Fs/Floresta Estacional Semidecidual Submontana',
    'F',
    325.97,
    true,
    'Fs'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Fs/Floresta Estacional Semidecidual Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'La/Campinarana Arborizada',
    'F',
    1109.90,
    true,
    'La'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'La/Campinarana Arborizada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Lb/Campinarana Arbustiva',
    'OFL',
    694.10,
    true,
    'Lb'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Lb/Campinarana Arbustiva'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Ld/Campinarana Florestada',
    'F',
    482.53,
    true,
    'Ld'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Ld/Campinarana Florestada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Lg/Campinarana gramíneo lenhosa',
    'G',
    443.67,
    true,
    'Lg'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Lg/Campinarana gramíneo lenhosa'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Pa/Vegetação com influência fluvial e/ou lacustre',
    'OFL',
    213.40,
    true,
    'Pa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Pa/Vegetação com influência fluvial e/ou lacustre'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Pf/Pioneiras com influência fluviomarinha (mangue)',
    'F',
    140.43,
    true,
    'Pf'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Pf/Pioneiras com influência fluviomarinha (mangue)'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Pm/Pioneiras com influência Marinha (restinga)',
    'F',
    127.60,
    true,
    'Pm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Pm/Pioneiras com influência Marinha (restinga)'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Rm/Refúgio montano',
    'OFL',
    330.73,
    true,
    'Rm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Rm/Refúgio montano'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Sa/Savana Arborizada',
    'F',
    634.70,
    true,
    'Sa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Sa/Savana Arborizada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Sd/Savana Florestada',
    'F',
    550.00,
    true,
    'Sd'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Sd/Savana Florestada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Sg/Savana Gramíneo-Lenhosa',
    'G',
    182.60,
    true,
    'Sg'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Sg/Savana Gramíneo-Lenhosa'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Sp/Savana Parque',
    'OFL',
    418.00,
    true,
    'Sp'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Sp/Savana Parque'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    '* Ta/Savana Estépica Arborizada',
    'F',
    472.63,
    true,
    'Ta'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = '* Ta/Savana Estépica Arborizada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Td/Savana Estépica Florestada',
    'F',
    92.40,
    true,
    'Td'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Td/Savana Estépica Florestada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    '* Tg/Savana Estépica Gramíneo Lenhosa',
    'G',
    64.17,
    true,
    'Tg'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = '* Tg/Savana Estépica Gramíneo Lenhosa'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Tp/Savana Estépica Parque',
    'OFL',
    97.17,
    true,
    'Tp'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Tp/Savana Estépica Parque'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Am/Floresta Ombrófila Aberta Montana',
    'F',
    143.73,
    true,
    'Am'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Am/Floresta Ombrófila Aberta Montana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'AMAZONIA',
    'Ca/Floresta Estacional Decidual Aluvial',
    'F',
    394.90,
    true,
    'Ca'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'AMAZONIA' 
    AND dd.nome_fitofisionomia = 'Ca/Floresta Estacional Decidual Aluvial'
);

-- Inserir dados de desmatamento para CAATINGA
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CAATINGA',
    'Aa/Floresta Ombrófila Aberta Aluvial',
    'F',
    210.21,
    true,
    'Aa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CAATINGA' 
    AND dd.nome_fitofisionomia = 'Aa/Floresta Ombrófila Aberta Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CAATINGA',
    'Ab/Floresta Ombrófila Aberta Terras Baixas',
    'F',
    210.21,
    true,
    'Ab'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CAATINGA' 
    AND dd.nome_fitofisionomia = 'Ab/Floresta Ombrófila Aberta Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CAATINGA',
    'Ta/Savana Estépica Arborizada (caatinga aberta)',
    'F',
    67.03,
    true,
    'Ta'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CAATINGA' 
    AND dd.nome_fitofisionomia = 'Ta/Savana Estépica Arborizada (caatinga aberta)'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CAATINGA',
    'Td/Savana Estépica Florestada (caatinga densa)',
    'F',
    158.91,
    true,
    'Td'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CAATINGA' 
    AND dd.nome_fitofisionomia = 'Td/Savana Estépica Florestada (caatinga densa)'
);

-- Inserir dados de desmatamento para CERRADO
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CERRADO',
    'Aa/Floresta Ombrófila Aberta Aluvial',
    'F',
    532.77,
    true,
    'Aa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CERRADO' 
    AND dd.nome_fitofisionomia = 'Aa/Floresta Ombrófila Aberta Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'CERRADO',
    'Ab/Floresta Ombrófila Aberta das Terras Baixas',
    'F',
    608.26,
    true,
    'Ab'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'CERRADO' 
    AND dd.nome_fitofisionomia = 'Ab/Floresta Ombrófila Aberta das Terras Baixas'
);

-- Inserir dados de desmatamento para MATA_ATLANTICA
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'MATA_ATLANTICA',
    'Aa/Floresta Ombrófila Aberta - Aluvial',
    'F',
    172.66,
    true,
    'Aa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'MATA_ATLANTICA' 
    AND dd.nome_fitofisionomia = 'Aa/Floresta Ombrófila Aberta - Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'MATA_ATLANTICA',
    'D/Floresta Ombrófila Densa (Floresta Tropical Pluvial)',
    'F',
    308.07,
    true,
    'D'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'MATA_ATLANTICA' 
    AND dd.nome_fitofisionomia = 'D/Floresta Ombrófila Densa (Floresta Tropical Pluvial)'
);

-- Inserir dados de desmatamento para PAMPA
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PAMPA',
    'Ca/Floresta Estacional Decidual - Aluvial',
    'F',
    499.29,
    true,
    'Ca'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PAMPA' 
    AND dd.nome_fitofisionomia = 'Ca/Floresta Estacional Decidual - Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PAMPA',
    'E/Estepe',
    'F',
    34.47,
    true,
    'E'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PAMPA' 
    AND dd.nome_fitofisionomia = 'E/Estepe'
);

-- Inserir dados de desmatamento para PANTANAL
INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Aa/Floresta Ombrófila Aberta Aluvial',
    'F',
    532.77,
    true,
    'Aa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Aa/Floresta Ombrófila Aberta Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Ab/Floresta Ombrófila Aberta Terras Baixas',
    'F',
    608.30,
    true,
    'Ab'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Ab/Floresta Ombrófila Aberta Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'As/Floresta Ombrófila Aberta Submontana',
    'F',
    484.00,
    true,
    'As'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'As/Floresta Ombrófila Aberta Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Cb/Floresta Estacional Decidual Terras Baixas',
    'F',
    216.70,
    true,
    'Cb'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Cb/Floresta Estacional Decidual Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Cs/Floresta Estacional Decidual Submontana',
    'F',
    475.93,
    true,
    'Cs'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Cs/Floresta Estacional Decidual Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Da/Floresta Ombrófila Densa Aluvial',
    'F',
    479.23,
    true,
    'Da'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Da/Floresta Ombrófila Densa Aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Db/Floresta Ombrófila Densa de Terras Baixas',
    'F',
    679.43,
    true,
    'Db'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Db/Floresta Ombrófila Densa de Terras Baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Dm/Floresta Ombrófila Densa Montana',
    'F',
    508.57,
    true,
    'Dm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Dm/Floresta Ombrófila Densa Montana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Ds/Floresta Ombrófila Densa Submontana',
    'F',
    737.37,
    true,
    'Ds'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Ds/Floresta Ombrófila Densa Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Fa/Floresta Estacional Semidecidual aluvial',
    'F',
    240.53,
    true,
    'Fa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Fa/Floresta Estacional Semidecidual aluvial'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Fb/Floresta Estacional Semidecidual de terras baixas',
    'F',
    299.57,
    true,
    'Fb'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Fb/Floresta Estacional Semidecidual de terras baixas'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Fm/Floresta Estacional Semidecidual montana',
    'F',
    461.27,
    true,
    'Fm'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Fm/Floresta Estacional Semidecidual montana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Fs/Floresta Estacional Semidecidual Submontana',
    'F',
    325.97,
    true,
    'Fs'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Fs/Floresta Estacional Semidecidual Submontana'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Sa/Savana Arborizada',
    'F',
    634.70,
    true,
    'Sa'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Sa/Savana Arborizada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Sd/Savana Florestada',
    'F',
    550.00,
    true,
    'Sd'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Sd/Savana Florestada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Sg/Savana Gramíneo-Lenhosa',
    'G',
    182.60,
    true,
    'Sg'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Sg/Savana Gramíneo-Lenhosa'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Sp/Savana Parque',
    'OFL',
    418.00,
    true,
    'Sp'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Sp/Savana Parque'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Ta/Savana Estépica Arborizada',
    'F',
    472.63,
    true,
    'Ta'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Ta/Savana Estépica Arborizada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Td/Savana Estépica Florestada',
    'F',
    92.40,
    true,
    'Td'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Td/Savana Estépica Florestada'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Tg/Savana Estépica Gramíneo Lenhosa',
    'G',
    64.17,
    true,
    'Tg'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Tg/Savana Estépica Gramíneo Lenhosa'
);

INSERT INTO dados_desmatamento (fator_mut_id, bioma, nome_fitofisionomia, categoria_desmatamento, estoque_carbono, valor_unico, sigla_fitofisionomia)
SELECT 
    fm.id,
    'PANTANAL',
    'Tp/Savana Estépica Parque',
    'OFL',
    97.17,
    true,
    'Tp'
FROM fator_mut fm 
WHERE fm.nome = 'Estoque de Carbono por Desmatamento'
AND NOT EXISTS (
    SELECT 1 FROM dados_desmatamento dd 
    WHERE dd.fator_mut_id = fm.id 
    AND dd.bioma = 'PANTANAL' 
    AND dd.nome_fitofisionomia = 'Tp/Savana Estépica Parque'
);