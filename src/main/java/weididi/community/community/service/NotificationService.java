package weididi.community.community.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import weididi.community.community.dto.NotificationDTO;
import weididi.community.community.dto.PageNationDTO;
import weididi.community.community.enums.NotificationEnum;
import weididi.community.community.enums.NotificationStatusEnum;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.exception.CustomizeException;
import weididi.community.community.mapper.NotificationMapper;
import weididi.community.community.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PageNationDTO list(Integer id, Integer page, Integer size) {
        //size*(page-1)分页当前在数据库全部数据中的停止位置

        PageNationDTO<NotificationDTO> pageNationDTO=new PageNationDTO<>();

        //从数据库获取总共有多少条问题
        NotificationExample notificationExample = new NotificationExample();
        //这只有限定，但count方法值讨论其ID主键？？？？
        notificationExample.createCriteria().andReceiverEqualTo(id);
        Integer totalcount = (int)notificationMapper.countByExample(notificationExample);

        //解决输入错误页码的问题

        if(page>((totalcount%size)==0?totalcount/size:totalcount/size+1)){
            page=(totalcount%size)==0?totalcount/size:totalcount/size+1;
        }
        if(page<1){
            page=1;
        }
        pageNationDTO.setPagenation(totalcount,page,size);
        Integer offset=size*(page-1);
        //List<Question> questions = questionMapper.listById(id,offset,size);
        //从数据库中倒找所有通知登陆者的消息
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        //使未读结果置顶
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if(notifications.size()==0){
            return pageNationDTO;
        }
/*        Set<Integer> disUserIds = notifications.stream().map(notification -> notification.getNotifier()).collect(Collectors.toSet());
        ArrayList<Integer> userIds = new ArrayList<>(disUserIds);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));*/

        List<NotificationDTO> notificationDTOS=new ArrayList<>();
        for(Notification notification:notifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setType(NotificationEnum.nameOfType(notification.getTypeId()));
            notificationDTOS.add(notificationDTO);
        }


        pageNationDTO.setDatas(notificationDTOS);

        return pageNationDTO;
    }


    public Long unReadCount(Integer id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setType(NotificationEnum.nameOfType(notification.getTypeId()));
        return  notificationDTO;
    }
}
