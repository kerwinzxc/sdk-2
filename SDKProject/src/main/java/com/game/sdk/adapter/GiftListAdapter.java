package com.game.sdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.request.GiftGetRequestBean;
import com.game.sdk.domain.result.BaseResultBean;
import com.game.sdk.domain.result.GiftListResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.ui.GiftBagFragment;
import com.game.sdk.ui.YunBindMobileActivity;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;
import com.game.sdk.view.YunAuthenticationView;
import com.game.sdk.view.YunBindMobileView;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;
import java.util.List;

public class GiftListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GiftListResultBean.InfoBean.ListBean> gettableList;
    private static Context context;
    private GiftBagFragment fragment;

    public GiftListAdapter(GiftBagFragment fragment, Context context, List<GiftListResultBean.InfoBean.ListBean> list) {
        this.fragment = fragment;
        this.context = context;
        this.gettableList = new ArrayList<>();
        this.gettableList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context, MResource.getIdByName(context, "R.layout.item_gift_bag"), null);
        RecyclerView.ViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).item_tv_name.setText(gettableList.get(position).getName());
        ((MyViewHolder) holder).item_tv_instr.setText(gettableList.get(position).getInstr());
        ((MyViewHolder) holder).item_tv_term.setText("有效期至："+gettableList.get(position).getTerm());
        ((MyViewHolder) holder).item_ll_foot.setVisibility(View.GONE);
        if (SdkConstant.GIFT_NOTGET.equals(gettableList.get(position).getIs_get()) ){//0 未领取状态
            if (gettableList.get(position).getType() == SdkConstant.GIFT_MAKECODE){ // 0 通码
                ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_gift_makecode")));
                ((MyViewHolder) holder).ivStatus.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.button_makecode")));
            }else if (gettableList.get(position).getType() == SdkConstant.GIFT_UNIQUECODE){// 1 唯一码
                ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_gift_uniquecode")));
                ((MyViewHolder) holder).ivStatus.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.button_uniquecode")));
            }
        }else if(SdkConstant.GIFT_GETTED.equals(gettableList.get(position).getIs_get())){//1 已领取状态
            if (gettableList.get(position).getType() == SdkConstant.GIFT_MAKECODE){ // 0 通码
                ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_makecode_grey")));
            }else if (gettableList.get(position).getType() == SdkConstant.GIFT_UNIQUECODE){// 1 唯一码
                ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_uniquecode_grey")));
            }
            ((MyViewHolder) holder).ivStatus.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.button_getted")));
        }
        if (position == gettableList.size()-1){
            ((MyViewHolder) holder).item_ll_foot.setVisibility(View.VISIBLE);
        }
        ((MyViewHolder) holder).ivStatus.setImageResource(MResource.getIdByName(context, ("R.drawable.item_get")));
        //点击事件
        ((MyViewHolder) holder).ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SdkConstant.GIFT_NOTGET.equals(gettableList.get(position).getIs_get())){ //未领取状态才可点击
                    //判断是否绑定过手机
                    boolean bind = (boolean)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND, false);
                    if (bind){
                        requestGiftGet(gettableList.get(position).getId());
                    }else {
                        SharedPreferencesUtil.putData(SdkConstant.BINDMOBILETYPE, SdkConstant.BINDMOBILETYPE_USERCENTER);
                        Intent mIntent = new Intent(context, YunBindMobileActivity.class);
                        context.startActivity(mIntent);
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gettableList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_tv_name, item_tv_instr, item_tv_term;
        private ImageView ivStatus;
        private RelativeLayout item_rl;
        private LinearLayout item_ll_foot;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_tv_name = itemView.findViewById(MResource.getIdByName(context,"R.id.item_tv_name"));
            item_tv_instr = itemView.findViewById(MResource.getIdByName(context,"R.id.item_tv_instr"));
            item_tv_term = itemView.findViewById(MResource.getIdByName(context,"R.id.item_tv_term"));
            ivStatus = itemView.findViewById(MResource.getIdByName(context,"R.id.ivStatus"));
            item_rl = itemView.findViewById(MResource.getIdByName(context, "R.id.item_rl"));
            item_ll_foot = itemView.findViewById(MResource.getIdByName(context,"R.id.item_ll_foot"));
        }
    }

    private void requestGiftGet(String giftId){
        SharedPreferences sp = context.getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        String session = sp.getString(SdkConstant.YUN_SP_SESSION, "");

        final GiftGetRequestBean giftGetRequestBean = new GiftGetRequestBean();
        giftGetRequestBean.setMethod(SdkConstant.YUN_SDK_GIFT_GET);
        giftGetRequestBean.setUid(uid);
        giftGetRequestBean.setSession(session);
        giftGetRequestBean.setGift_id(giftId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(giftGetRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BaseResultBean>(context) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                if (data != null) {
                    Log.d("d:",data.toString());
                    if(data.getMsg().equals("success")){
                        T.s(context, "领取成功！请到已领取菜单复制礼包码！");
                        fragment.requestGetableList();
                    }
                }
            }
            @Override
            public void onDataFailure(BaseResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


}
