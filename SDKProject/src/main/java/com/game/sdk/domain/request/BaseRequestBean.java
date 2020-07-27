package com.game.sdk.domain.request;


import com.game.sdk.SdkConstant;

public class BaseRequestBean {

    /**
     * method : sdk.user.reg
     * v : 1.0
     * session :
     * format :
     * client_key : 97393242
     * device : iPhone7,1||10.3.2||1||zh-Hans-CN
     * ua : Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89
     * timestamp : 1508299479
     * sign : 4766B6E2FEA09D076274034D3A69930C
     */

    private String method;
    private String v = "1.0";
    private String sdk_v = SdkConstant.YZ_SDK_VERSION;
    private String session;
    private String format = SdkConstant.YZ_SDK_FORMAT;
    private String client_key = SdkConstant.YZ_CLIENTKEY;
    private String device = SdkConstant.YZ_DEVICE;
    private String ua = SdkConstant.YZ_UA;
    private String timestamp;
    private String sign;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        setTimestamp((System.currentTimeMillis() / 1000) + "");
        this.method = method;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getClientKey() {
        return client_key;
    }

    public void setClientKey(String client_key) {
        this.client_key = client_key;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSdk_v() {
        return sdk_v;
    }

    public void setSdk_v(String sdk_v) {
        this.sdk_v = sdk_v;
    }
}
