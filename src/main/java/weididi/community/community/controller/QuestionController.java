package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.mapper.QuestionMapper;
import weididi.community.community.service.QuestionService;

//这个控制器，是点进一个问题时，显示这个问题的详细信息。
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer id,
                           Model model){

        QuestionDTO questionDTO=questionService.getById(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
