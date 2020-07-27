package com.game.sdk.floatwindow;


import android.app.Activity;

public interface IFloatView {

    // 移除悬浮窗口
    void removeFloat();

    // 显示悬浮窗口
    void showFloat(Activity activity);

    // 移除悬浮窗口
    void hidFloat();
    void openucenter();
}
