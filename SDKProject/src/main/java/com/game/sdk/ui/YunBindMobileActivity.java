package com.game.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.game.sdk.SdkConstant;
import com.game.sdk.util.ActivityHook;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;
import com.game.sdk.view.ViewStackManager;
import com.game.sdk.view.YunBindMobileView;
import com.game.sdk.view.YunPayView;

public class YunBindMobileActivity extends Activity {
    private static final String TAG = YunBindMobileActivity.class.getSimpleName();
    private YunBindMobileView yunBindMobile;
    private ViewStackManager viewStackManager;
    private SharedPreferences sp;
    private LinearLayout ll_linearLayout;
    private String BINDMOBILETYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityHook.hookOrientation(this);//hook，绕过检查
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this, "R.layout.yun_sdk_activity_bind_mobile"));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (DeviceUtil.isPortrait(this)){

//            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()*(0.9));
        }else {
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()/2);
            lp.height = (int) ((int)getWindowManager().getDefaultDisplay().getHeight()*(0.9));
        }
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        YunBindMobileActivity.this.setFinishOnTouchOutside(false);
        sp = getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);

        setupUI();
    }

    private void setupUI() {
        viewStackManager = ViewStackManager.getInstance(this);
        yunBindMobile = (YunBindMobileView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_bind_mobile"));
        ll_linearLayout = (LinearLayout) findViewById(MResource.getIdByName(this, "R.id.ll_linearLayout"));
        viewStackManager.addBackupView(yunBindMobile);
        viewStackManager.addView(yunBindMobile);
        BINDMOBILETYPE = (String) SharedPreferencesUtil.getData(SdkConstant.BINDMOBILETYPE, "");
        if (BINDMOBILETYPE.equals(SdkConstant.BINDMOBILETYPE_LOGIN)){
//            ll_linearLayout.setBackgroundColor(Color.parseColor("#bbf9f9f9"));
        }else if (BINDMOBILETYPE.equals(SdkConstant.BINDMOBILETYPE_USERCENTER)){
            ll_linearLayout.setBackgroundColor(Color.parseColor("#ccf9f9f9"));
        }
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void finish() {
        super.finish();
    }

    public YunBindMobileView getYunBindMobile() {
        return yunBindMobile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewStackManager.clear();
    }

    /**
     * 通知回调成功并关闭activity
     */
    public void callBackFinish() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, YunBindMobileActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
//        starter.putExtra("type", type);
        context.startActivity(starter);
    }
}
