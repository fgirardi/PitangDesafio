INSERT INTO users (email, password, data_cadastro)
VALUES ('fabiano@example.com', '$2a$10$...', NOW());

-- Associar roles ao usu�rio fabiano na tabela user_roles
INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN'); -- ADMIN
INSERT INTO user_roles (user_id, role) VALUES (1, 'USER'); -- USER
