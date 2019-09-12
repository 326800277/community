package weididi.community.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

//擦，这一节不知道在干些什么，这个控制器解决的好像是没有特殊处理（Handler）的所有异常
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request, Model model) {
        HttpStatus status = this.getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("message","你自己的请求错了，麻烦换一换");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","服务器冒烟了");
        }
        return  new ModelAndView("error");
    }


    private HttpStatus getStatus(HttpServletRequest request) {
        //获取服务端异常状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
