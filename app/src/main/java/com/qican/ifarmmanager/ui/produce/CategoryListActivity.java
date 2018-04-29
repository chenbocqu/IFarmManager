package com.qican.ifarmmanager.ui.produce;


import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Category;
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

public class CategoryListActivity extends ComListActivity<Category> {

    List<Category> mData;
    ProducePara mPara;
    String categoryName, categoryCode;

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
                                Category category = new Category();

                                JSONObject obj = array.getJSONObject(i);

                                if (obj.has("deviceCategory"))
                                    category.setCategory(obj.getString("deviceCategory"));
                                if (obj.has("deviceCategoryName"))
                                    category.setName(obj.getString("deviceCategoryName"));

                                // 内层
                                ArrayList<DeviceType> types = new ArrayList<DeviceType>();

                                if (obj.has("deviceType")) {

                                    JSONArray typeArr = obj.getJSONArray("deviceType");

                                    for (int j = 0; j < typeArr.length(); j++) {
                                        DeviceType type = new DeviceType();
                                        JSONObject objType = typeArr.getJSONObject(j);
                                        if (objType.has("deviceType"))
                                            type.setType(objType.getString("deviceType"));

                                        if (objType.has("deviceTypeName"))
                                            type.setName(objType.getString("deviceTypeName"));

                                        types.add(type);
                                    }

                                }
                                category.setTypes(types);
                                mData.add(category);
                            }
                            replaceAll(mData);
                        } catch (JSONException e) {
                            showError();
                        }
                    }
                });

    }

    @Override
    public CommonAdapter<Category> getAdapter() {
        return new CommonAdapter<Category>(this, R.layout.item_common_choose, mData) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final Category item, int position) {
                helper.setText(R.id.tv_name, item.getName());
                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 记录类别
                        categoryName = item.getName();
                        categoryCode = item.getCategory();

                        // 选择类型
                        myTool.startActivityForResult(item.getTypes(), TypeListActivity.class, REQUEST_FOR_TYPE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FOR_TYPE:

                if (resultCode == RESULT_OK) {
                    mPara = (ProducePara) data.getSerializableExtra(KEY_TYPE);
                    if (mPara != null) {
                        mPara.setCategoryName(categoryName);
                        mPara.setCategoryCode(categoryCode);

                        Intent intent = new Intent();
                        intent.putExtra(KEY_TYPE, mPara);

                        setResult(RESULT_OK, intent);

                        finish();
                    }
                }

                break;
        }
    }
}
