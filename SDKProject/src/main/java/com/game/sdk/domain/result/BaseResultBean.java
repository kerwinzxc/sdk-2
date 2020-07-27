package com.game.sdk.domain.result;


public class BaseResultBean {


    /**
     * code : 1007
     * msg : 缺少验证码参数
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResultBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
