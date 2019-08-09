package weididi.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageNationDTO {

    private List<QuestionDTO> questions;
    private  boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNextPage;
    private boolean showEndPage;
    private Integer currentPages;
    private Integer totalPage;
//当前这个页码条里面所包含的页码有哪些
    private List<Integer> pages=new ArrayList<>();


    public void setPagenation(Integer totalcount, Integer page, Integer size) {
        totalPage=(totalcount%size)==0?totalcount/size:totalcount/size+1;

        currentPages=page;

        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i>0){
                //每次只在数组下标为0处添加
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }

        if(page==1){
            showPrevious=false;
        }else {
            showPrevious=true;
        }
        if(page==totalPage){
            showNextPage=false;
        }else {
            showNextPage=true;
        }
        if(pages.contains(1)){
            showFirstPage=false;
        }else {
            showFirstPage=true;
        }
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else {
            showEndPage=true;
        }


    }
}
