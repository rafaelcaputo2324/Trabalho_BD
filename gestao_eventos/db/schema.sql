-- Scripts DDL para Criação do Banco de Dados de Gestão de Eventos (H2 Database)

-- Remoção de tabelas na ordem inversa de dependência para evitar erros de FK
DROP TABLE IF EXISTS certificados;
DROP TABLE IF EXISTS atividades;
DROP TABLE IF EXISTS palestrantes;
DROP TABLE IF EXISTS inscricoes;
DROP TABLE IF EXISTS eventos;
DROP TABLE IF EXISTS participantes;

-- 1. Tabela de Participantes
CREATE TABLE participantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    CONSTRAINT unique_email_participante UNIQUE (email)
);

-- 2. Tabela de Eventos
CREATE TABLE eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),
    data_evento DATE NOT NULL,
    local VARCHAR(150) NOT NULL,
    capacidade_maxima INT NOT NULL CHECK (capacidade_maxima > 0)
);

-- 3. Tabela de Palestrantes
CREATE TABLE palestrantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100),
    CONSTRAINT unique_email_palestrante UNIQUE (email)
);

-- 4. Tabela de Inscrições (Relacionamento N:N entre Participante e Evento)
CREATE TABLE inscricoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    participante_id INT NOT NULL,
    evento_id INT NOT NULL,
    data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Confirmada' CHECK (status IN ('Pendente', 'Confirmada', 'Cancelada')),
    FOREIGN KEY (participante_id) REFERENCES participantes(id) ON DELETE CASCADE,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    CONSTRAINT unique_inscricao UNIQUE (participante_id, evento_id)
);

-- 5. Tabela de Atividades (Palestras/Workshops pertencentes a um Evento)
CREATE TABLE atividades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    evento_id INT NOT NULL,
    palestrante_id INT,
    titulo VARCHAR(150) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    duracao_minutos INT NOT NULL CHECK (duracao_minutos > 0),
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    FOREIGN KEY (palestrante_id) REFERENCES palestrantes(id) ON DELETE SET NULL
);

-- 6. Tabela de Certificados (Ligada a uma Inscrição Confirmada)
CREATE TABLE certificados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inscricao_id INT NOT NULL,
    codigo_autenticacao VARCHAR(50) NOT NULL,
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inscricao_id) REFERENCES inscricoes(id) ON DELETE CASCADE,
    CONSTRAINT unique_inscricao_certificado UNIQUE (inscricao_id),
    CONSTRAINT unique_codigo_certificado UNIQUE (codigo_autenticacao)
);

-- Inserção de dados para teste inicial

-- Participantes
INSERT INTO participantes (nome, email, telefone) VALUES 
('Carlos Silva', 'carlos.silva@email.com', '(11) 98888-1111'),
('Ana Souza', 'ana.souza@email.com', '(11) 98888-2222'),
('Marcos Oliveira', 'marcos.oliveira@email.com', '(21) 97777-3333'),
('Julia Costa', 'julia.costa@email.com', '(31) 96666-4444'),
('Felipe Santos', 'felipe.santos@email.com', '(11) 95555-5555');

-- Eventos
INSERT INTO eventos (titulo, descricao, data_evento, local, capacidade_maxima) VALUES 
('Semana de Tecnologia 2026', 'Congresso sobre inovação, desenvolvimento de software e Inteligência Artificial.', '2026-09-15', 'Auditório Principal e Online', 150),
('Workshop de Java Swing Avançado', 'Prática intensiva de desenvolvimento de interfaces modernas com Swing e FlatLaf.', '2026-10-10', 'Laboratório de Informática 3', 3),
('Simpósio de Banco de Dados', 'Discussões sobre modelagem, otimização de consultas e NoSQL.', '2026-11-05', 'Auditório Bloco B', 50);

-- Palestrantes
INSERT INTO palestrantes (nome, email, especialidade) VALUES 
('Dr. Roberto Mendes', 'roberto.mendes@email.com', 'Inteligência Artificial e Big Data'),
('Profa. Helena Dias', 'helena.dias@email.com', 'Engenharia de Software e Java'),
('Msc. André Lima', 'andre.lima@email.com', 'Administração de Bancos de Dados');

-- Atividades dos Eventos
INSERT INTO atividades (evento_id, palestrante_id, titulo, data_hora, duracao_minutos) VALUES 
(1, 1, 'O Futuro dos Modelos de Linguagem na Indústria', '2026-09-15 09:00:00', 90),
(1, 2, 'Desenvolvimento Eficiente de APIs com Java', '2026-09-15 11:00:00', 60),
(2, 2, 'Criando Componentes Customizados no Swing', '2026-10-10 14:00:00', 180),
(3, 3, 'Otimização de Índices e Planos de Execução', '2026-11-05 10:00:00', 120);

-- Inscrições de teste
-- Evento 1 tem capacidade para 150 (está tranquilo)
INSERT INTO inscricoes (participante_id, evento_id, status) VALUES 
(1, 1, 'Confirmada'),
(2, 1, 'Confirmada'),
(3, 1, 'Confirmada');

-- Evento 2 tem capacidade para 3 (para testar a regra de negócio do limite de capacidade)
INSERT INTO inscricoes (participante_id, evento_id, status) VALUES 
(1, 2, 'Confirmada'),
(2, 2, 'Confirmada'),
(3, 2, 'Confirmada'); -- Atingiu a capacidade máxima de 3!

-- Certificados de teste (só para inscrições confirmadas)
INSERT INTO certificados (inscricao_id, codigo_autenticacao) VALUES 
(1, 'CERT-2026-ST-0001'),
(2, 'CERT-2026-ST-0002');
