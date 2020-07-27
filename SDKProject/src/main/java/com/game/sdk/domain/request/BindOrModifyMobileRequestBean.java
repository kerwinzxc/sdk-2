package com.game.sdk.domain.request;


public class BindOrModifyMobileRequestBean extends BaseRequestBean {


    private String uid;
    private String mobile;
    private String passwd;
    private String oldcode;
    private String vercode;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getOldcode() {
        return oldcode;
    }

    public void setOldcode(String oldcode) {
        this.oldcode = oldcode;
    }
}
