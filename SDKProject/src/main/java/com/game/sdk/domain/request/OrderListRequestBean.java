package com.game.sdk.domain.request;


public class OrderListRequestBean extends BaseRequestBean{


    /**
     * uid
     * session
     * page
     */

    private String uid;
    private String page;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
