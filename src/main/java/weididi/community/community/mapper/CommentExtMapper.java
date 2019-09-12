package weididi.community.community.mapper;
import weididi.community.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}