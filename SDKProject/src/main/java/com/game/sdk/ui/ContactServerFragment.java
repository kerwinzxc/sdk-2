package com.game.sdk.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sdk.SdkConstant;
import com.game.sdk.log.T;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;

public class ContactServerFragment extends Fragment implements View.OnClickListener{

    private ImageView iv_copy ;
    private TextView yun_sdk_tv_back, tv_server_account, tv_time;
    private String account;
    private ClipboardManager cm;
    private ClipData mClipData;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "frag_contact_server"), container,false);
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
        yun_sdk_tv_back = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_tv_back"));
        tv_server_account = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.tv_server_account"));
        tv_time = (TextView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.tv_time"));
        iv_copy = (ImageView) view.findViewById(MResource.getIdByName(getActivity(), "R.id.iv_copy"));
        yun_sdk_tv_back.setOnClickListener(this);
        iv_copy.setOnClickListener(this);
        account = (String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SERVICE_QQ, "");
        tv_server_account.setText(account);
        tv_time.setText((String)SharedPreferencesUtil.getData(SdkConstant.YUN_SP_SERVICE_time, ""));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == yun_sdk_tv_back.getId()){
            getActivity().onBackPressed();
        } else if(view.getId() == iv_copy.getId()){
            cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            mClipData = ClipData.newPlainText("Label", account);
            cm.setPrimaryClip(mClipData);
            T.show(getContext(), "复制成功", 0);
        }
    }

}
