package com.game.sdk.floatwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.game.sdk.ui.TabActivity;
import com.game.sdk.util.MResource;
import com.game.sdk.util.RegExpUtil;

public class FloatViewImpl implements IFloatView {
    protected static final String TAG = FloatViewImpl.class.getSimpleName();
    /**
     * 悬浮view
     **/
    private static FloatViewImpl instance = null;
    // 定义浮动窗口布局
    private RelativeLayout mFloatLayout;
    private LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private PopupWindow mPopWindow;
    private ImageView mFloatView/*, float_item_id*/;
    private Context mContext;
    private final int MOBILE_QUERY = 1;
    private boolean isleft = true;
    private int ViewRawX;
    private int ViewRawY;
    private float beforeX;
    private float beforeY;
    private final int scaledTouchSlop;
    private Handler handler;

    private FloatViewImpl(Context context) {
        this.mContext = context.getApplicationContext();
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * @param context
     * @return
     */
    public synchronized static FloatViewImpl getInstance(Context context) {
        if (instance == null) {
            instance = new FloatViewImpl(context);
        }
        return instance;
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MOBILE_QUERY:
//                    setHiddenState();
////                    if (mPopWindow != null && mFloatLayout != null)
////                        mPopWindow.update();
//                    break;
//            }
//        }
//    };

    private void setHiddenState(){
        mFloatView.setImageResource(MResource.getIdByName(mContext, "drawable", isleft ? "yun_sdk_pull_right" : "yun_sdk_pull_left"));
        ViewGroup.LayoutParams lp = mFloatView.getLayoutParams();
        lp.height = RegExpUtil.dp2Px(mContext,54) ;
        lp.width = RegExpUtil.dp2Px(mContext,26);
        mFloatView.setLayoutParams(lp);
    }


    private void createFloatView(Activity activity) {
        Log.d("dddd", "createFloatView:"+mFloatLayout);
        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 获取浮动窗口视图所在布局
        if (mFloatLayout == null){
            mFloatLayout = (RelativeLayout) inflater.inflate(MResource
                    .getIdByName(mContext, MResource.LAYOUT, "yun_sdk_float_layout"), null);
        }
        if (mPopWindow != null) {
            mPopWindow.dismiss();
            mPopWindow = null;
        }
        if (mPopWindow == null){
            mPopWindow = new PopupWindow(mFloatLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (mPopWindow.isShowing()){
                mPopWindow.dismiss();
            }else {
                mPopWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.LEFT|Gravity.TOP, 0 ,0);
            }
        }
        // 浮动窗口按钮
        mFloatView = (ImageView) mFloatLayout.findViewById(MResource
                .getIdByName(mContext, "R.id.yun_sdk_iv_float"));
        initUI();
    }

    private void initUI() {
        handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MOBILE_QUERY:
                    setHiddenState();
//                    if (mPopWindow != null && mFloatLayout != null)
//                        mPopWindow.update();
                    break;
            }
        }
    };

            // 浮动窗口按钮
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {
            int orgX, orgY;
            int offsetX,offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                mFloatView.setImageResource(MResource.getIdByName(mContext,
                        "drawable", "yun_sdk_fload"));
                ViewGroup.LayoutParams lp = mFloatView.getLayoutParams();
                lp.height = RegExpUtil.dp2Px(mContext,54);
                lp.width = RegExpUtil.dp2Px(mContext,54);
                mFloatView.setLayoutParams(lp);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                            beforeX = event.getRawX();
                            beforeY = event.getRawY();
                        orgX = (int) event.getX();
                        orgY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                            ViewRawX = (int) event.getRawX()
                                    - mFloatView.getMeasuredWidth() / 2;
                            ViewRawY = (int) event.getRawY()
                                    - mFloatView.getMeasuredHeight() / 2 - 25;
                            pullover(3000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;
                        mPopWindow.update((int) event.getRawX()
                                - mFloatView.getMeasuredWidth() / 2, (int) event.getRawY()
                                - mFloatView.getMeasuredHeight() / 2 - 25, -1, -1, true);
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(onclick);

//        pullover(3000);
    }

    private void pullover(int delayTime) {
        // 传送msg
        int width = mWindowManager.getDefaultDisplay().getWidth();
        beforeX = 0;
        beforeY = ViewRawY;
        isleft = true;
        if (ViewRawX > width / 2) {
            beforeX = width;
            isleft = false;
        }
        mPopWindow.update((int)beforeX,(int)beforeY, -1, -1, true);
        handler.removeMessages(MOBILE_QUERY);
        Message msg = handler.obtainMessage(MOBILE_QUERY);
        handler.sendMessageDelayed(msg, delayTime);
    }

    /**
     * 打开用户中心
     */
    public void openucenter() {
        hidFloat();
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(new WebRequestBean()));
//        FloatWebActivity.start(mContext, SdkApi.getWebUser(), "用户中心", httpParamsBuild.getHttpParams().getUrlParams().toString(), httpParamsBuild.getAuthkey());
    }

    private OnClickListener onclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mFloatView.getId()) {
                hidFloat();
//                HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(new WebRequestBean()));
//                FloatWebActivity.start(mContext, S1dkApi.getWebUser(), "用户中心", httpParamsBuild.getHttpParams().getUrlParams().toString(), httpParamsBuild.getAuthkey());
//
//                Intent intent = new Intent(mContext, WebViewActivity.class);
//                intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_WEB_CENTER);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);

                Intent intent = new Intent(mContext, TabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return;
            }

        }
    };

    // 移除悬浮窗口
    public void removeFloat() {
        hidFloat();
        instance = null;
    }

    // 显示悬浮窗口
    public void showFloat(Activity activity) {
        if (instance != null) {
            createFloatView(activity);
            pullover(0);
        }
    }

    // 移除悬浮窗口
    public void hidFloat() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        if (mFloatLayout != null) {
            mFloatLayout.removeAllViews();
            mPopWindow.dismiss();
            mFloatLayout = null;
            mWindowManager = null;
        }

//            if (mWindowManager == null) {
//                mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
//            }

//            mWindowManager.removeView(mFloatLayout);
    }
}
