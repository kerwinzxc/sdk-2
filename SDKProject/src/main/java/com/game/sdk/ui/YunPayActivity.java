package com.game.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.game.sdk.SdkConstant;
import com.game.sdk.util.ActivityHook;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.view.ViewStackManager;
import com.game.sdk.view.YunPayView;

public class YunPayActivity extends Activity {
    private static final String TAG = YunPayActivity.class.getSimpleName();
    private YunPayView yunPayView;
    private ViewStackManager viewStackManager;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityHook.hookOrientation(this);//hook，绕过检查
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this, "R.layout.yun_sdk_activity_yun_pay"));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (DeviceUtil.isPortrait(this)){
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()*(0.9));
        }else {
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()/2);
            lp.height = (int) ((int)getWindowManager().getDefaultDisplay().getHeight()*(0.9));
        }
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        YunPayActivity.this.setFinishOnTouchOutside(false);
        sp = getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);

        setupUI();
    }

    private void setupUI() {
        viewStackManager = ViewStackManager.getInstance(this);
        yunPayView = (YunPayView) findViewById(MResource.getIdByName(this, "R.id.yun_sdk_pay"));
        viewStackManager.addBackupView(yunPayView);
        viewStackManager.addView(yunPayView);
    }

    @Override
    public void onBackPressed() { }

    public YunPayView getYunPayView() {
        return yunPayView;
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
        Intent starter = new Intent(context, YunPayActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
//        starter.putExtra("type", type);
        context.startActivity(starter);
    }
}
