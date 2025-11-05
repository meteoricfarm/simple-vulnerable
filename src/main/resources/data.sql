CREATE TABLE users (
    id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL
);

INSERT INTO users (id, username) VALUES (1, 'alice');
INSERT INTO users (id, username) VALUES (2, 'bob');