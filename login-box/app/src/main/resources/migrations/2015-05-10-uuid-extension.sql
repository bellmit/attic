--liquibase formatted sql

--changeset owen:install_uuid_extension
create extension "uuid-ossp";
