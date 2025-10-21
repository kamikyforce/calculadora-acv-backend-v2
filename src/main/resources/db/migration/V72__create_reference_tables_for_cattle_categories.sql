-- Migration V68: Criar tabelas de referência para categorias de gado

-- Tabela de categorias de corte
CREATE TABLE bd_categorias_corte (
    id BIGSERIAL PRIMARY KEY,
    categoria VARCHAR(50) NOT NULL,
    idade VARCHAR(100) NOT NULL,
    CONSTRAINT uk_categoria_corte UNIQUE (categoria, idade)
);

-- Tabela de categorias de leite
CREATE TABLE bd_categorias_leite (
    id BIGSERIAL PRIMARY KEY,
    categoria VARCHAR(50) NOT NULL,
    idade VARCHAR(100) NOT NULL,
    CONSTRAINT uk_categoria_leite UNIQUE (categoria, idade)
);

-- Tabela de códigos 4CN
CREATE TABLE bd_codigos_4cn (
    id BIGSERIAL PRIMARY KEY,
    categoria_calculadora VARCHAR(50) NOT NULL UNIQUE,
    codigo_4cn VARCHAR(10) NOT NULL
);

-- Inserir dados de categorias de corte
INSERT INTO bd_categorias_corte (categoria, idade) VALUES
('Bezerra', 'até 12 meses'),
('Novilho', 'entre 13 e 24 meses'),
('Novilha', 'entre 13 meses e o primeiro parto'),
('Novilho confinado', 'entre 13 e 24 meses'),
('Novilha confinada', 'entre 13 meses e o primeiro parto'),
('Boi', 'acima de 24 meses'),
('Vaca', 'acima de 24 meses'),
('Boi confinado', 'acima de 24 meses'),
('Vaca confinada', 'acima de 24 meses'),
('Touro', 'acima de 24 meses');

-- Inserir dados de categorias de leite
INSERT INTO bd_categorias_leite (categoria, idade) VALUES
('Bezerra', 'até 12 meses'),
('Novilha', 'entre 13 meses e o primeiro parto'),
('Vaca leiteira', 'em lactação'),
('Vaca seca', 'fora de lactação'),
('Touro', 'acima de 24 meses');

-- Inserir códigos 4CN
INSERT INTO bd_codigos_4cn (categoria_calculadora, codigo_4cn) VALUES
('Touro', 'T>2'),
('Boi', 'MC>2NC'),
('Vaca', 'FC>2NC'),
('Boi confinado', 'O>2C'),
('Vaca confinada', 'O>2C'),
('Bezerra', 'B<1'),
('Novilho', '1<B<2'),
('Novilha', '1<B<2');