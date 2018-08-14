/**
 * 通用列表
 */
package com.qican.ifarmmanager.ui.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.adapter.CommonAdapter;
import com.mingle.widget.LoadingView;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.utils.CommonTools;
import com.qican.ifarmmanager.view.refresh.PullListView;
import com.qican.ifarmmanager.view.refresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class ComListActivity<T> extends FragmentActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {

    protected ArrayList<T> myDatas;
    protected CommonTools myTool;
    CommonAdapter<T> mAdapter;

    protected PullToRefreshLayout pullToRefreshLayout;

    PullListView pullListView;

    protected LoadingView avi;

    TextView tvTitle, tvMenu;
    LinearLayout llBack;
    View rlNodata, vHeader = null, rlNetError;
    ImageView ivNetError;
    private LinearLayout llMenu, llIconMenu;
    private View.OnClickListener menuListener, iconListener;
    private String menuText;
    private int iconResId = 0;
    private ImageView ivIconMenu;
    SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

        if (llBack != null)
            llBack.setOnClickListener(this);

        llMenu.setOnClickListener(this);
        llIconMenu.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        myDatas = new ArrayList<>();
        showContent();

        init();

        initTitleBar();

        avi.setVisibility(View.GONE);
        mAdapter = getAdapter();
        if (vHeader != null) pullListView.addHeaderView(vHeader);
        pullListView.setAdapter(mAdapter);
        notifyDatasetChanged();
    }

    public void showContent() {


        hideProgress();

        rlNodata.setVisibility(View.GONE);

        hideError();
    }


    private void initTitleBar() {

        if (tvTitle != null)
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
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        pullListView = (PullListView) findViewById(R.id.pullListView);
        avi = findViewById(R.id.avi);
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

        avi.setLoadingText("加载中...");
    }

    protected void showNoData() {

        showContent();

        rlNodata.setVisibility(View.VISIBLE);
    }

    private void hideNoData() {
        rlNodata.setVisibility(View.GONE);
    }


    public void showProgress() {

        showContent();

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("加载中...");
        mDialog.show();

    }

    private void hideProgress() {

        pullToRefreshLayout.refreshFinish(true);

        if (mDialog != null && mDialog.isShowing())
            mDialog.dismissWithAnimation();
    }

    public void showError() {
        showContent();
        hideProgress();
        if (avi.getVisibility() == View.VISIBLE) avi.setVisibility(View.GONE);
        ivNetError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        ivNetError.setVisibility(View.GONE);
    }

    public void notifyDatasetChanged() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.isEmpty()) {
            showNoData();
        } else {
            hideNoData();
        }
        avi.setVisibility(View.GONE);
        ivNetError.setVisibility(View.GONE);
    }

    protected void setVisible(@IdRes int id, int visible) {
        View v = findViewById(id);
        if (v != null) v.setVisibility(visible);
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

    public abstract String getUITitle();

    public abstract void init();

    public abstract CommonAdapter<T> getAdapter();

    @Override
    public abstract void onRefresh(PullToRefreshLayout l);

    @Override
    public abstract void onLoadMore(PullToRefreshLayout l);

    public void setRightMenu(String menuText, View.OnClickListener menuListener) {
        this.menuText = menuText;
        this.menuListener = menuListener;
    }

    public void setIconMenu(@DrawableRes int resId, View.OnClickListener iconListener) {
        this.iconResId = resId;
        this.iconListener = iconListener;
    }

    protected View getHeaderView() {
        return vHeader;
    }

    protected int getHeadLayout() {
        return 0;
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

    public void replaceAll(List<T> data) {
        mAdapter.replaceAll(data);
        notifyDatasetChanged();
    }

    protected void setVisible(@IdRes int id, boolean isVisible) {
        View v = findViewById(id);
        if (v != null) v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void setText(@IdRes int id, String text) {
        TextView tv = (TextView) findViewById(id);
        if (tv == null) Log.w("DEBUG_", "TextView is NULL !");
        if (tv != null && text != null)
            tv.setText(text);
    }

    protected void setImg(@IdRes int id, Bitmap bm) {

        ImageView iv = (ImageView) findViewById(id);

        if (iv != null)
            iv.setImageBitmap(bm);
    }

    protected void setImg(@IdRes int id, String url) {

        ImageView iv = (ImageView) findViewById(id);
        myTool.showImage(url, iv);
    }


    // 注册点击事件
    protected void registerClickListener(@IdRes int id) {

        View v = findViewById(id);

        if (v != null) v.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        if (mDialog.isShowing()) return;

        super.onBackPressed();

    }

    /**
     * 延时800ms退出
     */
    protected void finishDelay() {

        // 延时退出
        new CountDownTimer(800, 500) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();

    }
}
