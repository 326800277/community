package weididi.community.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
//@EnableWebMvc:SpringBoot对SpringMVC的自动配置不需要了，所有都是我们自己配置；所有的SpringMVC的自动配置都失效了
//这个地方默认样式是不拦截静态路径的
//https://blog.csdn.net/weixin_43153309/article/details/88639130
//方法2 实现继承WebMvcConfigurer
//因为是接口实现所以WebMvcAutoConfiguration还会默认配置，所以静态资源是可以访问的
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    //自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //自定义拦截器，拦截所有路径
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
/*                .excludePathPatterns("/js/**")
                .excludePathPatterns("/fonts/**")
                .excludePathPatterns("/css/**")*/

        //这里无法完成spring自动注入
        //registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
//        拦截除了/admin/**的所有路径
//        registry.addInterceptor(new ThemeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");

    }
}