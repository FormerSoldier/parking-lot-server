create table if not exists customer
(
    id          varchar(255) not null
        primary key,
    create_time datetime     null,
    isvip       bit          not null,
    phone       varchar(255) not null,
    point       int          not null,
    times       int          not null,
    update_time datetime     null,
    user_id     int          null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
create index FK_CUSTOMER_USER_ID
    on customer (user_id);
create table if not exists parking_boy
(
    id                 varchar(255) not null
        primary key,
    create_time        datetime     null,
    free               bit          not null,
    gender             int          not null,
    join_time          datetime     not null,
    name               varchar(255) not null,
    order_num_in_close int          not null,
    order_num_in_open  int          not null,
    status             varchar(255) not null,
    update_time        datetime     null,
    user_id            int          not null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
create index FK_PARKING_BOY_USER_ID
    on parking_boy (user_id);
create table if not exists parking_boy_parking_lot_list
(
    parking_boy_id      varchar(255) not null,
    parking_lot_list_id varchar(255) not null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
create index FK_PB_PL_PL_ID
    on parking_boy_parking_lot_list (parking_lot_list_id);
create index FK_PB_PL_PB_ID
    on parking_boy_parking_lot_list (parking_boy_id);
create table if not exists parking_lot
(
    id            varchar(255) not null
        primary key,
    address       varchar(255) null,
    capacity      int          not null,
    create_time   datetime     null,
    name          varchar(255) not null,
    update_time   datetime     null,
    used_capacity int          not null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
create table if not exists parking_order
(
    id                  varchar(255) not null
        primary key,
    car_no              varchar(255) not null,
    create_time         datetime     null,
    end_time            datetime     null,
    fetch_time          datetime     null,
    number              varchar(255) null,
    order_status        varchar(255) not null,
    price               double       not null,
    start_time          datetime     null,
    submit_time         datetime     null,
    update_time         datetime     null,
    customer_id         varchar(255) not null,
    fetch_parkingboy_id varchar(255) null,
    park_parkingboy_id  varchar(255) null,
    parkinglot_id       varchar(255) null,
    constraint UK_NUMBER
        unique (number)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
create index FK_PARKING_ORDER_PARKING_LOT_ID
    on parking_order (parkinglot_id);
create index FK_PARKING_ORDER_PARKING_FETCH_BOY_ID
    on parking_order (fetch_parkingboy_id);
create index FK_PARKING_ORDER_PARKING_PARK_BOY_ID
    on parking_order (park_parkingboy_id);
create index FK_PARKING_ORDER_CUSTOMER_ID
    on parking_order (customer_id);
create table if not exists user_master
(
    id          int auto_increment
        primary key,
    create_time datetime     null,
    delete_flag bit          null,
    name        varchar(255) not null,
    password    varchar(255) not null,
    roles       varchar(255) null,
    update_time datetime     null,
    username    varchar(255) not null,
    constraint UK_USERNAME
        unique (username)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
INSERT INTO user_master (id, create_time, delete_flag, name, password, roles, update_time, username)
VALUES (1, '2019-07-30 15:14:43', false, 'SuperManager',
        'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'ADMIN', '2019-07-30 15:14:43',
        'supermanager');
INSERT INTO user_master (id, create_time, delete_flag, name, password, roles, update_time, username)
VALUES (2, '2019-07-30 15:15:05', false, 'MASTER', 'fc613b4dfd6736a7bd268c8a0e74ed0d1c04a959f59dd74ef2874983fd443fc9',
        'ADMIN', '2019-07-30 15:15:05', 'admin');
INSERT INTO user_master (id, create_time, delete_flag, name, password, roles, update_time, username)
VALUES (3, '2019-07-30 15:16:13', false, 'IVY_BOSS', 'a5e7c002443743c5836758c7d1cd8ddefd9fcf2061daa0efaac683fb99966057',
        'ADMIN', '2019-07-30 15:16:13', 'ivyboss');