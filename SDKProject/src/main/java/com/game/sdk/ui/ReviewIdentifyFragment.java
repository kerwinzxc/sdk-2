package com.game.sdk.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;

public class ReviewIdentifyFragment extends Fragment implements View.OnClickListener{

    private TextView yun_sdk_tv_back, tv_id_account, tv_idNum, tv_bind_phone;
    private String IDCARD, REALNAME, BIND_MOBILE;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "frag_review_identify"), container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setupUI() {
        IDCARD = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_IDCARD, "");
        REALNAME = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_REALNAME, "");
        BIND_MOBILE = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND_MOBILE, "");

        yun_sdk_tv_back = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_tv_back"));
        tv_id_account = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.tv_id_account"));
        tv_idNum = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.tv_idNum"));
        tv_bind_phone = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.tv_bind_phone"));
        yun_sdk_tv_back.setOnClickListener(this);
        tv_id_account.setText(REALNAME);
        tv_idNum.setText(IDCARD);
        tv_bind_phone.setText(BIND_MOBILE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == yun_sdk_tv_back.getId()){
            getActivity().onBackPressed();
        }
    }

}
