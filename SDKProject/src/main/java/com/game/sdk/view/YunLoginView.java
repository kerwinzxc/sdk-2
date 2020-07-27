package com.game.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.game.sdk.log.T;
import com.game.sdk.ui.YunBindMobileActivity;
import com.game.sdk.ui.YunLoginActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.List;

import static com.game.sdk.util.BaseAppUtil.setTextChangedListener;


public class YunLoginView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = YunLoginView.class.getSimpleName();
    //登陆
    LinearLayout yunLlLoginRegister;
    RelativeLayout yunRlLogin, yunYKLogin;
    private YunLoginActivity loginActivity;
    private EditText yun_et_loginAccount;
    private EditText yun_et_loginPwd;
    private Button yun_btn_loginSubmit;
    private ImageView yun_img_show_pwd, yun_sdk_iv_cancel, yun_sdk_iv_pwd_cancel;
    private ImageView yun_iv_logo;
    private boolean showPwd = false;
    private TextView yun_btn_loginSubmitForgetPwd;
    private ViewStackManager viewStackManager;
    private PopupWindow pw_select_user;
    private RecordUserAdapter pw_adapter;
    private List<UserInfo> userInfoList;
    private ImageView yun_iv_loginUserSelect;
    private LinearLayout llLoginAccount;
    private TextView yun_sdk_tv_mRegisterFastRegister;
    private SharedPreferences sp;

    public YunLoginView(Context context) {
        super(context);
        setupUI();
    }

    public YunLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void setupUI() {
        loginActivity = (YunLoginActivity) getContext();
        viewStackManager = ViewStackManager.getInstance(loginActivity);
        sp = loginActivity.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(), MResource.LAYOUT, "yun_sdk_login_lastest"), this);
        yunRlLogin = (RelativeLayout) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_rl_login"));
        yunYKLogin = (RelativeLayout) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_yk_login"));
        yun_et_loginAccount = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_loginAccount"));
        yun_et_loginPwd = (EditText) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_et_loginPwd"));
        yun_img_show_pwd = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_img_show_pwd"));
        yun_sdk_iv_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_cancel"));
        yun_sdk_iv_pwd_cancel = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_pwd_cancel"));
        yun_btn_loginSubmitForgetPwd = (TextView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_btn_loginSubmitForgetPwd"));
        yun_btn_loginSubmit = (Button) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_btn_loginSubmit"));
        yunLlLoginRegister = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_ll_loginRegister"));
        yun_iv_loginUserSelect = (ImageView) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_iv_loginUserSelect"));
        llLoginAccount = (LinearLayout) findViewById(MResource.getIdByName(getContext(), "R.id.yun_sdk_ll_loginAccount"));
        yun_sdk_tv_mRegisterFastRegister = (TextView) findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_tv_mRegisterFastRegister"));


        String YUN_SP_IS_REG= (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_IS_REG,"");
        String YUN_SP_IS_FAST_REG = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_IS_FAST_REG,"");
        if (YUN_SP_IS_REG.equals(SdkConstant.YUN_YES)){
            if (!YUN_SP_IS_FAST_REG.equals(SdkConstant.YUN_YES)){
                yun_sdk_tv_mRegisterFastRegister.setVisibility(GONE);
            }
        }else {
            yun_sdk_tv_mRegisterFastRegister.setVisibility(GONE);
            yunLlLoginRegister.setVisibility(GONE);
        }
        yunLlLoginRegister.setOnClickListener(this);
        yun_btn_loginSubmit.setOnClickListener(this);
        yun_img_show_pwd.setOnClickListener(this);
        yun_sdk_iv_cancel.setOnClickListener(this);
        yun_sdk_iv_pwd_cancel.setOnClickListener(this);
        yun_iv_loginUserSelect.setOnClickListener(this);
        yun_btn_loginSubmitForgetPwd.setOnClickListener(this);
        yun_sdk_tv_mRegisterFastRegister.setOnClickListener(this);

        //获取最后一次登陆账号进行登陆显示
        UserInfo userInfoLast = UserLoginInfodao.getInstance(loginActivity).getUserInfoLast();
        if (userInfoLast != null) {
            if (!TextUtils.isEmpty(userInfoLast.username) && !TextUtils.isEmpty(userInfoLast.password)) {
                yun_et_loginAccount.setText(userInfoLast.username);
                yun_et_loginPwd.setText(userInfoLast.password);
            }
        }
        setTextChangedListener(yun_et_loginAccount, yun_sdk_iv_cancel);
        setTextChangedListener(yun_et_loginPwd, yun_sdk_iv_pwd_cancel);
//        }
//        initThirdLogin();
    }

    /**
     * 初始化第三方登陆
     */
//    private void initThirdLogin() {
//        if (SdkConstant.thirdLoginInfoList != null && SdkConstant.thirdLoginInfoList.size() > 0) {
//            yun_sdk_ll_loginByThird.setVisibility(VISIBLE);
//            ShareSDK.initSDK(loginActivity.getApplicationContext());
//            for (ThirdLoginInfo thirdLoginInfo : SdkConstant.thirdLoginInfoList) {
//                if ("5".equals(thirdLoginInfo.getOauth_type())) {
//                    yun_sdk_iv_loginQq.setVisibility(VISIBLE);
//                    yun_sdk_tv_loginByThird.setVisibility(VISIBLE);
//                    ThirdLoginUtil.initQQ(thirdLoginInfo.getOauth_appid(), thirdLoginInfo.getOauth_appsecret());
//                } else if ("6".equals(thirdLoginInfo.getOauth_type())) {
//                    yun_sdk_iv_loginWx.setVisibility(VISIBLE);
//                    yun_sdk_tv_loginByThird.setVisibility(VISIBLE);
//                    ThirdLoginUtil.initWx(thirdLoginInfo.getOauth_appid(), thirdLoginInfo.getOauth_appsecret());
//                } else if ("7".equals(thirdLoginInfo.getOauth_type())) {
//                    yun_sdk_iv_loginWb.setVisibility(VISIBLE);
//                    yun_sdk_tv_loginByThird.setVisibility(VISIBLE);
//                    ThirdLoginUtil.initXinNan(thirdLoginInfo.getOauth_appid(), thirdLoginInfo.getOauth_appsecret(), thirdLoginInfo.getOauth_redirecturl());
//                }
//            }
//        }
//    }
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yunLlLoginRegister.getId()) {
            viewStackManager.addView(loginActivity.getYunRegisterView());
        } else if (view.getId() == yun_btn_loginSubmit.getId()) {
            submitLogin();
        } else if (view.getId() == yun_sdk_tv_mRegisterFastRegister.getId()) {//游客登录  一键注册
            viewStackManager.showView(viewStackManager.getViewByClass(YunUserNameRegisterView.class));
        } else if (view.getId() == yun_img_show_pwd.getId()) {
            if (showPwd) {
                yun_et_loginPwd.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPwd = false;
                yun_img_show_pwd.setImageResource(MResource.getIdByName(getContext(), "R.drawable.yun_sdk_sdk_biyan") );
            } else {
                yun_et_loginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPwd = true;
                yun_img_show_pwd.setImageResource(MResource.getIdByName(getContext(), "R.drawable.yun_sdk_sdk_yanjing") );
            }
        } else if (view.getId() == yun_btn_loginSubmitForgetPwd.getId()) {
            viewStackManager.showView(viewStackManager.getViewByClass(YunForgetPwdView.class));
        } else if(view.getId() == yun_sdk_iv_cancel.getId()){
            yun_et_loginAccount.setText("");
        } else if(view.getId() == yun_sdk_iv_pwd_cancel.getId()){
            yun_et_loginPwd.setText("");
        }
        else if (view.getId() == yun_iv_loginUserSelect.getId()) {
            userselect(yun_et_loginAccount, llLoginAccount.getWidth());
        }
    }

    private void submitLogin() {
        final String userName = yun_et_loginAccount.getText().toString().trim();
        final String password = yun_et_loginPwd.getText().toString();

        if (TextUtils.isEmpty(userName)){
            T.s(loginActivity, "账号不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)){
            T.s(loginActivity, "密码不能为空");
            return;
        }

        if (!RegExpUtil.isMatchPassword(password)) {
            T.s(loginActivity, getResources().getString(MResource.getIdByName(loginActivity,"R.string.toast_pwd_instructure")));
            return;
        }

        final LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setMethod(SdkConstant.YUN_SDK_USER_LOGIN);
        loginRequestBean.setUname(userName);
        loginRequestBean.setPasswd(password);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(loginRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserResultBean>(loginActivity) {
            @Override
            public void onDataSuccess(final UserResultBean data) {
                //快速登陆需要延时2秒供用户选择是否切换账号
                if (data != null && getVisibility()  == VISIBLE) {//当前界面还在显示状态才执行
                    //接口回调通知
                    Log.d("d", "~~~登陆返回:"+data.getInfo().toString());
                    sp.edit().putString(SdkConstant.YUN_SP_SESSION, data.getInfo().getSession())//
                            .putString(SdkConstant.YUN_SP_UID, data.getInfo().getUid())//
                            .putBoolean(SdkConstant.YUN_SP_AUTH, data.getInfo().isAuth())//
                            .putString(SdkConstant.YUN_SP_BIND_MOBILE, data.getInfo().getMobile())//
                            .putString(SdkConstant.YUN_SP_IDCARD, data.getInfo().getIdcard())
                            .putString(SdkConstant.YUN_SP_REALNAME, data.getInfo().getRealname())
//                            .putBoolean(SdkConstant.YUN_SP_IS_VALIDATE, data.getInfo().isIs_validate())
                            .putString(SdkConstant.USERNAME, userName)
                            .putString(SdkConstant.PWD, password)
                            .putBoolean(SdkConstant.YUN_SP_BIND, data.getInfo().isBind())
                            .commit();

                    LoginControl.saveUserToken(data.getInfo().getSession());

                    loginActivity.loginCompleted(data);

                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(loginActivity).findUserLoginInfoByName(userName)) {
                        UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(userName, password);
                    } else {
                        UserLoginInfodao.getInstance(loginActivity).deleteUserLoginByName(userName);
                        UserLoginInfodao.getInstance(loginActivity).saveUserLoginInfo(userName, password);
                    }

//                    String YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
//                    if (!YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_NO)){
//                        if (!data.getInfo().isAuth()){
//                            SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
////                            viewStackManager.showView(viewStackManager.getViewByClass(YunAuthenticationView.class));
//                            viewStackManager.showView(loginActivity.getYunAuthenticationView());
//                        }else {
//                            loginActivity.loginAuthenticCompleted();
//                            loginActivity.callBackFinish();
//                        }
//                    }else{
//                        loginActivity.loginAuthenticCompleted();
//                        loginActivity.callBackFinish();
//                    }

                    //是否需要实名认证
                    String YUN_SP_INIT_IS_AUTH = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_AUTH,"");
                    //是否需要绑定手机号
                    String YUN_SP_INIT_IS_BIND = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_INIT_IS_BIND,"");
                    if (!YUN_SP_INIT_IS_AUTH.equals(SdkConstant.YUN_NO)){ //需要实名认证走实名验证
                        if (!data.getInfo().isAuth()){ //没有实名验证过
                            SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_LOGIN);
                            viewStackManager.showView(loginActivity.getYunAuthenticationView());
                            return;
                        }
                    }
                    if (!YUN_SP_INIT_IS_BIND.equals(SdkConstant.YUN_NO)){ //需要绑定手机号的走绑定手机号
                        if (!data.getInfo().isBind()) {//没有绑定过手机
                            loginActivity.callBackFinish();
                            SharedPreferencesUtil.putData(SdkConstant.BINDMOBILETYPE, SdkConstant.BINDMOBILETYPE_LOGIN);
                            Intent intent = new Intent(loginActivity, YunBindMobileActivity.class);
                            loginActivity.startActivity(intent);
//                            viewStackManager.showView(viewStackManager.getViewByClass(YunBindMobileView.class));
                            return;
                        }
                    }
                    loginActivity.loginAuthenticCompleted();
                    loginActivity.callBackFinish();
                }
            }

            @Override
            public void onDataFailure(UserResultBean data) {
                loginActivity.loginFailed(data);
//                T.showToast(loginActivity, "Failure:111 "+data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("正在登录...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void userselect(View v, int width) {
        L.e(TAG, "width=" + width);
        if (pw_select_user != null && pw_select_user.isShowing()) {
            pw_select_user.dismiss();
        } else {
            userInfoList = UserLoginInfodao.getInstance(loginActivity)
                    .getUserLoginInfo();
            if (null == userInfoList || userInfoList.isEmpty()) {
                return;
            }
            if (null == pw_adapter) {
                pw_adapter = new RecordUserAdapter();
            }
            if (pw_select_user == null) {
                View view = LayoutInflater.from(loginActivity).inflate(MResource.getIdByName(loginActivity, "R.layout.yun_sdk_pop_record_account"), null);

                ListView lv_pw = (ListView) view.findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_lv_pw"));

                // LinearLayout.LayoutParams lp=new
                // LinearLayout.LayoutParams(200,-2 );
                // lv_pw.setLayoutParams(lp);

                lv_pw.setCacheColorHint(0x00000000);
                lv_pw.setAdapter(pw_adapter);
                lv_pw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterview,
                                            View view, int position, long row) {
                        pw_select_user.dismiss();
                        UserInfo userInfo = userInfoList.get(position);

                        yun_et_loginAccount.setText(userInfo.username);
                        yun_et_loginPwd.setText(userInfo.password);


                    }
                });
                pw_select_user = new PopupWindow(view, width,
                        LinearLayout.LayoutParams.WRAP_CONTENT, true);
                pw_select_user.setBackgroundDrawable(new ColorDrawable(
                        0x00000000));
                pw_select_user.setContentView(view);
            } else {
                pw_adapter.notifyDataSetChanged();
            }
            pw_select_user.showAsDropDown(v, 0, 0);
        }
    }

    /**
     * popupwindow显示已经登录用户的设配器
     *
     * @author Administrator
     */
    private class RecordUserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return userInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (null == convertView) {
                View view = LayoutInflater.from(loginActivity).inflate(MResource.getIdByName(loginActivity,
                        "R.layout.yun_sdk_pop_record_account_list_item"), null);

                convertView = view;
            }
            TextView tv_username = (TextView) convertView.findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_tv_username"));
            ImageView iv_delete = (ImageView) convertView.findViewById(MResource.getIdByName(loginActivity, "R.id.yun_sdk_iv_delete"));
            iv_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果删除的用户名与输入框中的用户名一致将删除输入框中的用户名与密码
                    if (yun_et_loginAccount.getText().toString().trim()
                            .equals(userInfoList.get(position).username)) {
                        yun_et_loginAccount.setText("");
                        yun_et_loginPwd.setText("");
                    }
                    UserLoginInfodao.getInstance(loginActivity)
                            .deleteUserLoginByName(
                                    userInfoList.get(position).username);
                    userInfoList.remove(position);
                    if (null != pw_adapter) {
                        if (userInfoList.isEmpty()) {
                            pw_select_user.dismiss();
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            tv_username.setText(userInfoList.get(position).username);
            return convertView;
        }
    }
}
