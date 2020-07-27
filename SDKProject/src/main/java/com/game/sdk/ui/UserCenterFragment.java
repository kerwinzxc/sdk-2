package com.game.sdk.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.YunsdkManager;
import com.game.sdk.util.MResource;
import com.game.sdk.util.SharedPreferencesUtil;

import static com.game.sdk.util.RegExpUtil.getStarMobile;

public class UserCenterFragment extends Fragment implements View.OnClickListener{

    private TextView tv_user_account, tv_user_verifify, tv_user_bind, tv_identified,
                    tv_version, tv_binded_mobile;
    private ImageView yun_sdk_iv_cancel;
    private RelativeLayout  rl_modify_pwd, rl_recharge_record,
            rl_mobile_authentication, rl_user_verify, rl_server_info, rl_screen;
    private LinearLayout ll_swith_account;

    private boolean userAuth;
    private String userBinded;
    private Fragment newFrag;
    private FragmentTransaction transaction;
    private YunLoginActivity loginActivity;
    private View view;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(MResource.getIdByName(getActivity(),
                MResource.LAYOUT, "yun_sdk_user_center"), container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userAuth = (Boolean) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_AUTH, false);
        userBinded = (String) SharedPreferencesUtil.getData(SdkConstant.YUN_SP_BIND_MOBILE, "");
        setupUI();
    }

    private void setupUI() {
        rl_screen = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_screen"));
//        rl_screen.getBackground().setAlpha(12);
        yun_sdk_iv_cancel = (ImageView)view.findViewById(MResource.getIdByName(getActivity(),"R.id.yun_sdk_iv_cancel"));
        tv_user_account = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_user_account"));
        tv_user_verifify = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_user_verifify"));
        tv_identified = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_identified"));
        rl_server_info = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_server_info"));
        ll_swith_account = (LinearLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.ll_swith_account"));
        tv_binded_mobile = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_binded_mobile"));
        tv_version = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_version"));
        tv_user_bind = (TextView) view.findViewById(MResource.getIdByName(getActivity(),"R.id.tv_user_bind"));
        rl_modify_pwd = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_modify_pwd"));
        rl_recharge_record = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_recharge_record"));
        rl_mobile_authentication = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_mobile_authentication"));
        rl_user_verify = (RelativeLayout) view.findViewById(MResource.getIdByName(getActivity(),"R.id.rl_user_verify"));

        tv_user_account.setText(getStarMobile(SharedPreferencesUtil.getUserName(getActivity())));
        tv_version.setText("版本号："+SdkConstant.YZ_SDK_VERSION);
        yun_sdk_iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, "");
                getActivity().finish();
            }
        });
        rl_modify_pwd.setOnClickListener(this);
        rl_recharge_record.setOnClickListener(this);
        rl_mobile_authentication.setOnClickListener(this);
        rl_user_verify.setOnClickListener(this);
        rl_server_info.setOnClickListener(this);
        ll_swith_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YunsdkManager.getInstance().switchAccount();
                getActivity().finish();
            }
        });

        if(userAuth){
            tv_identified.setText("已认证");
//            tv_identified.setVisibility(View.VISIBLE);
        }else {
            tv_identified.setText("去认证");
//            tv_identified.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userBinded) ){
            tv_user_bind.setText("换绑");
            tv_user_bind.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_black")) );
            tv_user_bind.setBackgroundResource(MResource.getIdByName(getActivity(),"R.drawable.yun_sdk_disable_bg") );
//            tv_user_bind.setCompoundDrawables(null,null,null,null);
        }else{
            tv_user_bind.setText("绑定");
            tv_user_bind.setTextColor(getActivity().getResources().getColor(MResource.getIdByName(getActivity(),"R.color.yun_sdk_title")) );
            tv_user_bind.setBackgroundResource( MResource.getIdByName(getActivity(),"R.drawable.yun_sdk_enable_bg") );
        }
        tv_binded_mobile.setText(userBinded);
    }

    @Override
    public void onClick(View view) {
        transaction = getFragmentManager().beginTransaction();
        if(view.getId() == rl_modify_pwd.getId()){
            newFrag = new ModifyPwdFragment();
            transaction.addToBackStack(null).replace(MResource.getIdByName(getActivity(),"R.id.flTabFragment"), newFrag);
            transaction.commit();
        }
//        弃除充值记录
//        else if(view.getId() == rl_recharge_record.getId()){
//            Intent intent = new Intent(getActivity(), RechargeRecordActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
        else if(view.getId() == rl_mobile_authentication.getId()){
              newFrag = new BindOrModifyMobileFragment();
              transaction.addToBackStack(null).replace(MResource.getIdByName(getActivity(),"R.id.flTabFragment"), newFrag);
              transaction.commit();
        } else if(view.getId() == rl_user_verify.getId()){
//            YunLoginActivity.start(getActivity(), YunLoginActivity.TYPE_AUTHENTICATION);
            if (userAuth){
                newFrag = new ReviewIdentifyFragment();
                transaction.addToBackStack(null).replace(MResource.getIdByName(getActivity(),"R.id.flTabFragment"), newFrag);
            }else {
                SharedPreferencesUtil.putData(SdkConstant.AUTHENTICTYPE, SdkConstant.AUTHENTICTYPE_USERCENTER);
                newFrag = new AuthenticFragment();
                Bundle args = new Bundle();
                args.putString("AUTHENTICType", "UserCenter");
                newFrag.setArguments(args);
                transaction.addToBackStack(null).replace(MResource.getIdByName(getActivity(),"R.id.flTabFragment"), newFrag);
            }
            transaction.commit();

        }else if(view.getId() == rl_server_info.getId()){
            newFrag = new ContactServerFragment();
            transaction.addToBackStack(null).replace(MResource.getIdByName(getActivity(),"R.id.flTabFragment"), newFrag);
            transaction.commit();
        }

    }
}
