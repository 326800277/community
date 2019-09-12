package weididi.community.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weididi.community.community.dto.CommentCreateDTO;
import weididi.community.community.dto.CommentDTO;
import weididi.community.community.dto.ResultDTO;
import weididi.community.community.enums.CommentTypeEnum;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.model.Comment;
import weididi.community.community.model.User;
import weididi.community.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

//    @ResponseBody和@RequestBody都是自动把提交和接受的数据转化为JSON格式
    @ResponseBody
    @RequestMapping(value = "/comment",method=RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if(commentCreateDTO==null||StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment,user);
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("message","成功");
//        return objectObjectHashMap;
        //都是要返回一个成功信息
        return ResultDTO.okOf();
    }


    //获取所有的二级评论，并以JSON形式传输到JS代码中，在前端界面显示出来
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method=RequestMethod.GET)
    public ResultDTO comments(@PathVariable(name="id") Integer id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(commentDTOS);
    }
}
