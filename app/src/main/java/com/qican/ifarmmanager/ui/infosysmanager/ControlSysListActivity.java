package com.qican.ifarmmanager.ui.infosysmanager;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComSys;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.ui.sys.AddControlSysActivity;
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

public class ControlSysListActivity extends ComListActivity<ComSys> {

    public final static String KEY_CONTROL_SYS = "KEY_CONTROL_SYS";
    public final static String REQUEST_FOR_SYS = "REQUEST_FOR_SYS";
    String commond;

    @Override
    public String getUITitle() {
        return "控制系统";
    }

    @Override
    public void init() {

        commond = (String) myTool.getParam(String.class);

        setRightMenu("添加控制系统", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTool.startActivity(AddControlSysActivity.class);
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
        OkHttpUtils.post().url(myTool.getServAdd() + "farmControlSystem/query")
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
                                        ComSys sys = new ComSys();

                                        sys.setLocation(obj.getString("systemLocation"));
                                        sys.setId(obj.getString("systemId"));
                                        sys.setDesc(obj.getString("systemDescription"));
                                        sys.setFarmId(obj.getString("farmId"));
                                        sys.setTypeCode(obj.getString("systemTypeCode"));

                                        if (obj.has("medicineNum"))
                                            sys.setMedicineNum(obj.getInt("medicineNum"));
                                        if (obj.has("districtNum"))
                                            sys.setDistrictNum(obj.getInt("districtNum"));
                                        if (obj.has("fertierNum"))
                                            sys.setFertierNum(obj.getInt("fertierNum"));

                                        sys.setDistrict(obj.getString("systemDistrict"));
                                        sys.setCode(obj.getString("systemCode"));
                                        sys.setType(obj.getString("systemType"));
                                        sys.setNo(obj.getString("systemNo"));
                                        sys.setCreateTime(obj.getString("systemCreateTime"));

                                        myDatas.add(sys);
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
    public CommonAdapter<ComSys> getAdapter() {
        return new CommonAdapter<ComSys>(this, R.layout.item_acquisitor_device, myDatas) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final ComSys item, int position) {
                String no = item.getNo();
                helper
                        .setText(R.id.tv_name, no)
                        .setText(R.id.tv_desc, item.getDesc())
                        .setText(R.id.tv_time, TimeUtils.formatTime(item.getCreateTime()));

                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (REQUEST_FOR_SYS.equals(commond)) {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_CONTROL_SYS, item);
                            setResult(RESULT_OK, intent);
                            finish();
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
                myTool.showInfo("暂无更多信息！");
                l.loadMoreFinish(true);
            }
        }, 1500);
    }
}
