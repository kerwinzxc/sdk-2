package com.game.sdk.domain.request;



public class UserMobileRegRequestBean extends BaseRequestBean {


    /**
     * passwd : 123456
     * mobile : 13888888888
     * vercode : 1234
     */

    private String passwd;
    private String mobile;
    private String vercode;
    private String imei;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    /**
     * sid : 3
     */

    private String sid;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
