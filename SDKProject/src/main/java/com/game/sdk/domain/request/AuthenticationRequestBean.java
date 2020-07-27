package com.game.sdk.domain.request;


public class AuthenticationRequestBean extends BaseRequestBean{


    /**
     * uid
     * session
     * realname
     * idcard
     */

    private String uid;
    private String realname;
    private String idcard;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
