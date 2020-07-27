package com.game.sdk.listener;

import com.game.sdk.domain.NotProguard;


@NotProguard
public interface OnInitSdkListener {
    void initSuccess(String code,String msg);
    void initError(String code,String msg);
}
