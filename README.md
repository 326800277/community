## 威弟弟社区
## 资料
[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content)  
[es](https://elasticsearch.cn/explore)  
[Github deploy key](https://help.github.com/en)  
[Bootstrap](https://v3.bootcss.com/components/#navbar-default)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[thymeleaf帮助文档](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
[MarkDown 插件](http://editor.md.ipandao.com/)
[UCloud UFile SDK](https://github.com/ucloud/ufile-sdk-java)
## 工具
[Git](https://git-scm.com/)
[flyway](https://flywaydb.org/getstarted/firststeps/maven)
[lombok](https://www.projectlombok.org/features/all)
[mybatis generator](http://www.mybatis.org/generator/running/runningWithMaven.html)

## 笔记
    @GetMapping("/publish/{questionId}")
    public String republish(@PathVariable(name="questionId") Integer id,
                            Model model);
    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,;
            
    mybatis generator的使用;
    
    //Example具有限定功能，当没有限定时，传入一个无参构造Example（必传）
     //使用创建的sql语句，语句中使id等于,有where xxx=xxx的语句就用这个
     UserExample example=new UserExample();
     userExample.createCriteria().andIdEqualTo(dbUser.getId());
     //更新局部数据，直接是有where之前的update语句;
     User updateUser = new User();
     updateUser.setGmtModify(System.currentTimeMillis());
     updateUser.setAvatarUrl(user.getAvatarUrl());
     updateUser.setName(user.getName());
     updateUser.setToken(user.getToken());
     userMapper.updateByExampleSelective(updateUser,example);                       

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


Create Table

CREATE TABLE `questionid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `title` varchar(50) DEFAULT NULL,
  `gmt_create` bigint(20) DEFAULT NULL,
  `gmt_modify` bigint(20) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `comment_count` int(11) DEFAULT '0',
  `view_count` int(11) DEFAULT '0',
  `like_count` int(11) DEFAULT '0',
  `tag` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

```
``` bash
terminal中使用
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```