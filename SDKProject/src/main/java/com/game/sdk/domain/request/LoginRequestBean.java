package com.game.sdk.domain.request;


public class LoginRequestBean extends BaseRequestBean{


    /**
     * passwd : 123456
     * uname : 13888888888
     * vercode : 1234
     */

    private String passwd;
    private String uname;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
