--liquibase formatted sql

--changeset owen:domainevententry
create table domainevententry (
    aggregateidentifier varchar
        not null,
    sequencenumber bigint
        not null,
    type varchar
        not null,
    eventidentifier varchar
        not null,
    metadata varchar,
    payload varchar
        not null,
    payloadrevision varchar,
    payloadtype varchar
        not null,
    timestamp varchar
        not null,
    primary key (aggregateidentifier, sequencenumber, type)
);

--changeset owen:snapshotevententry
create table snapshotevententry (
    aggregateidentifier varchar
        not null,
    sequencenumber bigint
        not null,
    type varchar
        not null,
    eventidentifier varchar
        not null,
    metadata varchar,
    payload varchar
        not null,
    payloadrevision varchar,
    payloadtype varchar
        not null,
    timestamp varchar
        not null,
    primary key (aggregateidentifier, sequencenumber, type)
);
