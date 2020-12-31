package com.nanjingtaibai.response;

public enum ResultCode implements CustomizeResultCode {
    /**
     * 20000:"成功"
     */
    SUCCESS(20000,"成功"),
    /**
     * 20001:"失败"
     */
    ERROR(20001,"失败"),
    /**
     * 3004:"算术异常"
     */
    ARITHMETIC_EXCEPTION(3004,"算术异常"),
    /**
     * 3005
     */
    PASS_NOT_CORRECT(3005,"密码不正确，请重新尝试"),
    /**
     * 3006:"尚未登录"
     */
    NOT_LOGIN(3006,"尚未登录"),
    /**
     * 3007:"用户不存在"
     */
    USER_NOT_FOUND_EXCEPTION(3007,"用户不存在"),
    /**
     * 2005:"尚未登录"
     */
    INTRODUCTION_NOT_FOUND(2005,"尚未找到信息"),
    /**
     * 404:"您请求的页面不存在"
     */
    PAGE_NOT_FOUND(404,"您请求的页面不存在"),
    /**
     * 500:"服务端异常"
     */
    INTERNAL_SERVER_ERROR(500,"服务端异常"),
    /**
     * 2001:"服务端异常"
     */
    UNKNOW_SERVER_ERROR(2001,"未知异常，请联系管理员")

    ;

    private Integer code;
    private String message;

    ResultCode(Integer code,String message){
        this.code=code;
        this.message=message;

    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
