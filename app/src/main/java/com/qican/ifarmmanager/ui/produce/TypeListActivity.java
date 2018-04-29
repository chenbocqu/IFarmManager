package com.qican.ifarmmanager.ui.produce;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.DeviceType;
import com.qican.ifarmmanager.bean.ProducePara;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;

import java.util.ArrayList;

import static com.qican.ifarmmanager.ui.produce.DeviceProduceActivity.KEY_TYPE;

public class TypeListActivity extends ComListActivity<DeviceType> {

    ArrayList<DeviceType> mData;
    Bitmap mBm;


    @Override
    public String getUITitle() {
        return "选择设备类型";
    }

    @Override
    public void init() {

        mData = (ArrayList<DeviceType>) myTool.getParam(ArrayList.class);
        if (mData == null) {
            mData = new ArrayList<>();
            myTool.log("mdata is null !");
        }

        mBm = BitmapFactory.decodeResource(getResources(), R.drawable.device);

        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showContent();
                addAll(mData);
            }
        }, 200);
    }

    @Override
    public CommonAdapter<DeviceType> getAdapter() {

        return new CommonAdapter<DeviceType>(this, R.layout.item_common_choose) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final DeviceType item, int position) {
                helper.setText(R.id.tv_name, item.getName());
                helper.setImageBitmap(R.id.iv_img, mBm);
                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProducePara para = new ProducePara();
                        para.setTypeName(item.getName());
                        para.setTypeCode(item.getType());

                        Intent intent = new Intent();
                        intent.putExtra(KEY_TYPE, para);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout l) {
        l.refreshFinish(true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout l) {
        l.loadMoreFinish(true);
    }
}
