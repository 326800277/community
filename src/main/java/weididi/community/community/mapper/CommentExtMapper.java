package weididi.community.community.mapper;
import weididi.community.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}