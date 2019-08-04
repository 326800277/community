## 威弟弟社区
## 资料
[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content)  
[es](https://elasticsearch.cn/explore)  
[Github deploy key](https://help.github.com/en)  
[Bootstrap](https://v3.bootcss.com/components/#navbar-default)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
## 工具
[Git](https://git-scm.com/)
[flyway](https://flywaydb.org/getstarted/firststeps/maven)

## 图解说明
![Image text](https://github.com/326800277/community/blob/master/img%20storage/QQ%E5%9B%BE%E7%89%8720190729235659.png)

## 脚本
```sql
Create Table

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account_id` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `token` char(36) DEFAULT NULL,
  `gmt_create` bigint(20) DEFAULT NULL,
  `gmt_modify` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) 
```
```bash
mvn flyway:migrate
```