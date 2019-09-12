package weididi.community.community.cache;

import org.apache.commons.lang3.StringUtils;
import weididi.community.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public  static List<TagDTO> get(){
        List<TagDTO> tagDTOS=new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","node","python","javascript","golang","ruby","c++","node.js","rust"));
        tagDTOS.add(program);

        TagDTO frameWork = new TagDTO();
        frameWork.setCategoryName("平台框架");
        frameWork.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTOS.add(frameWork);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux","nginx","docker","apache","ubuntu","centos","缓存 tomcat","负载均衡","unix","hadoop"));
        tagDTOS.add(server);

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库");
        database.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql"));
        tagDTOS.add(database);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","vim","eclipse","maven","svn"));
        tagDTOS.add(tool);

        return tagDTOS;
    }

    //验证，若不是标签库中的标签，就不让其有效发布
    public static String filterInValid(String tags){
        String[] split = StringUtils.split(tags, ",");

        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return invalid;
    }
}
