package weididi.community.community.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import weididi.community.community.dto.ResultDTO;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//handler处理者
//使用@ControllerAdvice注解，全局捕获异常类，只要作用在@RequestMapping上(就是自己定义过的访问路径)，所有的异常都会被捕获
@ControllerAdvice
public class CustomizeExceptionHandler {
    //这个类，拦截的是整个项目中可处理的异常，比如在代码中throw的异常就被捕获到了Throwable e中
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
        String contentType = request.getContentType();
        //错误页面跳转
        HttpStatus status = getStatus(request);
        if("application/json".equals(contentType)){
            //返回JSON,由于ModelAndView和@RequestMapping不能同时返回，这里需要有HttpServletResponse来强行手动转换。
            ResultDTO resultDTO=null;
            if(e instanceof CustomizeException) {
                resultDTO=ResultDTO.errorOf((CustomizeException) e);
            }else {
                resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                //这里就直接往页面上传JSON等数据了
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                //writer直接传好了
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }else {
            //这个是父类指向子类的指针 instanceof子类。这个就是判断e是不是指向子类的，可以用
            if(e instanceof CustomizeException) {
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
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
