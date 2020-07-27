//package com.game.sdk.ui;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.OrientationHelper;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.game.sdk.SdkConstant;
//import com.game.sdk.domain.request.OrderListRequestBean;
//import com.game.sdk.domain.result.RechargeRecordResultBean;
//import com.game.sdk.http.HttpCallbackDecode;
//import com.game.sdk.http.HttpParamsBuild;
//import com.game.sdk.log.T;
//import com.game.sdk.util.GsonUtil;
//import com.game.sdk.util.MResource;
//import com.kymjs.rxvolley.RxVolley;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
//
//public class RechargeRecordActivity extends Activity implements View.OnClickListener{
//
//    private TextView yun_sdk_tv_back;
//    private ImageView yun_sdk_iv_cancel;
//     RecyclerView rvOrder;
//    private static final String TAG = RechargeRecordActivity.class.getSimpleName();
//    private int PAGE = 1;
//    private List<RechargeRecordResultBean.InfoBean.ListBean> orderList ;
//    private RechargeRecordAdapter adapter;
//    private int lastLoadDataItemPosition;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(MResource.getIdByName(this,
//                MResource.LAYOUT, "activity_recharge_record"));
//        setupUI();
//    }
//
//
//    private void setupUI() {
//        orderList = new ArrayList();
//        yun_sdk_tv_back = (TextView)findViewById(MResource.getIdByName(this,"R.id.yun_sdk_tv_back"));
//        yun_sdk_iv_cancel = (ImageView) findViewById(MResource.getIdByName(this,"R.id.yun_sdk_iv_cancel"));
//        rvOrder = (RecyclerView) findViewById(MResource.getIdByName(this,"R.id.rv_order_list"));
//
//        yun_sdk_tv_back.setOnClickListener(this);
//        yun_sdk_iv_cancel.setOnClickListener(this);
//
//        requestList(PAGE);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        rvOrder.setLayoutManager(layoutManager);//线性
//        ((LinearLayoutManager) layoutManager).setOrientation(OrientationHelper.VERTICAL);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        rvOrder.addItemDecoration(dividerItemDecoration);
//        rvOrder.setHasFixedSize(true);
//
//        adapter = new RechargeRecordAdapter(orderList);
//
//        rvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (newState == SCROLL_STATE_IDLE &&
//                        lastLoadDataItemPosition == adapter.getItemCount() ){
//                    requestList(PAGE);
//
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                if (layoutManager instanceof LinearLayoutManager){
//                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
//                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
//                    int l = manager.findLastCompletelyVisibleItemPosition();
//                    lastLoadDataItemPosition = firstVisibleItem+(l-firstVisibleItem)+1;
//                }
//            }
//        });
//        rvOrder.setAdapter(adapter);
//
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == yun_sdk_tv_back.getId() || view.getId() == yun_sdk_iv_cancel.getId()) {// 返回
//            RechargeRecordActivity.this.finish();
//        }
//    }
//
//    private void addDatas(RechargeRecordResultBean.InfoBean infoBean){
//        for (int i = 0; i< infoBean.getList().size(); i++){
//            orderList.add(infoBean.getList().get(i));
//        }
//    }
//
//    private void requestList(int page){
//
//        SharedPreferences sp = RechargeRecordActivity.this.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
//        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
//        String session = sp.getString(SdkConstant.YUN_SP_SESSION, "");
//
//        final OrderListRequestBean orderListRequestBean = new OrderListRequestBean();
//        orderListRequestBean.setMethod(SdkConstant.YUN_SDK_ORDER_LIST);
//        orderListRequestBean.setUid(uid);
//        orderListRequestBean.setSession(session);
//        orderListRequestBean.setPage(String.valueOf(page));
//
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(orderListRequestBean));
//
//        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RechargeRecordResultBean>(RechargeRecordActivity.this) {
//            @Override
//            public void onDataSuccess(RechargeRecordResultBean data) {
//                if (data != null) {
//                    Log.d(TAG,data.toString());
//                    if(data.getInfo().getList().size()<1){
//                        T.s(RechargeRecordActivity.this, "无更多数据");
//                        return;
//                    }
//                    PAGE++;
//                    addDatas(data.getInfo());
//                    adapter.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onDataFailure(RechargeRecordResultBean data) {
////                T.showToast(RechargeRecordActivity.this, "Failure: "+data.getMsg());
//            }
//        };
//        httpCallbackDecode.setShowTs(true);
//        httpCallbackDecode.setLoadingCancel(false);
//        httpCallbackDecode.setShowLoading(false);
//        httpCallbackDecode.setLoadMsg("加载中...");
//
//        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//
//    public class RechargeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        private List<RechargeRecordResultBean.InfoBean.ListBean> list;
//
//        public RechargeRecordAdapter(List<RechargeRecordResultBean.InfoBean.ListBean> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View v = getLayoutInflater().inflate(MResource.getIdByName(RechargeRecordActivity.this, "R.layout.item_recharge_record"), null, false);
//            RecyclerView.ViewHolder holder = new MyViewHolder(v);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//            ((MyViewHolder) holder).appName.setText(list.get(position).getApp_name());
//            ((MyViewHolder) holder).payedFee.setText(list.get(position).getPayed_fee());
//            ((MyViewHolder) holder).orderCode.setText(list.get(position).getCode());
//            ((MyViewHolder) holder).orderCreated.setText(list.get(position).getCreated());
//            ((MyViewHolder) holder).orderPayment.setText(list.get(position).getPayment());
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
//        public TextView appName, payedFee, orderCode, orderCreated, orderPayment;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            appName = itemView.findViewById(MResource.getIdByName(RechargeRecordActivity.this,"R.id.item_tv_app_name"));//游戏名
//            payedFee = itemView.findViewById(MResource.getIdByName(RechargeRecordActivity.this,"R.id.item_tv_payed_fee"));//支付金额
//            orderCode = itemView.findViewById(MResource.getIdByName(RechargeRecordActivity.this,"R.id.item_tv_order_code"));//订单号
//            orderCreated = itemView.findViewById(MResource.getIdByName(RechargeRecordActivity.this,"R.id.item_tv_created"));//创建时间
//            orderPayment = itemView.findViewById(MResource.getIdByName(RechargeRecordActivity.this,"R.id.item_tv_payment"));//创建时间
//        }
//    }
//
//}
