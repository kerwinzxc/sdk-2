package com.game.sdk.domain.request;


public class GiftGetRequestBean extends BaseRequestBean{


    /**
     * uid
     * gift_id
     */

    private String uid;
    private String gift_id;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }
}
