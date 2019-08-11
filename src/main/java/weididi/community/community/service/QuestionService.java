package weididi.community.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weididi.community.community.domain.Question;
import weididi.community.community.domain.User;
import weididi.community.community.dto.PageNationDTO;
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

    public PageNationDTO list(Integer page, Integer size) {
        //size*(page-1)分页当前在数据库全部数据中的停止位置

        PageNationDTO pageNationDTO=new PageNationDTO();
        //从数据库获取总共有多少条问题
        Integer totalcount = questionMapper.count();
        //解决输入错误页码的问题
        if(page<1){
            page=1;
        }
        if(page>((totalcount%size)==0?totalcount/size:totalcount/size+1)){
            page=(totalcount%size)==0?totalcount/size:totalcount/size+1;
        }
        pageNationDTO.setPagenation(totalcount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOlist=new ArrayList<>();
        //函数封装，消除重复
        questionAddUser(questionDTOlist,questions);

        pageNationDTO.setQuestions(questionDTOlist);

        return pageNationDTO;
    }

    public PageNationDTO list(Integer id, Integer page, Integer size) {
        //size*(page-1)分页当前在数据库全部数据中的停止位置

        PageNationDTO pageNationDTO=new PageNationDTO();
        //从数据库获取总共有多少条问题
        Integer totalcount = questionMapper.countByUserId(id);

        //解决输入错误页码的问题

        if(page>((totalcount%size)==0?totalcount/size:totalcount/size+1)){
            page=(totalcount%size)==0?totalcount/size:totalcount/size+1;
        }
        if(page<1){
            page=1;
        }
        pageNationDTO.setPagenation(totalcount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions = questionMapper.listById(id,offset,size);
        List<QuestionDTO> questionDTOlist=new ArrayList<>();

        questionAddUser(questionDTOlist,questions);

        pageNationDTO.setQuestions(questionDTOlist);

        return pageNationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question=questionMapper.getById(id);
        User user=userMapper.findById(question.getCreatorId());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    //实现一个解决重复的功能
    public void questionAddUser(List<QuestionDTO> questionDTOlist,List<Question> questions){
        for(Question question:questions){
            //调试会出现无法读入，各种null的现象，这是由于书写不规范？需要 配置中mybatis.configuration.map-underscore-to-camel-case=true
            User user=userMapper.findById(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            //快速复制BEAN对象
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOlist.add(questionDTO);
        }
    }
}
