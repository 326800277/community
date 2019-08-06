package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.domain.Question;
import weididi.community.community.domain.User;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.mapper.QuestionMapper;
import weididi.community.community.mapper.UserMapper;
import weididi.community.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

//  要实现不重复登陆，登陆一次之后保留一个Cookie后，由Cookie信息完成登陆
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
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
//        获取带有Question和user相关信息的questionDTO的集合
        List<QuestionDTO> questionList=questionService.list();
        model.addAttribute( "questions",questionList);
        return "index";
    }
}
