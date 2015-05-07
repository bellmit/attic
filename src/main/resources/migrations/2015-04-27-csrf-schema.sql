--liquibase formatted sql

--changeset owen:create_csrf_schema
create schema csrf;
comment on schema csrf is
    $$Stores cross-site request forgery secrets and state.$$;

--changeset owen:create_csrf_tokens_table
create table csrf.token (
    session varchar
        not null,
    secret varchar
        not null,
    issued_at timestamp with time zone
        not null,
    primary key (session, secret)
);
create index token_issued_at
    on csrf.token (issued_at);
comment on table csrf.token is
    $$CSRF session-to-token bindings.$$;
