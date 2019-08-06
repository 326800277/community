package weididi.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import weididi.community.community.domain.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    
    @Insert("insert into question (title,description,gmt_create,gmt_modify,creator_id,tag) values (#{title},#{description},#{gmtCreate},#{gmtModify},#{creatorId},#{tag})")
    public void create(Question question);

    @Select("select * from question")
    List<Question> list();
}
