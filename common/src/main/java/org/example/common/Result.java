package org.example.common;

import lombok.Data;
import org.springframework.web.servlet.View;

@Data
public class Result extends Throwable {
    private String code;
    private String message;
    private Object data;

    public Result(String status, String msg) {

        this.code = status;
        this.message = msg;
    }

    public Result() {

    }

    public static Result success(){
        Result result=new Result();
        result.setCode("200");
        result.setMessage("请求成功");
        return result;
    }
    public static Result error(){
        Result result=new Result();
        result.setCode("500");
        result.setMessage("系统错误");
        return result;
    }
    //可以设置数据的result方法
    public static Result success(Object data){
        Result result=success();
        result.setData(data);
        return result;
    }


    public static Result error(String message) {
        Result result=new Result();
        result.setMessage(message);
        return result;
    }
}
