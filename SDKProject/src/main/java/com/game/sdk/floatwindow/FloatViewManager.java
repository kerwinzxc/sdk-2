package com.game.sdk.floatwindow;

import android.app.Activity;
import android.content.Context;

import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;


public class FloatViewManager {
    private static FloatViewManager instance = null;
    private Context mContext;
    private IFloatView iFloatView;

    private FloatViewManager(Context context) {
        this.mContext = context.getApplicationContext();
//        if("0".equals(SdkConstant.SHOW_INDENTIFY)){//不需要实名认证
        iFloatView = FloatViewImpl.getInstance(mContext);
//        }else{
//            iFloatView=IdentifyFloatViewImpl.getInstance(mContext);
//        }
    }

    /**
     * @param context
     * @return
     */
    public synchronized static FloatViewManager getInstance(Context context) {
        if (instance == null) {
            instance = new FloatViewManager(context);
        }
        return instance;
    }

    // 移除悬浮窗口
    public void removeFloat() {
        try {
            iFloatView.removeFloat();
            instance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示悬浮窗口
    public void showFloat(Activity activity) {
        try {
            iFloatView.showFloat(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 移除悬浮窗口
    public void hidFloat() {
        try {
            iFloatView.hidFloat();
            instance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开网页
     */
    public void openUrl(String url, String title) {
        hidFloat();
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(new WebRequestBean()));
//        FloatWebActivity.start(mContext, url, title, httpParamsBuild.getHttpParams().getUrlParams().toString(), httpParamsBuild.getAuthkey());
    }

    /**
     * 打开用户中心
     */
    public void openucenter() {
        hidFloat();
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(new WebRequestBean()));
//        FloatWebActivity.start(mContext, SdkApi.getWebUser(), "用户中心", httpParamsBuild.getHttpParams().getUrlParams().toString(), httpParamsBuild.getAuthkey());
    }
}
