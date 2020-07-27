package com.game.sdk.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.result.GettedResultBean;
import com.game.sdk.log.T;
import com.game.sdk.util.MResource;

import java.util.ArrayList;
import java.util.List;

public class GiftGettedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<GettedResultBean.InfoBean.ListBean> gettedList;
    private static Context context;
    public int total; //总数

    public GiftGettedListAdapter(Context context, List<GettedResultBean.InfoBean.ListBean> list) {
        this.context = context;
        this.gettedList = new ArrayList<>();
//        this.gettedList = list;
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
        ((MyViewHolder) holder).item_ll_foot.setVisibility(View.GONE);
        ((MyViewHolder) holder).item_tv_name.setText(gettedList.get(position).getGift_name());
        ((MyViewHolder) holder).item_tv_instr.setText(gettedList.get(position).getGift_intro());
        ((MyViewHolder) holder).item_tv_term.setText("有效期至："+gettedList.get(position).getGift_term());
        if (gettedList.get(position).getGift_type() == SdkConstant.GIFT_MAKECODE){ // 0 通码
            ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_gift_makecode")));
            ((MyViewHolder) holder).ivStatus.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bt_copy_makecode")));
        }else if (gettedList.get(position).getGift_type() == SdkConstant.GIFT_UNIQUECODE){// 1 唯一码
            ((MyViewHolder) holder).item_rl.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bg_gift_uniquecode")));
            ((MyViewHolder) holder).ivStatus.setBackgroundResource(MResource.getIdByName(context, ("R.drawable.bt_copy_uniquecode")));
        }
        if (position == (total-1)){
            ((MyViewHolder) holder).item_ll_foot.setVisibility(View.VISIBLE);
        }
        ((MyViewHolder) holder).ivStatus.setImageResource(MResource.getIdByName(context, ("R.drawable.item_get")));
        //点击事件
        ((MyViewHolder) holder).ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", gettedList.get(position).getGift_code());
                cm.setPrimaryClip(mClipData);
                T.s(context, "礼包码复制成功！");

            }
        });
    }

    @Override
    public int getItemCount() {
        return gettedList.size();
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

}
