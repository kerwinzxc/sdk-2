package com.game.sdk.domain.request;



public class SmsSendRequestBean extends BaseRequestBean {

    /**
     * mobile : mobile
     */

    private String mobile;
    private String type;
    private String uid;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
