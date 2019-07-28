create table if not exists parking_lot_dev.user_master
(
    id          int auto_increment
        primary key,
    delete_flag boolean      null default false,
    name        varchar(255) not null,
    password    varchar(255) not null,
    roles       varchar(255) null,
    username    varchar(255) not null,
    constraint UK_username
        unique (username)
)
    engine = InnoDB;

