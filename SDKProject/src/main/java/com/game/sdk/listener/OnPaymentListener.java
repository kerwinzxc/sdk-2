package com.game.sdk.listener;

import com.game.sdk.domain.NotProguard;
import com.game.sdk.domain.result.QueryResultBean;

/**
 * author janecer 2014-3-29上午10:54:41 玩家充值金额的回调
 */
@NotProguard
public interface OnPaymentListener {

    /**
     * 充值成功回调，给用户一个简单提示，金币发放已通过后台服务器进行通知 目前易宝，与财付通充值没有消息回调 直接通过TTW后台服务器通知
     */
    void paymentSuccess(QueryResultBean queryResultBean);

    /**
     * 充值失败回调
     */
    void paymentError(QueryResultBean queryResultBean);
}
