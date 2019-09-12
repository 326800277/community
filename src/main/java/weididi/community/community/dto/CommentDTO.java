package weididi.community.community.dto;

import lombok.Data;
import weididi.community.community.model.User;

@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Integer commentCount;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private String content;
    private User user;
}
