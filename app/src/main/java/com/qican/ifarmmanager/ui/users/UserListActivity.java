package com.qican.ifarmmanager.ui.users;


import android.view.View;
import android.widget.ImageView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.beanfromservice.User;
import com.qican.ifarmmanager.listener.BeanCallBack;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class UserListActivity extends ComListActivity<ComUser> {

    ArrayList<ComUser> mData;

    @Override
    public String getUITitle() {
        return "选择用户";
    }

    @Override
    public void init() {

        mData = new ArrayList<>();
        requestData();
    }

    private void requestData() {

        Map para = new HashMap<String, String>();
        para.put("managerId", myTool.getManagerId());
        para.put("token", myTool.getToken());

        showProgress();

        OkHttpUtils.post().url(myTool.getServAdd() + "manager/allUser")
                .params(para)
                .build()
                .execute(new BeanCallBack<List<User>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pullToRefreshLayout.refreshFinish(true);
                        showError();
                    }

                    @Override
                    public void onResponse(List<User> users, int id) {
                        pullToRefreshLayout.refreshFinish(true);
                        showContent();

                        if (users == null) return;
                        mData.clear();

                        for (User user : users) {
                            ComUser comUser = new ComUser(user);
                            mData.add(comUser);
                        }

                        if (mData.isEmpty()) {
                            showNoData();
                            return;
                        }

                        addAll(mData);
                    }
                });
    }

    @Override
    public CommonAdapter<ComUser> getAdapter() {
        return new CommonAdapter<ComUser>(this, R.layout.item_user, mData) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, ComUser item, int position) {

                helper
                        .setText(R.id.tv_nickname, item.getNickName())
                        .setText(R.id.tv_autograph, item.getSignature())
                        .setImageUrl(R.id.iv_headimage, item.getHeadImgUrl());

                ImageView ivSex = helper.getView(R.id.iv_sex);
                myTool.showSex(item.getSex(), ivSex);
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
                myTool.showInfo("没有更多信息！");
                l.loadMoreFinish(true);
            }
        }, 1500);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
