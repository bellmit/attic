create table frame (
    current_frame bigint
        not null
);
insert into frame
    values (0);

create table command (
    frame bigint
        not null,
    seq bigserial
        not null,
    command json
        not null,
    primary key (frame, seq)
);

create table state (
    frame bigint
        primary key,
    state json
        not null
);
insert into state
    values (0, '{}');

create table events (
    frame bigint
        not null
        primary key,
    events json
        not null
);
