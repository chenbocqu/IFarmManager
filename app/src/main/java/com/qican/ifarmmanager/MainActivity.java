package com.qican.ifarmmanager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.device.VerifyDeviceActivity;
import com.qican.ifarmmanager.ui.farm.FarmListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.ui.produce.DeviceProduceActivity;
import com.qican.ifarmmanager.ui.qrcode.ScanActivity;
import com.qican.ifarmmanager.ui.setting.IFMSettingActivity;
import com.qican.ifarmmanager.ui.setting.SettingActivity;
import com.qican.ifarmmanager.ui.users.ChooseUserActivity;
import com.qican.ifarmmanager.utils.CommonTools;

public class MainActivity extends TitleBarActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String FROM_HOME_PAGE = "from_home_page";
    CommonTools myTool;
    SwipeRefreshLayout srl;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        myTool = new CommonTools(this);

        srl = findViewById(R.id.srl);

        myTool.setHeightByWindow(findViewById(R.id.ll_control_group1), 1 / 3f);
        myTool.setHeightByWindow(findViewById(R.id.ll_control_group2), 1 / 3f);
        myTool.setHeightByWindow(findViewById(R.id.ll_control_group3), 1 / 3f);
        myTool.setHeightByWindow(findViewById(R.id.ll_control_group4), 1 / 3f);
    }

    private void initEvent() {
        findViewById(R.id.ll_produce).setOnClickListener(this);
        findViewById(R.id.ll_icon_menu).setOnClickListener(this);
        findViewById(R.id.ll_user_manager).setOnClickListener(this);
        srl.setOnRefreshListener(this);
        registerClickListener(R.id.ll_menu);
        registerClickListener(R.id.ll_choose_user);
        registerClickListener(R.id.ll_farm);
        registerClickListener(R.id.ll_jizhongqing);
        registerClickListener(R.id.ll_ifm_setting);
    }

    private void initData() {
        setText(R.id.tv_menu, "登录");
//        findViewById(R.id.ll_icon_menu).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.llBack:
                finish();
                break;

            case R.id.ll_choose_user:
            case R.id.ll_user_manager:
                myTool.startActivity(ChooseUserActivity.class);
                break;

            case R.id.ll_produce:
//                myTool.startActivity(ProduceActivity.class);
//                myTool.startActivity(CategoryListActivity.class);
                myTool.startActivity(DeviceProduceActivity.class);
                break;
            case R.id.ll_menu:
                myTool.startActivity(LoginActivity.class);
                break;
            case R.id.ll_icon_menu:
                myTool.startActivity(FROM_HOME_PAGE, ScanActivity.class);
                break;
            case R.id.ll_setting:
                myTool.startActivity(SettingActivity.class);
                break;
            case R.id.ll_farm:
                myTool.startActivity(FarmListActivity.class);
                break;

            // 添加集中器
            case R.id.ll_jizhongqing:
                myTool.startActivity(VerifyDeviceActivity.class);
                break;

            case R.id.ll_ifm_setting:
                myTool.startActivity(IFMSettingActivity.class);
                break;
        }

    }

    @Override
    public void init() {
        initView();
        initData();
        initEvent();
        registerClickListener(R.id.ll_setting);
    }

    @Override
    public void onRefresh() {
        srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
            }
        }, 1500);
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.ll_icon_menu).setVisibility(myTool.isLogin() ? View.VISIBLE : View.GONE);
        findViewById(R.id.ll_menu).setVisibility(myTool.isLogin() ? View.GONE : View.VISIBLE);

        // 设置用户信息
        ComUser user = myTool.getUserInfo();
        if (user != null) {
            setText(R.id.tv_userid, user.getNickName() + "(" + user.getId() + ")");
        }
    }
}
