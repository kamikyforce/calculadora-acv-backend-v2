-- V1__create_tables.sql

-- Tabela de usuários
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         cpf VARCHAR(14) UNIQUE NOT NULL,
                         data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         ativo BOOLEAN DEFAULT TRUE,
                         origem_autenticacao VARCHAR(20) NOT NULL DEFAULT 'GOVBR' CHECK (origem_autenticacao IN ('GOVBR', 'INTERNO')),
                         tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ADMINISTRADOR', 'INDUSTRIA', 'CERTIFICADORA'))
);

-- Tabela de perfis
CREATE TABLE perfil (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(50) UNIQUE NOT NULL
);

-- Tabela de administradores
CREATE TABLE administrador (
                               id SERIAL PRIMARY KEY,
                               usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                               orgao VARCHAR(100) NOT NULL,
                               perfil_id INT REFERENCES perfil(id) ON DELETE SET NULL
);

-- Tabela de certificadoras
CREATE TABLE certificadora (
                               id SERIAL PRIMARY KEY,
                               nome VARCHAR(100) NOT NULL,
                               cnpj VARCHAR(18) UNIQUE NOT NULL,
                               data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               inventarios_tratados INT DEFAULT 0,
                               estado VARCHAR(2) NOT NULL,
                               tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('FRIGORIFICO', 'LATICINIO', 'AMBOS')),
                               ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de indústrias
CREATE TABLE industria (
                           id SERIAL PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL,
                           cnpj VARCHAR(18) UNIQUE NOT NULL,
                           data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           inventarios_tratados INT DEFAULT 0,
                           estado VARCHAR(2) NOT NULL,
                           tipo VARCHAR(20) NOT NULL DEFAULT 'INDUSTRIA',
                           ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de relacionamento usuário-certificadora
CREATE TABLE usuario_certificadora (
                                       id SERIAL PRIMARY KEY,
                                       usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                                       certificadora_id INT NOT NULL REFERENCES certificadora(id) ON DELETE CASCADE,
                                       UNIQUE(usuario_id, certificadora_id)
);

-- Tabela de relacionamento usuário-indústria
CREATE TABLE usuario_industria (
                                   id SERIAL PRIMARY KEY,
                                   usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                                   industria_id INT NOT NULL REFERENCES industria(id) ON DELETE CASCADE,
                                   UNIQUE(usuario_id, industria_id)
);

-- Índices para performance
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_cpf ON usuario(cpf);
CREATE INDEX idx_usuario_tipo ON usuario(tipo);
CREATE INDEX idx_certificadora_cnpj ON certificadora(cnpj);
CREATE INDEX idx_certificadora_estado ON certificadora(estado);
CREATE INDEX idx_certificadora_tipo ON certificadora(tipo);
CREATE INDEX idx_industria_cnpj ON industria(cnpj);
CREATE INDEX idx_industria_estado ON industria(estado);
CREATE INDEX idx_administrador_usuario_id ON administrador(usuario_id);
CREATE INDEX idx_usuario_certificadora_usuario_id ON usuario_certificadora(usuario_id);
CREATE INDEX idx_usuario_certificadora_certificadora_id ON usuario_certificadora(certificadora_id);
CREATE INDEX idx_usuario_industria_usuario_id ON usuario_industria(usuario_id);
CREATE INDEX idx_usuario_industria_industria_id ON usuario_industria(industria_id);