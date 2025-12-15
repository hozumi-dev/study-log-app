create table if not exists study_log (
    id bigserial primary key,
    memo varchar(255),
    minutes integer,
    study_data data,
    subject varchar(255)
);