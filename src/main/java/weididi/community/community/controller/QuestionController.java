package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import weididi.community.community.dto.CommentDTO;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.enums.CommentTypeEnum;
import weididi.community.community.service.CommentService;
import weididi.community.community.service.QuestionService;

import java.util.List;

//这个控制器，是点进一个问题时，显示这个问题的详细信息。
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer id,
                           Model model){

        QuestionDTO questionDTO=questionService.getById(id);
        List<QuestionDTO> relatedQuestions =questionService.selectRelated(questionDTO);
        List<CommentDTO> comments=  commentService.listByTargetId(id, CommentTypeEnum.QUESTION.getType());
        //增加阅读数
        questionService.increaseView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
