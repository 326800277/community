package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.domain.User;
import weididi.community.community.dto.PageNationDTO;
import weididi.community.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public  String profile(@PathVariable(name="action") String action,
                           Model model,
                           HttpServletRequest request,
                           @RequestParam(name="page",defaultValue = "1") Integer page,
                           @RequestParam(name="size",defaultValue = "2") Integer size){
        //因为要判断user==null，所以需要获得user，而在拦截器中在request中的session中保存了，这里取出。
        // 不是同一个对象，就算是拦截器也不能共用成员变量
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }

        if("question".equals(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的问题");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PageNationDTO list = questionService.list(user.getId(), page, size);
        model.addAttribute("pageNations",list);
        return "profile";
    }
}
