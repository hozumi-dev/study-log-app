create table if not exists public.study_log (
    id bigserial primary key,
    memo varchar(255),
    minutes integer,
    study_data date,
    subject varchar(255)
);

alter table public.study_log
    add column if not exists seconds integer;

update public.study_log
set seconds = coalesce(minutes, 0) * 60
where seconds is null;