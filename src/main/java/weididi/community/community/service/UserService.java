package weididi.community.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weididi.community.community.domain.User;
import weididi.community.community.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //数据库中以前就存在的USER
        User pastUser =userMapper.findByAccountId(user.getAccountId());
        if(user.getAccountId()!=null){
            if(pastUser==null){
                //新建
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModify(user.getGmtCreate());
                userMapper.insert(user);
            }else {
                //更新
                user.setGmtModify(System.currentTimeMillis());
                userMapper.update(user);
            }
        }
    }
}
