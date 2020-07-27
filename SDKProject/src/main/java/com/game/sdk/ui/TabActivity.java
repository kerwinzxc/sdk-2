package com.game.sdk.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.game.sdk.R;
import com.game.sdk.util.ActivityHook;
import com.game.sdk.util.DeviceUtil;
import com.game.sdk.util.MResource;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rgRadioGroup;
    private RadioButton rbMy, rbGift;
    private FrameLayout flTabFragment;
    public static List<Fragment> fragments = new ArrayList<>();
    public static Fragment curFragment;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction transaction;
    public  Context mContext = TabActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityHook.hookOrientation(this);//hook，绕过检查
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);//设置窗口周围触摸不消失
        setContentView(MResource.getIdByName(this, MResource.LAYOUT, "activity_tab")  );

        initView();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (DeviceUtil.isPortrait(this)){
            lp.width = (int) (getWindowManager().getDefaultDisplay().getWidth());
            lp.height = (int)(getWindowManager().getDefaultDisplay().getHeight()*(0.8));
            lp.gravity = Gravity.BOTTOM;
            flTabFragment.setBackgroundResource(MResource.getIdByName(this,"R.drawable.bg_tab_fra"));
        }else {
            lp.width = (int) ((int)getWindowManager().getDefaultDisplay().getWidth()/2);
            lp.height = (int) ((int)getWindowManager().getDefaultDisplay().getHeight());
            lp.gravity = Gravity.LEFT;
            flTabFragment.setBackgroundResource(MResource.getIdByName(this,"R.drawable.bg_tab_fra_landscape"));
        }
        getWindow().setAttributes(lp);
//        getWindow().setDimAmount(0.5f);//设置窗口周围透明
        fragments = getFragments();
        rgRadioGroup.setOnCheckedChangeListener(this);
        defaultFragment();
    }

    private void defaultFragment(){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        curFragment = fragments.get(0);
        transaction.replace(MResource.getIdByName(this,"R.id.flTabFragment"), curFragment);
        transaction.commit();
    }

    private void initView(){
        rgRadioGroup = (RadioGroup) findViewById(MResource.getIdByName(this,"R.id.rgRadioGroup"));
        rbMy = (RadioButton) findViewById(MResource.getIdByName(this,"R.id.rbMy"));
        rbGift = (RadioButton) findViewById(MResource.getIdByName(this,"R.id.rbGift"));
        flTabFragment = (FrameLayout) findViewById(MResource.getIdByName(this,"R.id.flTabFragment"));
//        setRidioButtonSize();
    }

    private void setRidioButtonSize(){
        RadioButton[] radioButtons = new RadioButton[]{rbMy,rbGift};
        for (RadioButton rb : radioButtons){
            Drawable[] drawable = rb.getCompoundDrawables();
            Rect rect = new Rect(0,0,50,50);
            drawable[1].setBounds(rect);
            rb.setCompoundDrawables(null, drawable[1], null, null);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (checkedId == R.id.rbMy) {
            curFragment = fragments.get(0);
            transaction.replace(MResource.getIdByName(this,"R.id.flTabFragment"), curFragment);
        }else if (checkedId == R.id.rbGift) {
            curFragment = fragments.get(1);
            transaction.replace(MResource.getIdByName(this,"R.id.flTabFragment"), curFragment);
        }
        setTabState();
        transaction.commit();

    }

    private void setTabState(){
        setMyState();
        setGiftState();
    }

    private void setMyState(){
        if (rbMy.isChecked()){
            rbMy.setTextColor(this.getResources().getColor(MResource.getIdByName(this,"R.color.yun_sdk_tab_select")));
        }else{
            rbMy.setTextColor(this.getResources().getColor(MResource.getIdByName(this,"R.color.yun_sdk_tab_unselect")));
        }
    }

    private void setGiftState(){
        if (rbGift.isChecked()){
            rbGift.setTextColor(this.getResources().getColor(MResource.getIdByName(this,"R.color.yun_sdk_tab_select")));
        }else{
            rbGift.setTextColor(this.getResources().getColor(MResource.getIdByName(this,"R.color.yun_sdk_tab_unselect")));
        }
    }


    public List<Fragment> getFragments(){
        fragments.add(new UserCenterFragment());
        fragments.add(new GiftBagFragment());
        return fragments;
    }
}
