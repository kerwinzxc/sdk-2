package com.game.sdk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.sdk.SdkConstant;
import com.game.sdk.util.MResource;

public class AgreementActivity extends Activity implements View.OnClickListener{

    private TextView yun_sdk_tv_back, yun_sdk_tv_title;
    private ImageView yun_sdk_iv_cancel;
    private WebView webviewAgreement;
    private String getIntentString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this,
                MResource.LAYOUT, "yun_sdk_agreement"));
        setupUI();
    }

    private void setupUI() {
        yun_sdk_tv_back = (TextView)findViewById(MResource.getIdByName(this,"R.id.yun_sdk_tv_back"));
        yun_sdk_iv_cancel = (ImageView)findViewById(MResource.getIdByName(this,"R.id.yun_sdk_iv_cancel"));
        yun_sdk_tv_title = (TextView) findViewById(MResource.getIdByName(this,"R.id.yun_sdk_tv_title"));
        webviewAgreement = (WebView) findViewById(MResource.getIdByName(this,"R.id.webviewAgreement"));
        yun_sdk_tv_back.setOnClickListener(this);
        yun_sdk_iv_cancel.setOnClickListener(this);

        getIntentString = getIntent().getStringExtra(SdkConstant.YUN_AGREEMENT);
        if (getIntentString.equals(SdkConstant.YUN_REGISTER_AGREEMENT)){
            yun_sdk_tv_title.setText(getResources().getString(MResource.getIdByName(AgreementActivity.this,"R.string.agreement_register_agreement")));
            webviewAgreement.loadUrl(SdkConstant.URL_REGISTER_AGREEMENT);
        }else {
            yun_sdk_tv_title.setText(getResources().getString(MResource.getIdByName(AgreementActivity.this,"R.string.agreement_privacy_item")));
            webviewAgreement.loadUrl(SdkConstant.URL_PRIVACY_AGREEMENT);
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == yun_sdk_tv_back.getId() || view.getId() == yun_sdk_iv_cancel.getId() ) { //返回
            AgreementActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
