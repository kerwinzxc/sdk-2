package com.game.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.domain.request.BaseRequestBean;
import com.game.sdk.domain.result.InitResultBean;
import com.game.sdk.domain.result.SystemServiceBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.log.T;
import com.game.sdk.util.ActivityHook;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;
import com.game.sdk.view.YunAuthenticationView;
import com.game.sdk.view.YunFastLoginView;
import com.game.sdk.view.YunForgetPwdView;
import com.game.sdk.view.YunLoginView;
import com.game.sdk.view.YunPayView;
import com.game.sdk.view.YunRegisterView;
import com.game.sdk.view.YunUserNameRegisterView;
import com.game.sdk.view.ViewStackManager;
import com.kymjs.rxvolley.RxVolley;

import static com.game.sdk.SdkConstant.LOGIN_INIT;

public class YunLoginActivity extends FragmentActivity {
    private static final String TAG = YunLoginActivity.class.getSimpleName();
    public  static int TYPE_FAST_LOGIN = 0;
    public  static int TYPE_LOGIN = 1;
    public  static int TYPE_AUTHENTICATION = 2;
    public  static int TYPE_FORGETPWD = 3;
    public  static int TYPE_PAY = 5;
    //    public final static int TYPE_REGISTER_LOGIN=2;
    private final static int CODE_LOGIN_FAIL = -1;//登陆失败
    private final static int CODE_LOGIN_CANCEL = -2;//用户取消登陆
    YunLoginView yunLoginView;
    YunRegisterView yunRegisterView;
    private YunFastLoginView yunFastLoginView;
    private YunUserNameRegisterView yunUserNameRegisterView;
    private YunAuthenticationView yunAuthenticationView;
    private YunForgetPwdView yunForgetPwdView;
    private YunPayView yunPayView;
    private ViewStackManager viewStackManager;
//    private boolean callBacked;//是否已经回调过了
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public final static int REQUEST_PHONE_STATE = 20;
    public static Activity yunLoginActivity ;
    public static UserResultBean data;
    public static OnLoginListener onLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityHook.hookOrientation(this);//hook，绕过检查
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this, "R.layout.yun_sdk_activity_yun_login"));
        YunLoginActivity.this.setFinishOnTouchOutside(false);
        setupUI();
        requestServiceInfo();
    }

    private void setScreenSize(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (DeviceUtil.isPortrait(this)){
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()*(0.9));
        }else {
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()/2);
            lp.height = (int) ((int)getWindowManager().getDefaultDisplay().getHeight()*(0.9));
        }
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
    }

    private void setupUI() {
        yunLoginActivity = this;
//        callBacked = false;
        onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
        viewStackManager = ViewStackManager.getInstance(this);
        int type = getIntent().getIntExtra("type", -1);
        yunLoginView = (YunLoginView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_loginView"));
        yunFastLoginView = (YunFastLoginView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_fastLoginView"));
        yunRegisterView = (YunRegisterView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_registerView"));
        yunUserNameRegisterView = (YunUserNameRegisterView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_userNameRegisterView"));
        yunAuthenticationView = (YunAuthenticationView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_authentication"));
        yunForgetPwdView  = (YunForgetPwdView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_forgetPwdView"));
        yunPayView = (YunPayView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_pay"));
        viewStackManager.addBackupView(yunLoginView);
        viewStackManager.addBackupView(yunFastLoginView);
        viewStackManager.addBackupView(yunRegisterView);
        viewStackManager.addBackupView(yunUserNameRegisterView);
        viewStackManager.addBackupView(yunAuthenticationView);
        viewStackManager.addBackupView(yunForgetPwdView);
        viewStackManager.addBackupView(yunPayView);
        switchUI(type);
    }

    public void switchUI(int type) {
        if (type == TYPE_FAST_LOGIN) {
            viewStackManager.addView(yunFastLoginView);
        } else if (type == TYPE_LOGIN) {
            viewStackManager.addView(yunLoginView);
        } else if(type == TYPE_AUTHENTICATION){
            SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
            viewStackManager.addView(yunAuthenticationView);
        } else if(type == TYPE_FORGETPWD){
            viewStackManager.addView(yunForgetPwdView);
        } else if(type == TYPE_PAY){
            viewStackManager.addView(yunPayView);
        }
//        else {
//            viewStackManager.addView(yunRegisterView);
//        }
    }

    @Override
    public void onBackPressed() {
        if (!viewStackManager.isLastView()){
            if (yunForgetPwdView.getVisibility() == View.VISIBLE){
                viewStackManager.removeTopView();
            }
        }
    }

    @Override
    protected void onResume() {
        setScreenSize();
        super.onResume();
    }

    public YunUserNameRegisterView getYunUserNameRegisterView() {
        return yunUserNameRegisterView;
    }


    public YunFastLoginView getYunFastLoginView() {
        return yunFastLoginView;
    }


    public YunRegisterView getYunRegisterView() {
        return yunRegisterView;
    }

    public YunLoginView getYunLoginView() {
        return yunLoginView;
    }

    public YunAuthenticationView getYunAuthenticationView() {
        return yunAuthenticationView;
    }

    public YunPayView getYunPayView() {
        return yunPayView;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewStackManager.clear();
        Log.d(TAG, "~~~ yunLoginOnDestroy");
//        if (!callBacked) {//还没有回调过，是用户取消登陆
//            UserResultBean userResultBean = new UserResultBean();
//            userResultBean.setCode(CODE_LOGIN_CANCEL);
//            userResultBean.setMsg("用户取消登陆");
//            OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
//            if (onLoginListener != null) {
//                onLoginListener.loginError(userResultBean);
//            }
//        }
    }

    private void requestServiceInfo(){
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.setMethod(SdkConstant.YUN_SDK_SYSTEM_SERVICE);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        Log.d("d", "~~~绑定:"+httpParamsBuild.getHttpParams().getUrlParams().toString());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SystemServiceBean>(YunLoginActivity.this) {
            @Override
            public void onDataSuccess(SystemServiceBean data) {
                if (data != null) {
                    if (data.getCode() == 1){
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_SERVICE_QQ, data.getInfo().getQq());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_SERVICE_time, data.getInfo().getTime());
                    }else{
                        T.showToast(YunLoginActivity.this, "获取客服信息失败:"+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(SystemServiceBean data) {
//                T.showToast(YunLoginActivity.this, "获取客服信息失败: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 通知回调成功并关闭activity
     */
    public void callBackFinish() {
//        this.callBacked = true;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public static void start(Context context, int type) {
        Intent starter = new Intent(context, YunLoginActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
        starter.putExtra("type", type);
        context.startActivity(starter);
    }

    public static void loginAuthenticCompleted(){
        OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
        if (onLoginListener != null) {
            onLoginListener.loginAuthenticOver();
        }
    }

    public static void loginCompleted(UserResultBean userResultBean){
        YunsdkManager.getInstance().requestSDKInit(LOGIN_INIT); //0 登陆 1 支付
        if (onLoginListener == null) onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
        onLoginListener.loginSuccess(userResultBean);
    }

    public static void loginFailed(UserResultBean userResultBean){
        if (onLoginListener == null) onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
        onLoginListener.loginError(userResultBean);
    }
}
