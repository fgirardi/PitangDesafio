INSERT INTO users (username, email, birthday, phone, password, data_cadastro)
VALUES ('fabiano', 'fabiano@example.com', '1978-01-01', '123456789', '$2a$10$...', NOW());

-- Associar roles ao usu�rio fabiano na tabela user_roles
INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN'); -- ADMIN
INSERT INTO user_roles (user_id, role) VALUES (1, 'USER'); -- USER
