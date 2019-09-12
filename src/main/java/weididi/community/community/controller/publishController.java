package weididi.community.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weididi.community.community.cache.TagCache;
import weididi.community.community.model.Question;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.model.User;
import weididi.community.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
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
        String inValid = TagCache.filterInValid(tag);
        if(StringUtils.isNotBlank(inValid)){
            model.addAttribute("error","输入非法标签："+inValid);
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
        //输入的ID值可为空，当为空时，没有影响，不为空时正好改变当前问题
        question.setId(id);

        questionService.createOrUpdate(question);
        model.addAttribute("tags",TagCache.get());
        return "redirect:/";
    }


//    不行，还是得做一个修改页面，这个页面不知道为什么没有样式，况且，还要保持发布后question的ID不变
    @GetMapping("/publish/{questionId}")
    public String republish(@PathVariable(name="questionId") Integer id,
                            Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }
}
