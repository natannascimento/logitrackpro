CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL
);

-- Usuário seed para login em ambiente de avaliação/demo (não há tela de cadastro no escopo).
-- Senha em texto claro: admin123 — hash BCrypt gerado offline.
INSERT INTO usuarios (email, senha) VALUES
('admin@logitrackpro.com', '$2b$10$LMfC8jxbAlVLxxkVKqGgt.tHiQnUWsIkjLZ3cGinncY.tC378qw9G');
