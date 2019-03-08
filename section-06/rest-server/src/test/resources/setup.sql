CREATE TABLE status (
                      id VARCHAR,
                      status VARCHAR,
                      result INTEGER
);

CREATE TABLE tasks (
                     id SERIAL NOT NULL,
                     todo_id INTEGER,
                     text VARCHAR,
                     done boolean
);

CREATE TABLE todos (
                     id SERIAL NOT NULL,
                     text VARCHAR
);

CREATE TABLE tokens (
                      id SERIAL NOT NULL,
                      value VARCHAR
);

INSERT INTO Tokens (value) VALUES ('123');