package weididi.community.community.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weididi.community.community.dto.QuestionQueryDTO;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.exception.CustomizeException;
import weididi.community.community.mapper.QuestionExtMapper;
import weididi.community.community.model.Question;
import weididi.community.community.model.QuestionExample;
import weididi.community.community.model.User;
import weididi.community.community.dto.PageNationDTO;
import weididi.community.community.dto.QuestionDTO;
import weididi.community.community.mapper.QuestionMapper;
import weididi.community.community.mapper.UserMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//服务层为中间层，可以执行一些相关操作，比如下面的，获得包含Question和user相关信息的questionDTO，并将其放进集合。
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PageNationDTO list(String search, Integer page, Integer size) {
        if(StringUtils.isNotBlank(search)){
            //        获取正则表达式模糊查询的语句
            String[] tags = StringUtils.split(search, ",");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        //size*(page-1)分页当前在数据库全部数据中的停止位置
        PageNationDTO<QuestionDTO> pageNationDTO=new PageNationDTO<>();
        //从数据库获取总共有多少条问题,这个地方需要重构，找到符合特定查询标准的问题。
        //Integer totalcount = (int)questionMapper.countByExample(new QuestionExample());
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalcount = questionExtMapper.countBySearch(questionQueryDTO);
        //解决输入错误页码的问题
        if(page>((totalcount%size)==0?totalcount/size:totalcount/size+1)){
            page=(totalcount%size)==0?totalcount/size:totalcount/size+1;
        }
        if(page<1){
            page=1;
        }
        pageNationDTO.setPagenation(totalcount,page,size);
        Integer offset=size*(page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions =questionExtMapper.selectBySearch(questionQueryDTO);

        //这个地方有时间修复一下，需要新问题置顶为好。
        //List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOlist=new ArrayList<>();
        //函数封装，消除重复
        questionAddUser(questionDTOlist,questions);

        pageNationDTO.setDatas(questionDTOlist);

        return pageNationDTO;
    }

    public PageNationDTO list(Integer id, Integer page, Integer size) {
        //size*(page-1)分页当前在数据库全部数据中的停止位置

        PageNationDTO<QuestionDTO> pageNationDTO=new PageNationDTO<>();

        //从数据库获取总共有多少条问题
        QuestionExample questionExample = new QuestionExample();
        //这只有限定，但count方法值讨论其ID主键？？？？
        questionExample.createCriteria().andCreatorIdEqualTo(id);
        Integer totalcount = (int)questionMapper.countByExample(questionExample);

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

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorIdEqualTo(id);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOlist=new ArrayList<>();

        questionAddUser(questionDTOlist,questions);

        pageNationDTO.setDatas(questionDTOlist);

        return pageNationDTO;
    }

    public QuestionDTO getById(Integer id) {

        Question question = questionMapper.selectByPrimaryKey(id);

        //当输入的iD错误，返回为空时，异常处理，自定义异常
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        User user=userMapper.selectByPrimaryKey(question.getCreatorId());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    //实现一个解决重复的功能
    public void questionAddUser(List<QuestionDTO> questionDTOlist,List<Question> questions){
        for(Question question:questions){
            //调试会出现无法读入，各种null的现象，这是由于书写不规范？需要 配置中mybatis.configuration.map-underscore-to-camel-case=true
            User user=userMapper.selectByPrimaryKey(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            //快速复制BEAN对象
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOlist.add(questionDTO);
        }
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModify(System.currentTimeMillis());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModify(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated= questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void increaseView(Integer id) {

        //一般解决并发的思路就是在更新的时候，直接有数据库内的数据增加。
        // 这就是一次操作，直接增加，没有中途获得一个数（这个数可能被同时被多个用户拿到），导致更新错误。
        Question question = new Question();
        //确定要操作的问题是哪一个
        question.setId(id);
//        这个地方相当于，给定一个增加的次数
        question.setViewCount(1);
        questionExtMapper.incView(question);

        //这个方法，无法解决高并发问题
/*        Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(question.getViewCount()+1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        questionMapper.updateByExampleSelective(updateQuestion,questionExample);*/
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
//        获取正则表达式模糊查询的语句
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
//        自定义模糊查询
        List<Question> questions = questionExtMapper.selectRelated(question);
//        将questions的map集合遍历复制有效信息后转化为List集合
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
