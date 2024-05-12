\! chcp 1251
CREATE DATABASE scheduler;
\c scheduler

CREATE TABLE role_list(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_(
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT REFERENCES role_list(id),
    surname VARCHAR(100),
    firstname VARCHAR(100),
    patronymic VARCHAR(100)
);

CREATE TABLE board(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creator_id INT REFERENCES user_(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE type_of_task(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE task(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_id INT REFERENCES type_of_task(id),
    descryption TEXT,
    deadline DATE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task_board(
    board_id INT REFERENCES board(id) ON DELETE CASCADE,
    task_id INT REFERENCES task(id) ON DELETE CASCADE,
    PRIMARY KEY (board_id, task_id)
);

CREATE TABLE task_user(
    user_id INT REFERENCES user_(id) ON DELETE CASCADE,
    task_id INT REFERENCES task(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, task_id)
);

CREATE TABLE checklist(
    id SERIAL PRIMARY KEY,
    board_id INT REFERENCES board(id) ON DELETE CASCADE,
    name TEXT,
    deadline DATE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE checklist_item(
    id SERIAL PRIMARY KEY,
    checklist_id INT REFERENCES checklist(id) ON DELETE CASCADE,
    name TEXT,
    deadline DATE,
    flag BOOLEAN
);