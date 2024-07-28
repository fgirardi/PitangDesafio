-- Cria as roles ADMIN e USER
INSERT INTO user_role (role) VALUES ('admin');
INSERT INTO user_role (role) VALUES ('user');

-- Cria o usuario padrao FABIANO
INSERT INTO users (first_name, last_name, email, birthday, phone, password, data_cadastro)
VALUES ('Fabiano', 'Sobrenome', 'fabiano@example.com', '1980-01-01', '123456789', '{bcrypt}$2a$10$DowJcJ7uJ7IjF.Jdj7h3ZuStl', NOW());

-- Relaciona o usuario FABIANO com as roles ADMIN e USER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id_person, r.id
FROM users u, user_role r
WHERE u.email = 'fabiano@example.com' AND (r.role = 'admin' OR r.role = 'user');
