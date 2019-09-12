package weididi.community.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weididi.community.community.dto.CommentDTO;
import weididi.community.community.enums.CommentTypeEnum;
import weididi.community.community.enums.NotificationEnum;
import weididi.community.community.enums.NotificationStatusEnum;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.exception.CustomizeException;
import weididi.community.community.mapper.*;
import weididi.community.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//将评论注入
@Service
public class CommentService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public void insert(Comment comment, User commentator) {
        //没有登录
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUNT);
        }
        //评论类型错误
        if(comment.getType()==null||!CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TPYE_PARAM_ERROR);
        }

        //类型2否则1
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知,这个时候已近滚到数据库里面去了，用的时候直接找
            createNotify(comment, dbComment.getCommentator(),commentator.getName(),question.getTitle(),NotificationEnum.REPLY_COMMENT,question.getId());
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeErrorCode.PARENTID_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            createNotify(comment,question.getCreatorId(), commentator.getName(), question.getTitle(),NotificationEnum.REPLY_QUESTION,question.getId());

        }
    }

    private void createNotify(Comment comment, Integer receiver, String notifierName, String outTitle, NotificationEnum notificationEnum,Integer outerId) {
        //            这里的评论相当于评论的评论
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setTypeId(notificationEnum.getStatus());
        notification.setOuterId(outerId);//评论的主问题的ID
        notification.setNotifier(comment.getCommentator());//获取作出点赞等行为的人的ID
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);//评论发起者ID
        notification.setNotifierName(notifierName);//需要通知的人的名字
        notification.setOuterTitle(outTitle);//评论问题的主题
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Integer id, Integer type) {
        CommentExample commentExample = new CommentExample();
        //CommentTypeEnum.QUESTION还只是个赋值为1的对象。
//        限定该问题下的关于该问题的回复
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type);
        //希望其按照gmt_create的大小倒序排列！
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        //JAVA8语法
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重得评论人
        //这个lambda表达式相当于，1，comments.stream()获取流
        //2,（可以将对象转换为其他对象）.map
        // comment -> comment.getCommentator(),相当于执行一个对象循环，获取所有评论者的ID值
        //4，（得到的对象转换为集合！！！）.collect(Collectors.toSet())相当于用collect接口里面的方法，将得到的数据去重。
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds =new ArrayList<>();
        userIds.addAll(commentators);

        //获取多个评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
