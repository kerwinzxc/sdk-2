package com.game.sdk.domain.request;


public class UserNameRegRequestBean extends BaseRequestBean {


    /**
     * uname : zs25427
     * passwd : a111111
     * imei : 273789135116287dd15727198fd96c40
     * deviceinfo : iPhone7,1||10.3.2||1||zh-Hans-CN
     */

    private String uname;
    private String passwd;
    private String imei;
    /**
     * sid : 3
     */

    private String sid;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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
