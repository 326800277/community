CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account_id` varchar(100) ,
  `name` varchar(50) ,
  `token` char(36) ,
  `gmt_create` bigint(20) ,
  `gmt_modify` bigint(20) ,
  PRIMARY KEY (`id`)
);