package com.game.sdk.domain.request;


import java.io.Serializable;

public class PayRequestBean extends BaseRequestBean implements Serializable {

    /**
     * uid : 18511907352
     * out_trade_no : null||android7.0||d8:c7:71:ca:89:57||866961031349588||DLI-AL10||null
     * product_price : sdk.sms.reg.send
     * product_count : 1.0
     * product_id : 1.0
     * product_name : 1.0
     * product_desc : 1.0
     * exchange_rate : 1.0
     * currency_name : 1.0
     * role_type : 1.0
     * server_id : 1.0
     * server_name : 1.0
     * role_id : 1.0
     * role_name : 1.0
     * party_name : 1.0
     * role_level : 1.0
     * role_vip : 1.0
     * role_balance : 1.0
     * rolelevel_ctime : 1.0
     * rolelevel_mtime : 1.0
     */

    private String uid;
    private String out_trade_no;
    private String product_price;
    private String product_count;
    private String product_id;
    private String product_name;
    private String product_desc;
    private String exchange_rate;
    private String currency_name;
    private String role_type;
    private String server_id;
    private String server_name;
    private String role_id;
    private String role_name;
    private String party_name;
    private String role_level;
    private String role_vip;
    private String role_balance;
//    private String rolelevel_ctime;
    private String rolelevel_mtime;
    private String extend;
    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOutTradeNo() {
        return out_trade_no;
    }

    public void setOutTradeNo(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getProductPrice() {
        return product_price;
    }

    public void setProductPrice(String product_price) {
        this.product_price = product_price;
    }

    public String getProductCount() {
        return product_count;
    }

    public void setProductCount(String product_count) {
        this.product_count = product_count;
    }

    public String getProductId() {
        return product_id;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public String getProductDesc() {
        return product_desc;
    }

    public void setProductDesc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getExchangeRate() {
        return exchange_rate;
    }

    public void setExchangeRate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getCurrencyName() {
        return currency_name;
    }

    public void setCurrencyName(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getRoleType() {
        return role_type;
    }

    public void setRoleType(String role_type) {
        this.role_type = role_type;
    }

    public String getServerId() {
        return server_id;
    }

    public void setServerId(String server_id) {
        this.server_id = server_id;
    }

    public String getServerName() {
        return server_name;
    }

    public void setServerName(String server_name) {
        this.server_name = server_name;
    }

    public String getRoleId() {
        return role_id;
    }

    public void setRoleId(String role_id) {
        this.role_id = role_id;
    }

    public String getRoleName() {
        return role_name;
    }

    public void setRoleName(String role_name) {
        this.role_name = role_name;
    }

    public String getPartyName() {
        return party_name;
    }

    public void setPartyName(String party_name) {
        this.party_name = party_name;
    }

    public String getRoleLevel() {
        return role_level;
    }

    public void setRoleLevel(String role_level) {
        this.role_level = role_level;
    }

    public String getRoleVip() {
        return role_vip;
    }

    public void setRoleVip(String role_vip) {
        this.role_vip = role_vip;
    }

    public String getRoleBalance() {
        return role_balance;
    }

    public void setRoleBalance(String role_balance) {
        this.role_balance = role_balance;
    }

//    public String getRolelevelCtime() {
//        return rolelevel_ctime;
//    }
//
//    public void setRolelevelCtime(String rolelevel_ctime) {
//        this.rolelevel_ctime = rolelevel_ctime;
//    }

    public String getRolelevelMtime() {
        return rolelevel_mtime;
    }

    public void setRolelevelMtime(String rolelevel_mtime) {
        this.rolelevel_mtime = rolelevel_mtime;
    }

    @Override
    public String toString() {
        return "PayRequestBean{" +
                "uid='" + uid + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_count='" + product_count + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_desc='" + product_desc + '\'' +
                ", exchange_rate='" + exchange_rate + '\'' +
                ", currency_name='" + currency_name + '\'' +
                ", role_type='" + role_type + '\'' +
                ", server_id='" + server_id + '\'' +
                ", server_name='" + server_name + '\'' +
                ", role_id='" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                ", party_name='" + party_name + '\'' +
                ", role_level='" + role_level + '\'' +
                ", role_vip='" + role_vip + '\'' +
                ", role_balance='" + role_balance + '\'' +
                ", rolelevel_mtime='" + rolelevel_mtime + '\'' +
                ", extend='" + extend + '\'' +
                '}';
    }
}
