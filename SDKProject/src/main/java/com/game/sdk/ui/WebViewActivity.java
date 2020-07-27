package com.game.sdk.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.YunsdkService;
import com.game.sdk.domain.CommonJsForWeb;
import com.game.sdk.domain.request.PayRequestBean;
import com.game.sdk.domain.request.QueryRequestBean;
import com.game.sdk.domain.result.PayinfoResultBean;
import com.game.sdk.domain.result.QueryResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnPaymentListener;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.util.BaseAppUtil;
import com.game.sdk.util.DialogUtil;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MD5;
import com.game.sdk.util.MResource;
import com.kymjs.rxvolley.RxVolley;

import java.io.IOException;
import java.util.HashMap;


public class WebViewActivity extends BaseActivity implements OnClickListener {
    public final static String URL = "url";
    private static final String TAG = WebViewActivity.class.getName();
    private WebView wv;
    private TextView yun_sdk_tv_wv_title;
    private ImageView iv_cancel;
    private String url;
    private RelativeLayout yun_sdk_rl_agreement;
    private Intent intent;
    private String web_style;
    private String sign = "";
    private String timestamp = "";
    private String url_suffix = "";
    private SharedPreferences sp;
    private String session = "";
    private String uid = "";
    private String order_code = "";
    private CommonJsForWeb commonJsForWeb;
    private Context mContext;
    private Button bt_back;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WebViewActivity.this;
        YunsdkManager.getInstance().removeFloatView();
        setContentView(MResource.getIdByName(getApplication(), "layout", "yun_sdk_activity_float_web"));
        initUI();
        InitStyle();
    }

    private void initUI() {
        wv = (WebView) findViewById(MResource.getIdByName(getApplication(),
                "R.id.yun_sdk_wv_content"));
        webviewInit();
        iv_cancel = (ImageView) findViewById(MResource.getIdByName(getApplication(), "R.id.yun_sdk_iv_cancel"));
        yun_sdk_tv_wv_title = (TextView) findViewById(MResource.getIdByName(getApplication(), "R.id.yun_sdk_tv_wv_title"));
        yun_sdk_rl_agreement = (RelativeLayout)findViewById(MResource.getIdByName(getApplication(), "R.id.yun_sdk_rl_agreement"));
        bt_back = (Button)findViewById(MResource.getIdByName(getApplication(), "R.id.bt_back"));

        iv_cancel.setOnClickListener(this);
        bt_back.setOnClickListener(this);

    }

    private void InitStyle() {
        intent = getIntent();

        sp = getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        session = sp.getString(SdkConstant.YUN_SP_SESSION, "");
        uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        Log.d(TAG,"uid：" + uid);
        if (intent != null) {

            web_style = intent.getStringExtra(SdkConstant.YUN_WEB_STYLE);
            Log.d(TAG,"状态：" + web_style);
            if(web_style.equals(SdkConstant.YUN_WEB_PAY_LINK)){
                String iopUrl = intent.getStringExtra(SdkConstant.YUN_WEB_STYLE_VALUE);
                Log.d("d", "iopUrl:"+ iopUrl);
                this.wv.loadUrl(iopUrl);
                if (iopUrl.indexOf("wxpay") != -1){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bt_back.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
                }

            }
//            else if (web_style.equals(SdkConstant.YUN_WEB_PAY))  {
//                yun_sdk_tv_wv_title.setText("充值");
//                Log.d(TAG,"生成订单成功：" + SdkConstant.YUN_WEB_PAY);
//                final PayRequestBean payRequestBean = (PayRequestBean) intent.getSerializableExtra(SdkConstant.YUN_WEB_PAY_INFO);
//                payRequestBean.setUid(uid+"");
//                payRequestBean.setSession(session);
//                payRequestBean.setMethod(SdkConstant.YUN_SDK_ORDER_ADD);
//
//                HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(payRequestBean));
//
//                HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PayinfoResultBean>(mContext) {
//                    @Override
//                    public void onDataSuccess(PayinfoResultBean data) {
//                        if (data != null) {
//                            Log.d(TAG,"生成订单成功：" + data.getMsg());
//                            //接口回调通知
//                            HashMap<String, String> map_p = new HashMap<String, String>();
//                            map_p.put("client_key", SdkConstant.YZ_CLIENTKEY);
//                            map_p.put("timestamp", payRequestBean.getTimestamp());
//                            map_p.put("uid", uid);
//                            map_p.put("session", session);
//                            Log.d(TAG,"~~~ uid ="+uid+" order_code "+order_code+" session "+session);
//
//                            order_code = data.getInfo().getOrderCode();
//                            if (order_code== null || order_code.equals("")) {
//                                T.showToast(mContext, mContext.getString(MResource.getIdByName(
//                                        getApplication(), "R.string.ssdk_order_code_info")));
//
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        WebViewActivity.this.finish();
//                                    }
//                                }, 2000);
//                                return;
//                            }
//                            Log.d(TAG,"~~~ uid ="+uid+" order_code "+order_code+" session "+session);
//                            sp.edit().putString("order_code",order_code).commit();
//                            map_p.put("order_code", order_code);
//
//                            try {
//                                sign = MD5.getSignature(map_p, SdkConstant.YZ_CLIENTSECRET);
//                            } catch (IOException e) {
//
//                            }
//
//                            url_suffix = "payment";
//
//                            url = SdkConstant.ROOT_URL + url_suffix + "?client_key=" + SdkConstant.YZ_CLIENTKEY //
//                                    + "&uid=" + uid + "&session=" + session + "&timestamp=" + payRequestBean.getTimestamp() //
//                                    + "&order_code=" + data.getInfo().getOrderCode() + "&sign=" + sign;
//
//                            L.e("URL = " + url);
//                            Log.d("d", "URL = " + url);
//
//                            WebViewActivity.this.wv.loadUrl(url);
//                        }
//                    }
//
//                    @Override
//                    public void onDataFailure(PayinfoResultBean data) {
//
//                    }
//                };
//
//                httpCallbackDecode.setShowTs(true);
//                httpCallbackDecode.setLoadingCancel(false);
//                httpCallbackDecode.setShowLoading(false);
//
//                if (payRequestBean.getUid() == null || payRequestBean.getUid().equals("")) {
//                    T.showToast(mContext, mContext.getString(MResource.getIdByName(
//                            getApplication(), "R.string.ssdk_uid_info")));
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            WebViewActivity.this.finish();
//                        }
//                    }, 2000);
//                    return;
//                }
//
//
//                if (payRequestBean.getSession() == null || payRequestBean.getSession().equals("")) {
//                    T.showToast(mContext, mContext.getString(MResource.getIdByName(
//                            getApplication(), "R.string.ssdk_session_info")));
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            WebViewActivity.this.finish();
//                        }
//                    }, 2000);
//                    return;
//                }
//
//                RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
//
//            }
            else if(web_style.equals(SdkConstant.YUN_AGREEMENT)){
                yun_sdk_rl_agreement.setVisibility(View.VISIBLE);
                String url = intent.getStringExtra(SdkConstant.YUN_WEB_STYLE_VALUE);
                if (url.contains("policy")){
                    yun_sdk_tv_wv_title.setText("隐私协议");
                }else if (url.contains("agreement")){
                    yun_sdk_tv_wv_title.setText("注册协议");
                }
                this.wv.loadUrl(url);

            }
        }
    }

    public void lodurl(final WebView webView, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    /**
     * 微付通支付结果回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (commonJsForWeb != null) {
            commonJsForWeb.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void webviewInit() {
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setAppCacheEnabled(false);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDefaultTextEncodingName("UTF-8");

        commonJsForWeb = new CommonJsForWeb(this);
        wv.addJavascriptInterface(commonJsForWeb, "yunsdk");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!DialogUtil.isShowing()) {
                    DialogUtil.showDialog(mContext, "正在加载...");
                }
                L.e("testWebview onPageStarted", "url=" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                L.e(TAG, "url=" + url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webviewCompat(wv);
                try {
                    DialogUtil.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                resend.sendToTarget();
            }

        });

        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if (!TextUtils.isEmpty(title)) {
//                    tv_charge_title.setText(title);
//                } else {
//                    if (TextUtils.isEmpty(mContext.title)) {
//                        tv_charge_title.setText("yunsdk");
//                    } else {
//                        tv_charge_title.setText(mContext.title);
//                    }
//                }
            }
        });
        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    T.s(mContext, "已开始下载，可在通知栏查看进度");
                    Intent intent = new Intent(mContext, YunsdkService.class);
                    intent.putExtra(YunsdkService.DOWNLOAD_APK_URL, url);
                    mContext.startService(intent);
                } catch (Exception e) {
                    Toast.makeText(mContext, "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        webviewCompat(wv);
    }

    @Override
    public void onClick(View v) {
//        if ( v.getId() == iv_return.getId()) {
//            String url = wv.getUrl();
//            L.e(TAG, "当前页面的url=" + url);
//            if (url != null && (url.toLowerCase().contains("user.html") ||//
//                    url.toLowerCase().contains("passport-forgot.html") ||//
//                    url.toLowerCase().contains("/payment-"))) {//是首页面，直接finish
//                finish();
//            } else {//其它则直接返回用户中心
//                WebBackForwardList webBackForwardList = wv.copyBackForwardList();
//                L.e(TAG, "历史网页数量：" + webBackForwardList.getSize());
//                if (webBackForwardList.getSize() > 1) {
//                    String index_url = webBackForwardList.getItemAtIndex(0).getUrl();
//                    wv.goBackOrForward(1 - webBackForwardList.getSize());
//                    L.e(TAG, "返回后加载的主页为：" + index_url);
//                } else {
//                    finish();
//                }
//            }
//        }

        if (v.getId() == iv_cancel.getId()) {
            this.finish();
        }else if (v.getId() == bt_back.getId()) {
            this.finish();
        }
    }

    /**
     * 一些版本特性操作，需要适配、
     *
     * @param mWebView webview
     * @date 6/3
     * @reason 在微蓝项目的时候遇到了 返回键 之后 wv显示错误信息
     */
    private void webviewCompat(WebView mWebView) {
        if (BaseAppUtil.isNetWorkConneted(mWebView.getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv != null) {
            wv.removeAllViews();
            wv.destroy();
            wv = null;
        }

        if (web_style.equals(SdkConstant.YUN_WEB_PAY_LINK)) {
            queryOrder();
        }

        if (commonJsForWeb != null) {
            commonJsForWeb.onDestory();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }


    private void queryOrder() {
        order_code = sp.getString("order_code", "");
        Log.d("orderCode",order_code);
        QueryRequestBean queryRequestBean = new QueryRequestBean();
        queryRequestBean.setMethod(SdkConstant.YUN_SDK_ORDER_STATUS_GET);
        queryRequestBean.setUid(uid);
        queryRequestBean.setOrderCode(order_code);
        queryRequestBean.setSession(session);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(queryRequestBean));
        Log.d("orderCode",GsonUtil.getGson().toJson(queryRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<QueryResultBean>(getApplicationContext()) {
            @Override
            public void onDataSuccess(QueryResultBean data) {
                L.e("查询成功：" + data.getMsg());
                OnPaymentListener onPaymentListener = YunsdkManager.getInstance().getPaymentListener();
                if (onPaymentListener != null) {
                    onPaymentListener.paymentSuccess(data);
                }
            }
            @Override
            public void onDataFailure(QueryResultBean data) {
                Log.d(TAG,"!!!!!!"+data.toString());
                OnPaymentListener onPaymentListener = YunsdkManager.getInstance().getPaymentListener();
                if (onPaymentListener != null) {
                    onPaymentListener.paymentError(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                Log.d(TAG,"!!!!!!"+msg);
//                T.showToast(mContext, msg);
            }
        };

        if (queryRequestBean.getUid() == null || queryRequestBean.getUid().equals("")) {
            T.s(mContext, mContext.getString(MResource.getIdByName(
                    getApplication(), "R.string.ssdk_uid_info")));
            return;
        }

        if (queryRequestBean.getSession() == null || queryRequestBean.getSession().equals("")) {
            T.s(mContext, mContext.getString(MResource.getIdByName(
                    getApplication(), "R.string.ssdk_session_info")));
            return;
        }

        if (queryRequestBean.getOrderCode() == null || queryRequestBean.getOrderCode().equals("")) {
            T.s(mContext, mContext.getString(MResource.getIdByName(
                    getApplication(), "R.string.ssdk_order_code_info")));
            return;
        }

        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        Log.d("Params",httpParamsBuild.getHttpParams().toString());
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
