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
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.game.sdk.SdkConstant.AUTHENTICTYPE;
import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;

public class YunAuthenticationView extends FrameLayout implements View.OnClickListener {
    private Activity getActivity;
    private TextView tvNextTime, tv_tips, yun_sdk_tv_back, yun_sdk_btn_SendCode;
    private EditText yun_sdk_et_name, yun_sdk_et_IDCard, yun_sdk_et_mobile, yun_sdk_et_code;
    private Button yun_sdk_btn_uRegisterSubmit;
    private ImageView yun_sdk_iv_name_cancel, yun_sdk_iv_id_cancel, yun_sdk_iv_mobile_cancel;
    private LinearLayout ll_mobile, linearLayout, ll_idCard, ll_name, ll_phone, ll_code;
    String YUN_SP_BIND_MOBILE, YUN_SP_INIT_IS_AUTH, AUTHENTICTYPE;
//    boolean bindedMobile;

    public YunAuthenticationView(Context context) {
        super(context);
        setupUI();
    }

    public YunAuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunAuthenticationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void switchUI(boolean isBindedMobile){
        if (!isBindedMobile){
            ll_mobile.setVisibility(VISIBLE);
        }else {
            ll_mobile.setVisibility(GONE);
        }
        if (AUTHENTICTYPE.equals(SdkConstant.AUTHENTICTYPE_LOGIN)){
            if (YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_YES)){
                tvNextTime.setVisibility(GONE);
            }else {
                tvNextTime.setVisibility(VISIBLE);
            }
        }
    }

    private void setupUI() {
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(),
                MResource.LAYOUT, "yun_sdk_authentication"), this);
        ll_mobile = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.ll_mobile"));
        linearLayout = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.linearLayout"));
        ll_idCard = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.ll_idCard"));
        ll_name = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.ll_name"));
        ll_phone = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.ll_phone"));
        ll_code = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.ll_code"));
        tvNextTime = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.tvNextTime"));
        tv_tips = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.tv_tips"));
        yun_sdk_tv_back = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_tv_back"));
        yun_sdk_et_name = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_name"));
        yun_sdk_et_IDCard = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_IDCard"));
        yun_sdk_et_mobile = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_mobile"));
        yun_sdk_et_code = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_code"));
        yun_sdk_iv_mobile_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_mobile_cancel"));
        yun_sdk_iv_name_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_name_cancel"));
        yun_sdk_iv_id_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_iv_id_cancel"));
        yun_sdk_btn_uRegisterSubmit = (Button) findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_btn_uRegisterSubmit"));
        yun_sdk_btn_SendCode = (TextView) findViewById(MResource.getIdByName(getContext(),
                        "R.id.yun_sdk_btn_SendCode"));
        String textStr = getResources().getString(MResource.getIdByName(getContext(),"R.string.authentication_tip"));
        tv_tips.setText(RegExpUtil.fromHtml(textStr));

        yun_sdk_btn_uRegisterSubmit.setOnClickListener(this);
        yun_sdk_btn_SendCode.setOnClickListener(this);
        yun_sdk_iv_name_cancel.setOnClickListener(this);
        yun_sdk_iv_id_cancel.setOnClickListener(this);
        yun_sdk_iv_mobile_cancel.setOnClickListener(this);
        yun_sdk_tv_back.setOnClickListener(this);
        tvNextTime.setOnClickListener(this);

        setTextChangedListener(yun_sdk_et_name, yun_sdk_iv_name_cancel);
        yun_sdk_et_name.setFilters(new InputFilter[]{getInputFilter()});
        setTextChangedListener(yun_sdk_et_IDCard, yun_sdk_iv_id_cancel);
        setTextChangedListener(yun_sdk_et_mobile, yun_sdk_iv_mobile_cancel);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_btn_uRegisterSubmit.getId()) {// 确认并提交
            refreshData();
//            if (bindedMobile){
                submitAuthentication();
//            }else {
//                bindMobileCommit();
//            }
        }else if (view.getId() == yun_sdk_btn_SendCode.getId()) {// 发送验证码
            sendSms();
        }else if(view.getId() == tvNextTime.getId()){ //下次再说
            String AUTHENTICTYPE = (String)SharedPreferencesUtil.getData(SdkConstant.AUTHENTICTYPE,"");
            if (SdkConstant.AUTHENTICTYPE_LOGIN.equals(AUTHENTICTYPE) ){
                OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
                if (onLoginListener != null) {
                    onLoginListener.loginAuthenticOver();
                }
            }
            SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, "");
            getActivity.finish();
        }else if(view.getId() == yun_sdk_iv_name_cancel.getId()){
            yun_sdk_et_name.setText("");
        }else if(view.getId() == yun_sdk_iv_id_cancel.getId()){
            yun_sdk_et_IDCard.setText("");
        }else if(view.getId() == yun_sdk_iv_mobile_cancel.getId()){
            yun_sdk_et_mobile.setText("");
        }else if(view.getId() == yun_sdk_tv_back.getId()){
            getActivity.onBackPressed();
            SharedPreferencesUtil.putData(AUTHENTICTYPE, "");
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

        final String name = yun_sdk_et_name.getText().toString();
        final String IDCard = yun_sdk_et_IDCard.getText().toString();

        if (TextUtils.isEmpty(name)) {
            T.s(getContext(), "请输入名字");
            return;
        }
        if (name.length()<2 ){
            T.s(getContext(), "名字长度最少两位");
            return;
        }
        if (TextUtils.isEmpty(IDCard)) {
            T.s(getContext(), "请输入身份证号");
            return;
        }
        if (!RegExpUtil.isIdCardNumber(IDCard)) {
            T.s(getContext(), "请输入正确的身份证号");
            return;
        }
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
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    if (data.getCode() == 1){
                        T.showToast(getActivity, data.getMsg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile());
                        //绑定成功 刷新当前页面
                        switchUI(true);
                        submitAuthentication();
                    }else{
                        T.showToast(getActivity, data.getMsg());
                    }
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity, "Failure: "+data.getMsg());
            }
        };

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("加载中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void submitAuthentication() {
        final String name = yun_sdk_et_name.getText().toString();
        final String IDCard = yun_sdk_et_IDCard.getText().toString();

        if (TextUtils.isEmpty(name)) {
            T.s(getContext(), "请输入名字");
            return;
        }
        if (name.length()<2 ){
            T.s(getContext(), "名字长度最少两位");
            return;
        }
        if (TextUtils.isEmpty(IDCard)) {
            T.s(getContext(), "请输入身份证号");
            return;
        }
        if (!RegExpUtil.isIdCardNumber(IDCard)) {
            T.s(getContext(), "请输入正确的身份证号");
            return;
        }
        AuthenticationRequestBean authenticationRequestBean = new AuthenticationRequestBean();
        authenticationRequestBean.setMethod(SdkConstant.YUN_SDK_AUTHENTICATION);
        authenticationRequestBean.setUid((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, ""));
        authenticationRequestBean.setSession((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, ""));
        authenticationRequestBean.setRealname(name);
        authenticationRequestBean.setIdcard(IDCard);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(authenticationRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getContext()) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    SharedPreferencesUtil.putData(AUTHENTICTYPE, "");
                    if (data.getCode() == 1){
                        T.s(getContext(), "验证成功 ");
                        SharedPreferences sp = getContext().getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
                        sp.edit().putBoolean(SdkConstant.YUN_SP_AUTH, true)//
                                .putString(SdkConstant.YUN_SP_IDCARD, data.getInfo().getIdcard())
                                .putString(SdkConstant.YUN_SP_REALNAME, data.getInfo().getRealname())
//                                .putBoolean(SdkConstant.YUN_SP_IS_VALIDATE, data.getInfo().isIs_validate())
                                .commit();
                        String AUTHENTICTYPE = (String)SharedPreferencesUtil.getData(SdkConstant.AUTHENTICTYPE,"");
                        if (SdkConstant.AUTHENTICTYPE_LOGIN.equals(AUTHENTICTYPE) ){
                            OnLoginListener onLoginListener = YunsdkManager.getInstance().getOnLoginListener();
                            if (onLoginListener != null) {
                                onLoginListener.loginAuthenticOver();
                            }
                            getActivity.finish();
                        }else if(SdkConstant.AUTHENTICTYPE_USERCENTER.equals(AUTHENTICTYPE)){
                            getActivity.onBackPressed();
                        }else if(SdkConstant.AUTHENTICTYPE_PAY.equals(AUTHENTICTYPE)){
                            YunsdkManager.getInstance().varifyPayParams();
                        }
                    }else{
                        T.s(getContext(), "Failure: "+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getContext(), data.getMsg());
                SharedPreferencesUtil.putData(AUTHENTICTYPE, "");
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("实名认证中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * EditText限制只能输入汉字
     */
    public InputFilter getInputFilter() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (TextUtils.isEmpty(source)){
                    return "";
                }
                for (int i = start; i < end; i++) {
                    if (stringFilterChinese(source) && !source.toString().contains("。") && !source.toString ().contains("，")) {
                        return "";
                    }
                }
                return null;
            }
        };
        return filter;
    }

    /**
     * 限制只能输入汉字，过滤非汉字
     */
    public boolean stringFilterChinese(CharSequence str) {
        //只允许汉字，正则表达式匹配出所有非汉字
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null)handler.removeCallbacksAndMessages(null);
        SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, "");
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE){
            refreshData();
        }
    }

    private void refreshData(){
//        YUN_SP_BIND_MOBILE = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND_MOBILE, "");
//        bindedMobile = TextUtils.isEmpty(YUN_SP_BIND_MOBILE)? false : true;
        YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
        AUTHENTICTYPE = (String)SharedPreferencesUtil.getData(SdkConstant.AUTHENTICTYPE,"");
        if (SdkConstant.AUTHENTICTYPE_USERCENTER.equals(AUTHENTICTYPE)){
            getActivity = (TabActivity) getContext();
            linearLayout.setBackground(null);
            yun_sdk_tv_back.setVisibility(VISIBLE);
            // ll_idCard ll_name ll_phone ll_code
            ll_idCard.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg_white")));
            ll_name.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg_white")));
            ll_phone.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg_white")));
            ll_code.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg_white")));
        }else {
            getActivity = (YunLoginActivity) getContext();
            linearLayout.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.radius_bg")));
            //布局变化
            ll_idCard.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg")));
            ll_name.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg")));
            ll_phone.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg")));
            ll_code.setBackgroundResource(MResource.getIdByName(getContext(), ("R.drawable.yun_sdk_input_bg")));

        }
//        if (!bindedMobile){
//            ll_mobile.setVisibility(VISIBLE);
//        }else {
//            ll_mobile.setVisibility(GONE);
//        }
        if (AUTHENTICTYPE.equals(SdkConstant.AUTHENTICTYPE_LOGIN)){
            if (YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_YES)){
                tvNextTime.setVisibility(GONE);
            }else {
                tvNextTime.setVisibility(VISIBLE);
            }
        }
    }
}
