package weididi.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import weididi.community.community.dto.NotificationDTO;
import weididi.community.community.enums.NotificationEnum;
import weididi.community.community.model.User;
import weididi.community.community.service.NotificationService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public  String profile(@PathVariable(name="id") Integer id,
                           HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        //这里是用户读过通知之后，后台做的操作
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationEnum.REPLY_COMMENT.getStatus() == notificationDTO.getTypeId()
                || NotificationEnum.REPLY_QUESTION.getStatus() == notificationDTO.getTypeId()) {
            return "redirect:/question/" + notificationDTO.getOuterId();
        } else {
            return "redirect:/";
        }
    }
}
