CREATE TABLE IF NOT EXISTS gift_certificate
(
    id               BIGSERIAL PRIMARY KEY NOT NULL,
    name             VARCHAR(255)          NOT NULL UNIQUE,
    description      VARCHAR(255),
    price            NUMERIC(10, 2) CHECK (price > 0),
    duration         INT CHECK ( duration > 0 ),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tag
(
    id   BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS certificate_tag
(
    certificate_id BIGINT REFERENCES gift_certificate (id),
    tag_id         BIGINT REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (certificate_id, tag_id)
);
<<<<<<< HEAD

CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    first_name  VARCHAR(255),
    second_name VARCHAR(255),
    username    VARCHAR(25) UNIQUE    NOT NULL
);

CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    user_id       BIGINT
        CONSTRAINT user_id_key REFERENCES users (id),
    number_order  uuid,
    create_date  timestamp
);

CREATE TABLE orders_certificates
(
    id  BIGSERIAL PRIMARY KEY NOT NULL ,
    price             NUMERIC(10, 2) CHECK (price > 0),
    certificate_id    BIGINT CONSTRAINT certificate_id_key REFERENCES gift_certificate (id),
    order_id          BIGINT CONSTRAINT order_id_key REFERENCES orders (id)
)
=======

CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    first_name  VARCHAR(50),
    second_name VARCHAR(50),
    username    VARCHAR(50) UNIQUE    NOT NULL
);
CREATE TABLE IF NOT EXISTS orders
(
    id             BIGSERIAL PRIMARY KEY NOT NULL,
    user_id        BIGINT
        CONSTRAINT user_id_key REFERENCES users (id),
    certificate_id BIGINT
        CONSTRAINT certificate_id_key REFERENCES gift_certificate (id),
    price          NUMERIC(10, 2) CHECK (price > 0),
    purchase_date  TIMESTAMP
);
INSERT INTO users(first_name, second_name, username)
VALUES ('Jon', 'Fedor', 'Piksi'),
       ('Slivestr','Silver','Silver12')
>>>>>>> task-2_
