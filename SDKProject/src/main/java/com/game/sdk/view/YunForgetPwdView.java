package com.game.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.sdk.R;
import com.game.sdk.SdkConstant;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.request.SmsSendRequestBean;
import com.game.sdk.domain.request.UserForgetPwdRequestBean;
import com.game.sdk.domain.result.BaseResultBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.kymjs.rxvolley.RxVolley;

import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;

public class YunForgetPwdView extends FrameLayout implements View.OnClickListener{

    private Button yun_sdk_btn_mRegisterSendCode, yun_sdk_btn_confirm;
    private EditText yun_sdk_et_mRegisterAccount, yun_sdk_et_mRegisterCode, yun_sdk_et_mRegisterPwd, yun_sdk_et_mRegisterPwd_again;
    private ImageView yun_sdk_img_show_pwd, yun_sdk_img_show_pwd_again;
    private ImageView yun_sdk_iv_cancel, yun_sdk_iv_account_cancel, yun_sdk_iv_code_cancel, yun_sdk_iv_pwd_cancel, yun_sdk_iv_repwd_cancel;
    private TextView yun_sdk_tv_back;
    private boolean showPwd = false;
    private boolean showPwdAgain = false;
    private Activity getActivity;

    public YunForgetPwdView(Context context) {
        super(context);
        setupUI();
    }

    public YunForgetPwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunForgetPwdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void setupUI() {
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(),
                MResource.LAYOUT, "yun_sdk_forget_pwd"), this);
        getActivity = (YunLoginActivity) getContext();
        yun_sdk_btn_mRegisterSendCode = (Button)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_btn_mRegisterSendCode"));
        yun_sdk_btn_confirm = (Button)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_btn_confirm"));
        yun_sdk_et_mRegisterCode = (EditText)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_et_mRegisterCode"));
        yun_sdk_et_mRegisterPwd = (EditText)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_et_mRegisterPwd"));
        yun_sdk_et_mRegisterPwd_again = (EditText)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_et_mRegisterPwd_again"));
        yun_sdk_img_show_pwd = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_img_show_pwd"));
        yun_sdk_img_show_pwd_again = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_img_show_pwd_again"));
        yun_sdk_iv_account_cancel = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_iv_account_cancel"));
        yun_sdk_iv_cancel = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_iv_cancel"));
        yun_sdk_iv_code_cancel = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_iv_code_cancel"));
        yun_sdk_iv_pwd_cancel = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_iv_pwd_cancel"));
        yun_sdk_iv_repwd_cancel = (ImageView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_iv_repwd_cancel"));
        yun_sdk_et_mRegisterAccount = (EditText)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_et_mRegisterAccount"));
        yun_sdk_et_mRegisterAccount.requestFocus();
        yun_sdk_tv_back = (TextView)
                findViewById(MResource.getIdByName(getActivity,
                        "R.id.yun_sdk_tv_back"));
        yun_sdk_btn_confirm.setOnClickListener(this);
        yun_sdk_btn_mRegisterSendCode.setOnClickListener(this);
        yun_sdk_img_show_pwd.setOnClickListener(this);
        yun_sdk_img_show_pwd_again.setOnClickListener(this);
        yun_sdk_iv_account_cancel.setOnClickListener(this);
        yun_sdk_iv_cancel.setOnClickListener(this);
        yun_sdk_iv_code_cancel.setOnClickListener(this);
        yun_sdk_iv_pwd_cancel.setOnClickListener(this);
//        yun_sdk_iv_repwd_cancel.setOnClickListener(this);
        yun_sdk_tv_back.setOnClickListener(this);
        //设置输入监听
        setTextChangedListener(yun_sdk_et_mRegisterAccount, yun_sdk_iv_account_cancel);
        setTextChangedListener(yun_sdk_et_mRegisterCode, yun_sdk_iv_code_cancel);
        setTextChangedListener(yun_sdk_et_mRegisterPwd, yun_sdk_iv_pwd_cancel);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_btn_mRegisterSendCode.getId()) {// 发送验证码
            sendSms();
        }else if (view.getId() == yun_sdk_btn_confirm.getId()) {// 确定按钮
            confirmCommit();
        }else if (view.getId() == yun_sdk_img_show_pwd.getId()) {
            if (showPwd) {
                yun_sdk_et_mRegisterPwd
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPwd = false;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getActivity,
                        "R.drawable.yun_sdk_sdk_biyan"));
            } else {
                yun_sdk_et_mRegisterPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPwd = true;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getActivity,
                        "R.drawable.yun_sdk_sdk_yanjing"));
            }
        }
//        else if (view.getId() == yun_sdk_img_show_pwd_again.getId()) {
//            if (showPwdAgain) {
//                yun_sdk_et_mRegisterPwd_again
//                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                showPwdAgain = false;
//                yun_sdk_img_show_pwd_again.setImageResource(MResource.getIdByName(this,
//                        "R.drawable.yun_sdk_sdk_biyan"));
//            } else {
//                yun_sdk_et_mRegisterPwd_again.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                showPwdAgain = true;
//                yun_sdk_img_show_pwd_again.setImageResource(MResource.getIdByName(this,
//                        "R.drawable.yun_sdk_sdk_yanjing"));
//            }
//        }
        else if(view.getId() == yun_sdk_tv_back.getId() || view.getId() == yun_sdk_iv_cancel.getId()){
            getActivity.onBackPressed();
        }else if(view.getId() == yun_sdk_iv_account_cancel.getId()){
            yun_sdk_et_mRegisterAccount.setText("");
        }else if(view.getId() == yun_sdk_iv_code_cancel.getId()){
            yun_sdk_et_mRegisterCode.setText("");
        }else if(view.getId() == yun_sdk_iv_pwd_cancel.getId()){
            yun_sdk_et_mRegisterPwd.setText("");
        }
//        else if(view.getId() == yun_sdk_iv_repwd_cancel.getId()){
//            yun_sdk_et_mRegisterPwd_again.setText("");
//        }

    }

    private void sendSms() {
        final String account = yun_sdk_et_mRegisterAccount.getText().toString().trim();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_correct_mobile")));
            return;
        }

        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setType("reg");
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_IFORGOT_SEND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    // 开始计时控件
                    startCodeTime(60);
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity, "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void confirmCommit(){
        final String account = yun_sdk_et_mRegisterAccount.getText().toString().trim();
        final String password = yun_sdk_et_mRegisterPwd.getText().toString().trim();
//        final String passwordAgain = yun_sdk_et_mRegisterPwd_again.getText().toString().trim();
        String authCode = yun_sdk_et_mRegisterCode.getText().toString().trim();
//
        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_correct_mobile")));
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_verify_first")));
            return;
        }
        if(TextUtils.isEmpty(password)){
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_pwd_first")));
            return;
        }
        if (!RegExpUtil.isMatchPassword(password)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_pwd_instructure")));
            return;
        }
//        if(!password.equals(passwordAgain)){
//            T.s(this, getResources().getString(MResource.getIdByName(ForgetPwdActivity.this,"R.string.toast_pwd_dont_match")));
//            return;
//        }

        UserForgetPwdRequestBean userForgetPwdRequestBean = new UserForgetPwdRequestBean();
        userForgetPwdRequestBean.setMethod(SdkConstant.YUN_SDK_USER_PWD_IFORGOT);
        userForgetPwdRequestBean.setMobile(account);
        userForgetPwdRequestBean.setVercode(authCode);
        userForgetPwdRequestBean.setPasswd(password);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(userForgetPwdRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BaseResultBean>(getActivity) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                if (data != null) {
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(getActivity).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(getActivity).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(getActivity).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(getActivity).saveUserLoginInfo(account, password);
                    }
                    T.s(getActivity, data.getMsg());
                    getActivity.onBackPressed();
                }
            }

            @Override
            public void onDataFailure(BaseResultBean data) {
//                T.showToast(getActivity, "Failure: "+data.getMsg());
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    Handler handler = new Handler();

    private void startCodeTime(int time) {
        yun_sdk_btn_mRegisterSendCode.setTag(time);
        if (time <= 0) {
            yun_sdk_btn_mRegisterSendCode.setText(getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_get_verifycode")));
            yun_sdk_btn_mRegisterSendCode.setClickable(true);
            return;
        } else {
            yun_sdk_btn_mRegisterSendCode.setClickable(false);
            yun_sdk_btn_mRegisterSendCode.setText(time + "秒");
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("d"," onDetachedFromWindow");
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }
}
