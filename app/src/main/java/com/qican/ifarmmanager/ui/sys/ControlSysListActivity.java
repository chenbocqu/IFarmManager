package com.qican.ifarmmanager.ui.sys;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.ControlSys;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.utils.TimeUtils;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class ControlSysListActivity extends ComListActivity<ControlSys> {


    public static final String KEY_SYS = "KEY_SYS";
    String syscode = "";
    List<ControlSys> mData;

    @Override
    public String getUITitle() {
        return "选择控制系统";
    }

    @Override
    public void init() {

        syscode = (String) myTool.getParam(syscode);
        if ("".equals(syscode)) return;

        mData = new ArrayList<>();
        request();
    }

    private void request() {

        myTool.log("managerId：" + myTool.getManagerId());
        myTool.log("token：" + myTool.getToken());
        myTool.log("systemCode：" + syscode);

        ComUser user = myTool.getUserInfo();
        if (user == null) {
            myTool.showInfo("请先选择一个要管理的用户！");
            return;
        }
        showProgress();

        OkHttpUtils.post().url(myTool.getServAdd() + "manager/controlSystemList")
                .addParams("managerId", myTool.getManagerId())
                .addParams("userId", user.getId())
                .addParams("token", myTool.getToken())
                .addParams("systemCode", syscode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pullToRefreshLayout.refreshFinish(true);
                        showError();

                        myTool.showInfo(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log(response);

                        pullToRefreshLayout.refreshFinish(true);
                        showContent();

                        if (response == null) return;
                        if (response.equals("lose efficacy")) {
                            myTool.showInfo("Token失效，请重新登陆！");
                            myTool.startActivity(LoginActivity.class);
                        }

                        JSONArray array = null;
                        try {
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                ControlSys sys = new ControlSys();
                                if (obj.has("systemId"))
                                    sys.setSystemId(obj.getString("systemId"));

                                if (obj.has("farmId"))
                                    sys.setFarmId(obj.getString("farmId"));

                                if (obj.has("farmName"))
                                    sys.setFarmName(obj.getString("farmName"));

                                if (obj.has("systemCode"))
                                    sys.setSystemCode(obj.getString("systemCode"));

                                if (obj.has("systemType"))
                                    sys.setSystemType(obj.getString("systemType"));

                                if (obj.has("systemTypeCode"))
                                    sys.setSystemTypeCode(obj.getString("systemTypeCode"));

                                if (obj.has("systemDistrict"))
                                    sys.setSystemDistrict(obj.getString("systemDistrict"));

                                if (obj.has("systemDescription"))
                                    sys.setSystemDescription(obj.getString("systemDescription"));

                                if (obj.has("canNum"))
                                    sys.setCanNum(obj.getString("canNum"));

                                if (obj.has("districtSum"))
                                    sys.setDistrictSum(obj.getString("districtSum"));

                                if (obj.has("systemLocation"))
                                    sys.setSystemLocation(obj.getString("systemLocation"));

                                if (obj.has("systemCreateTime"))
                                    sys.setSystemCreateTime(obj.getString("systemCreateTime"));

                                if (obj.has("systemNo"))
                                    sys.setSystemNo(obj.getString("systemNo"));

                                myTool.log(sys.toString());
                                mData.add(sys);
                            }

                            notifyDatasetChanged();


                        } catch (JSONException e) {
                            myTool.showInfo(e.getMessage());
                        }
                    }
                });
    }

    @Override
    public CommonAdapter<ControlSys> getAdapter() {
        return new CommonAdapter<ControlSys>(this, R.layout.item_control_sys, mData) {

            @Override
            public void onUpdate(BaseAdapterHelper helper, final ControlSys item, int position) {

                ImageView ivHead = helper.getView(R.id.iv_img);
                helper
                        .setText(R.id.tv_name, item.getSystemType())
                        .setText(R.id.tv_time, TimeUtils.formatTime(item.getSystemCreateTime()))
                        .setText(R.id.tv_desc,
                                ("".equals(item.getSystemDescription()) ? item.getSystemDescription() : "暂无系统描述") +
                                        "（" + item.getFarmName() + item.getSystemDistrict() + "）");

                helper.getView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.putExtra(KEY_SYS, item);
                        setResult(RESULT_OK, intent);

                        finish();
                    }
                });
            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout l) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout l) {

    }
}
