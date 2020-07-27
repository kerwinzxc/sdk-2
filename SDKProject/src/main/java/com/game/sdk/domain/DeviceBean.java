package com.game.sdk.domain;


public class DeviceBean {
    private String device_id="";//	是	STRING	玩家设备ID IOS为openUDID ANDROID 为IMEI码
    private String userua="";//	是	STRING	玩家设备UA
    private String ipaddrid;//	否	INT	玩家IP所在地编号
    private String deviceinfo;//	否	STRING	玩家设备信息 包括手机号码,用户系统版本,双竖线隔开
    private String idfv;//	否	STRING	玩家设备IDFV 有传
    private String idfa;//	否	STRING	玩家设备IDFA 有传
    private String local_ip;//	否	STRING	玩家设备本地IP 有传
    private String mac;//	否	STRING	玩家设备MAC 有传

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public String getUserua() {
        return userua;
    }

    public void setUserua(String userua) {
        this.userua = userua;
    }

    public String getIpaddrid() {
        return ipaddrid;
    }

    public void setIpaddrid(String ipaddrid) {
        this.ipaddrid = ipaddrid;
    }

    public String getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(String deviceinfo) {
        this.deviceinfo = deviceinfo;
    }

    public String getIdfv() {
        return idfv;
    }

    public void setIdfv(String idfv) {
        this.idfv = idfv;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getLocalIp() {
        return local_ip;
    }

    public void setLocalIp(String local_ip) {
        this.local_ip = local_ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
