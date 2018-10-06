package com.qican.ifarmmanager.ui.sys;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.DeviceType;
import com.qican.ifarmmanager.bean.SysType;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

public class SysTypeListActivity extends ComListActivity<SysType> {

    List<SysType> mData;
    public static final String KEY_TYPE = "KEY_TYPE";

    String commond = "";

    @Override
    public String getUITitle() {
        return "选择系统类型";
    }

    @Override
    public void init() {

        commond = (String) myTool.getParam(String.class);

        mData = new ArrayList<>();
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

        OkHttpUtils.post().url(myTool.getServAdd() + "farmControlSystem/type")
                .addParams("managerId", myTool.getManagerId())
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

                        myTool.log("onResponse：" + response);
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
                                JSONObject o = arr.getJSONObject(i);
                                SysType type = new SysType();
                                type.setSystemCode(o.getString("systemCode"));
                                type.setSystemType(o.getString("systemType"));
                                type.setSystemTypeCode(o.getString("systemTypeCode"));
                                mData.add(type);
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
    public CommonAdapter<SysType> getAdapter() {
        return new CommonAdapter<SysType>(this, R.layout.item_device_type) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final SysType item, int position) {

                helper
                        .setText(R.id.tv_name, item.getSystemTypeCode())
                        .setText(R.id.tv_code, item.getSystemType());

                helper.setOnClickListener(R.id.rl_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.putExtra(KEY_TYPE, item);
                        setResult(RESULT_OK, intent);
                        finish();

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
}
