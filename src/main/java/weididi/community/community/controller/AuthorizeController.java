package weididi.community.community.controller;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.domain.User;
import weididi.community.community.dto.AccessTokenDTO;
import weididi.community.community.dto.GithubUser;
import weididi.community.community.mapper.UserMapper;
import weididi.community.community.provider.GithubPrivoder;

import javax.servlet.http.HttpServletRequest;
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
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
//        这一行不要也行，这是再次设置一个回调函数
        //accessTokenDTO.setRedirect_uri(Redirect_uri);

//        accessToken携带code访问https://github.com/login/oauth/access_token，返回一个访问令牌
        String accessToken = githubPrivoder.getAccessToken(accessTokenDTO);
//        根据访问令牌的到用户信息,因为和domain层名字冲突，shift+F6改
        GithubUser githubUser = githubPrivoder.getUser(accessToken);

        if(githubUser!=null){
            //登陆成功，写cookie和session
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccountId(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
        //System.out.println(githubUser.getName());
        //return "index";
    }
}
