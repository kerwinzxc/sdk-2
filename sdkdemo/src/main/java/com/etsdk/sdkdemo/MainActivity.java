package com.etsdk.sdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.sdk.YunsdkManager;
import com.game.sdk.domain.SubmitRoleInfoCallBack;
import com.game.sdk.domain.request.PayRequestBean;
import com.game.sdk.domain.request.RoleRequestBean;
import com.game.sdk.domain.result.QueryResultBean;
import com.game.sdk.domain.result.UserResultBean;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.listener.OnLogoutListener;
import com.game.sdk.listener.OnPaymentListener;
import com.game.sdk.log.T;
import com.game.sdk.ui.YunLoginActivity;

import static com.game.sdk.ui.YunLoginActivity.REQUEST_PHONE_STATE;

/*
test
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnTestLogin;
    EditText etTestMoney;
    Button btnTestCharger;
    Button btnTestSendRoleinfo;
    public static YunsdkManager sdkManager;
    private Button btn_test_logout;
    private Button btn_test_switchAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PHONE_STATE);
            }
        }
        setupUI();
    }
    private void setupUI() {
        btnTestLogin = (Button) findViewById(R.id.btn_test_login);
        etTestMoney = (EditText) findViewById(R.id.et_test_money);
        btnTestCharger = (Button) findViewById(R.id.btn_test_charger);
        btnTestSendRoleinfo = (Button) findViewById(R.id.btn_test_sendRoleinfo);
        btn_test_logout = (Button) findViewById(R.id.btn_test_logout);
        btn_test_switchAccount = (Button) findViewById(R.id.btn_test_switchAccount);
        btnTestCharger.setOnClickListener(this);
        btnTestLogin.setOnClickListener(this);
        btnTestSendRoleinfo.setOnClickListener(this);
        btn_test_logout.setOnClickListener(this);
        btn_test_switchAccount.setOnClickListener(this);
        //获得sdk单例
        sdkManager = YunsdkManager.getInstance();

        //sdk初始化
        sdkManager.initSdk(this, new OnInitSdkListener() {
            @Override
            public void initSuccess(String code, String msg) {
                Log.e(TAG, "initSdk=" + msg);
            }

            @Override
            public void initError(String code, String msg) {
                T.s(MainActivity.this, msg);
            }
        });

        //添加sdk登陆监听,包含正常登陆，切换账号登陆，登陆过期后重新登陆
        sdkManager.addLoginListener(new OnLoginListener() {
            @Override
            public void loginSuccess(UserResultBean userResultBean) {
                Log.e(TAG, "登陆成功 Uid=" +
                        userResultBean.getInfo().getUid() + "  session=" + userResultBean.getInfo().getSession());
                T.s(MainActivity.this, "登陆成功");
                //一般登陆成功后需要显示浮点
                sdkManager.showFloatView(MainActivity.this);
            }

            @Override
            public void loginError(UserResultBean userResultBean) {
                Log.e(TAG, " code=" + userResultBean.getCode() + "  msg=" + userResultBean.getMsg());
            }

            @Override
            public void loginAuthenticOver() {
                Log.d(TAG, " loginAuthentic");
            }
        });

        sdkManager.addLogoutListener(new OnLogoutListener() {
            @Override
            public void logoutSuccess(int type, String code, String msg) {
                Log.d(TAG, "登出成功，类型type=" + type + " code=" + code + " msg=" + msg);
                if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {//正常退出成功
                    Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                }
                if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {//切换账号退出成功
                    //游戏此时可跳转到登陆页面，让用户进行切换账号
                    Toast.makeText(MainActivity.this, "退出登陆", Toast.LENGTH_SHORT).show();

                }
                if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {//登陆过期退出成功
                    //游戏此时可跳转到登陆页面，让用户进行重新登陆
                    sdkManager.showLogin(YunLoginActivity.TYPE_FAST_LOGIN);
                }
            }

            @Override
            public void logoutError(int type, String code, String msg) {
                Log.e(TAG, "登出失败，类型type=" + type + " code=" + code + " msg=" + msg);
                if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {//正常退出失败

                }
                if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {//切换账号退出失败

                }
                if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {//登陆过期退出失败

                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_login:
                sdkManager.showLogin(YunLoginActivity.TYPE_FAST_LOGIN);
                break;
            case R.id.btn_test_charger:
                String money_str = etTestMoney.getText().toString().trim();
                String money = "0.01";
                if (!TextUtils.isEmpty(money_str) && !"".equals(money_str)) {
                    money = money_str;
                }
                PayRequestBean payRequestBean = new PayRequestBean();
                payRequestBean.setOutTradeNo("123123123123123");
                payRequestBean.setProductPrice(money);
                payRequestBean.setProductCount("1");
                payRequestBean.setProductId("100");
                payRequestBean.setProductName("666元宝");
                payRequestBean.setProductDesc("屠龙宝刀，点击就送");
                payRequestBean.setExchangeRate("100");
                payRequestBean.setCurrencyName("金币");
                payRequestBean.setRoleType("1");
                payRequestBean.setServerId("11");
                payRequestBean.setServerName("沙巴克");
                payRequestBean.setRoleId("2587");
                payRequestBean.setRoleName("雄霸天下");
                payRequestBean.setPartyName("葬爱家族");
                payRequestBean.setRoleLevel("100");
                payRequestBean.setRoleVip("7");
                payRequestBean.setRoleBalance("200.0");
//                payRequestBean.setRolelevelCtime("0");
                payRequestBean.setRolelevelMtime("0");
                payRequestBean.setExtend("测试");
                sdkManager.showPay(payRequestBean, new OnPaymentListener() {
                    @Override
                    public void paymentSuccess(QueryResultBean queryResultBean) {
                        if (queryResultBean.getInfo().getStatus().equals("0")) {
                            Toast.makeText(MainActivity.this, "订单未支付", Toast.LENGTH_SHORT).show();
                        } else if (queryResultBean.getInfo().getStatus().equals("1")) {
                            Toast.makeText(MainActivity.this, "订单支付成功"
                                    , Toast.LENGTH_SHORT).show();

                        } else if (queryResultBean.getInfo().getStatus().equals("2")) {

                            Toast.makeText(MainActivity.this, "订单支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void paymentError(QueryResultBean queryResultBean) {
//                        Toast.makeText(MainActivity.this, queryResultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_test_sendRoleinfo:
                RoleRequestBean roleRequestBean = new RoleRequestBean();
                roleRequestBean.setRoleName("狂少");
                roleRequestBean.setRoleLevel("2");
                roleRequestBean.setServerName("劲舞团");
                roleRequestBean.setRoleType("1");
                roleRequestBean.setServerId("222");
                roleRequestBean.setRoleId("789");
                roleRequestBean.setPartyName("皇族");
                roleRequestBean.setRoleVip("7");
                roleRequestBean.setRoleBalance("1");
//                roleRequestBean.setRolelevelCtime("1580166691");
                roleRequestBean.setRolelevelMtime("1851190735");

                sdkManager.setRoleInfo(roleRequestBean, new SubmitRoleInfoCallBack() {
                    @Override
                    public void submitSuccess() {
                        Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void submitFail(String msg) {
                        Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_test_logout:
//                //调用此方法前请先设置登出监听
                sdkManager.logout();
                break;
            case R.id.btn_test_switchAccount:
//                //切换账号会退出登陆，请在登出监听中接收切换退出结果
                sdkManager.switchAccount();
                break;
        }
    }


    /**
     * 在游戏销毁时需要调用sdk的销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdkManager.recycle();
    }

    /**
     * 游戏一般在界面可使用时显示浮点
     */
    @Override
    protected void onResume() {
        super.onResume();
        sdkManager.showFloatView(MainActivity.this);
    }

    /**
     * 游戏一般在界面不可见时移除浮点
     */
    @Override
    protected void onStop() {
        super.onStop();
        sdkManager.removeFloatView();
    }
}
