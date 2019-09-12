package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.dto.PageNationDTO;
import weididi.community.community.service.QuestionService;


@Controller
public class IndexController { ;
    @Autowired
    private QuestionService questionService;

//  要实现不重复登陆，登陆一次之后保留一个Cookie后，由Cookie信息完成登陆
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "5") Integer size,
                        @RequestParam(name="search",required=false) String search){

        if(search==""){
            search=null;
        }
//      获取带有Question和user相关信息的questionDTO的集合
        PageNationDTO pageNations=questionService.list(search,page,size);
        model.addAttribute( "pageNations",pageNations);
        model.addAttribute("search",search);
        return "index";
    }
}
