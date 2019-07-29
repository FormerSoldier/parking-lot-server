create table if not exists customer(
`id`  varchar(255) NULL ,
`phone`  char(11) NOT NULL ,
`point`  int ZEROFILL NOT NULL ,
`times`  int ZEROFILL NOT NULL ,
`is_vip`  varchar(100) NOT NULL ,
`user_id`  varchar(255) NOT NULL ,
PRIMARY KEY (`id`),
CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_master` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;