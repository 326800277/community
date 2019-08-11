package weididi.community.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import weididi.community.community.domain.User;
import weididi.community.community.mapper.UserMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//自定义拦截器
@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        //防止Cookies空指针异常。
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    //在数据库中找到对应Cookie的用户信息
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //将该用户丢到session域中，不用再次登陆
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
//        返回true时继续执行，否则停止
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
