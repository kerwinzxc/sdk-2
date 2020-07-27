package com.game.sdk.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.adapter.GiftGettedListAdapter;
import com.game.sdk.adapter.GiftListAdapter;
import com.game.sdk.domain.request.GiftGettedRequestBean;
import com.game.sdk.domain.request.GiftListRequestBean;
import com.game.sdk.domain.result.GettedResultBean;
import com.game.sdk.domain.result.GiftListResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.game.sdk.util.MResource;
import com.game.sdk.view.ViewStackManager;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class GiftBagFragment extends Fragment implements View.OnClickListener {

    private TextView tv_version, tvGettable, tvGetted;
    private ImageView yun_sdk_iv_cancel;
    private LinearLayout ll_default_page;

    private List<GiftListResultBean.InfoBean.ListBean> gettableList;
    private List<GettedResultBean.InfoBean.ListBean> gettedList;
    RecyclerView rvGettable, rvGetted;

    private GiftListAdapter gettableAdapter;
    private GiftGettedListAdapter gettedAdapter;
    private int GETTEDPAGE = 1;
    private int DEFAULTSIZE = 20;
    private int lastLoadDataItemPosition;
    private static GiftBagFragment giftBagFragment;
    public ViewStackManager viewStackManager;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "yun_sdk_gift_bag"), container,false);
        viewStackManager = ViewStackManager.getInstance(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }
        public static GiftBagFragment getInstance() {
        if (null == giftBagFragment) {
            giftBagFragment = new GiftBagFragment();
        }
        return giftBagFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setupUI() {
        ll_default_page = (LinearLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.ll_default_page"));
        yun_sdk_iv_cancel = (ImageView)view.findViewById(MResource.getIdByName(getActivity(),"R.id.yun_sdk_iv_cancel"));
        tv_version = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_version"));
        tvGetted = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tvGetted"));
        tvGettable = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tvGettable"));
        rvGettable = (RecyclerView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.RvGettable"));
        rvGetted = (RecyclerView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.RvGetted"));

        gettableList = new ArrayList();
        gettedList = new ArrayList<>();
        gettableAdapter = new GiftListAdapter(this, getContext(), gettableList);
        gettedAdapter = new GiftGettedListAdapter(getContext(), gettedList);
        tv_version.setText("版本号："+SdkConstant.YZ_SDK_VERSION);
        yun_sdk_iv_cancel.setOnClickListener(this);
        tvGetted.setOnClickListener(this);
        tvGettable.setOnClickListener(this);

        rvGettable.setAdapter(gettableAdapter);
        //rvGetted
        rvGetted.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE &&
                        lastLoadDataItemPosition == gettedAdapter.getItemCount() ){
                        requestGettedList(GETTEDPAGE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager){
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
                    int l = manager.findLastCompletelyVisibleItemPosition();
                    lastLoadDataItemPosition = firstVisibleItem+(l-firstVisibleItem)+1;
                }
            }
        });
        rvGetted.setAdapter(gettedAdapter);
        //默认进来是可领取状态
        setGettableStatus();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == tvGettable.getId()){ //点击可领取
            setGettableStatus();
        }else if(view.getId() == tvGetted.getId()){//点击已领取
            setGettedStatus();
        }else if(view.getId() == yun_sdk_iv_cancel.getId()){
            getActivity().finish();
        }
    }

    public void requestGetableList(){
        //数据清空 重新获取数据
        gettableList.clear();
        gettableAdapter.notifyDataSetChanged();
        ll_default_page.setVisibility(View.GONE);
        SharedPreferences sp = getContext().getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        final GiftListRequestBean giftListRequestBean = new GiftListRequestBean();
        giftListRequestBean.setMethod(SdkConstant.YUN_SDK_GIFT_LIST);
        giftListRequestBean.setUid(uid);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(giftListRequestBean));
        Log.d("d", "~~~list:"+httpParamsBuild.getHttpParams().getUrlParams().toString());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GiftListResultBean>(getContext()) {
            @Override
            public void onDataSuccess(GiftListResultBean data) {
                if (data != null) {
                    Log.d("d:",data.toString());
                    if(data.getInfo().getGift_list().size()<1){
                        //显示空白页面
                        ll_default_page.setVisibility(View.VISIBLE);
                        rvGettable.setVisibility(View.GONE);
                        return;
                    }
                    ll_default_page.setVisibility(View.GONE);
                    rvGettable.setVisibility(View.VISIBLE);
                    addGettableDatas(data.getInfo());
                    gettableAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onDataFailure(GiftListResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public void requestGettedList(int page){
        ll_default_page.setVisibility(View.GONE);
        SharedPreferences sp = getContext().getSharedPreferences(SdkConstant.YUN_SP_LABEL, Context.MODE_PRIVATE);
        String uid = sp.getString(SdkConstant.YUN_SP_UID, "");
        String session = sp.getString(SdkConstant.YUN_SP_SESSION, "");
        final GiftGettedRequestBean bean = new GiftGettedRequestBean();
        bean.setMethod(SdkConstant.YUN_USER_GIFT_LIST);
        bean.setUid(uid);
        bean.setSession(session);
        bean.setPage(String.valueOf(page));
        bean.setSize(String.valueOf(DEFAULTSIZE));
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(bean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GettedResultBean>(getContext()) {
            @Override
            public void onDataSuccess(GettedResultBean data) {
                if (data != null) {
                    Log.d("ddd:",data.getInfo().getList().size()+"");
                    if(Integer.parseInt(data.getInfo().getCount()) < 1){
                        //显示空白页面
                        ll_default_page.setVisibility(View.VISIBLE);
                        rvGetted.setVisibility(View.GONE);
                        return;
                    }
                    if (data.getInfo().getList().size()<1) return;
                    ll_default_page.setVisibility(View.GONE);
                    rvGetted.setVisibility(View.VISIBLE);
                    addGettedDatas(data.getInfo());
                    gettedAdapter.gettedList = gettedList;
                    GETTEDPAGE++;
                    gettedAdapter.total = Integer.parseInt(data.getInfo().getCount());
                    gettedAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onDataFailure(GettedResultBean data) {
                Log.d("onDataFailure:",data.getMsg());
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("加载中...");
        RxVolley.post(SdkConstant.BASE_URL, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void addGettableDatas(GiftListResultBean.InfoBean infoBean){
        for (int i = 0; i< infoBean.getGift_list().size(); i++){
            gettableList.add(infoBean.getGift_list().get(i));
        }
    }

    private void addGettedDatas(GettedResultBean.InfoBean infoBean){
        for (int i = 0; i< infoBean.getList().size(); i++){
            gettedList.add(infoBean.getList().get(i));
        }
    }

    public void setGettableStatus(){
        tvGettable.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_33")));
        tvGettable.setTextSize(17f);
        tvGettable.getPaint().setFakeBoldText(true);
        tvGetted.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_66")));
        tvGetted.setTextSize(15f);
        tvGetted.getPaint().setFakeBoldText(false);

        rvGettable.setVisibility(View.VISIBLE);
        rvGetted.setVisibility(View.GONE);

        requestGetableList();

    }

    private void setGettedStatus(){
        tvGettable.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_66")));
        tvGettable.setTextSize(15f);
        tvGettable.getPaint().setFakeBoldText(false);
        tvGetted.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_33")));
        tvGetted.setTextSize(17f);
        tvGetted.getPaint().setFakeBoldText(true);
        rvGetted.setVisibility(View.VISIBLE);
        rvGettable.setVisibility(View.GONE);

        //数据清空 重新获取数据
        gettedList.clear();
        GETTEDPAGE = 1;
        gettedAdapter.total = 0;
        gettedAdapter.gettedList = new ArrayList<>();
        gettedAdapter.notifyDataSetChanged();
        requestGettedList(GETTEDPAGE);
    }
}
