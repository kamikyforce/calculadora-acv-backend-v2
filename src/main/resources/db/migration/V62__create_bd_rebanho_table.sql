-- V54__create_bd_rebanho_table.sql
-- Criação da tabela BD_REBANHO baseada nos dados da planilha 250717_Criacao_animal_lotes(BD_rebanho).csv

CREATE TABLE bd_rebanho (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    sistema_producao VARCHAR(50) NOT NULL,
    regiao VARCHAR(10) NOT NULL,
    codigo_4o_in VARCHAR(50) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    idade VARCHAR(100) NOT NULL,
    peso_animal_vivo DECIMAL(8,2) NOT NULL,
    peso_animal_maduro DECIMAL(8,2) NOT NULL,
    taxa_digestibilidade DECIMAL(5,2) NOT NULL,
    ganho_peso DECIMAL(8,4) NOT NULL,
    producao_leite DECIMAL(8,2) NOT NULL,
    teor_gordura_leite DECIMAL(5,2) NOT NULL,
    taxa_prenhez DECIMAL(5,2) NOT NULL,
    percentual_femeas_prenhas DECIMAL(5,2) NOT NULL,
    coeficiente_neg DECIMAL(5,2) NOT NULL,
    coeficiente_nem DECIMAL(6,3) NOT NULL,
    coeficiente_nea DECIMAL(5,2) NOT NULL,
    taxa_conversao_ch4 DECIMAL(5,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bd_rebanho_tipo_sistema ON bd_rebanho (tipo, sistema_producao);
CREATE INDEX idx_bd_rebanho_regiao ON bd_rebanho (regiao);
CREATE INDEX idx_bd_rebanho_categoria ON bd_rebanho (categoria);
CREATE INDEX idx_bd_rebanho_tipo_regiao_categoria ON bd_rebanho (tipo, regiao, categoria);

-- Inserção dos dados da planilha BD_REBANHO
INSERT INTO bd_rebanho (
    tipo, sistema_producao, regiao, codigo_4o_in, categoria, idade, 
    peso_animal_vivo, peso_animal_maduro, taxa_digestibilidade, ganho_peso, 
    producao_leite, teor_gordura_leite, taxa_prenhez, percentual_femeas_prenhas,
    coeficiente_neg, coeficiente_nem, coeficiente_nea, taxa_conversao_ch4
) VALUES
-- Região Norte (N) - Corte
('Corte', 'Pasto', 'N', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Corte', 'Pasto', 'N', 'MC>2NC', 'Boi', 'acima de 24 meses', 392.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'N', 'FC>2NC', 'Vaca', 'acima de 24 meses', 309.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Confinado', 'N', 'O>2C', 'Boi confinado', 'acima de 24 meses', 359.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.00, 6.50),
('Corte', 'Confinado', 'N', 'O>2C', 'Vaca confinada', 'acima de 24 meses', 359.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.00, 6.50),
('Corte', 'Pasto', 'N', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'N', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'N', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),

-- Região Nordeste (NE) - Corte
('Corte', 'Pasto', 'NE', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Corte', 'Pasto', 'NE', 'MC>2NC', 'Boi', 'acima de 24 meses', 362.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'NE', 'FC>2NC', 'Vaca', 'acima de 24 meses', 286.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Confinado', 'NE', 'O>2C', 'Boi confinado', 'acima de 24 meses', 333.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.00, 6.50),
('Corte', 'Confinado', 'NE', 'O>2C', 'Vaca confinada', 'acima de 24 meses', 333.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.00, 6.50),
('Corte', 'Pasto', 'NE', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'NE', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'NE', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),

-- Região Sudeste (SE) - Corte
('Corte', 'Pasto', 'SE', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Corte', 'Pasto', 'SE', 'MC>2NC', 'Boi', 'acima de 24 meses', 387.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'SE', 'FC>2NC', 'Vaca', 'acima de 24 meses', 321.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Confinado', 'SE', 'O>2C', 'Boi confinado', 'acima de 24 meses', 361.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.00, 6.50),
('Corte', 'Confinado', 'SE', 'O>2C', 'Vaca confinada', 'acima de 24 meses', 361.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.00, 6.50),
('Corte', 'Pasto', 'SE', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'SE', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'SE', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),

-- Região Sul (S) - Corte
('Corte', 'Pasto', 'S', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Corte', 'Pasto', 'S', 'MC>2NC', 'Boi', 'acima de 24 meses', 374.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'S', 'FC>2NC', 'Vaca', 'acima de 24 meses', 332.50, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Confinado', 'S', 'O>2C', 'Boi confinado', 'acima de 24 meses', 361.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.00, 6.50),
('Corte', 'Confinado', 'S', 'O>2C', 'Vaca confinada', 'acima de 24 meses', 361.50, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.00, 6.50),
('Corte', 'Pasto', 'S', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'S', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'S', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),

-- Região Centro-Oeste (CO) - Corte
('Corte', 'Pasto', 'CO', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Corte', 'Pasto', 'CO', 'MC>2NC', 'Boi', 'acima de 24 meses', 421.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'CO', 'FC>2NC', 'Vaca', 'acima de 24 meses', 323.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Confinado', 'CO', 'O>2C', 'Boi confinado', 'acima de 24 meses', 375.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.00, 6.50),
('Corte', 'Confinado', 'CO', 'O>2C', 'Vaca confinada', 'acima de 24 meses', 375.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.00, 6.50),
('Corte', 'Pasto', 'CO', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'CO', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Corte', 'Pasto', 'CO', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),

-- Região Norte (N) - Leite
('Leite', 'Pasto', 'N', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Leite', 'Pasto', 'N', 'FC>2NC', 'Vaca', 'acima de 24 meses', 309.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'N', 'FC>2NC', 'Vaca seca', 'fora de lactação', 309.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'N', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'N', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'N', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'N', 'Vaca alta produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 68.40, 0.0001, 5.48, 3.67, 0.10, 0.60, 0.80, 0.386, 0.17, 6.30),
('Leite', 'Pasto', 'N', 'Vaca baixa produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 59.80, 0.0001, 5.48, 3.99, 0.10, 0.60, 0.80, 0.386, 0.17, 6.50),

-- Região Nordeste (NE) - Leite
('Leite', 'Pasto', 'NE', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Leite', 'Pasto', 'NE', 'FC>2NC', 'Vaca', 'acima de 24 meses', 286.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'NE', 'FC>2NC', 'Vaca seca', 'fora de lactação', 286.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.10, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'NE', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'NE', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'NE', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'NE', 'Vaca alta produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 68.40, 0.0001, 5.48, 3.67, 0.10, 0.60, 0.80, 0.386, 0.17, 6.30),
('Leite', 'Pasto', 'NE', 'Vaca baixa produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 59.80, 0.0001, 5.48, 3.99, 0.10, 0.60, 0.80, 0.386, 0.17, 6.50),

-- Região Sudeste (SE) - Leite
('Leite', 'Pasto', 'SE', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Leite', 'Pasto', 'SE', 'FC>2NC', 'Vaca', 'acima de 24 meses', 286.00, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'SE', 'FC>2NC', 'Vaca seca', 'fora de lactação', 286.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.10, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'SE', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'SE', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'SE', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'SE', 'Vaca alta produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 68.40, 0.0001, 5.48, 3.67, 0.10, 0.60, 0.80, 0.386, 0.17, 6.30),
('Leite', 'Pasto', 'SE', 'Vaca baixa produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 59.80, 0.0001, 5.48, 3.99, 0.10, 0.60, 0.80, 0.386, 0.17, 6.50),

-- Região Sul (S) - Leite
('Leite', 'Pasto', 'S', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Leite', 'Pasto', 'S', 'FC>2NC', 'Vaca', 'acima de 24 meses', 332.50, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'S', 'FC>2NC', 'Vaca seca', 'fora de lactação', 332.50, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.10, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'S', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'S', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'S', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'S', 'Vaca alta produção', 'Vaca leiteira', 'em lactação', 531.00, 508.00, 68.40, 0.0001, 5.48, 3.67, 0.10, 0.55, 0.80, 0.386, 0.17, 6.30),
('Leite', 'Pasto', 'S', 'Vaca baixa produção', 'Vaca leiteira', 'em lactação', 531.00, 508.00, 59.80, 0.0001, 5.48, 3.99, 0.10, 0.55, 0.80, 0.386, 0.17, 6.50),

-- Região Centro-Oeste (CO) - Leite
('Leite', 'Pasto', 'CO', 'T>2', 'Touro', 'acima de 24 meses', 550.00, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.00, 0.00, 1.20, 0.370, 0.17, 6.50),
('Leite', 'Pasto', 'CO', 'FC>2NC', 'Vaca', 'acima de 24 meses', 332.50, 330.00, 59.80, 0.0001, 1.10, 4.90, 0.10, 0.65, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'CO', 'FC>2NC', 'Vaca seca', 'fora de lactação', 332.50, 330.00, 59.80, 0.0001, 0.00, 0.00, 0.10, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'CO', 'B<1', 'Bezerro', 'até 12 meses', 163.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'CO', '1<B<2', 'Novilha', 'entre 13 meses e o primeiro parto', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 0.80, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'CO', '1<B<2', 'Novilho', 'entre 13 e 24 meses', 293.00, 330.00, 59.80, 0.30, 0.00, 0.00, 0.00, 0.00, 1.00, 0.322, 0.36, 6.50),
('Leite', 'Pasto', 'CO', 'Vaca alta produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 68.40, 0.0001, 5.48, 3.67, 0.10, 0.60, 0.80, 0.386, 0.17, 6.30),
('Leite', 'Pasto', 'CO', 'Vaca baixa produção', 'Vaca leiteira', 'em lactação', 508.00, 508.00, 59.80, 0.0001, 5.48, 3.99, 0.10, 0.60, 0.80, 0.386, 0.17, 6.50);

-- Comentários sobre a tabela
COMMENT ON TABLE bd_rebanho IS 'Tabela contendo dados de referência para cálculos de rebanho baseados na 4ª Comunicação Nacional e IPCC 2019';