package com.game.sdk.domain.request;


public class GiftGettedRequestBean extends BaseRequestBean{


    /**
     * uid
     * page
     * size
     */

    private String uid;
    private String page;
    private String size;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
