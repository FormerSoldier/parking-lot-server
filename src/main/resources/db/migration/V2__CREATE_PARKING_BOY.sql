create table if not exists parking_boy
(
    id        varchar(255) not null
        primary key,
    gender    int          not null,
    join_time datetime     not null,
    name      varchar(255) not null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;