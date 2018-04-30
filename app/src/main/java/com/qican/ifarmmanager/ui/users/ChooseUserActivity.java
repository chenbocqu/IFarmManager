package com.qican.ifarmmanager.ui.users;

import android.view.View;
import android.widget.ImageView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.ui.base.ComListActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

public class ChooseUserActivity extends ComListActivity<ComUser> {

    ArrayList<ComUser> mData;

    @Override
    public String getUITitle() {
        return "选择用户";
    }

    @Override
    public void init() {
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

//        Map para = new HashMap<String, String>();
//        para.put("managerId", myTool.getManagerId());
//        para.put("token", myTool.getToken());

        showProgress();

        OkHttpUtils.post().url(myTool.getServAdd() + "manager/allUser")
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
                                ComUser user = new ComUser();
                                JSONObject obj = arr.getJSONObject(i);

                                user.setId(obj.getString("userId"));
                                user.setPassword(obj.getString("userPwd"));
                                user.setRegisterTime(obj.getString("userRegisterTime"));
                                user.setLastLoginTime(obj.getString("userLastLoginTime"));

                                if (obj.has("userName"))
                                    user.setNickName(obj.getString("userName"));
                                if (obj.has("userSex")) user.setSex(obj.getString("userSex"));
                                if (obj.has("userImageUrl"))
                                    user.setHeadImgUrl(obj.getString("userImageUrl"));
                                if (obj.has("userBackImageUrl"))
                                    user.setBgImgUrl(obj.getString("userBackImageUrl"));
                                if (obj.has("userSignature"))
                                    user.setSignature(obj.getString("userSignature"));
                                if (obj.has("userRole")) user.setRole(obj.getString("userRole"));

                                mData.add(user);
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
    public CommonAdapter<ComUser> getAdapter() {
        return new CommonAdapter<ComUser>(this, R.layout.item_user, mData) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final ComUser item, int position) {

                helper
                        .setText(R.id.tv_nickname, item.getNickName())
                        .setText(R.id.tv_autograph, item.getSignature())
                        .setImageUrl(R.id.iv_headimage, item.getHeadImgUrl());

                ImageView ivSex = helper.getView(R.id.iv_sex);
                myTool.showSex(item.getSex(), ivSex);

                helper.setOnClickListener(R.id.rl_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myTool.setUserInfo(item);
                        finishDelay();
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
