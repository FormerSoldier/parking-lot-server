CREATE TABLE `parking_lot` (
      `id` varchar(255) NOT NULL,
      `address` varchar(255) DEFAULT NULL,
      `capacity` int(11) DEFAULT NULL,
      `name` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

