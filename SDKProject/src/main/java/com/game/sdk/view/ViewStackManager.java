package com.game.sdk.view;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.game.sdk.log.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewStackManager {
    private static final String TAG = ViewStackManager.class.getSimpleName();
    private static List<View> uiStackList = Collections.synchronizedList(new ArrayList<View>());
    private static List<View> backupViewUi = new ArrayList<View>();
    private static ViewStackManager instance;
    private Activity mActivity;

    public synchronized static ViewStackManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new ViewStackManager(activity);
        }
        return instance;
    }

    private ViewStackManager(Activity activity) {
        this.mActivity = activity;
    }

    public void hiddenAllView() {
        for (View view : backupViewUi) {
            if (view != null){
                view.setVisibility(View.GONE);
            }
//            Log.d(TAG, "~~~隐藏：" + view.getClass().getSimpleName());
        }
    }



    public View getViewByClass(Class clazz) {
        for (View view : backupViewUi) {
            if (view.getClass().getSimpleName().equals(clazz.getSimpleName())) {
                return view;
            }
        }
        return null;
    }

    public void showView(View view) {
        int index = uiStackList.indexOf(view);
        if (index >= 0) {
            //移除上面的
            for (int i = uiStackList.size() - 1; i > index; i--) {
                removeView(uiStackList.get(i));
            }
            view.setVisibility(View.VISIBLE);//显示自己
        } else {//没有找到，直接添加一个
            addView(view);
        }
    }
    /**
     * 添加一个view,并显示
     */
    public void addView(View view) {
        uiStackList.add(view);
        hiddenAllView();
        if (view != null){
            view.setVisibility(View.VISIBLE);
        }

//        Log.d(TAG, "~~~显示：" + view.getClass().getSimpleName());
//        Log.d(TAG, "~~~添加完后size=" + uiStackList.size());
    }

    public void removeTopView() {
        if (uiStackList.size() > 1) {
            int index = uiStackList.size() - 1;
            View view = uiStackList.get(index);
            removeView(view);
            L.e(TAG, "移除了顶部view：" + view.getClass().getSimpleName());
            showTopView();
        } else {//没有了
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }

    public void showTopView() {
        hiddenAllView();
        if (!uiStackList.isEmpty()) {
            uiStackList.get(uiStackList.size() - 1).setVisibility(View.VISIBLE);
        } else {//没有要显示的view
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }

    /**
     * 移除view显示最上面一个view,没有view则关闭页面
     *
     * @param view
     */
    public void removeView(View view) {
        uiStackList.remove(view);
        view.setVisibility(View.GONE);
        L.e(TAG, "移除了：" + view.getClass().getSimpleName());
    }

    public static boolean isLastView() {
        return uiStackList.size() == 1;
    }

    public void addBackupView(View view) {
        backupViewUi.add(view);
    }

    public void clear() {
        uiStackList.clear();
        backupViewUi.clear();
        instance = null;
    }
}
