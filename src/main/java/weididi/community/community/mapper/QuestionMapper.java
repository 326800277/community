package weididi.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import weididi.community.community.domain.Question;
import weididi.community.community.dto.QuestionDTO;

import java.util.List;

@Mapper
public interface QuestionMapper {
    
    @Insert("insert into question (title,description,gmt_create,gmt_modify,creator_id,tag) values (#{title},#{description},#{gmtCreate},#{gmtModify},#{creatorId},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value="offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator_id=#{userId} limit #{offset},#{size}")
    List<Question> listById(@Param(value = "userId")Integer id, @Param(value="offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator_id=#{userId}")
    Integer countByUserId(@Param(value = "userId") Integer id);

    @Select("select * from question where id=#{id}")
    Question getById(@Param(value = "id") Integer id);
}
