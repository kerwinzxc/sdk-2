package com.game.sdk.domain.request;


import java.io.Serializable;

public class QueryRequestBean extends BaseRequestBean implements Serializable {


    /**
     * uid : 5
     * order_code : e57d
     */

    private String uid;
    private String order_code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderCode() {
        return order_code;
    }

    public void setOrderCode(String order_code) {
        this.order_code = order_code;
    }
}
