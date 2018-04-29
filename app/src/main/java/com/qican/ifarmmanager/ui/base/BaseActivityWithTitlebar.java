/**
 * 带有titlebar的通用窗口必须包含titlebar.xml的布局
 */
package com.qican.ifarmmanager.ui.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.utils.CommonTools;


public abstract class BaseActivityWithTitlebar extends FragmentActivity implements View.OnClickListener {
    protected CommonTools myTool;

    TextView tvTitle, tvMenu;
    LinearLayout llBack;

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
    }

    // UI交互常用方法
    protected void setImg(@IdRes int id, Bitmap bitmap) {
        ImageView iv = (ImageView) findViewById(id);
        if (bitmap != null && iv != null)
            iv.setImageBitmap(bitmap);
    }

    protected void setImg(@IdRes int id, String url) {
        ImageView iv = (ImageView) findViewById(id);
        if (url != null && iv != null)
            Glide.with(this).load(url).centerCrop().into(iv);
    }

    protected void setTextById(@IdRes int id, String info) {
        TextView tv = (TextView) findViewById(id);
        if (info != null && tv != null)
            tv.setText(info);
    }

    private void initData() {
        init();
        initTitleBar();
    }

    protected void setTitle(String title) {
        tvTitle.setText(title);
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
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llBack = (LinearLayout) findViewById(R.id.llBack);

        // titleBar
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        llIconMenu = (LinearLayout) findViewById(R.id.ll_icon_menu);
        tvMenu = (TextView) findViewById(R.id.tv_menu);
        ivIconMenu = (ImageView) findViewById(R.id.iv_icon_menu);
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
}
