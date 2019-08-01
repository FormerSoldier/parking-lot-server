create table if not exists expense_rate
(
    id           bigint not null
        primary key,
    expense_rate double not null,
    status       bit    not null
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

INSERT INTO expense_rate (id, expense_rate, status)
VALUES (1, 10, false);