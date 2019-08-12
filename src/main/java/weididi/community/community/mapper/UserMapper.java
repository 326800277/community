package weididi.community.community.mapper;

import org.apache.ibatis.annotations.*;
import weididi.community.community.domain.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modify,avatar_url) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModify},#{avatarUrl})")
    void insert(User user);
//    #中识别的的是对象的成员变量，若方法中传入的不是对象，则需要@Param来识别
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id =#{creatorId}")
    User findById(@Param("creatorId") Integer creatorId);

    @Select("select * from user where account_id =#{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name=#{name},token=#{token},gmt_modify=#{gmtModify},avatar_url=#{avatarUrl} where account_id=#{accountId}")
    void update(User user);
}
