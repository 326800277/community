package weididi.community.community.provider;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import weididi.community.community.dto.AccessTokenDTO;
import weididi.community.community.dto.GithubUser;

import java.io.IOException;

@Component
public class GithubPrivoder {
/*    public static void main(String[] args) {
        GithubPrivoder githubPrivoder = new GithubPrivoder();
    }*/

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        //这里使用的是在控制器传入的GitHub相关信息
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //携带request信息传入到GitHub
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
//            划分，得到令牌
            String s = string.split("&")[0].split("=")[1];
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
//        https://api.github.com/user?access_token=路径是查询用户信息的路径，然后传入令牌accessToken
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
//        得到用户信息
        Response response = client.newCall(request).execute();
//        得到一条关于用户信息的JSON数据
        String string = response.body().string();
//        将JSON数据转换为对象
        GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
        return githubUser;
    }
}
