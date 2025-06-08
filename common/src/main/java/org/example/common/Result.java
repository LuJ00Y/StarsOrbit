package org.example.common;

import lombok.Data;

@Data
public class Result {
    private String code;
    private String message;
    private Object data;

    public Result(String status, String msg) {

        this.code = status;
        this.message = msg;
    }

    public Result() {

    }

    private static Result success(){
        Result result=new Result();
        result.setCode("200");
        result.setMessage("请求成功");
        return result;
    }
    private static Result error(){
        Result result=new Result();
        result.setCode("500");
        result.setMessage("系统错误");
        return result;
    }
    //可以设置数据的result方法
    private static Result success(Object data){
        Result result=success();
        result.setData(data);
        return result;
    }




}
