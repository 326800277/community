package weididi.community.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@MapperScan(value = "weididi.community.community.mapper")
@SpringBootApplication
@MapperScan(basePackages ="weididi.community.community.mapper" )
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
