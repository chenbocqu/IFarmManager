/**
 * 通用列表
 */
package com.qican.ifarmmanager.ui.base;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.adapter.CommonAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mingle.widget.LoadingView;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.utils.CommonTools;
import com.qican.ifarmmanager.view.refresh.PullListView;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;

import java.util.List;

public abstract class ContListActivity<T> extends Activity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {
    protected CommonTools myTool;
    CommonAdapter<T> mAdapter;

    PullToRefreshLayout pullToRefreshLayout;

    PullListView pullListView;

    protected LoadingView loadView;

    TextView tvTitle, tvMenu;
    LinearLayout llBack;
    View rlNodata, vHeader = null;
    ImageView ivNetError;

    private LinearLayout llMenu, llIconMenu;
    private View.OnClickListener menuListener, iconListener;
    private String menuText;
    private int iconResId = 0;

    private ImageView ivIconMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llMenu.setOnClickListener(this);
        llIconMenu.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        init();

        initTitleBar();

        loadView.setVisibility(View.GONE);
        mAdapter = getAdapter();

        if (vHeader != null) pullListView.addHeaderView(vHeader);
        pullListView.setAdapter(mAdapter);
        notifyDatasetChanged();
    }

    protected void setText(@IdRes int id, String text) {

        TextView tv = (TextView) findViewById(id);

        if (tv != null && text != null)
            tv.setText(text);

    }

    private void initTitleBar() {
        tvTitle.setText(getUITitle());

        if (menuListener != null)
            llMenu.setVisibility(View.VISIBLE);

        if (menuText != null) {
            llMenu.setVisibility(View.VISIBLE);
            tvMenu.setText(menuText);
        }

        if (iconListener != null) {
            llIconMenu.setVisibility(View.VISIBLE);
        }

        if (iconResId != 0) {
            llIconMenu.setVisibility(View.VISIBLE);
            ivIconMenu.setImageBitmap(BitmapFactory.decodeResource(getResources(), iconResId));
        }
    }

    private void initView() {
        myTool = new CommonTools(this);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        pullListView = (PullListView) findViewById(R.id.listView);
        loadView = (LoadingView) findViewById(R.id.loadView);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        rlNodata = findViewById(R.id.rl_nodata);
        ivNetError = (ImageView) findViewById(R.id.iv_net_error);

        // titleBar
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        llIconMenu = (LinearLayout) findViewById(R.id.ll_icon_menu);
        tvMenu = (TextView) findViewById(R.id.tv_menu);
        ivIconMenu = (ImageView) findViewById(R.id.iv_icon_menu);

        // header
        if (getHeadLayout() != 0)
            vHeader = View.inflate(this, getHeadLayout(), null);
    }

    protected int getHeadLayout() {
        return 0;
    }

    protected boolean isEmpty() {
        return mAdapter.getData().isEmpty();
    }

    private void showNoData() {
        rlNodata.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(rlNodata);
    }

    private void hideNoData() {
        rlNodata.setVisibility(View.GONE);
//        YoYo.with(Techniques.FadeOut)
//                .withListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        rlNodata.setVisibility(View.GONE);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                })
//                .duration(100)
//                .playOn(rlNodata);
    }


    public void notifyDatasetChanged() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getData().isEmpty()) {
            showNoData();
        } else {
            hideNoData();
        }
        loadView.setVisibility(View.GONE);
        ivNetError.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                this.finish();
                break;
            case R.id.ll_menu:
                if (menuListener != null) menuListener.onClick(llMenu); // 文字菜单点击事件接口回调
                break;
            case R.id.ll_icon_menu:
                if (iconListener != null) iconListener.onClick(llIconMenu);
                break;
        }
    }

    public void addAll(List<T> data) {
        mAdapter.addAll(data);
        notifyDatasetChanged();
    }

    public void add(@NonNull T item) {
        mAdapter.add(item);
        notifyDatasetChanged();
    }

    public void remove(@NonNull T item) {
        mAdapter.remove(item);
        notifyDatasetChanged();
    }

    public abstract String getUITitle();

    public abstract void init();

    public abstract CommonAdapter<T> getAdapter();

    //设置布局文件
    protected abstract int getContentView();

    public void setRightMenu(String menuText, View.OnClickListener menuListener) {
        this.menuText = menuText;
        this.menuListener = menuListener;
    }

    public void setIconMenu(@DrawableRes int resId, View.OnClickListener iconListener) {
        this.iconResId = resId;
        this.iconListener = iconListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    protected View getHeaderView() {
        return vHeader;
    }

    @Override
    public abstract void onRefresh(PullToRefreshLayout pullToRefreshLayout);

    @Override
    public abstract void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
}
