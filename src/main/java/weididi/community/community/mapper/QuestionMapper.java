package weididi.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import weididi.community.community.domain.Question;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modify,creator_id,tag) values (#{title},#{description},#{gmtCreate},#{gmtModify},#{creatorId},#{tag})")
    public void create(Question question);
}
