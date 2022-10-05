CREATE TABLE gift_certificate
(
    id               BIGSERIAL PRIMARY KEY NOT NULL,
    name             VARCHAR(255)          NOT NULL UNIQUE,
    description      VARCHAR(255),
    price            NUMERIC(10, 2) CHECK (price > 0),
    duration         INT CHECK ( duration > 0 ),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP
);

CREATE TABLE tag
(
    id   BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE certificate_tag
(
    certificate_id BIGINT REFERENCES gift_certificate (id),
    tag_id         BIGINT REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (certificate_id, tag_id)
);

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
);

INSERT INTO users(first_name, second_name, username)
VALUES ('Jon', 'Fedor', 'Piksi'),
       ('Slivestr','Silver','Silver12')



