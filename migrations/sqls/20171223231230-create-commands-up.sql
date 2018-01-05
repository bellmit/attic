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
    values (-199, 1, 3);

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

insert into state values (1, '{}');

create table events (
    frame bigint
        not null
        primary key,
    events json
        not null
);
