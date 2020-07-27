package com.game.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.domain.request.AuthenticationRequestBean;
import com.game.sdk.domain.request.BindOrModifyMobileRequestBean;
import com.game.sdk.domain.request.SmsSendRequestBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.ui.TabActivity;
import com.game.sdk.ui.YunBindMobileActivity;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;

public class YunBindMobileView extends FrameLayout implements View.OnClickListener {
    private YunBindMobileActivity getActivity;
    private TextView tvNextTime, yun_sdk_tv_back, yun_sdk_btn_SendCode;
    private EditText  yun_sdk_et_mobile, yun_sdk_et_code;
    private Button yun_sdk_btn_uRegisterSubmit;
    private ImageView yun_sdk_iv_mobile_cancel;
    String BINDMOBILETYPE, YUN_SP_INIT_IS_BIND;

    public YunBindMobileView(Context context) {
        super(context);
        setupUI();
    }

    public YunBindMobileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunBindMobileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }


    private void setupUI() {
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(),
                MResource.LAYOUT, "yun_sdk_bind_mobile"), this);
        getActivity = (YunBindMobileActivity) getContext();
        tvNextTime = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.tvNextTime"));
        yun_sdk_tv_back = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_tv_back"));
        yun_sdk_et_mobile = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_mobile"));
        yun_sdk_et_code = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_code"));
        yun_sdk_iv_mobile_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_mobile_cancel"));
        yun_sdk_btn_uRegisterSubmit = (Button) findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_btn_uRegisterSubmit"));
        yun_sdk_btn_SendCode = (TextView) findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_btn_SendCode"));
        yun_sdk_btn_uRegisterSubmit.setOnClickListener(this);
        yun_sdk_btn_SendCode.setOnClickListener(this);
        yun_sdk_iv_mobile_cancel.setOnClickListener(this);
        yun_sdk_tv_back.setOnClickListener(this);
        tvNextTime.setOnClickListener(this);

        setTextChangedListener(yun_sdk_et_mobile, yun_sdk_iv_mobile_cancel);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_btn_uRegisterSubmit.getId()) {// 确认并提交
            refreshData();
            bindMobileCommit();
        }else if (view.getId() == yun_sdk_btn_SendCode.getId()) {// 发送验证码
            sendSms();
        }else if(view.getId() == tvNextTime.getId()){ //下次再说
            //处理回调
            if (BINDMOBILETYPE.equals(SdkConstant.BINDMOBILETYPE_LOGIN)){
                OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
                if (onLoginListener != null) {
                    onLoginListener.loginAuthenticOver();
                }
            }
            getActivity.finish();
        }else if(view.getId() == yun_sdk_iv_mobile_cancel.getId()){
            yun_sdk_et_mobile.setText("");
        }else if(view.getId() == yun_sdk_tv_back.getId()){
            getActivity.onBackPressed();
        }
    }

    private void sendSms() {
        final String account = yun_sdk_et_mobile.getText().toString().trim();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_correct_mobile")));
            return;
        }
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setUid((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, ""));
        smsSendRequestBean.setSession((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, ""));
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_BIND_SEND);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e(getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_send_successful")));
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

    Handler handler = new Handler();

    private void startCodeTime(int time) {
        yun_sdk_btn_SendCode.setTag(time);
        if (time <= 0) {
            yun_sdk_btn_SendCode.setText(getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_get_verifycode")));
            yun_sdk_btn_SendCode.setClickable(true);
            return;
        } else {
            yun_sdk_btn_SendCode.setClickable(false);
            yun_sdk_btn_SendCode.setText(time + "秒");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = Integer.parseInt(yun_sdk_btn_SendCode.getTag().toString());
                startCodeTime(--delayTime);
            }
        }, 1000);
    }

    //先绑定手机号
    private void bindMobileCommit(){
        final String account = yun_sdk_et_mobile.getText().toString();
        String authCode = yun_sdk_et_code.getText().toString();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_correct_mobile")));
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(getActivity, getResources().getString(MResource.getIdByName(getActivity,"R.string.toast_input_verify_first")));
            return;
        }
        BindOrModifyMobileRequestBean bindRequestBean = new BindOrModifyMobileRequestBean();
        bindRequestBean.setUid((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, ""));
        bindRequestBean.setSession((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, ""));
        bindRequestBean.setMobile(account);
        bindRequestBean.setVercode(authCode);
        bindRequestBean.setMethod(SdkConstant.YUN_SDK_USER_MOBILE_BIND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(bindRequestBean));
//        Log.d("d", "~~~绑定:"+httpParamsBuild.getHttpParams().getUrlParams().toString());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
//                    if (data.getCode() == 1){
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_BIND, true);
                        T.showToast(getActivity, data.getMsg());
                        getActivity.finish();
//                    }
                }else{
                    T.showToast(getActivity, data.getMsg());
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity, "Failure: "+data.getMsg());
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("绑定中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null)handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE){
            refreshData();
        }
    }

    private void refreshData(){
        BINDMOBILETYPE = (String)SharedPreferencesUtil.getData(SdkConstant.BINDMOBILETYPE, "");
        YUN_SP_INIT_IS_BIND = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_BIND,"");

        if (BINDMOBILETYPE.equals(SdkConstant.BINDMOBILETYPE_LOGIN)){
            if (YUN_SP_INIT_IS_BIND.equals(SdkConstant.YUN_YES)){
                tvNextTime.setVisibility(GONE);
            }else {
                tvNextTime.setVisibility(VISIBLE);
            }
        }else {
            tvNextTime.setVisibility(VISIBLE);
        }

    }
}
