package com.game.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.request.PayGetLinkRequestBean;
import com.game.sdk.domain.request.PayRequestBean;
import com.game.sdk.domain.result.PayGetLinkResultBean;
import com.game.sdk.domain.result.PayinfoResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.ui.WebViewActivity;
import com.game.sdk.ui.YunPayActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;
import com.kymjs.rxvolley.RxVolley;


public class YunPayView extends FrameLayout implements View.OnClickListener {
    private YunPayActivity yunPayActivity;
    private ImageView payIvClose;
    private TextView payTvMoney, payTvGoodsName;
    private LinearLayout ll_alipay, ll_wechat;
//    private RecyclerView rvPayList;
    Handler handler = new Handler();
//    PayMethodAdapter payMethodAdapter;
    private String orderCode;
//    OkHttpClient client = new OkHttpClient();

    public YunPayView(Context context, Activity activity) {
        super(context);
        setupUI();
    }

    public YunPayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public YunPayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    private void setupUI() {
        yunPayActivity = (YunPayActivity) getContext();
        LayoutInflater.from(getContext()).inflate(MResource.getIdByName(getContext(),
                MResource.LAYOUT, "yun_sdk_pay"), this);

        payIvClose = (ImageView) findViewById(MResource.getIdByName(yunPayActivity, "R.id.pay_iv_close"));
        payTvMoney = (TextView) findViewById(MResource.getIdByName(yunPayActivity, "R.id.pay_tv_money"));
        payTvGoodsName = (TextView) findViewById(MResource.getIdByName(yunPayActivity, "R.id.pay_tv_goods_name"));
        ll_alipay = (LinearLayout) findViewById(MResource.getIdByName(yunPayActivity, "R.id.ll_alipay"));
        ll_wechat = (LinearLayout) findViewById(MResource.getIdByName(yunPayActivity, "R.id.ll_wechat"));
//        rvPayList = (RecyclerView) findViewById(MResource.getIdByName(yunPayActivity, "R.id.rv_pay_list"));

        payIvClose.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(yunPayActivity, 2);
//        rvPayList.setLayoutManager(layoutManager);//线性

        charge();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == payIvClose.getId()) { //关闭
            yunPayActivity.callBackFinish();
        }else if (view.getId() == ll_alipay.getId()) {
            chargeLink("1");
        }else if (view.getId() == ll_wechat.getId()) {
            chargeLink("3");
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 自动设置相应的布局尺寸
//        if (getChildCount() > 0) {
//            View childAt = getChildAt(0);
//            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
//            layoutParams.leftMargin = (int)
//                    (getResources().getDimension(MResource.getIdByName(yunPayActivity,
//                            "R.dimen.yun_sdk_activity_horizontal_margin")));
//            layoutParams.rightMargin = layoutParams.leftMargin;
//        }
    }

    private void charge() {
        PayRequestBean payRequestBean = (PayRequestBean) yunPayActivity.getIntent().getSerializableExtra(SdkConstant.YUN_WEB_PAY_INFO);
        payRequestBean.setUid((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, ""));
        payRequestBean.setSession((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, ""));
        payRequestBean.setMethod(SdkConstant.YUN_SDK_ORDER_ADD);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(payRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PayinfoResultBean>(yunPayActivity) {
            @Override
            public void onDataSuccess(PayinfoResultBean data) {
                if (data != null) {
//                    payMethodAdapter = new PayMethodAdapter(data.getInfo().getOthers());
//                    rvPayList.setAdapter(payMethodAdapter);

                    payTvMoney.setText(data.getInfo().getPayFee()+"元");
                    payTvGoodsName.setText(data.getInfo().getDesc());
                    orderCode = data.getInfo().getOrderCode();
                    Log.d("orderCode", orderCode);
                    SharedPreferences sp = yunPayActivity.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
                    sp.edit().putString("order_code", orderCode).commit();
                }
            }

            @Override
            public void onDataFailure(PayinfoResultBean data) {
//                T.showToast(yunPayActivity, data.getMsg());

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);

        if (payRequestBean.getUid() == null || payRequestBean.getUid().equals("")) {
            T.showToast(yunPayActivity, yunPayActivity.getString(MResource.getIdByName(yunPayActivity, "R.string.ssdk_uid_info")));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yunPayActivity.finish();
                }
            }, 2000);
            return;
        }


        if (payRequestBean.getSession() == null || payRequestBean.getSession().equals("")) {
            T.showToast(yunPayActivity, yunPayActivity.getString(MResource.getIdByName(yunPayActivity, "R.string.ssdk_session_info")));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yunPayActivity.finish();
                }
            }, 2000);
            return;
        }

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


//    public class PayMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        private List<PayinfoResultBean.InfoBean.OthersBean> list;
//
//        public PayMethodAdapter(List<PayinfoResultBean.InfoBean.OthersBean> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View v = yunPayActivity.getLayoutInflater().inflate(MResource.getIdByName(yunPayActivity, "R.layout.item_pay_method"), null, false);
//            RecyclerView.ViewHolder holder = new MyViewHolder(v);
//            return holder;
//        }
//
//        Bitmap bitmap;
//        ImageView imageView;
//        private static final int COMPLETED = 0;
//        private Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == COMPLETED) {
//                    imageView.setImageBitmap(bitmap); //UI更改操作
//                }
//            }
//        };
//
//        @Override
//        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
//            Request request = new Request.Builder()
//                    .url(list.get(position).getImg())
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    InputStream inputStream = response.body().byteStream();
//                    imageView = ((MyViewHolder) holder).ivPay;
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    yunPayActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((MyViewHolder) holder).ivPay.setImageBitmap(bitmap);
//                        }
//                    });
//
////
////                    Message message = new Message();
////                    message.what = COMPLETED;
////                    handler.sendMessage(message);
//                }
//
//            });
//
////            Glide.with(yunPayActivity)
////                    .load()
////                    .into( ((MyViewHolder) holder).ivPay);
//            ((MyViewHolder) holder).ivPay.setOnClickListener(
//                    new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            chargeLink(list.get(position).getType());
//                        }
//                    });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        public ImageView ivPay;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            ivPay = itemView.findViewById(MResource.getIdByName(yunPayActivity, "R.id.item_iv_pay"));
//        }
//
//    }


    private void chargeLink(String type) {
        final PayGetLinkRequestBean payRequestBean = new PayGetLinkRequestBean();
        payRequestBean.setUid((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_UID, ""));
        payRequestBean.setSession((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SESSION, ""));
        payRequestBean.setOrder_code(orderCode);
        payRequestBean.setType(type);
        payRequestBean.setMethod(SdkConstant.YUN_SDK_ORDER_GET_LINK);

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(payRequestBean));

        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PayGetLinkResultBean>(yunPayActivity) {
            @Override
            public void onDataSuccess(PayGetLinkResultBean data) {
                if (data != null) {
                    if (data.getCode() == 1) {
                        yunPayActivity.callBackFinish();
                        Intent intent = new Intent(yunPayActivity, WebViewActivity.class);
                        intent.putExtra(SdkConstant.YUN_WEB_STYLE, SdkConstant.YUN_WEB_PAY_LINK);

                        intent.putExtra(SdkConstant.YUN_WEB_STYLE_VALUE, data.getInfo().getIop());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        yunPayActivity.startActivity(intent);
                    }
                }
            }

            @Override
            public void onDataFailure(PayGetLinkResultBean data) {
//                T.showToast(yunPayActivity, data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);

        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
