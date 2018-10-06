package com.qican.ifarmmanager.ui.farm;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.ControlSys;
import com.qican.ifarmmanager.bean.Farm;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.ui.sys.ControlSysListActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.qican.ifarmmanager.ui.sys.ControlSysListActivity.KEY_SYS;

public class FarmListActivity extends ComListActivity<Farm> {

    private static final int REQUEST_FOR_SYS = 1;
    List<Farm> mData;
    public static final String KEY_FARM = "KEY_FARM";
    public static final String SYS_LIST = "SYS_LIST";

    String commond = "", key;

    @Override
    public String getUITitle() {
        return "农场";
    }

    @Override
    public void init() {

        commond = (String) myTool.getParam(String.class);

        mData = new ArrayList<>();
        setRightMenu("添加农场", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTool.startActivity(AddFarmActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {

        myTool.log("managerId：" + myTool.getManagerId());
        myTool.log("token：" + myTool.getToken());
        ComUser user = myTool.getUserInfo();
        if (user == null) {
            myTool.showInfo("请先选择一个要管理的用户！");
            return;
        }
        showProgress();

        OkHttpUtils.post().url(myTool.getServAdd() + "farm/manager/getFarmList")
                .addParams("managerId", myTool.getManagerId())
                .addParams("userId", user.getId())
                .addParams("token", myTool.getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.log("err : " + e.getClass().getName() + ", msg :" + e.getMessage());
                        pullToRefreshLayout.refreshFinish(true);
                        showError();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        pullToRefreshLayout.refreshFinish(true);
                        showContent();

                        if (response == null) return;
                        if (response.equals("lose efficacy")) {
                            myTool.showInfo("Token失效，请重新登陆！");
                            myTool.startActivity(LoginActivity.class);
                        }

                        try {

                            JSONArray arr = new JSONArray(response);

                            mData.clear();

                            for (int i = 0; i < arr.length(); i++) {

                                Farm farm = new Farm();
                                JSONObject obj = arr.getJSONObject(i);

                                farm.setId(obj.getString("farmId"));
                                farm.setName(obj.getString("farmName"));
                                farm.setTime(obj.getString("farmCreateTime"));

                                if (obj.has("farmLocation"))
                                    farm.setLocation(obj.getString("farmLocation"));
                                if (obj.has("farmDetailAddress"))
                                    farm.setDetialAddress(obj.getString("farmDetailAddress"));
                                if (obj.has("farmImageUrl"))
                                    farm.setImgUrl(obj.getString("farmImageUrl"));
                                if (obj.has("farmLabel"))
                                    farm.setLabels(obj.getString("farmLabel"));
                                if (obj.has("farmDescribe"))
                                    farm.setDesc(obj.getString("farmDescribe"));

                                mData.add(farm);

                            }

                            if (mData.isEmpty()) {
                                showNoData();
                                return;
                            }
                            replaceAll(mData);

                        } catch (JSONException e) {
                            myTool.showInfo(e.getMessage());
                            showError();
                        }
                    }

                });
    }

    @Override
    public CommonAdapter<Farm> getAdapter() {
        return new CommonAdapter<Farm>(this, R.layout.item_farm) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final Farm item, int position) {

                helper
                        .setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_desc, item.getDesc());

                if (item.getImgUrl() != null)
                    helper.setImageUrl(R.id.iv_pic, item.getImgUrl());

                helper.setOnClickListener(R.id.rl_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myTool.setFarm(item);

                        if (KEY_FARM.equals(commond)) {

                            Intent intent = new Intent();
                            intent.putExtra(KEY_FARM, item);
                            setResult(RESULT_OK, intent);

                            finish();
                        }

                        if (commond == null) {
                            finishDelay();
                            return;
                        }
                        if (commond.contains("-")) {

                            String infos[] = commond.split("-");

                            if (infos.length != 2) return;

                            key = infos[0];
                            String syscode = infos[1];

                            // 请求系统列表
                            if (SYS_LIST.equals(key)) {
                                myTool.startActivityForResult(syscode, ControlSysListActivity.class, REQUEST_FOR_SYS);
                            }

                        }

                    }
                });
            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout l) {
        requestData();
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout l) {
        l.postDelayed(new Runnable() {
            @Override
            public void run() {
                l.loadMoreFinish(true);
            }
        }, 1500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FOR_SYS:

                if (resultCode == RESULT_OK) {

                    ControlSys sys = (ControlSys) data.getSerializableExtra(KEY_SYS);

                    Intent intent = new Intent();
                    intent.putExtra(KEY_SYS, sys);
                    setResult(RESULT_OK, intent);

                    finish();
                }
                break;
        }
    }
}
