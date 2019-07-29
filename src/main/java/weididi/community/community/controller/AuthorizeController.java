package weididi.community.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.dto.AccessTokenDTO;
import weididi.community.community.dto.GithubUser;
import weididi.community.community.provider.GithubPrivoder;

import java.io.IOException;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubPrivoder githubPrivoder;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("7ebe77075791bac8578c");
        accessTokenDTO.setClient_secret("bacc980a6fd3acd1077d5f7aaa117c0b7d9d126c");
//        这一行不要也行，这是再次设置一个回调函数
        //accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");

//        accessToken携带code访问https://github.com/login/oauth/access_token，返回一个访问令牌
        String accessToken = githubPrivoder.getAccessToken(accessTokenDTO);
//        根据访问令牌的到用户信息
        GithubUser user = githubPrivoder.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
