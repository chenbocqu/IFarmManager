package com.qican.ifarmmanager.ui.infosysmanager;


import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.AcquisitorDevice;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.DeviceInfo;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.device.VerifyDeviceActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.utils.TimeUtils;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class AcquisitorListActivity extends ComListActivity<AcquisitorDevice> {

    @Override
    public String getUITitle() {
        return "采集设备";
    }

    @Override
    public void init() {

        setRightMenu("添加采集设备", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTool.startActivity(VerifyDeviceActivity.class);
            }
        });

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

        Map<String, String> map = new HashMap<>();
        map.put("managerId", myTool.getManagerId());
        map.put("token", myTool.getToken());
        map.put("userId", user.getId());

        showProgress();
        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "device/collectorDevice/query")
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showError();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log("res : " + response);
                        showContent();

                        if (response == null || response.length() == 0) {
                            showError();
                            return;
                        }

                        if (response.equals("lose efficacy")) {
                            myTool.showInfo("Token失效，请重新登陆！");
                            myTool.startActivity(LoginActivity.class);
                        }

                        try {
                            // 判断为array还是object
                            switch (response.charAt(0)) {
                                case '[':
                                    myDatas.clear();

                                    JSONArray arr = new JSONArray(response);
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = arr.getJSONObject(i);
                                        AcquisitorDevice device = new AcquisitorDevice();

                                        device.setType(obj.getString("deviceType"));
                                        device.setOrderNo(obj.getString("deviceOrderNo"));
                                        device.setCollectorId(obj.getString("collectorId"));
                                        device.setDistrict(obj.getString("deviceDistrict"));
                                        device.setFarmId(obj.getString("farmId"));
                                        device.setVersion(obj.getString("deviceVersion"));
                                        device.setCreatTime(obj.getString("deviceCreateTime"));
                                        device.setId(obj.getString("deviceId"));
                                        device.setLocation(obj.getString("deviceLocation"));
                                        myDatas.add(device);
                                    }

                                    replaceAll(myDatas);

                                    break;

                                case '{':

                                    JSONObject obj = new JSONObject(response);
                                    String res = obj.getString("response");
                                    if (res == null) {
                                        showError();
                                        return;
                                    }
                                    switch (res) {
                                        case "no_id":
                                            myTool.showInfo("没有ID，请选择！");
                                            break;
                                    }

                                    break;
                            }


                        } catch (JSONException e) {
                            myTool.showInfo(e.getMessage());
                            showError();
                        }
                    }
                });

    }

    @Override
    public CommonAdapter<AcquisitorDevice> getAdapter() {
        return new CommonAdapter<AcquisitorDevice>(this, R.layout.item_acquisitor_device, myDatas) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, AcquisitorDevice item, int position) {
                helper
                        .setText(R.id.tv_name, item.getType())
                        .setText(R.id.tv_time, TimeUtils.formatTime(item.getCreatTime()));
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
                myTool.showInfo("暂无更多信息！");
                l.loadMoreFinish(true);
            }
        }, 1500);
    }
}
