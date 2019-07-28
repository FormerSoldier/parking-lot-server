SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `parking_boy`
-- ----------------------------
DROP TABLE IF EXISTS `parking_boy`;
CREATE TABLE `parking_boy` (
  `id` varchar(255) NOT NULL,
  `gender` int(11) DEFAULT NULL,
  `join_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

