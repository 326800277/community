package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.domain.Question;
import weididi.community.community.domain.User;
import weididi.community.community.mapper.QuestionMapper;
import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){
//      model就是在前端页面上能直接获取到的信息，在thymeleaf中使用时更加方便

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if(title==null || title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null || description==""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag==null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        //而在拦截器中在request中的session中保存了user，这里取出。
        // 不是同一个对象，就算是拦截器也不能共用成员变量
        User user = (User) request.getSession().getAttribute("user");
        Question question=new Question();
        //解决错误信息？？？？？？
        if(user==null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreatorId(user.getId());
        question.setGmtCreate(user.getGmtCreate());
        question.setGmtModify(user.getGmtModify());
        questionMapper.create(question);
        return "redirect:/";
    }
}
