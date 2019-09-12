
function post(){

    // #question_id是html标签的id 值
    var question_id= $("#question_id").val();
    var comment_content=$("#comment_content").val();
    commentToTarget(question_id,1,comment_content);
}

function comment_post(e) {
    var comment_id= e.getAttribute("data-id");
    var comment_content=$("#input-"+comment_id).val();
    commentToTarget(comment_id,2,comment_content);

}


function commentToTarget(targetId,type,content){
    if(!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type:"POST",
        url: "/comment",
        contentType:"application/json",
        //这个地方question_id，和comment_content都是JS对象，我们需要JSON.stringify将其转换成字符串
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success:function (response) {
            if(response.code==200){
                // 回复完成后隐藏
                //$("#comment_section").hide();
                window.location.reload();//刷新页面
            }else {
                if(response.code=2003){
                    //confirm（）弹出一个窗口，给一个bool值，若是点确定即为true
                    var isAccepted=confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=7ebe77075791bac8578c&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        //只读的localStorage 属性允许你访问一个Document 源（origin）的对象 Storage；存储的数据将保存在浏览器会话中。
                        // localStorage 类似 sessionStorage，但其区别在于：存储在 localStorage 的数据可以长期保留；
                        // 而当页面会话结束——也就是说，当页面被关闭时，存储在 sessionStorage 的数据会被清除 。
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    // 这里接受commentController的response返回信息,这里可以直接弹窗弹出异常信息。
                    alert(response.message)
                }
            }
            // 控制台打印消息
            console.log(response);
        },
        dataType:"json"
    });
}

/**
 * 展开二级评论,collapse,添加一个下拉框
 */

function  collapseComments(e) {
    //debugger;
    var id =e.getAttribute("data-id");
    var comments=$("#comment-"+id);

    // 获取一下二级评论的展开状态
    var collapse =e.getAttribute("data-collapse");
    if(collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        var subCommentContainer=$("#comment-"+id);
        if(subCommentContainer.children().length!=1){
            console.log(data);
            //为该类添加一个样式
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }else{
            $.getJSON( "/comment/"+id, function( data ) {
                $.each( data.data.reverse(), function(index,comment) {
                    var mediaLeftElement= $( "<div/>", {
                        "class": "media-left"
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));
                    var mediaBodyElement=$("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    })));
                    var mediaElement=$("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement=$("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comment"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
                //为该类添加一个样式
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            })
        }
    }
}

function selectTag(e) {
    var value =e.getAttribute("data-tag");
    var previous =$("#tag").val();
    if(previous.indexOf(value)==-1){
        if(previous){
            $("#tag").val(previous+","+value);
        }else{
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}