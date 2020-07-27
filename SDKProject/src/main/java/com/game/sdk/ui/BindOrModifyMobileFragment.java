package com.game.sdk.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.request.BindOrModifyMobileRequestBean;
import com.game.sdk.domain.request.SmsSendRequestBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;

public class BindOrModifyMobileFragment extends Fragment implements View.OnClickListener{

    private Button yun_sdk_btn_mRegisterSendCode, btn_CurrSendCode, yun_sdk_btn_confirm, btn_curr_confirm;
    private EditText yun_sdk_et_mRegisterAccount, yun_sdk_et_mRegisterCode, etCurrCode;
    private ImageView yun_sdk_iv_cancel, yun_sdk_iv_account_cancel, yun_sdk_iv_code_cancel, iv_curr_code_cancel;
    private TextView tv_curr_mobile, yun_sdk_tv_back, yun_sdk_tv_charge_title;
    private LinearLayout yun_sdk_ll_bind, yun_sdk_ll_currbind;
    private boolean isMobileBinded = false;
    private String uid, session, userBinded, oldCode;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "activity_bind_or_modify_mobile"), container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        uid = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, "");
        session = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, "");
        tv_curr_mobile = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_curr_mobile"));
        yun_sdk_btn_mRegisterSendCode = (Button)view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_btn_mRegisterSendCode"));
        btn_CurrSendCode = (Button)view.findViewById(MResource.getIdByName(getActivity(), "R.id.btn_CurrSendCode"));
        yun_sdk_btn_confirm = (Button)view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_btn_confirm"));
        btn_curr_confirm = (Button)view.findViewById(MResource.getIdByName(getActivity(), "R.id.btn_curr_confirm"));
        yun_sdk_et_mRegisterCode = (EditText)view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_mRegisterCode"));
        etCurrCode = (EditText)view.findViewById(MResource.getIdByName(getActivity(), "R.id.etCurrCode"));
        yun_sdk_iv_account_cancel = (ImageView)view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_account_cancel"));
        yun_sdk_iv_cancel = (ImageView)view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_cancel"));
        yun_sdk_iv_code_cancel = (ImageView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_code_cancel"));
        iv_curr_code_cancel = (ImageView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.iv_curr_code_cancel"));
        yun_sdk_et_mRegisterAccount = (EditText) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_mRegisterAccount"));
        yun_sdk_tv_back = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_tv_back"));
        yun_sdk_tv_charge_title = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_tv_charge_title"));
        yun_sdk_ll_bind = (LinearLayout) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_ll_bind"));
        yun_sdk_ll_currbind = (LinearLayout) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_ll_currbind"));

        userBinded = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND_MOBILE, "");
        isMobileBinded = TextUtils.isEmpty(userBinded)? false : true;
        if (isMobileBinded){
            yun_sdk_tv_charge_title.setText("换绑手机");
            yun_sdk_ll_currbind.setVisibility(View.VISIBLE);
        }else {
            yun_sdk_tv_charge_title.setText("绑定手机");
            yun_sdk_ll_bind.setVisibility(View.VISIBLE);
        }
        tv_curr_mobile.setText(userBinded);
        yun_sdk_et_mRegisterAccount.setHint(getResources().getString(MResource.getIdByName(getActivity(),"R.string.input_mobile")) );

        yun_sdk_btn_confirm.setOnClickListener(this);
        btn_curr_confirm.setOnClickListener(this);
        yun_sdk_btn_mRegisterSendCode.setOnClickListener(this);
        btn_CurrSendCode.setOnClickListener(this);
        yun_sdk_iv_account_cancel.setOnClickListener(this);
        yun_sdk_iv_cancel.setOnClickListener(this);
        yun_sdk_iv_code_cancel.setOnClickListener(this);
        iv_curr_code_cancel.setOnClickListener(this);
        yun_sdk_tv_back.setOnClickListener(this);
        //设置输入监听
        setTextChangedListener(yun_sdk_et_mRegisterAccount, yun_sdk_iv_account_cancel);
        setTextChangedListener(yun_sdk_et_mRegisterCode, yun_sdk_iv_code_cancel);
        setTextChangedListener(etCurrCode, iv_curr_code_cancel);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_btn_mRegisterSendCode.getId()) {// 绑定发送验证码
            sendSms();
        }else if(view.getId() == btn_CurrSendCode.getId()){// 换绑手机发送验证码
            currSendSms();
        }
        else if (view.getId() == yun_sdk_btn_confirm.getId()) {// 确定按钮
            confirmCommit();
        }else if (view.getId() == btn_curr_confirm.getId()) {// 换绑手机下一步按钮
            CurrConfirmCheck();
        }else if(view.getId() == yun_sdk_tv_back.getId() ){
            getActivity().onBackPressed();
        }else if(view.getId() == yun_sdk_iv_cancel.getId()){
            getActivity().finish();
        }else if(view.getId() == yun_sdk_iv_account_cancel.getId()){
            yun_sdk_et_mRegisterAccount.setText("");
        }else if(view.getId() == yun_sdk_iv_code_cancel.getId()){
            yun_sdk_et_mRegisterCode.setText("");
        }

    }

    private void sendSms() {
        final String account = yun_sdk_et_mRegisterAccount.getText().toString().trim();

        if (!RegExpUtil.isMobileNumber(account)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_correct_mobile")));
            return;
        }
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setUid(uid);
        smsSendRequestBean.setSession(session);
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_BIND_SEND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e(getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_send_successful")));
                    // 开始计时控件
                    startCodeTime(60, yun_sdk_btn_mRegisterSendCode);
                }
            }
            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity(), "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    private void currSendSms() {
        //换绑手机发送短信所需参数  uid session
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setUid(uid);
        smsSendRequestBean.setSession(session);
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_CHANGE_SEND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e(getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_send_successful")));
                    // 开始计时控件
                    startCodeTime(60, btn_CurrSendCode);
                }
            }
            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity(), "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void confirmCommit(){
        final String mobile = yun_sdk_et_mRegisterAccount.getText().toString();
        String authCode = yun_sdk_et_mRegisterCode.getText().toString();

        if (!RegExpUtil.isMobileNumber(mobile)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_correct_mobile")));
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_verify_first")));
            return;
        }
        BindOrModifyMobileRequestBean bindRequestBean = new BindOrModifyMobileRequestBean();
        bindRequestBean.setUid(uid);
        bindRequestBean.setSession(session);
        bindRequestBean.setMobile(mobile);
        if (isMobileBinded)bindRequestBean.setOldcode(oldCode);
        bindRequestBean.setVercode(authCode);
        if (isMobileBinded){
            bindRequestBean.setMethod(SdkConstant.YUN_SDK_USER_MOBILE_CHANGE);
        }else {
            bindRequestBean.setMethod(SdkConstant.YUN_SDK_USER_MOBILE_BIND);
        }

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(bindRequestBean));
        Log.d("d", "~~~绑定:"+httpParamsBuild.getHttpParams().getUrlParams().toString());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    if (data.getCode() == 1){
                        T.s(getActivity(), data.getMsg());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile());
                        SharedPreferencesUtil.putData(SdkConstant.YUN_SP_BIND, true);
                        getActivity().onBackPressed();
                    }else{
                        T.showToast(getActivity(), data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(UserResultBean data) {
//                T.showToast(getActivity(), "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("处理中...");

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }
    private void CurrConfirmCheck(){
        final String authCode = etCurrCode.getText().toString();
        if (TextUtils.isEmpty(authCode)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_verify_first")));
            return;
        }
        oldCode = authCode;
        yun_sdk_ll_currbind.setVisibility(View.GONE);
        yun_sdk_ll_bind.setVisibility(View.VISIBLE);
    }

    Handler handler = new Handler();

    private void startCodeTime(int time, final Button button) {
        button.setTag(time);
        if (time <= 0) {
            button.setText(getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_get_verifycode")));
            button.setClickable(true);
            return;
        } else {
            button.setClickable(false);
            button.setText("重新获取("+time + "s)");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = Integer.parseInt(button.getTag().toString());
                startCodeTime(--delayTime, button);
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }
}
