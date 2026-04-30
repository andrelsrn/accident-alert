CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       role VARCHAR(50),
                       active BOOLEAN DEFAULT TRUE
);

CREATE TABLE accidents (
                           id BIGSERIAL PRIMARY KEY,
                           description VARCHAR(255) NOT NULL,
                           location VARCHAR(255) NOT NULL,
                           severity VARCHAR(50),
                           created_at TIMESTAMP,
                           user_id BIGINT,
                           victim_name VARCHAR(255),
                           victim_department VARCHAR(255),
                           CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY,
                               accident_id BIGINT,
                               message VARCHAR(255),
                               recipient_email VARCHAR(255),
                               status VARCHAR(50),
                               created_at TIMESTAMP,
                               CONSTRAINT fk_accident FOREIGN KEY (accident_id) REFERENCES accidents(id)
);
