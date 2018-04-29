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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.utils.CommonTools;


public abstract class TitleBarActivity extends FragmentActivity implements View.OnClickListener {

    protected CommonTools myTool;

    private View.OnClickListener menuListener, iconListener;
    private String menuText;
    private int iconResId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initDataForTitleBar();
        initEventForTitleBar();
    }

    protected void initEventForTitleBar() {
        registerClickListener(R.id.llBack);
        registerClickListener(R.id.ll_menu);
        registerClickListener(R.id.ll_icon_menu);
    }

    private void initDataForTitleBar() {

        myTool = new CommonTools(this);

        init();
        initTitleBar();
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

    protected void setTitle(String title) {
        setText(R.id.tv_title, title);
    }

    private void initTitleBar() {
        setText(R.id.tv_title, getUITitle());

        if (menuListener != null || menuText != null) {
            setVisible(R.id.ll_menu, true);
            setText(R.id.tv_menu, menuText);
        }

        if (iconListener != null || iconResId != 0) {
            setVisible(R.id.ll_icon_menu, true);
            setImg(R.id.iv_icon_menu, BitmapFactory.decodeResource(getResources(), iconResId));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                this.finish();
                break;
            case R.id.ll_menu:
                if (menuListener != null)
                    menuListener.onClick(findViewById(R.id.ll_icon_menu)); // 文字菜单点击事件接口回调
                break;
            case R.id.ll_icon_menu:
                if (iconListener != null) iconListener.onClick(findViewById(R.id.ll_icon_menu));
                break;
        }
    }

    public String getUITitle() {
        return "";
    }

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
