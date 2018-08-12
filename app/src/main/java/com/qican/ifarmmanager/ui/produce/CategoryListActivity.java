package com.qican.ifarmmanager.ui.produce;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Category;
import com.qican.ifarmmanager.bean.DeviceCategory;
import com.qican.ifarmmanager.bean.DeviceType;
import com.qican.ifarmmanager.bean.ProducePara;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.qican.ifarmmanager.ui.produce.DeviceProduceActivity.KEY_TYPE;
import static com.qican.ifarmmanager.ui.produce.DeviceProduceActivity.REQUEST_FOR_TYPE;

public class CategoryListActivity extends ComListActivity<DeviceCategory> {

    List<DeviceCategory> mData;

    @Override
    public String getUITitle() {
        return "选择类别";
    }

    @Override
    public void init() {
        mData = new ArrayList<>();
        requestData();
    }

    private void requestData() {

        String url = myTool.getServAdd() + "device/category";
        showProgress();

        // 刷新数据
        OkHttpUtils.post().url(url)
                .addParams("userId", myTool.getManagerId())
                .addParams("signature", myTool.getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        myTool.log(e.getMessage());
                        showError();

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log("选择类型 response : " + response);

                        // 出错的几种情况
                        if (response == null || response.length() == 0 || '[' != response.charAt(0)) {
                            showError();
                            return;
                        }

                        showContent();

                        // 解析Json
                        try {
                            JSONArray array = new JSONArray(response);

                            if (array.length() != 0) mData.clear();

                            for (int i = 0; i < array.length(); i++) {
                                // 一个Category
                                DeviceCategory foo = new DeviceCategory();

                                JSONObject obj = array.getJSONObject(i);

                                if (obj.has("deviceType"))
                                    foo.setType(obj.getString("deviceType"));
                                if (obj.has("deviceTypeCode"))
                                    foo.setTypeCode(obj.getString("deviceTypeCode"));
                                if (obj.has("deviceDescription"))
                                    foo.setDesc(obj.getString("deviceDescription"));
                                if (obj.has("deviceCode"))
                                    foo.setCode(obj.getString("deviceCode"));
                                if (obj.has("deviceName"))
                                    foo.setName(obj.getString("deviceName"));

                                mData.add(foo);
                            }
                            replaceAll(mData);
                        } catch (JSONException e) {
                            myTool.log("选择类型 Err : " + e.getMessage());
                            showError();
                        }
                    }
                });

    }

    @Override
    public CommonAdapter<DeviceCategory> getAdapter() {
        return new CommonAdapter<DeviceCategory>(this, R.layout.item_common_choose, mData) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final DeviceCategory item, int position) {

                final String showName = item.getName() + ("".equals(item.getDesc()) ? "" : "（" + item.getDesc() + "）");

                helper.setText(R.id.tv_name, showName);
                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
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
                myTool.showInfo("没有更多类别~");
                l.loadMoreFinish(true);
            }
        }, 1000);
    }
}
