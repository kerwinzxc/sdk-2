package com.game.sdk.domain.request;


import java.io.Serializable;

public class RoleRequestBean extends BaseRequestBean implements Serializable {


    /**
     * uid : 5
     * server_name : hsjsjhsnznznsn
     * role_name : e57d
     * role_type : 1
     * server_id : 123
     * role_id : 412
     * party_name : eeeee
     * role_vip : 11
     * role_balance : 2
     * rolelevel_ctime : 1580166691
     * rolelevel_mtime : 1851190735
     * role_level : 15
     */

    private String uid;
    private String server_name;
    private String role_name;
    private String role_type;
    private String server_id;
    private String role_id;
    private String party_name;
    private String role_vip;
    private String role_balance;
//    private String rolelevel_ctime;
    private String rolelevel_mtime;
    private String role_level;
    /**
     * sid : 11111111111
     */

    private String sid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServerName() {
        return server_name;
    }

    public void setServerName(String server_name) {
        this.server_name = server_name;
    }

    public String getRoleName() {
        return role_name;
    }

    public void setRoleName(String role_name) {
        this.role_name = role_name;
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

    public String getRoleId() {
        return role_id;
    }

    public void setRoleId(String role_id) {
        this.role_id = role_id;
    }

    public String getPartyName() {
        return party_name;
    }

    public void setPartyName(String party_name) {
        this.party_name = party_name;
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

    public String getRoleLevel() {
        return role_level;
    }

    public void setRoleLevel(String role_level) {
        this.role_level = role_level;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
