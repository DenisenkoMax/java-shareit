CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(512) NOT NULL,
    requestor_id BIGINT       NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT FK_ITEM_REQUEST_REQUESTOR FOREIGN KEY (requestor_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(512),
    available   BOOLEAN,
    owner_id    BIGINT       NOT NULL,
    request_id  BIGINT,
    CONSTRAINT FK_ITEM_OWNER FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT FK_ITEM_REQUEST FOREIGN KEY (request_id) REFERENCES requests (id)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT                      NOT NULL,
    booker_id  BIGINT                      NOT NULL,
    status     VARCHAR                     NOT NULL,
    CONSTRAINT FK_BOOKING_BOOKER FOREIGN KEY (booker_id) REFERENCES users (id),
    CONSTRAINT FK_BOOKING_ITEM FOREIGN KEY (item_id) REFERENCES items (id)
    );


CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text      VARCHAR(512) NOT NULL,
    item_id   BIGINT       NOT NULL,
    author_id BIGINT       NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT FK_COMMENT_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT FK_COMMENT_ITEM FOREIGN KEY (item_id) REFERENCES items (id)
    );
