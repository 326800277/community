package weididi.community.community.dto;

import lombok.Data;
import weididi.community.community.model.User;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer creatorId;
    private String tag;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private User user;
}
