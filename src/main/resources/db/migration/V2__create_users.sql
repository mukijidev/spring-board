--
CREATE TABLE users (
                       id          BIGINT       NOT NULL AUTO_INCREMENT,
                       username    VARCHAR(30)  NOT NULL,
                       password    VARCHAR(100) NOT NULL,   -- BCrypt hash
                       created_at  DATETIME(6)  NOT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT uk_users_username UNIQUE (username)
);