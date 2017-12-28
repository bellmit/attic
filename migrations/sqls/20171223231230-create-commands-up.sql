create table frame (
    oldest_frame bigint
        not null,
    current_frame bigint
        not null,
    command_frame bigint
        not null,
    check (command_frame > current_frame),
    check (current_frame > oldest_frame)
);
insert into frame
    values (-200, 0, 2);

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

create table events (
    frame bigint
        not null
        primary key,
    events json
        not null
);
