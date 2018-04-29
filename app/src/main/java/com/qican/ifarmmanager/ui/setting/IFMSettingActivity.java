package com.qican.ifarmmanager.ui.setting;

import android.view.View;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.ui.base.BaseActivityWithTitlebar;


public class IFMSettingActivity extends BaseActivityWithTitlebar {

    IpModifyDialog ipModifyDialog;

    @Override
    public String getUITitle() {
        return "设置";
    }

    @Override
    public void init() {

        ipModifyDialog = new IpModifyDialog(this, R.style.Translucent_NoTitle);

        findViewById(R.id.rl_ipaddress).setOnClickListener(this);
        findViewById(R.id.rl_about).setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_ifm_setting;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.rl_ipaddress:
                ipModifyDialog.show();
                break;

            case R.id.rl_about:
                myTool.showInfo("about");
//                myTool.startActivity(AboutActivity.class);
                break;
        }
    }
}
