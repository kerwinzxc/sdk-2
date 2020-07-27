package com.game.sdk;

import com.game.sdk.domain.NotProguard;

import java.util.List;

@NotProguard
public class SdkConstant {

    public static String YZ_CLIENTKEY_NAME = "YZ_CLIENTKEY";//API接口验证序号
    public static String YZ_CLIENTSECRET_NAME = "YZ_CLIENTSECRET";//API接口密钥
    public static String YZ_URL_NAME = "YZ_URL";//接口服务器地址
    public static String YZ_FORMAT_NAME = "YZ_FORMAT";

    //nativie自动注入常量
    public static String YZ_APPKEY = "";//API接口验证序号
    public static String YZ_APPSECRET = "";//API接口密钥
    public static String YZ_CLIENTKEY = "";//客户端序号
    public static String YZ_CLIENTSECRET = "";//客户端密钥

    public static String YZ_SDK_VERSION = "V2.2.0";

    public static String YZ_SDK_FORMAT = "json";

    public static String YZ_DEVICE = "";

    public static String YZ_UA = "";

    public static String YZ_DEFAULT_SID = "0";//API接口验证序号

    public static String DOMAIN_URL = "";

    public static String ROOT_URL = DOMAIN_URL + "router/";//网页地址

    //地址信息
    public static String BASE_URL = ROOT_URL + "client";//接口地址

    public static String BASE_SUFFIX_URL = "";//后缀
    public static String BASE_IP;

    public static String YZ_AGENT = "";
    public static String FROM = "3";//1-WEB、2-WAP、3-Android、4-IOS、5-WP

    //渠道编号
    public static String PROJECT_CODE;
    //使用地址方式
    public static String USE_URL_TYPE;//1域名，2ip

    public static String APP_PACKAGENAME;//app包名
    //是否显示邀请码
    public static String SHOW_INVITATION = SdkConfig.SHOW_INVITATION;

    //是否显示实名认证
    public static String SHOW_INDENTIFY = SdkConfig.SHOW_INDENTIFY;

    //rsa密钥
    public static String RSA_PUBLIC_KEY;
//    ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOvTgQeOuMIop6psK0Mk58fHur" +
//            "Sbx4pKye3reS5a6Lax3IrLazLGKQEnd+S+1q5BBVwc+JCJi/AUdbJeDkx+cCfE0M" +
//            "LbNt5DiZeKBN/hV4C+pOm0AjEkWQmJfIzsgfVpcifn1R5KsgZ0FtbfO7MOFAcYox" +
//            "HCYZduX4jhIZbgxrmwIDAQAB";

    /**
     * 初始化注入
     */
    public static String userToken = "";

    /**
     * 服务器时间-本地时间
     */
    public static long SERVER_TIME_INTERVAL = 0;

//    public static DeviceBean deviceBean;

//    public static List<ThirdLoginInfo> thirdLoginInfoList;//第三方登陆信息

    //sp中保存的rsa密钥
    public static String SP_RSA_PUBLIC_KEY = "sp_rsa_public_key";
    public final static String SP_VERSION_CODE = "versionCode";
    public final static String SP_OPEN_CNT = "sp_openCnt";

    public final static boolean READ_APP_AGENT = false;

    public final static String CODE_SUCCESS = "200";
    public final static String CODE_NOLOGIN = "-1000";//未登陆

    public static String packageName = "";//包名

    //接口
    public final static String YUN_SDK_SMS_REG_SEND = "sdk.sms.reg.send";//用户注册短信发送
    public final static String YUN_SDK_USER_REG = "sdk.user.reg";//用户注册
    public final static String YUN_SDK_USER_REG_MOBILE = "sdk.user.reg.mobile";//用户注册（手机）
    public final static String YUN_SDK_USER_LOGIN = "sdk.user.login";//用户登入
    public final static String YUN_SDK_USER_LOGIN_TMP = "sdk.user.login.tmp";//用户登入（游客）
    public final static String YUN_SDK_ORDER_ADD = "sdk.order.add";//订单添加
    public final static String YUN_SDK_ORDER_GET_LINK = "sdk.order.iop.get";//获取第三方支付链接
    public final static String YUN_SDK_USER_ROLE_UPDATE = "sdk.user.role.update";//玩家游戏角色提交
    public final static String YUN_SDK_USER_LOGOUT = "sdk.user.logout";//用户退出登入
    public final static String YUN_SDK_ORDER_STATUS_GET = "sdk.order.status.get";
    public final static String YUN_SDK_USER_PWD_IFORGOT = "sdk.user.pwd.iforgot"; //忘记密码
    public final static String YUN_SDK_SMS_IFORGOT_SEND = "sdk.sms.iforgot.send"; //忘记密码短信
    public final static String YUN_SDK_USER_PWD_SET = "sdk.user.pwd.set"; //账号修改密码
    public final static String YUN_SDK_USER_PWD_MOBILE = "sdk.user.pwd.mobile"; //手机修改密码
    public final static String YUN_SDK_SMS_BIND_SEND = "sdk.sms.bind.send"; //绑定手机号短信
    public final static String YUN_SDK_SMS_CHANGE_SEND = "sdk.sms.change.send"; //换绑手机号短信
    public final static String YUN_SDK_SMS_CHANGE_CHECK = "sdk.sms.change.check"; //换绑手机号校验
    public final static String YUN_SDK_USER_MOBILE_BIND = "sdk.user.mobile.bind"; //绑定手机号
    public final static String YUN_SDK_USER_MOBILE_CHANGE = "sdk.user.mobile.bind.change"; //换绑手机号
    public final static String YUN_SDK_SMS_VERIFY_SEND = "sdk.sms.verify.send"; //给已绑定手机号的用户发送验证类（verify）短信验证码
    public final static String YUN_SDK_AUTHENTICATION = "sdk.user.auth"; //实名认证
    public final static String YUN_SDK_ORDER_LIST = "sdk.order.list"; //订单列表
    public final static String YUN_SDK_SYSTEM_SERVICE = "sdk.system.service"; //客服信息
    public final static String YUN_SDK_APP_INIT = "sdk.app.init"; //SDK初始化参数
    public final static String YUN_SDK_GIFT_LIST = "sdk.gift.list"; //礼包列表
    public final static String YUN_SDK_GIFT_GET = "sdk.user.gift.get"; //礼包领取
    public final static String YUN_USER_GIFT_LIST = "sdk.user.gift.list"; //已领礼包列表

    //网页跳转标示
    public final static String YUN_WEB_STYLE = "op_style";//网页模式标示
    public final static String YUN_WEB_STYLE_VALUE = "op_style_value";//网页模式标示
    public final static String YUN_WEB_PAY_LINK = "pay_link";
    public final static String YUN_WEB_PAY_INFO = "payinfo";//支付信息

    public final static String YUN_AGREEMENT = "agreement";//协议页面
    public final static String YUN_REGISTER_AGREEMENT = "register_agreement";//注册协议
    public final static String URL_REGISTER_AGREEMENT = "https://share.8688games.com/agreement.html";//注册协议url
    public final static String URL_PRIVACY_AGREEMENT = "https://share.8688games.com/policy.html";//隐私条款url

    //登录信息保存标签
    public final static String YUN_SP_LABEL = "user_info";
    public final static String YUN_SP_UID = "uid";
    public final static String YUN_SP_SESSION = "session";
    public final static String YUN_SP_AUTH = "auth";
    public final static String YUN_SP_BIND_MOBILE = "bind_mobile";
    public final static String YUN_SP_IDCARD = "idcard";
    public final static String YUN_SP_REALNAME = "realname";
    public final static String YUN_SP_BIND = "bind"; //是否已绑定手机号


    public final static String YUN_SP_SERVICE_QQ = "service_qq";
    public final static String YUN_SP_SERVICE_time = "service_time";
    //游戏初始化相关参数
    public final static String YUN_SP_IS_LOGIN = "sp_is_login";
    public final static String YUN_SP_IS_REG = "sp_is_reg";
    public final static String YUN_SP_IS_FAST_REG = "sp_is_fast_reg";
    public final static String YUN_SP_INIT_IS_AUTH = "sp_is_auth";
    public final static String YUN_SP_INIT_IS_BIND = "sp_is_bind";//是否需要绑定手机号 Y:必须绑定 N:无需绑定 T:可跳过绑定
    public final static String YUN_SP_PAY_IS_AUTH = "sp_pay_auth";
    public final static String YUN_YES = "Y";
    public final static String YUN_NO = "N";

    public final static String USERNAME = "username";
    public final static String PWD = "pwd";

    public final static String AUTHENTICTYPE = "AUTHENTICTYPE";
    public final static String AUTHENTICTYPE_LOGIN = "AUTHENTICTYPE_LOGIN";
    public final static String AUTHENTICTYPE_USERCENTER = "AUTHENTICTYPE_USERCENTER";
    public final static String AUTHENTICTYPE_PAY = "AUTHENTICTYPE_PAY";

    public final static String BINDMOBILETYPE = "BINDMOBILETYPE";
    public final static String BINDMOBILETYPE_LOGIN = "BINDMOBILETYPE_LOGIN";
    public final static String BINDMOBILETYPE_USERCENTER = "BINDMOBILETYPE_USERCENTER";

    public final static int LOGIN_INIT = 0;
    public final static int PAY_INIT = 1;

    public final static int GIFT_MAKECODE = 0;
    public final static int GIFT_UNIQUECODE = 1;

    public final static String GIFT_NOTGET = "0";
    public final static String GIFT_GETTED = "1";






}
