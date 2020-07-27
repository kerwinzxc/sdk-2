package com.game.sdk.domain.request;


public class TmpLoginRequestBean extends BaseRequestBean {


    /**
     * imei : 12312312312312321
     */

    private String imei;
    /**
     * sid : 3
     */

    private String sid;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
