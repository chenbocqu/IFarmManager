package com.qican.ifarmmanager.ui.infosysmanager;


import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.AcquisitorDevice;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.DeviceInfo;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.collector.CollectorListActivity;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class AcquisitorListActivity extends ComListActivity<AcquisitorDevice> {
    SweetAlertDialog mDig;

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
            showNoData();
            return;
        }
        if (myTool.getFarm() == null) {
            myTool.showInfo("请先选择一个要管理的农场！");
            showNoData();
            return;
        }

        String farmId = "NULL";

        if (myTool.getFarm().getId() != null)
            farmId = myTool.getFarm().getId();

        myTool.log("farmId: " + farmId);

        Map<String, String> map = new HashMap<>();
        map.put("managerId", myTool.getManagerId());
        map.put("token", myTool.getToken());
        map.put("userId", user.getId());
        map.put("farmId", farmId);

        showProgress();
        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "device/collectorDevice/query")
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.log(e.getMessage());
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

                                        device.setName(obj.getString("deviceName"));
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
            public void onUpdate(BaseAdapterHelper helper, final AcquisitorDevice item, int position) {
                helper
                        .setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_desc, item.getLocation())
                        .setText(R.id.tv_time, TimeUtils.formatTime(item.getCreatTime()));

                helper.setOnLongClickListener(R.id.ll_item, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        mDig = new SweetAlertDialog(AcquisitorListActivity.this, SweetAlertDialog.WARNING_TYPE);

                        mDig
                                .setTitleText("是否删除该设备？")
                                .setConfirmText("确定")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        delete(item);
                                    }
                                })
                                .show();

                        return false;
                    }
                });
            }
        };
    }

    private void delete(final AcquisitorDevice item) {
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
        map.put("deviceId", item.getId());
        map.put("userId", user.getId());


        mDig
                .setTitleText("正在删除" + item.getType() + "...")
                .showCancelButton(false)
                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "device/collectorDevice/delete")
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDig
                                .setTitleText(e.getMessage())
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log("res : " + response);

                        if (response == null) {
                            mDig
                                    .setTitleText("response == null")
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
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
                                    break;

                                case '{':

                                    JSONObject obj = new JSONObject(response);
                                    String res = obj.getString("response");
                                    if (res == null) {
                                        mDig
                                                .setTitleText("res == null")
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        return;
                                    }

                                    switch (res) {
                                        case "success":

                                            mDig
                                                    .setTitleText("删除成功！")
                                                    .setConfirmText("确定")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            mDig.dismissWithAnimation();
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                            myDatas.remove(item);
                                            replaceAll(myDatas);
                                            break;

                                        case "error":
                                            mDig
                                                    .setTitleText("删除失败！")
                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                            break;
                                    }

                                    break;
                            }


                        } catch (JSONException e) {
                            myTool.showInfo(e.getMessage());
                        }
                    }
                });
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
