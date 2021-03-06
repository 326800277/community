package weididi.community.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.model.User;
import weididi.community.community.dto.AccessTokenDTO;
import weididi.community.community.dto.GithubUser;
import weididi.community.community.provider.GithubPrivoder;
import weididi.community.community.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubPrivoder githubPrivoder;

    @Value("${github.client.id}")
    private String Client_id;

    @Value("${github.client.secret}")
    private String Client_secret;

    @Value("${github.redirect.uri}")
    private String Redirect_uri;


    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           //HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
//        这一行不要也行，这是再次设置一个回调函数
        accessTokenDTO.setRedirect_uri(Redirect_uri);

//        accessToken携带code访问https://github.com/login/oauth/access_token，返回一个访问令牌
        String accessToken = githubPrivoder.getAccessToken(accessTokenDTO);
//        根据访问令牌的到用户信息,因为和domain层名字冲突，shift+F6改
        GithubUser githubUser = githubPrivoder.getUser(accessToken);

        if(githubUser!=null){
            //登陆成功，写cookie和session
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            //Long型与String类型的转换
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
            //userMapper.insert(user);
            //保存一个Cookie
            response.addCookie(new Cookie("token",user.getToken()));
            //在拦截器中设定了Session
            //request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
        //System.out.println(githubUser.getName());
        //return "index";
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
