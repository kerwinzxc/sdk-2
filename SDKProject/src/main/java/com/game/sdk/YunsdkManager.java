package com.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;
import com.game.sdk.db.LoginControl;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.domain.SubmitRoleInfoCallBack;
import com.game.sdk.domain.request.BaseRequestBean;
import com.game.sdk.domain.request.LogoutRequestBean;
import com.game.sdk.domain.request.PayRequestBean;
import com.game.sdk.domain.request.RoleRequestBean;
import com.game.sdk.domain.result.InitResultBean;
import com.game.sdk.domain.result.PayinfoResultBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.floatwindow.FloatViewManager;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.listener.OnLogoutListener;
import com.game.sdk.listener.OnPaymentListener;
import com.game.sdk.log.L;
import com.game.sdk.log.SP;
import com.game.sdk.log.T;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.ui.YunPayActivity;
import com.game.sdk.util.BaseAppUtil;
import com.game.sdk.util.ChannelNewUtil;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.DialogUtil;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.toolbox.HTTPSTrustManager;

import static com.game.sdk.SdkConstant.PAY_INIT;


public class YunsdkManager {
    private final static int CODE_INIT_FAIL = -1;
    private final static int CODE_INIT_SUCCESS = 1;
    private static final boolean isDebug = false;
    //    private static final boolean isDebug = true;
    private static final String TAG = YunsdkManager.class.getSimpleName();
    private static YunsdkManager instance;
    private Context mContext;
    private OnInitSdkListener onInitSdkListener;
    private OnPaymentListener paymentListener;
    private OnLoginListener onLoginListener;
    private OnLogoutListener onLogoutListener;
    PayRequestBean payRequestBean;
    OnPaymentListener onPaymentListener;
    private int initRequestCount = 0;
    // public static Notice notice; //登录后公告
    private String payAuth;
    public static boolean isSwitchLogin = false; // 是否切换

    private Handler yunsdkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_INIT_FAIL:
                    if (msg.arg2 < 3) {// 最多重试3次
                        initSdk(msg.arg2 + 1);
                    } else {
                        // 关闭等待loading
                        onInitSdkListener.initError(msg.arg1 + "", msg.obj + "");
                        DialogUtil.dismissDialog();
                    }
                    break;

                case CODE_INIT_SUCCESS:
                    L.e("ZDCsdk1", SdkConstant.YZ_AGENT);
                    initRequestCount++;
                    onInitSdkListener.initSuccess("200", "初始化成功");
                    // 去初始化
                    // gotoStartup(1);
                    break;
            }
        }
    };

    // 单例模式
    @NotProguard
    public static synchronized YunsdkManager getInstance() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            L.d(TAG, "实例化失败,未在主线程调用");
            return null;
        }
        if (null == instance) {
            instance = new YunsdkManager();
        }
        return instance;
    }

    @NotProguard
    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @NotProguard
    private YunsdkManager() {
    }

    /**
     * 初始化设置
     */
    private void initSetting() {
        // RxVolley.setDebug(isDebug);
        L.init(isDebug);
        HTTPSTrustManager.allowAllSSL();// 开启https支持
        try {
            RxVolley.setRequestQueue(
                    RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(mContext, "yunHttp")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化sdk
     *
     * @param context           上下文对象
     * @param onInitSdkListener 回调监听
     */
    @NotProguard
    public void initSdk(Context context, OnInitSdkListener onInitSdkListener)  {
        this.onInitSdkListener = onInitSdkListener;
        this.mContext = context;
        initSetting();
        YunsdkService.startService(mContext);
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(mContext, "");
        SdkConstant.YZ_CLIENTKEY = DeviceUtil.getClientInfo(context, SdkConstant.YZ_CLIENTKEY_NAME);
        SdkConstant.YZ_CLIENTSECRET = DeviceUtil.getClientInfo(context, SdkConstant.YZ_CLIENTSECRET_NAME);
        SdkConstant.YZ_DEVICE = DeviceUtil.getDeviceBean(context).getDeviceinfo();
        SdkConstant.YZ_UA = new WebView(context).getSettings().getUserAgentString();
        SdkConstant.DOMAIN_URL = DeviceUtil.getClientInfo(context, SdkConstant.YZ_URL_NAME);
        SdkConstant.ROOT_URL = SdkConstant.DOMAIN_URL + "router/";
        SdkConstant.BASE_URL = SdkConstant.ROOT_URL + "client";// 域名
        SdkConstant.YZ_SDK_FORMAT = DeviceUtil.getClientInfo(context, SdkConstant.YZ_FORMAT_NAME);

        // 初始化sp
        SP.init(mContext);
        SharedPreferencesUtil.getInstance(mContext, SdkConstant.YUN_SP_LABEL);
        initRequestCount = 0;
        requestSDKInitInfo();
    }

    private void requestSDKInitInfo(){
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.setMethod(SdkConstant.YUN_SDK_APP_INIT);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<InitResultBean>(mContext) {
            @Override
            public void onDataSuccess(InitResultBean data) {
                if (data != null) {
                    if (data.getCode() == 1){
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_LOGIN, data.getInfo().getIs_login());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_REG, data.getInfo().getIs_reg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_FAST_REG, data.getInfo().getIs_fast_reg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_INIT_IS_AUTH, data.getInfo().getIs_auth());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_INIT_IS_BIND, data.getInfo().getIs_bind());
//                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_PAY_IS_AUTH, data.getInfo().getPay_auth());
                        //系统化参数获取成功后再initSDK
                        initSdk(1);
                    }else{
                        T.showToast(mContext, "获取初始化参数失败:"+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(InitResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 初始化相关数据 count=1标示正常请求，2表示在初始化时发现rsakey错误后的重试流程
     */
    private void initSdk(final int count) {
        if (!BaseAppUtil.isNetWorkConneted(mContext)) {
            Toast.makeText(mContext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        L.e(TAG, "isSLogin:" + isSwitchLogin);
        isSwitchLogin = mContext.getSharedPreferences("yun_sdk_sp", Context.MODE_PRIVATE).getBoolean("switch_login",
                false);
        // TODO 如果判断有切换账号逻辑，则不执行nativeInit，将使用net获取的值,此时直接返回init_success
        // if (isSwitchLogin) {
        Message message = Message.obtain();
        message.what = CODE_INIT_SUCCESS;
        message.arg2 = count;
        yunsdkHandler.sendMessage(message);
        // return;
        // }

        // 初始化native
        // AsyncTask<String, Integer, String> nativeAsyncTask = new AsyncTask<String,
        // Integer, String>() {
        // @Override
        // protected void onPreExecute() {
        // super.onPreExecute();
        // //弹出等待loading在，installer和startup都完成后或者出现异常时关闭
        //// DialogUtil.showDialog(mContext, false, "初始化中，请稍后……");
        // }
        //
        // @Override
        // protected String doInBackground(String... params) {
        // //初始化本地c配置
        // if (SdkNative.initLocalConfig(mContext, SdkNative.TYPE_SDK)) {
        // SdkNative.initNetConfig(mContext, new NativeListener() {
        // @Override
        // public void onSuccess() {
        // Message message = Message.obtain();
        // message.what = CODE_INIT_SUCCESS;
        // message.arg2 = count;
        // yunsdkHandler.sendMessage(message);
        // }
        //
        // @Override
        // public void onFail(int code, final String msg) {
        // L.e("ZDCsdk", "native 失败code=" + code);
        // L.e("ZDCsdk", "native 失败msg=" + msg);
        // Message message = Message.obtain();
        // message.what = CODE_INIT_FAIL;
        // message.arg1 = code;
        // message.obj = msg;
        // message.arg2 = count;
        // yunsdkHandler.sendMessage(message);
        // }
        // });
        // } else {
        // Message message = Message.obtain();
        // message.what = CODE_INIT_SUCCESS;
        // message.arg2 = count;
        // yunsdkHandler.sendMessage(message);
        // }
        // return null;
        // }
        // };

        // nativeAsyncTask.execute();
    }

    /**
     * count=1标示正常请求，2表示在初始化时发现rsakey错误后的重试流程
     *
     * @param count
     *            当前是第几次请求
     */
    // private void gotoStartup(final int count) {
    // StartUpBean startUpBean = new StartUpBean();
    // int open_cnt = SdkNative.addInstallOpenCnt(mContext);//增量更新openCnt
    // startUpBean.setOpen_cnt(open_cnt + "");
    // HttpParamsBuild httpParamsBuild = new
    // HttpParamsBuild(GsonUtil.getGson().toJson(startUpBean));
    // HttpCallbackDecode httpCallbackDecode = new
    // HttpCallbackDecode<StartupResultBean>(mContext, httpParamsBuild.getAuthkey())
    // {
    // @Override
    // public void onDataSuccess(StartupResultBean data) {
    // if (data != null) {
    // SdkConstant.userToken = data.getUser_token();
    // SdkConstant.SERVER_TIME_INTERVAL = data.getTimestamp() -
    // System.currentTimeMillis();
    // SdkConstant.thirdLoginInfoList = data.getOauth_info();
    // if ("1".equals(data.getUp_status())) {//版本更新
    // SdkNative.resetInstall(mContext);//有更新重置install数据
    // if (!TextUtils.isEmpty(data.getUp_url())) {
    // YunsdkService.startServiceByUpdate(mContext, data.getUp_url());
    // }
    // }
    // onInitSdkListener.initSuccess("200", "初始化成功");
    // }
    // }
    //
    // @Override
    // public void onFailure(String code, String msg) {
    // if (count < 3) {
    // //1001 请求KEY错误 rsakey 解密错误
    // if (HttpCallbackDecode.CODE_RSA_KEY_ERROR.equals(code)) {//删除本地公钥，重新请求rsa公钥
    // SdkNative.resetInstall(mContext);
    // L.e(TAG, "rsakey错误，重新请求rsa公钥");
    // if (initRequestCount < 2) {//initSdk只重试一次rsa请求
    // initSdk(1000);
    // return;
    // }
    // }
    // super.onFailure(code, msg);
    // gotoStartup(count + 1);//重试
    // } else {
    // super.onFailure(code, msg);
    // onInitSdkListener.initError(code, msg);
    // }
    // }
    // };
    // httpCallbackDecode.setShowTs(false);
    // httpCallbackDecode.setLoadingCancel(false);
    // httpCallbackDecode.setShowLoading(false);//对话框继续使用install接口，在startup联网结束后，自动结束等待loading
    // RxVolley.post(SdkApi.getStartup(), httpParamsBuild.getHttpParams(),
    // httpCallbackDecode);
    // }

    /**
     * 执行退出登陆
     *
     * @param type
     */
    public void logoutExecute(final int type) {
        if (!LoginControl.isLogin()) {
            if (onLogoutListener != null) {
                onLogoutListener.logoutSuccess(type, SdkConstant.CODE_NOLOGIN, "尚未登陆");
            }
            return;
        }

        LogoutRequestBean logoutRequestBean = new LogoutRequestBean();
        logoutRequestBean.setMethod(SdkConstant.YUN_SDK_USER_LOGOUT);
        SharedPreferences sp = mContext.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        String session = sp.getString(SdkConstant.YUN_SP_SESSION, "");
        logoutRequestBean.setUid(uid);
        logoutRequestBean.setSession(session);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(logoutRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(mContext) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                L.e("退出成功");
                removeFloatView();
                if (onLogoutListener != null) {
                    onLogoutListener.logoutSuccess(type, SdkConstant.CODE_SUCCESS, "退出成功");
                }
                LoginControl.clearLogin();
            }

            @Override
            public void onDataFailure(UserResultBean data) {
                L.e("退出失败");
                if (onLogoutListener != null) {
                    onLogoutListener.logoutError(type, data.getCode() + "", data.getMsg());
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                L.e("退出失败");
                if (onLogoutListener != null) {
                    onLogoutListener.logoutError(type, code, msg);
                }
            }
        };

        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);

        if (logoutRequestBean.getUid() == null || logoutRequestBean.getUid().equals("")) {
            Toast.makeText(mContext, "缺少用户ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (logoutRequestBean.getSession() == null || logoutRequestBean.getSession().equals("")) {
            Toast.makeText(mContext, "缺少session参数", Toast.LENGTH_SHORT).show();
            return;
        }

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 退出登陆
     */
    @NotProguard
    public void logout() {
        logoutExecute(OnLogoutListener.TYPE_NORMAL_LOGOUT);
    }

    /**
     * 退出登录
     */
    @NotProguard
    public void addLogoutListener(final OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    /**
     * 打开用户中心
     */
    @NotProguard
    public void openUcenter() {
        if (!LoginControl.isLogin()) {
            Toast.makeText(mContext, "请先登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        FloatViewManager.getInstance(mContext).openucenter();
    }

    /**
     * 显示登录
     */
//    @NotProguard
//    public void showLogin(int isShowQuikLogin) {
//        LoginControl.clearLogin();
//        // 普通登陆类型
//        removeFloatView();
//        if (isShowQuikLogin) {
//            YunLoginActivity.start(mContext, YunLoginActivity.TYPE_FAST_LOGIN);
//        } else {
//            YunLoginActivity.start(mContext, YunLoginActivity.TYPE_LOGIN);
//        }
//    }
    /**
     * 显示登录
     */
    @NotProguard
    public void showLogin(int showLoginType) {
        // TYPE_FAST_LOGIN，TYPE_LOGIN为普通登陆类型
        if (showLoginType == YunLoginActivity.TYPE_FAST_LOGIN ) {
            loginClearInfo();
            YunLoginActivity.start(mContext, YunLoginActivity.TYPE_FAST_LOGIN);
        } else if (showLoginType == YunLoginActivity.TYPE_LOGIN ){
            loginClearInfo();
            YunLoginActivity.start(mContext, YunLoginActivity.TYPE_LOGIN);
        }
        else if(showLoginType == YunLoginActivity.TYPE_AUTHENTICATION){
            YunLoginActivity.start(mContext, YunLoginActivity.TYPE_AUTHENTICATION);
        }
    }
    // 普通登陆类型清除用户信息
    private void loginClearInfo(){
        LoginControl.clearLogin();
        removeFloatView();
    }

    /**
     * 切换账号
     */
    @NotProguard
    public void switchAccount() {
        logoutExecute(OnLogoutListener.TYPE_SWITCH_ACCOUNT);
    }

    /**
     * 注册一个登录监听，需要在不使用的时候解除监听，例如onDestory方法中解除
     *
     * @param onLoginListener 登陆监听
     */
    @NotProguard
    public void addLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    /**
     * 解除登陆监听
     */
    @NotProguard
    public void removeLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = null;
    }

    /**
     * 启动支付
     *
     * @param payRequestBean
     */
    @NotProguard
    public void showPay(PayRequestBean payRequestBean, OnPaymentListener onPaymentListener) {
        this.payRequestBean = payRequestBean;
        this.onPaymentListener = onPaymentListener;
        requestSDKInit(PAY_INIT);
    }

    @NotProguard
    public void varifyPayParams(){
        if (payRequestBean == null) return;
        if (onPaymentListener == null) return;

        if (payRequestBean.getOutTradeNo() == null || payRequestBean.getOutTradeNo().equals("")) {
            Toast.makeText(mContext, "缺少游戏交易号参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getProductId() == null || payRequestBean.getProductId().equals("")) {
            Toast.makeText(mContext, "缺少商品ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getProductName() == null || payRequestBean.getProductName().equals("")) {
            Toast.makeText(mContext, "缺少商品名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getProductPrice() == null || payRequestBean.getProductPrice().equals("")) {
            Toast.makeText(mContext, "缺少价格参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getProductCount() == null || payRequestBean.getProductCount().equals("")) {
            Toast.makeText(mContext, "缺少商品数量参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getExchangeRate() == null || payRequestBean.getExchangeRate().equals("")) {
            Toast.makeText(mContext, "缺少虚拟币兑换比例参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getServerId() == null || payRequestBean.getServerId().equals("")) {
            Toast.makeText(mContext, "缺少游戏服务器ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getServerName() == null || payRequestBean.getServerName().equals("")) {
            Toast.makeText(mContext, "缺少游戏服务器名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getRoleId() == null || payRequestBean.getRoleId().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getRoleName() == null || payRequestBean.getRoleName().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getPartyName() == null) {
            Toast.makeText(mContext, "缺少帮派名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getRoleLevel() == null || payRequestBean.getRoleLevel().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色等级参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getRoleVip() == null || payRequestBean.getRoleVip().equals("")) {
            Toast.makeText(mContext, "缺少玩家vip等级参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (payRequestBean.getRoleBalance() == null || payRequestBean.getRoleBalance().equals("")) {
            Toast.makeText(mContext, "缺少玩家游戏中游戏币余额参数", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (payRequestBean.getRolelevelCtime() == null /*|| payRequestBean.getRolelevelCtime().equals("")*/) {
//            Toast.makeText(mContext, "缺少玩家创建角色的时间参数", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (payRequestBean.getRolelevelMtime() == null /*|| payRequestBean.getRolelevelMtime().equals("")*/) {
            Toast.makeText(mContext, "缺少玩家角色等级变化时间参数", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent mIntent = new Intent(mContext, YunPayActivity.class);
        Bundle mBundle = new Bundle();
//        mBundle.putString(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_WEB_PAY);
        mBundle.putSerializable(SdkConstant.YUN_WEB_PAY_INFO, payRequestBean);
        mIntent.putExtras(mBundle);
        paymentListener = onPaymentListener;
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(mIntent);
    }

    public OnPaymentListener getPaymentListener() {
        return paymentListener;
    }

    @NotProguard
    public void setRoleInfo(RoleRequestBean roleRequestBean, final SubmitRoleInfoCallBack submitRoleInfoCallBack) {
        if (!BaseAppUtil.isNetWorkConneted(mContext)) {
            Toast.makeText(mContext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!LoginControl.isLogin()) {
            Toast.makeText(mContext, "请先登录！", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = mContext.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        roleRequestBean.setUid(uid);
        roleRequestBean.setMethod(SdkConstant.YUN_SDK_USER_ROLE_UPDATE);
        String sid = ChannelNewUtil.getChannel(mContext);

        if (sid == null || sid.equals("")) {
            sid = SdkConstant.YZ_DEFAULT_SID;
        }
        L.e("sid = " + sid);
        roleRequestBean.setSid(sid);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(roleRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PayinfoResultBean>(mContext) {
            @Override
            public void onDataSuccess(PayinfoResultBean data) {
                if (data != null) {
                    L.e("提交角色成功：" + data.getMsg());
                    // 接口回调通知
                    submitRoleInfoCallBack.submitSuccess();
                }
            }

            @Override
            public void onDataFailure(PayinfoResultBean data) {
                submitRoleInfoCallBack.submitFail(data.getMsg());
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                L.e("提交角色失败！");
                if (!TextUtils.isEmpty(strMsg)) {
                    submitRoleInfoCallBack.submitFail(strMsg);
                } else {
                    submitRoleInfoCallBack.submitFail("发送失败");
                }
            }
        };

        if (roleRequestBean.getUid() == null || roleRequestBean.getUid().equals("")) {

            Toast.makeText(mContext, "缺少用户ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleType() == null || roleRequestBean.getRoleType().equals("")) {
            Toast.makeText(mContext, "缺少角色类型参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getServerId() == null || roleRequestBean.getServerId().equals("")) {
            Toast.makeText(mContext, "缺少游戏服务器ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getServerName() == null || roleRequestBean.getServerName().equals("")) {
            Toast.makeText(mContext, "缺少游戏服务器名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleId() == null || roleRequestBean.getRoleId().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色ID参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleName() == null || roleRequestBean.getRoleName().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getPartyName() == null) {
            Toast.makeText(mContext, "缺少帮派名称参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleLevel() == null || roleRequestBean.getRoleLevel().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色等级参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleVip() == null || roleRequestBean.getRoleVip().equals("")) {
            Toast.makeText(mContext, "缺少玩家vip等级参数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleRequestBean.getRoleBalance() == null || roleRequestBean.getRoleBalance().equals("")) {
            Toast.makeText(mContext, "缺少玩家游戏中游戏币余额参数", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (roleRequestBean.getRolelevelCtime() == null || roleRequestBean.getRolelevelCtime().equals("")) {
//            Toast.makeText(mContext, "缺少玩家创建角色的时间参数", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (roleRequestBean.getRolelevelMtime() == null || roleRequestBean.getRolelevelMtime().equals("")) {
            Toast.makeText(mContext, "缺少玩家角色等级变化时间参数", Toast.LENGTH_SHORT).show();
            return;
        }

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 显示浮标
     */
    @NotProguard
    public void showFloatView(Activity activity) {
        if (!LoginControl.isLogin()) {
            return;
        }
        final int sdkVersion = mContext.getApplicationInfo().targetSdkVersion;

        FloatViewManager.getInstance(mContext).showFloat(activity);
//        boolean floatWindowOpAllowed = HLAppUtil.isFloatWindowOpAllowed(mContext);
//        if (sdkVersion >= 23 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.canDrawOverlays(mContext)) {
//                FloatViewManager.getInstance(mContext).showFloat(activity);
//            } else {
//                new OpenFloatPermissionDialog().showDialog(mContext, true, null,
//                        new OpenFloatPermissionDialog.ConfirmDialogListener() {
//                            @Override
//                            public void ok() {
//                                // T.s(mContext, "悬浮窗权限未开启，请在权限管理中找到悬浮窗，并开启。");
//                                // 启动Activity让用户授权
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                mContext.startActivity(intent);
//                            }
//                            @Override
//                            public void cancel() { }
//                        });
//            }
//        }
//        else {
//            FloatViewManager.getInstance(mContext).showFloat(activity);
//        }

    }

    /**
     * 隐藏浮标
     */
    @NotProguard
    public void removeFloatView() {
        try {
            FloatViewManager.getInstance(mContext).hidFloat();
            L.e(TAG, "浮点隐藏了");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 资源回收
     */
    @NotProguard
    public void recycle() {
        try {
            onLogoutListener = null;// 登出监听置null
            logoutExecute(OnLogoutListener.TYPE_NORMAL_LOGOUT);
            // 移除浮标
            removeLoginListener(onLoginListener);
            LoginControl.clearLogin();
            FloatViewManager.getInstance(mContext).removeFloat();
            Intent intent = new Intent(mContext, YunsdkService.class);
            mContext.stopService(intent);
            mContext = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户注册的登陆监听
     *
     * @return
     */
    public OnLoginListener getOnLoginListener() {
        return onLoginListener;
    }

    public void requestSDKInit(final int type){
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.setMethod(SdkConstant.YUN_SDK_APP_INIT);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<InitResultBean>(mContext) {
            @Override
            public void onDataSuccess(InitResultBean data) {
                if (data != null) {
                    if (data.getCode() == 1){
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_LOGIN, data.getInfo().getIs_login());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_REG, data.getInfo().getIs_reg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_IS_FAST_REG, data.getInfo().getIs_fast_reg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_INIT_IS_AUTH, data.getInfo().getIs_auth());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_INIT_IS_BIND, data.getInfo().getIs_bind());
//                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_PAY_IS_AUTH, data.getInfo().getPay_auth());
                        payAuth = data.getInfo().getPay_auth();
                        if (type == PAY_INIT){
                            if ( (!TextUtils.isEmpty(payAuth)) && payAuth.equals(SdkConstant.YUN_YES)){
                                //已实名过不需再实名
                                boolean userAuth = (Boolean) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_AUTH, false);
                                if(!userAuth){
                                    SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_PAY);
                                    showLogin(YunLoginActivity.TYPE_AUTHENTICATION);
                                }else {
                                    varifyPayParams();
                                }

                            }else {
                                varifyPayParams();
                            }
                        }

                    }else{
                        T.showToast(mContext, "获取初始化参数失败:"+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(InitResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
