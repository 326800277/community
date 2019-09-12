package weididi.community.community.exception;

//窝草，这里是定义枚举类啊啊啊 啊 啊 啊啊啊啊啊啊啊
public enum CustomizeErrorCode implements ICustomizeErrorCode {
//枚举类，CustomizeErrorCode.QUESTION_NOT_FOUND相当于使用CustomizeErrorCode(String message)构造方法，
// 将"你找的问题不在了，要不要换个试试？"这个值传给构造方法中的形参。
//QUESTION_NOT_FOUND中有几个参数，构造函数就需一一对应有几个形参。
    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUNT(2002,"未选中任何评论或者回复"),
    NO_LOGIN(2003,"当前操作需要登陆，请登陆后重试"),
    SYS_ERROR(2004,"人忒多，稍后再试"),
    TPYE_PARAM_ERROR(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"该评论找不到了"),
    PARENTID_NOT_FOUND(2007,"所评论的对象没有"),
    CONTENT_IS_EMPTY(2008,"输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2009,"读到别人的通知信息了"),
    NOTIFICATION_NOT_FOUND(2010,"通知信息不见了"),
    FILE_UPLOAD_FAIL(2011,"图片上传失败")
    ;

    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message){
        this.message=message;
        this.code=code;
    }
}
