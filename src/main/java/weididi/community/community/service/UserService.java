package weididi.community.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weididi.community.community.mapper.UserMapper;
import weididi.community.community.model.User;
import weididi.community.community.model.UserExample;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //数据库中以前就存在的USER
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> pastUsers =userMapper.selectByExample(userExample);
        if(user.getAccountId()!=null){
            if(pastUsers.size()==0){
                //新建
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModify(user.getGmtCreate());
                userMapper.insert(user);
            }else {
                //更新
                User dbUser = pastUsers.get(0);
                User updateUser = new User();
                updateUser.setGmtModify(System.currentTimeMillis());
                updateUser.setAvatarUrl(user.getAvatarUrl());
                updateUser.setName(user.getName());
                updateUser.setToken(user.getToken());
                UserExample example=new UserExample();
                //使用创建的sql语句，语句中使id等于,有where xxx=xxx的语句就用这个
                userExample.createCriteria().andIdEqualTo(dbUser.getId());
                //更新局部数据，直接是有where之前的语句
                userMapper.updateByExampleSelective(updateUser,example);
            }
        }
    }
}
