package com.chen.bean;

import lombok.Data;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 17:57
 */
@Data
public class Result {
    private static final String STATE = "state";
    private static final String STATE_OK = "ok";
    private static final String STATE_FAIL = "fail";
    private static final String USER_NOT_EXIST_OE_PW_ERROR = "用户不存在或密码错误";
    private static final String VERIFY_CODE_ERROR = "验证码输入错误";
    private static final String PARAM_ERROR="参数异常";
    private Object state;
    private String msg;

    public Result() {
    }

    public Result(Object state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public Result(Object state) {
        this.state = state;
    }

    public static Result OK(){
        return new Result(Result.STATE_OK);
    }
    public static Result OK(String msg){
        return new Result(Result.STATE_OK,msg);
    }

    public static Result loginFail(){
        return new Result(Result.STATE_FAIL,Result.USER_NOT_EXIST_OE_PW_ERROR);
    }

    public static Result verifyFail(){
        return new Result(Result.STATE_FAIL,Result.VERIFY_CODE_ERROR);
    }


    public static Result state(){
        return new Result(Result.STATE);
    }

    public static Result fail(){
        return new Result(Result.STATE_FAIL);
    }

    public static Result fail(String msg){
        return new Result(Result.STATE_FAIL,msg);
    }

}
