package com.game.sdk.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.request.ModifiedPwdRequestBean;
import com.game.sdk.domain.request.SmsSendRequestBean;
import com.game.sdk.domain.result.BaseResultBean;
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

public class ModifyPwdFragment extends Fragment implements View.OnClickListener{

    private LinearLayout yun_sdk_ll_disphone, yun_sdk_ll_phone;
    private Button yun_sdk_btn_confirm, yun_sdk_btn_confirm_phone;
    private EditText yun_sdk_et_oldPwd, yun_sdk_et_mRegisterPwd, yun_sdk_et_mRegisterPwd_phone, yun_sdk_et_sms_phone;
    private ImageView yun_sdk_img_show_oldpwd, yun_sdk_img_show_pwd, yun_sdk_img_show_pwd_phone;
    private ImageView yun_sdk_iv_cancel, yun_sdk_iv_oldPwd_cancel, yun_sdk_iv_pwd_cancel, yun_sdk_iv_oldPwd_cancel_phone;
    private TextView yun_sdk_tv_back, tv_user_account, tv_user_account_phone, tv_getSMS;
    private boolean showPwd, showPwdPhone, showOldPwd, isBindedMobile;
    private String uid, session, bindedMobile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "activity_modify_pwd"), container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupUI();
    }

    private void setupUI() {
        bindedMobile = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND_MOBILE, "");
        uid = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, "");
        session = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, "");
        isBindedMobile = TextUtils.isEmpty(bindedMobile)? false : true;
        yun_sdk_ll_disphone = (LinearLayout) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_ll_disphone"));
        yun_sdk_ll_phone = (LinearLayout) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_ll_phone"));
        yun_sdk_btn_confirm = (Button) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_btn_confirm"));
        yun_sdk_btn_confirm_phone = (Button) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_btn_confirm_phone"));
        yun_sdk_et_oldPwd = (EditText) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_oldPwd"));
        yun_sdk_et_mRegisterPwd = (EditText) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_mRegisterPwd"));
        yun_sdk_et_mRegisterPwd_phone = (EditText) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_mRegisterPwd_phone"));
        yun_sdk_et_sms_phone = (EditText) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_et_sms_phone"));
        yun_sdk_img_show_oldpwd = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_img_show_oldpwd"));
        yun_sdk_img_show_pwd = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_img_show_pwd"));
        yun_sdk_img_show_pwd_phone = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_img_show_pwd_phone"));
        yun_sdk_iv_cancel = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_cancel"));
        yun_sdk_iv_oldPwd_cancel = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_oldPwd_cancel"));
        yun_sdk_iv_oldPwd_cancel_phone = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_oldPwd_cancel_phone"));
        yun_sdk_iv_pwd_cancel = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_pwd_cancel"));
        yun_sdk_tv_back = (TextView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_tv_back"));
        tv_getSMS = (TextView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.tv_getSMS"));
        tv_user_account = (TextView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.tv_user_account"));
        tv_user_account_phone = (TextView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.tv_user_account_phone"));

        if (isBindedMobile){
            yun_sdk_ll_phone.setVisibility(View.VISIBLE);
            yun_sdk_ll_disphone.setVisibility(View.GONE);
            tv_user_account_phone.setText(bindedMobile);
        }else {
            yun_sdk_ll_disphone.setVisibility(View.VISIBLE);
            yun_sdk_ll_phone.setVisibility(View.GONE);
            tv_user_account.setText(SharedPreferencesUtil.getUserName(getActivity()));
        }
        yun_sdk_btn_confirm.setOnClickListener(this);
        yun_sdk_btn_confirm_phone.setOnClickListener(this);
        yun_sdk_img_show_pwd.setOnClickListener(this);
        yun_sdk_img_show_pwd_phone.setOnClickListener(this);
        yun_sdk_img_show_oldpwd.setOnClickListener(this);
        yun_sdk_iv_cancel.setOnClickListener(this);
        yun_sdk_iv_oldPwd_cancel.setOnClickListener(this);
        yun_sdk_iv_oldPwd_cancel_phone.setOnClickListener(this);
        yun_sdk_iv_pwd_cancel.setOnClickListener(this);
        yun_sdk_tv_back.setOnClickListener(this);
        tv_getSMS.setOnClickListener(this);

        //设置输入监听
        setTextChangedListener(yun_sdk_et_oldPwd, yun_sdk_iv_oldPwd_cancel);
        setTextChangedListener(yun_sdk_et_sms_phone, yun_sdk_iv_oldPwd_cancel_phone);
        setTextChangedListener(yun_sdk_et_mRegisterPwd, yun_sdk_iv_pwd_cancel);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_btn_confirm.getId()) {
            disPhoneConfirmCommit();
        }else if (view.getId() == yun_sdk_btn_confirm_phone.getId()) {// 手机修改密码确定按钮
            phoneConfirmCommit();
        }else if (view.getId() == yun_sdk_img_show_pwd.getId()) {
            if (showPwd) {
                yun_sdk_et_mRegisterPwd
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPwd = false;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_biyan"));
            } else {
                yun_sdk_et_mRegisterPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPwd = true;
                yun_sdk_img_show_pwd.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_yanjing"));
            }
        }else if (view.getId() == yun_sdk_img_show_pwd_phone.getId()) {
            if (showPwdPhone) {
                yun_sdk_et_mRegisterPwd_phone
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPwdPhone = false;
                yun_sdk_img_show_pwd_phone.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_biyan"));
            } else {
                yun_sdk_et_mRegisterPwd_phone.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPwdPhone = true;
                yun_sdk_img_show_pwd_phone.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_yanjing"));
            }
        }else if (view.getId() == yun_sdk_img_show_oldpwd.getId()) {
            if (showOldPwd) {
                yun_sdk_et_oldPwd
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showOldPwd = false;
                yun_sdk_img_show_oldpwd.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_biyan"));
            } else {
                yun_sdk_et_oldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showOldPwd = true;
                yun_sdk_img_show_oldpwd.setImageResource(MResource.getIdByName(getActivity(), "R.drawable.yun_sdk_sdk_yanjing"));
            }
        }else if(view.getId() == yun_sdk_tv_back.getId() ){
            getActivity().onBackPressed();
        }else if(view.getId() == yun_sdk_iv_cancel.getId()){
            getActivity().finish();
        }else if(view.getId() == yun_sdk_iv_oldPwd_cancel.getId()){
            yun_sdk_et_oldPwd.setText("");
        }else if(view.getId() == yun_sdk_iv_oldPwd_cancel_phone.getId()){
            yun_sdk_et_sms_phone.setText("");
        }else if(view.getId() == yun_sdk_iv_pwd_cancel.getId()){
            yun_sdk_et_mRegisterPwd.setText("");
        }else if(view.getId() == tv_getSMS.getId()){
            phoneSendSms();
        }
    }

    private void disPhoneConfirmCommit(){
        final String account = SharedPreferencesUtil.getUserName(getActivity());
        final String oldPwd = yun_sdk_et_oldPwd.getText().toString();
        final String password = yun_sdk_et_mRegisterPwd.getText().toString();

        if (TextUtils.isEmpty(oldPwd)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_oldpwd")));
            return;
        }
        if(TextUtils.isEmpty(password)){
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_newpwd")));
            return;
        }
        if ( !RegExpUtil.isMatchPassword(oldPwd) || !RegExpUtil.isMatchPassword(password)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_pwd_instructure")));
            return;
        }
        ModifiedPwdRequestBean modifiedPwdRequestBean = new ModifiedPwdRequestBean();
        modifiedPwdRequestBean.setMethod(SdkConstant.YUN_SDK_USER_PWD_SET);
        modifiedPwdRequestBean.setUid(uid);
        modifiedPwdRequestBean.setSession(session);
        modifiedPwdRequestBean.setPasswd(oldPwd);
        modifiedPwdRequestBean.setNew_passwd(password);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(modifiedPwdRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BaseResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                if (data != null) {
                    if(data.getCode() == 1){
                        T.s(getActivity(), "修改成功");
                        //保存账号到数据库
                        if (!UserLoginInfodao.getInstance(getActivity()).findUserLoginInfoByName(account)) {
                            UserLoginInfodao.getInstance(getActivity()).saveUserLoginInfo(account, password);
                        } else {
                            UserLoginInfodao.getInstance(getActivity()).deleteUserLoginByName(account);
                            UserLoginInfodao.getInstance(getActivity()).saveUserLoginInfo(account, password);
                        }
                        getActivity().onBackPressed();
                    }else{
                        T.s(getActivity(), "Failure:"+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(BaseResultBean data) {
//                T.showToast(getActivity(), "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("修改中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void phoneConfirmCommit(){
        final String account = SharedPreferencesUtil.getUserName(getActivity());
        final String password = yun_sdk_et_mRegisterPwd_phone.getText().toString();
        final String vercode = yun_sdk_et_sms_phone.getText().toString();

        if (TextUtils.isEmpty(vercode)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_verify_first")));
            return;
        }
        if(TextUtils.isEmpty(password)){
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_input_newpwd")));
            return;
        }
        if ( !RegExpUtil.isMatchPassword(password)) {
            T.s(getActivity(), getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_pwd_instructure")));
            return;
        }
        ModifiedPwdRequestBean modifiedPwdRequestBean = new ModifiedPwdRequestBean();
        modifiedPwdRequestBean.setMethod(SdkConstant.YUN_SDK_USER_PWD_MOBILE);
        modifiedPwdRequestBean.setUid(uid);
        modifiedPwdRequestBean.setSession(session);
        modifiedPwdRequestBean.setPasswd(password);
        modifiedPwdRequestBean.setVercode(vercode);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(modifiedPwdRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BaseResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                if (data != null) {
                    if(data.getCode() == 1){
                        T.s(getActivity(), "修改成功");
                        //保存账号到数据库
                        if (!UserLoginInfodao.getInstance(getActivity()).findUserLoginInfoByName(account)) {
                            UserLoginInfodao.getInstance(getActivity()).saveUserLoginInfo(account, password);
                        } else {
                            UserLoginInfodao.getInstance(getActivity()).deleteUserLoginByName(account);
                            UserLoginInfodao.getInstance(getActivity()).saveUserLoginInfo(account, password);
                        }
                        getActivity().onBackPressed();
                    }else{
                        T.s(getActivity(), "Failure:"+data.getMsg());
                    }
                }
            }
            @Override
            public void onDataFailure(BaseResultBean data) {
//                T.showToast(getActivity(), "Failure: "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("修改中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void phoneSendSms() {
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setUid(uid);
        smsSendRequestBean.setSession(session);
        smsSendRequestBean.setMethod(SdkConstant.YUN_SDK_SMS_VERIFY_SEND);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(getActivity()) {
            @Override
            public void onDataSuccess(UserResultBean data) {
                if (data != null) {
                    L.e(getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_send_successful")));
                    // 开始计时控件
                    startCodeTime(60);
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

    Handler handler = new Handler();
    private void startCodeTime(int time) {
        tv_getSMS.setTag(time);
        if (time <= 0) {
            tv_getSMS.setText(getResources().getString(MResource.getIdByName(getActivity(),"R.string.toast_get_verifycode")));
            tv_getSMS.setClickable(true);
            return;
        } else {
            tv_getSMS.setClickable(false);
            tv_getSMS.setText(time + "秒");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // int delayTime = (int) yun_sdk_btn_mRegisterSendCode.getTag();
                int delayTime = Integer.parseInt(tv_getSMS.getTag().toString());
                startCodeTime(--delayTime);
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }
}
