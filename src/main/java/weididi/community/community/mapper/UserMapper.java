package weididi.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import weididi.community.community.domain.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modify) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModify})")
    void insert(User user);
}
