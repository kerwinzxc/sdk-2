package com.game.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.db.LoginControl;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.request.UserNameRegRequestBean;
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
import com.game.sdk.util.SavePhotoFromViewUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import java.text.ParseException;

import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;


public class YunUserNameRegisterView extends FrameLayout implements View.OnClickListener {
    private static YunLoginActivity loginActivity;
    private ViewStackManager viewStackManager;
    private static LinearLayout linearlayoutPolicy, yun_sdk_rl_login;
    private static RelativeLayout rlOtherRegisterEnter;
    private static EditText yun_sdk_et_uRegisterAccount, yun_sdk_et_uRegisterPwd;
    private static TextView yun_sdk_tv_uRegisterBackLogin, tv_yun_sdk_mobileRegister, tvScreenShot;
    private static Button yun_sdk_btn_uRegisterSubmit;
    private static TextView yun_sdk_tv_uRegisterTitle;
    private static CheckBox cb_privacy_policy;
    private static TextView tv_register_policy, tv_privacy_policy;
    public static final int REGISTER_REQUEST_EXTERNAL_STORAGE = 1001;
    private UserResultBean userResultBean ;
    private SharedPreferences sp;

    public YunUserNameRegisterView(Context context) {
        super(context);
        setupUI();
        switchUI(false);
    }

    public YunUserNameRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
        switchUI(false);
    }

    public YunUserNameRegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
        switchUI(false);
    }

    private void setupUI() {
        loginActivity = (YunLoginActivity) getContext();
        viewStackManager = ViewStackManager.getInstance(loginActivity);
        sp = loginActivity.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(), MResource.LAYOUT, "yun_sdk_include_user_register"), this);
        yun_sdk_tv_uRegisterTitle = (TextView) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_tv_uRegisterTitle"));
        linearlayoutPolicy = (LinearLayout) findViewById(MResource.getIdByName(loginActivity, "R.id.linearlayoutPolicy"));
        yun_sdk_rl_login = (LinearLayout) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_rl_login"));
        yun_sdk_et_uRegisterAccount = (EditText) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_et_uRegisterAccount"));
        yun_sdk_et_uRegisterPwd = (EditText) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_et_uRegisterPwd"));
        yun_sdk_tv_uRegisterBackLogin = (TextView) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_tv_uRegisterBackLogin"));
        tv_yun_sdk_mobileRegister = (TextView) findViewById(MResource.getIdByName(loginActivity, "R.id.tv_yun_sdk_mobileRegister"));
        yun_sdk_btn_uRegisterSubmit = (Button) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_btn_uRegisterSubmit"));
        tvScreenShot = (TextView) findViewById(MResource.getIdByName(loginActivity, "R.id.tvScreenShot"));
        rlOtherRegisterEnter = (RelativeLayout) findViewById(MResource.getIdByName(loginActivity, "R.id.rlOtherRegisterEnter"));
        cb_privacy_policy = (CheckBox)
                findViewById(MResource.getIdByName(getContext(),
                        "R.id.cb_privacy_policy"));
        tv_privacy_policy = (TextView)findViewById(MResource.getIdByName(getContext(),"R.id.tv_privacy_policy"));
        tv_register_policy = (TextView)findViewById(MResource.getIdByName(getContext(),"R.id.tv_register_policy"));

        yun_sdk_tv_uRegisterBackLogin.setOnClickListener(this);
        tv_yun_sdk_mobileRegister.setOnClickListener(this);
        yun_sdk_btn_uRegisterSubmit.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_register_policy.setOnClickListener(this);
        cb_privacy_policy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    yun_sdk_btn_uRegisterSubmit.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.bg_selected")));
                }else {
                    yun_sdk_btn_uRegisterSubmit.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.bg_normal")));
                }
            }
        });
        //NEW 2017年2月28日10:12:23 - 加载switch资源
//        if (YunsdkManager.isSwitchLogin) {
//            MResource.loadImgFromSDCard(yun_sdk_iv_logo, MResource.PATH_FILE_ICON_LOGO);
//        }
    }

    public static void switchUI(boolean isScreenShot) {
        if (isScreenShot) {
            yun_sdk_tv_uRegisterTitle.setText("注册成功");
            linearlayoutPolicy.setVisibility(GONE);
            yun_sdk_et_uRegisterAccount.setEnabled(false);
            yun_sdk_et_uRegisterPwd.setEnabled(false);
            rlOtherRegisterEnter.setVisibility(GONE);
            yun_sdk_btn_uRegisterSubmit.setText("进入游戏");

            //检测是否有写的权限
            int permission = ContextCompat.checkSelfPermission(loginActivity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，申请权限，弹出对话框
                T.s(loginActivity, "存储权限被禁止,请到设置里打开");
            } else{
                try {
                    //保存图片
                    SavePhotoFromViewUtil savePhoto = new SavePhotoFromViewUtil(loginActivity);
                    savePhoto.SaveBitmapFromView(yun_sdk_rl_login);
                    tvScreenShot.setVisibility(VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            yun_sdk_et_uRegisterAccount.setEnabled(true);
            yun_sdk_tv_uRegisterTitle.setText("账号注册");
            yun_sdk_et_uRegisterAccount.setText("");
            yun_sdk_et_uRegisterPwd.setText("");
            tvScreenShot.setVisibility(GONE);
            linearlayoutPolicy.setVisibility(VISIBLE);
            rlOtherRegisterEnter.setVisibility(VISIBLE);
            yun_sdk_btn_uRegisterSubmit.setText("立即注册");
            yun_sdk_et_uRegisterAccount.setHint(RegExpUtil.RandomCharAndNum());
            yun_sdk_et_uRegisterPwd.setHint(RegExpUtil.createRandomCharData(8));
            yun_sdk_et_uRegisterPwd.setInputType(TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//InputType.TYPE_CLASS_TEXT
                    //| InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE){
            yun_sdk_et_uRegisterAccount.setHint(RegExpUtil.RandomCharAndNum());
            yun_sdk_et_uRegisterPwd.setHint(RegExpUtil.createRandomCharData(8));
            yun_sdk_et_uRegisterPwd.setInputType(TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == yun_sdk_tv_uRegisterBackLogin.getId()) {
            viewStackManager.showView(viewStackManager.getViewByClass(YunLoginView.class));
        } else if(v.getId() == tv_yun_sdk_mobileRegister.getId()){//手机注册
            viewStackManager.showView(viewStackManager.getViewByClass(YunRegisterView.class));
        } else if (v.getId() == yun_sdk_btn_uRegisterSubmit.getId()) {//提交注册
            submitRegister();
        }else if(v.getId() == tv_privacy_policy.getId()){
            Intent intent = new Intent(loginActivity, WebViewActivity.class);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_AGREEMENT);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE_VALUE, SdkConstant.URL_PRIVACY_AGREEMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginActivity.startActivity(intent);
        }else if(v.getId() == tv_register_policy.getId()){
            Intent intent = new Intent(loginActivity, WebViewActivity.class);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_AGREEMENT);
            intent.putExtra(SdkConstant.YUN_WEB_STYLE_VALUE, SdkConstant.URL_REGISTER_AGREEMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginActivity.startActivity(intent);
        }
    }

    private void submitRegister() {
        boolean accountIsEmpty = TextUtils.isEmpty(yun_sdk_et_uRegisterAccount.getText().toString().trim());
        boolean passwordIsEmpty = TextUtils.isEmpty(yun_sdk_et_uRegisterPwd.getText().toString());
        final String account = accountIsEmpty ? yun_sdk_et_uRegisterAccount.getHint().toString().trim() : yun_sdk_et_uRegisterAccount.getText().toString().trim() ;
        final String password = passwordIsEmpty ? yun_sdk_et_uRegisterPwd.getHint().toString() : yun_sdk_et_uRegisterPwd.getText().toString();

        //当前为 “立即注册” 按钮
        if (getResources().getString(MResource.getIdByName(loginActivity,"R.string.register_immediately")).equals(yun_sdk_btn_uRegisterSubmit.getText())){
            if(TextUtils.isEmpty(account)){
                T.s(loginActivity, "账号不能为空");
                return;
            }
            if(TextUtils.isEmpty(password)){
                T.s(loginActivity, "密码不能为空");
                return;
            }
            if (!cb_privacy_policy.isChecked()){
                T.s(loginActivity, "请勾选已阅读");
                return;
            }
            if (!RegExpUtil.isMatchAccount(account)) {
                T.s(loginActivity, "账号只能由6至16位英文或数字组成");
                return;
            }
            if (!RegExpUtil.isMatchPassword(password)) {
                T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_pwd_instructure")));
                return;
            }
            requestUserReg(account, password);
        } else {//注册完账号后 “进入游戏” 按钮
            if (SharedPreferencesUtil.getData(SdkConstant.YUN_SP_IS_LOGIN,"").equals(SdkConstant.YUN_NO)) {
                T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_cannot_login")));
                return;
            }
            LoginControl.saveUserToken(userResultBean.getInfo().getSession());
//            OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
//            if (onLoginListener != null) {
//                onLoginListener.loginSuccess(userResultBean);
//            }
            loginActivity.loginCompleted(userResultBean);
            //保存账号到数据库
            if (!UserLoginInfodao.getInstance(loginActivity).findUserLoginInfoByName(account)) {
                UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(account, password);
            } else {
                UserLoginInfodao.getInstance(loginActivity).deleteUserLoginByName(account);
                UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(account, password);
            }
            String YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
            if (!YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_NO)){
                if (!userResultBean.getInfo().isAuth()){
                    SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
                    viewStackManager.showView(viewStackManager.getViewByClass(YunAuthenticationView.class));
                }else {
                    loginActivity.loginAuthenticCompleted();
                    loginActivity.callBackFinish();
                }
            }else{
                loginActivity.loginAuthenticCompleted();
                loginActivity.callBackFinish();
            }
        }


    }

    private void requestUserReg(final String account, final String pwd){
        UserNameRegRequestBean userNameRegRequestBean = new UserNameRegRequestBean();
        userNameRegRequestBean.setMethod(SdkConstant.YUN_SDK_USER_REG);
        userNameRegRequestBean.setUname(account);
        userNameRegRequestBean.setPasswd(pwd);
        userNameRegRequestBean.setImei( DeviceUtil.getDeviceId(loginActivity));
        String sid = ChannelNewUtil.getChannel(loginActivity);

        if (sid == null || sid.equals("")) {
            sid = SdkConstant.YZ_DEFAULT_SID;
        }
        Log.d("d", "deviceID:"+DeviceUtil.getDeviceId(loginActivity));
        userNameRegRequestBean.setSid(sid);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(userNameRegRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(loginActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e("注册成功！！");
                    Log.d("d", "~~~名字注册返回:"+data.getInfo().toString());
                    //接口回调通知
                    sp.edit().putString(SdkConstant.YUN_SP_SESSION, data.getInfo().getSession())//
                            .putString(SdkConstant.YUN_SP_UID, data.getInfo().getUid())//
                            .putBoolean(SdkConstant.YUN_SP_AUTH, data.getInfo().isAuth())//
                            .putString(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile())//
                            .putString(SdkConstant.YUN_SP_IDCARD, data.getInfo().getIdcard())
                            .putString(SdkConstant.YUN_SP_REALNAME, data.getInfo().getRealname())
//                            .putBoolean(SdkConstant.YUN_SP_IS_VALIDATE, data.getInfo().isIs_validate())
                            .putBoolean(SdkConstant.YUN_SP_BIND, data.getInfo().isBind())
                            .putString(SdkConstant.USERNAME, account)
                            .putString(SdkConstant.PWD, pwd)
                            .commit();

                    Log.d("保存的账号信息",UserLoginInfodao.getInstance(loginActivity).getUserLoginInfo().toString());
                    userResultBean = data;
                    switchUI(true);
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
                loginActivity.loginFailed(data);
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("注册中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


}
