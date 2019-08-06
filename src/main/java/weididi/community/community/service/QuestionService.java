package weididi.community.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weididi.community.community.domain.Question;
import weididi.community.community.domain.User;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.mapper.QuestionMapper;
import weididi.community.community.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

//服务层为中间层，可以执行一些相关操作，比如下面的，获得包含Question和user相关信息的questionDTO，并将其放进集合。
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> list() {
        List<Question> questions = questionMapper.list();
        List<QuestionDTO> questionDTOlist=new ArrayList<>();
        for(Question question:questions){
            //调试会出现无法读入，各种null的现象，这是由于书写不规范？需要 配置中mybatis.configuration.map-underscore-to-camel-case=true
            User user=userMapper.findById(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            //快速复制BEAN对象
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOlist.add(questionDTO);
        }
        return questionDTOlist;
    }
}
