package com.qican.ifarmmanager.ui.collector;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Collector;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.device.VerifyDeviceActivity;
import com.qican.ifarmmanager.ui.infosysmanager.ControlDeviceListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.utils.TimeUtils;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class CollectorListActivity extends ComListActivity<Collector> {

    List<Collector> mData;
    public static final String KEY_COLLECTOR = "KEY_COLLECTOR";

    String commond = "";
    SweetAlertDialog mDig;

    @Override
    public String getUITitle() {
        return "集中器列表";
    }

    @Override
    public void init() {

        commond = (String) myTool.getParam(String.class);

        mData = new ArrayList<>();
        setRightMenu("添加集中器", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTool.startActivity(VerifyDeviceActivity.class);
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

        OkHttpUtils.post().url(myTool.getServAdd() + "device/concentrator/query")
                .params(map)
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
                                JSONObject obj = arr.getJSONObject(i);
                                Collector c = new Collector();

                                c.setId(obj.getString("collectorId"));

                                if (obj.has("collectorLocation"))
                                    c.setLocation(obj.getString("collectorLocation"));

                                if (obj.has("collectorVersion"))
                                    c.setVersion(obj.getString("collectorVersion"));

                                c.setType(obj.getString("collectorType"));
                                c.setCreateTime(obj.getString("collectorCreateTime"));

                                if (obj.has("farmId"))
                                    c.setFarmId(obj.getString("farmId"));

                                mData.add(c);

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
    public CommonAdapter<Collector> getAdapter() {
        return new CommonAdapter<Collector>(this, R.layout.item_control_device) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final Collector item, int position) {

                helper
                        .setText(R.id.tv_name, item.getType() + "（" + item.getId() + "）")
                        .setText(R.id.tv_desc, item.getLocation())
                        .setText(R.id.tv_time, TimeUtils.formatTime(item.getCreateTime()));

                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.putExtra(KEY_COLLECTOR, item);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });

                helper.setOnLongClickListener(R.id.ll_item, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        mDig = new SweetAlertDialog(CollectorListActivity.this, SweetAlertDialog.WARNING_TYPE);

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

    private void delete(final Collector item) {

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
        map.put("collectorId", item.getId());
        map.put("userId", user.getId());


        mDig
                .setTitleText("正在删除" + item.getType() + "...")
                .showCancelButton(false)
                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "device/concentrator/delete")
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

                                            mData.remove(item);
                                            replaceAll(mData);
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
                l.loadMoreFinish(true);
            }
        }, 1500);
    }
}
