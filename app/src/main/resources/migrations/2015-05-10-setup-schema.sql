--liquibase formatted sql

--changeset owen:create_config_schema
create schema config;
comment on schema config is
    $$Stores login-box configuration.$$;

--changeset owen:create_config_setup_completed_table
create table config.setup_completed (
    completed boolean
        primary key
        default true
        check (completed = true),
    completed_at timestamp with time zone
        not null
);
comment on table config.setup_completed is
    $$Flag table to indicate that setup has completed.$$;

--changeset owen:create_config_directory_type_table
create table config.directory_type (
    type varchar
        primary key
);
comment on table config.directory_type is
    $$Constraint on supported directory types.$$;
insert into config.directory_type
values
    ('internal');

--changeset owen:create_config_directory_table
create table config.directory (
    id uuid
        primary key,
    priority bigserial
        unique
        not null,
    type varchar
        not null
        references config.directory_type,
    unique (id, type)
);
comment on table config.directory is
    $$Configured directories.$$;
comment on column config.directory.priority is
    $$Lower number = higher priority. First directory for a user wins.$$;

--changeset owen:create_config_internal_directory_table
create table config.internal_directory (
    id uuid
        primary key,
    type varchar
        not null
        default 'internal'
        check (type = 'internal')
        references config.directory_type,
    foreign key (id, type)
        references config.directory (id, type)
);
comment on table config.internal_directory is
    $$Configuration specific to internal directories. Also provides referential integrity for internal directory users.$$;
