package weididi.community.community.dto;


import lombok.Data;

@Data
public class NotificationDTO {

    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier; //需要通知的人
    private String notifierName;
    private String outerTitle;
    private Integer outerId;//所在问题的Id
    private String type;//回复的类型
    private Integer typeId;

}
