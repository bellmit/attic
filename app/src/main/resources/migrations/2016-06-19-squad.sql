--liquibase formatted sql

--changeset owen:create_constrain_schema
create schema constrain;

--changeset owen:create_squad_character_archetype
create table constrain.squad_character_archetype (
    archetype varchar
        primary key
);
insert into constrain.squad_character_archetype
values
    ('SKIRMISHER'),
    ('HUNTER'),
    ('SAGE');

--changeset owen:squad_character_gender
create table constrain.squad_character_gender (
    gender varchar
        primary key
);
insert into constrain.squad_character_gender
values
    ('F'),
    ('M');

--changeset owen:squad_character_hair
create table constrain.squad_character_hair (
    hair varchar
        primary key
);
insert into constrain.squad_character_hair
values
    ('1'),
    ('2'),
    ('3');

--changeset owen:squad_character_hat
create table constrain.squad_character_hat (
    hat varchar
        primary key
);
insert into constrain.squad_character_hat
values
    ('0'),
    ('1'),
    ('2');

--changeset owen:squad_character_outfit
create table constrain.squad_character_outfit (
    outfit varchar
        primary key
);
insert into constrain.squad_character_outfit
values
    ('1'),
    ('2'),
    ('3'),
    ('4'),
    ('5'),
    ('6');

--changeset owen:create_squad_character
create table squad_character (
    userid varchar
        not null,
    sequence bigserial -- only valid per userid
        not null,
    name varchar
        not null,
    archetype varchar
        not null
        references constrain.squad_character_archetype,
    gender varchar
        not null
        references constrain.squad_character_gender,
    hair varchar
        not null
        references constrain.squad_character_hair,
    hat varchar
        not null
        references constrain.squad_character_hat,
    outfit varchar
        not null
        references constrain.squad_character_outfit,
    primary key (userid, sequence)
);
