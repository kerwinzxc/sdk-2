package com.game.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.game.sdk.SdkConstant;
import com.game.sdk.db.LoginControl;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.UserInfo;
import com.game.sdk.domain.request.LoginRequestBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.ui.YunBindMobileActivity;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.DialogUtil;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

public class YunFastLoginView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = YunFastLoginView.class.getSimpleName();
    //快速登陆
    ImageView yunIvFastLoading;

    TextView yunTvFastUserName;

    TextView yunTvFastChangeCount;

    LinearLayout yunLlFastLogin;
    private YunLoginActivity loginActivity;
    private ViewStackManager viewStackManager;
    Handler handler = new Handler();

    public YunFastLoginView(Context context) {
        super(context);
        setupUI();
    }

    public YunFastLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunFastLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void setupUI() {
        loginActivity = (YunLoginActivity) getContext();
        viewStackManager = ViewStackManager.getInstance(loginActivity);
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(), MResource.LAYOUT, "yun_sdk_include_fast_login"), this);
        yunIvFastLoading = (ImageView) findViewById(MResource.getIdByName(getContext(), "id", "yun_sdk_iv_fastLoading"));
        yunTvFastUserName = (TextView) findViewById(MResource.getIdByName(getContext(), "id", "yun_sdk_tv_fastUserName"));
        yunTvFastChangeCount = (TextView) findViewById(MResource.getIdByName(getContext(), "id", "yun_sdk_tv_fastChangeCount"));
        yunLlFastLogin = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "id", "yun_sdk_ll_fast_login"));

        yunTvFastChangeCount.setOnClickListener(this);
        yunIvFastLoading.setAnimation(DialogUtil.rotaAnimation());
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 在设置显示的时候判断是否能够进行快速登陆，不能则直接去登陆
     *
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            //获取最后一次登陆账号进行快速登陆
            UserInfo userInfoLast = UserLoginInfodao.getInstance(loginActivity).getUserInfoLast();
            if (userInfoLast != null) {
                if (!TextUtils.isEmpty(userInfoLast.username) && !TextUtils.isEmpty(userInfoLast.password)) {
                    yunTvFastUserName.setText(userInfoLast.username);
                    L.e("读取账号密码直接登录：userInfoLast.username = " + userInfoLast.username + " userInfoLast.password = " + userInfoLast.password);
                    submitLogin(userInfoLast.username, userInfoLast.password);
                    return;
                }
            }
            //上一次没有登录过，直接去登陆界面
            viewStackManager.addView(loginActivity.getYunLoginView());
            viewStackManager.removeView(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yunTvFastChangeCount.getId()) {
            viewStackManager.addView(loginActivity.getYunLoginView());
            viewStackManager.removeView(this);
        }
    }

    private void submitLogin(final String userName, final String password) {
        final LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setMethod(SdkConstant.YUN_SDK_USER_LOGIN);
        loginRequestBean.setUname(userName);
        loginRequestBean.setPasswd(password);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(loginRequestBean));
        Log.d("d", "~~~fast:"+httpParamsBuild.getHttpParams().getUrlParams().toString());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(loginActivity) {
            @Override
            public void onDataSuccess(final UserResultBean data) {
                //快速登陆需要延时2秒供用户选择是否切换账号
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data != null && getVisibility() == VISIBLE) {//当前界面还在显示状态才执行
                            L.e("登陆成功：" + data.getMsg());
                            //接口回调通知
                            SharedPreferences sp = loginActivity.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
                            sp.edit().putString(SdkConstant.YUN_SP_SESSION, data.getInfo().getSession())//
                                    .putString(SdkConstant.YUN_SP_UID, data.getInfo().getUid())//
                                    .putBoolean(SdkConstant.YUN_SP_AUTH, data.getInfo().isAuth())//
                                    .putString(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile())//
                                    .putString(SdkConstant.YUN_SP_IDCARD, data.getInfo().getIdcard())
                                    .putString(SdkConstant.YUN_SP_REALNAME, data.getInfo().getRealname())
                                    .putBoolean(SdkConstant.YUN_SP_BIND, data.getInfo().isBind())
                                    .commit();

                            Log.d(TAG,"~~~快速结果:"+data.getInfo().toString() );

                            LoginControl.saveUserToken(data.getInfo().getSession());

                            loginActivity.loginCompleted(data);

                            //保存账号到数据库
                            if (!UserLoginInfodao.getInstance(loginActivity).findUserLoginInfoByName(userName)) {
                                UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(userName, password);
                            } else {
                                UserLoginInfodao.getInstance(loginActivity).deleteUserLoginByName(userName);
                                UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(userName, password);
                            }

                            //是否需要实名认证
                            String YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
                            //是否需要绑定手机号
                            String YUN_SP_INIT_IS_BIND = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_BIND,"");
                            if (!YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_NO)){ //需要实名认证走实名验证
                                if (!data.getInfo().isAuth()){ //没有实名验证过
                                    SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
                                    viewStackManager.showView(viewStackManager.getViewByClass(YunAuthenticationView.class));
                                    return;
                                }
                            }
                            if (!YUN_SP_INIT_IS_BIND.equals(SdkConstant.YUN_NO)){ //需要绑定手机号的走绑定手机号
                                if (!data.getInfo().isBind()) {//没有绑定过手机
                                    loginActivity.callBackFinish();
                                    SharedPreferencesUtil.putData(SdkConstant.BINDMOBILETYPE, SdkConstant.BINDMOBILETYPE_LOGIN);
                                    Intent mIntent = new Intent(getContext(), YunBindMobileActivity.class);
                                    getContext().startActivity(mIntent);
                                    return;
                                }
                            }
                            loginActivity.loginAuthenticCompleted();
                            loginActivity.callBackFinish();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onDataFailure(UserResultBean data) {
                viewStackManager.addView(loginActivity.getYunLoginView());
                viewStackManager.removeView(YunFastLoginView.this);
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                //快速登陆出错，直接去登陆页面
                viewStackManager.addView(loginActivity.getYunLoginView());
                viewStackManager.removeView(YunFastLoginView.this);
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("正在登录...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
