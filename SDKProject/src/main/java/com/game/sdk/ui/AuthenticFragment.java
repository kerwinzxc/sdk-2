package com.game.sdk.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.game.sdk.util.MResource;

public class AuthenticFragment extends Fragment implements View.OnClickListener{

    private ImageView yun_sdk_iv_cancel ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "frag_authentic"), container,false);
        setupUI();
        return view;
    }

    private void setupUI() {
        yun_sdk_iv_cancel = (ImageView) getActivity().findViewById(MResource.getIdByName(getActivity(), "R.id.yun_sdk_iv_cancel"));

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == yun_sdk_iv_cancel.getId()){
            getActivity().finish();
        }
    }

}
