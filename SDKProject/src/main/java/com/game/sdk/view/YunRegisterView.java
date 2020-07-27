package com.game.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.db.LoginControl;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.request.SmsSendRequestBean;
import com.game.sdk.domain.request.UserMobileRegRequestBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.ui.WebViewActivity;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.ChannelNewUtil;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;

/**
 * 手机注册
 */
public class YunRegisterView extends FrameLayout implements View.OnClickListener {
    private YunLoginActivity loginActivity;
    private EditText yun_sdk_et_mRegisterAccount, yun_sdk_et_mRegisterPwd, yun_sdk_et_mRegisterCode;
    private Button yun_sdk_btn_mRegisterSendCode, yun_sdk_btn_mRegisterSubmit;
    private LinearLayout yun_sdk_ll_mRegisterUserNameRegister, yun_sdk_ll_mRegisterGotoLogin;
    private ViewStackManager viewStackManager;
    private ImageView yun_sdk_img_show_pwd, yun_sdk_iv_mobile_cancel, yun_sdk_iv_code_cancel, yun_sdk_iv_pwd_cancel;
    private CheckBox cb_privacy_policy;
    private TextView tv_privacy_policy, tv_register_policy;
    private boolean showPwd = false;
    private SharedPreferences sp;
    public YunRegisterView(Context context) {
        super(context);
        setupUI();
    }

    public YunRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunRegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void setupUI() {
        loginActivity = (YunLoginActivity) getContext();

        viewStackManager = ViewStackManager.getInstance(loginActivity);
        sp = loginActivity.getSharedPreferences(SdkConstant.YUN_SP_LABEL,
                Context.MODE_PRIVATE);
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(),
                MResource.LAYOUT, "yun_sdk_inlude_mobile_register"), this);
        yun_sdk_et_mRegisterAccount = (EditText)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_et_mRegisterAccount"));
        yun_sdk_et_mRegisterPwd = (EditText)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_et_mRegisterPwd"));
        yun_sdk_et_mRegisterCode = (EditText)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_et_mRegisterCode"));
        yun_sdk_btn_mRegisterSendCode = (Button)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_btn_mRegisterSendCode"));
//        yun_sdk_et_mInvitationCode = (EditText)
//                findViewById(MResource.getIdByName(loginActivity,
//                        "R.id.yun_sdk_et_mInvitationCode"));
        yun_sdk_btn_mRegisterSubmit = (Button)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_btn_mRegisterSubmit"));
        yun_sdk_ll_mRegisterUserNameRegister = (LinearLayout)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_ll_mRegisterUserNameRegister"));
        yun_sdk_ll_mRegisterGotoLogin = (LinearLayout)
                findViewById(MResource.getIdByName(loginActivity,
                        "R.id.yun_sdk_ll_mRegisterGotoLogin"));
        yun_sdk_img_show_pwd = (ImageView)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_img_show_pwd"));
        yun_sdk_iv_mobile_cancel = (ImageView)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_iv_mobile_cancel"));
        yun_sdk_iv_code_cancel = (ImageView)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_iv_code_cancel"));
        yun_sdk_iv_pwd_cancel = (ImageView)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_iv_pwd_cancel"));
        cb_privacy_policy = (CheckBox)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.cb_privacy_policy"));
        tv_privacy_policy = (TextView)findViewById(MResource.getIdByName(getContext(),"R.id.tv_privacy_policy"));
        tv_register_policy = (TextView)findViewById(MResource.getIdByName(getContext(),"R.id.tv_register_policy"));

        yun_sdk_btn_mRegisterSendCode.setOnClickListener(this);
        yun_sdk_btn_mRegisterSubmit.setOnClickListener(this);
        yun_sdk_img_show_pwd.setOnClickListener(this);
        yun_sdk_iv_mobile_cancel.setOnClickListener(this);
        yun_sdk_iv_code_cancel.setOnClickListener(this);
        yun_sdk_iv_pwd_cancel.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_register_policy.setOnClickListener(this);
        yun_sdk_ll_mRegisterUserNameRegister.setOnClickListener(this);
        // yun_sdk_ll_mRegisterFastRegister.setOnClickListener(this);
        yun_sdk_ll_mRegisterGotoLogin.setOnClickListener(this);
        cb_privacy_policy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    yun_sdk_btn_mRegisterSubmit.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.bg_selected")));
                }else {
                    yun_sdk_btn_mRegisterSubmit.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.bg_normal")));
                }
            }
        });
        //文字监听
        setTextChangedListener(yun_sdk_et_mRegisterAccount, yun_sdk_iv_mobile_cancel);
        setTextChangedListener(yun_sdk_et_mRegisterCode, yun_sdk_iv_code_cancel);
        setTextChangedListener(yun_sdk_et_mRegisterPwd, yun_sdk_iv_pwd_cancel);



        // NEW 2017年2月28日10:12:23 - 加载switch资源
//         if (YunsdkManager.isSwitchLogin) {
//         MResource.loadImgFromSDCard(yun_sdk_iv_logo, MResource.PATH_FILE_ICON_LOGO);
//         }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_ll_mRegisterGotoLogin.getId()) {// 退回登陆
            viewStackManager.showView(viewStackManager.getViewByClass(YunLoginView.class));
        } else if (view.getId() == yun_sdk_btn_mRegisterSendCode.getId()) {// 发送验证码
            sendSms();
        } else if (view.getId() == yun_sdk_btn_mRegisterSubmit.getId()) {// 提交注册
            submitRegister();
        }
        else if (view.getId() == yun_sdk_ll_mRegisterUserNameRegister.getId()) {// 用户名注册
            YunUserNameRegisterView yunUserNameRegisterView = (YunUserNameRegisterView) viewStackManager
                    .getViewByClass(YunUserNameRegisterView.class);
            if (yunUserNameRegisterView != null) {
                yunUserNameRegisterView.switchUI(false);
                viewStackManager.addView(yunUserNameRegisterView);
            }
        } else if (view.getId() == yun_sdk_img_show_pwd.getId()) {
            if (showPwd) {
                yun_sdk_et_mRegisterPwd
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPwd = false;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getContext(),"R.drawable.yun_sdk_sdk_biyan"));
            } else {
                yun_sdk_et_mRegisterPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPwd = true;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getContext(),"R.drawable.yun_sdk_sdk_yanjing"));
            }
        } else if(view.getId() == yun_sdk_iv_mobile_cancel.getId()){
            yun_sdk_et_mRegisterAccount.setText("");
        } else if(view.getId() == yun_sdk_iv_code_cancel.getId()){
            yun_sdk_et_mRegisterCode.setText("");
        } else if(view.getId() == yun_sdk_iv_pwd_cancel.getId()){
            yun_sdk_et_mRegisterPwd.setText("");
        }else if(view.getId() == tv_privacy_policy.getId()){
            Intent intent = new Intent(loginActivity, WebViewActivity.class);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_AGREEMENT);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE_VALUE, SdkConstant.URL_PRIVACY_AGREEMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginActivity.startActivity(intent);
        }else if(view.getId() == tv_register_policy.getId()){
            Intent intent = new Intent(loginActivity, WebViewActivity.class);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_AGREEMENT);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE_VALUE, SdkConstant.URL_REGISTER_AGREEMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginActivity.startActivity(intent);
        }
    }

    private void submitRegister() {
        final String account = yun_sdk_et_mRegisterAccount.getText().toString().trim();
        final String password = yun_sdk_et_mRegisterPwd.getText().toString();
        String authCode = yun_sdk_et_mRegisterCode.getText().toString().trim();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_input_correct_mobile")));
            return;
        }
        if (!RegExpUtil.isMatchPassword(password)) {
            T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_pwd_instructure")));
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_input_verify_first")));
            return;
        }
        if (!cb_privacy_policy.isChecked()){
            T.s(loginActivity, "请勾选已阅读");
            return;
        }

        UserMobileRegRequestBean userMobileRegRequestBean = new UserMobileRegRequestBean();
        userMobileRegRequestBean.setMethod(SdkConstant.YUN_SDK_USER_REG_MOBILE);
        userMobileRegRequestBean.setMobile(account);
        userMobileRegRequestBean.setPasswd(password);
        userMobileRegRequestBean.setVercode(authCode);
        userMobileRegRequestBean.setImei(DeviceUtil.getDeviceId(loginActivity));
//        userMobileRegRequestBean.setType("reg");
        String sid = ChannelNewUtil.getChannel(loginActivity);

        if (sid == null || sid.equals("")) {
            sid = SdkConstant.YZ_DEFAULT_SID;
        }
        userMobileRegRequestBean.setSid(sid);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(userMobileRegRequestBean));
//        Log.d("d", "~~~注册:"+httpParamsBuild.getHttpParams().getUrlParams().toString());

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(loginActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    Log.d("d", "手机注册返回:"+data.getInfo().toString());
                    // 接口回调通知
                    sp.edit().putString(SdkConstant.YUN_SP_SESSION, data.getInfo().getSession())//
                            .putString(SdkConstant.YUN_SP_UID, data.getInfo().getUid())//
                            .putBoolean(SdkConstant.YUN_SP_AUTH, data.getInfo().isAuth())//
                            .putString(SdkConstant.YUN_SP_BIND_MOBILE, account)//
                            .putString(SdkConstant.YUN_SP_IDCARD, data.getInfo().getIdcard())
                            .putString(SdkConstant.YUN_SP_REALNAME, data.getInfo().getRealname())
//                            .putBoolean(SdkConstant.YUN_SP_IS_VALIDATE, data.getInfo().isIs_validate())
                            .putBoolean(SdkConstant.YUN_SP_BIND, data.getInfo().isBind())
                            .putString(SdkConstant.USERNAME, account)
                            .putString(SdkConstant.PWD, password)
                            .commit();
                    LoginControl.saveUserToken(data.getInfo().getSession());

//                    OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
//                    if (onLoginListener != null) {
//                        onLoginListener.loginSuccess(data);
//                    }
                    loginActivity.loginCompleted(data);
                    //检测是否有写的权限
                    int permission = ContextCompat.checkSelfPermission(loginActivity,
                            "android.permission.WRITE_EXTERNAL_STORAGE");
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // 没有写的权限，申请权限，弹出对话框
                        T.s(loginActivity, "存储权限被禁止,请到设置里打开");
                    }else{
                        // 保存账号到数据库
                        if (!UserLoginInfodao.getInstance(loginActivity).findUserLoginInfoByName(account)) {
                            UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(account, password);
                        } else {
                            UserLoginInfodao.getInstance(loginActivity).deleteUserLoginByName(account);
                            UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(account, password);
                        }
                    }
                    Log.d("保存的数据库文件", UserLoginInfodao.getInstance(loginActivity).getUserLoginInfo().toString());

                    String YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
                    if (!YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_NO)){
                        if (!data.getInfo().isAuth()){
                            SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
                            viewStackManager.showView(viewStackManager.getViewByClass(YunAuthenticationView.class));
                        }else {
                            loginActivity.loginAuthenticCompleted();
                            loginActivity.callBackFinish();
                        }
                    } else {
                        loginActivity.loginAuthenticCompleted();
                        loginActivity.callBackFinish();
                    }
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
                loginActivity.loginFailed(data);
//                T.showToast(loginActivity, "Failure: "+data.getMsg());
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("注册中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void sendSms() {
        final String account = yun_sdk_et_mRegisterAccount.getText().toString().trim();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_input_correct_mobile")));
            return;
        }

        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setType("reg");
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_REG_SEND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(loginActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e(getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_send_successful")));
                    // 开始计时控件
                    startCodeTime(60);
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(loginActivity, "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    Handler handler = new Handler();

    private void startCodeTime(int time) {
        yun_sdk_btn_mRegisterSendCode.setTag(time);
        if (time <= 0) {
            yun_sdk_btn_mRegisterSendCode.setText(getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_get_verifycode")));
            yun_sdk_btn_mRegisterSendCode.setClickable(true);
            return;
        } else {
            yun_sdk_btn_mRegisterSendCode.setClickable(false);
            yun_sdk_btn_mRegisterSendCode.setText("重新获取("+time + "s)");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // int delayTime = (int) yun_sdk_btn_mRegisterSendCode.getTag();
                int delayTime = Integer.parseInt(yun_sdk_btn_mRegisterSendCode.getTag().toString());
                startCodeTime(--delayTime);
            }
        }, 1000);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }
}
