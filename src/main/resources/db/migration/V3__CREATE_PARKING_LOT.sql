create table if not exists parking_lot
(
	id varchar(255) not null
		primary key,
	address varchar(255) null,
	capacity int not null,
	name varchar(255) not null
)    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;