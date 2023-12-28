CREATE SEQUENCE primary_sequence START WITH 10000 INCREMENT BY 1;

CREATE TABLE projects (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    owner_id VARCHAR(255) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT PK_PROJECTS PRIMARY KEY (id)
);

CREATE TABLE tasks (
    id BIGINT NOT NULL,
    name VARCHAR(255),
    cost numeric(8, 2),
    time numeric(8, 2),
    date_completed TIMESTAMP,
    completed_by VARCHAR(255),
    project_id BIGINT NOT NULL,
    parent_task_id BIGINT,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT PK_TASKS PRIMARY KEY (id)
);

CREATE TABLE users (
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    nickname VARCHAR(255),
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (email)
);

CREATE TABLE notes (
    id BIGINT NOT NULL,
    text VARCHAR(5000) NOT NULL,
    author VARCHAR(255) NOT NULL,
    project_id BIGINT,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT PK_NOTES PRIMARY KEY (id)
);

CREATE TABLE authorities (
    id BIGINT NOT NULL,
    authority VARCHAR(100),
    user_id VARCHAR(255),
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT PK_AUTHORITIES PRIMARY KEY (id)
);

CREATE TABLE read_projectses (
    email VARCHAR(255) NOT NULL,
    id BIGINT NOT NULL
);

CREATE TABLE write_projectses (
    email VARCHAR(255) NOT NULL,
    id BIGINT NOT NULL
);

ALTER TABLE projects ADD CONSTRAINT fk_projects_owner_id FOREIGN KEY (owner_id) REFERENCES users (email) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tasks ADD CONSTRAINT fk_tasks_project_id FOREIGN KEY (project_id) REFERENCES projects (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tasks ADD CONSTRAINT fk_tasks_parent_task_id FOREIGN KEY (parent_task_id) REFERENCES tasks (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE notes ADD CONSTRAINT fk_notes_project_id FOREIGN KEY (project_id) REFERENCES projects (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE authorities ADD CONSTRAINT fk_authorities_user_id FOREIGN KEY (user_id) REFERENCES users (email) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE read_projectses ADD CONSTRAINT pk_read_projectses PRIMARY KEY (email, id);

ALTER TABLE read_projectses ADD CONSTRAINT fk_read_projectses_email FOREIGN KEY (email) REFERENCES users (email) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE read_projectses ADD CONSTRAINT fk_read_projectses_id FOREIGN KEY (id) REFERENCES projects (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE write_projectses ADD CONSTRAINT pk_write_projectses PRIMARY KEY (email, id);

ALTER TABLE write_projectses ADD CONSTRAINT fk_write_projectses_email FOREIGN KEY (email) REFERENCES users (email) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE write_projectses ADD CONSTRAINT fk_write_projectses_id FOREIGN KEY (id) REFERENCES projects (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

