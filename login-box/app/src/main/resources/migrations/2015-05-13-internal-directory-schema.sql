--liquibase formatted sql

--changeset owen:create_directory_schema
create schema directory;
comment on schema directory is
    $$Stores login-box's internal user directory.$$;

--changeset owen:create_directory_user_table
create table directory.user (
    id uuid
        not null
        primary key,
    directory_id uuid
        not null
        references config.internal_directory,
    username varchar
        not null,
    contact_email varchar
        not null,
    password varchar
        not null,
    unique (directory_id, username)
);
comment on table directory.user is
    $$Users stored per internal directory. Under normal operation, there is only one internal directory, but multiple are supported in the schema for consistency with other directory types. Login Box will work correctly if you manually configure multiple internal directories.$$;
