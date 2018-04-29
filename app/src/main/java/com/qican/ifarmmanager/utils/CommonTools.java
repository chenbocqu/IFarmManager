/**
 * 通用工具类
 *
 * @时间：2016-7-6
 */
package com.qican.ifarmmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.disk.DiskLruCacheHelper;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;

import java.io.IOException;
import java.io.Serializable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CommonTools {

    public static final String PACKAGE_NAME = "com.qican.ifarmmanager";

    public static final String USER_FILE_PATH = Environment.getExternalStorageDirectory() + "/"
            + PACKAGE_NAME; //用户文件

    public static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/"
            + PACKAGE_NAME;

    private Context mContext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    DiskLruCacheHelper cacheHelper = null;

    public CommonTools(Context context) {
        this.mContext = context;

        sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE);
        editor = sp.edit();

        try {
            cacheHelper = new DiskLruCacheHelper(mContext);
        } catch (IOException e) {

        }
    }

    private void showInfo(String msg, int duration) {
        Toast.makeText(mContext, msg, duration).show();
    }

    public void showInfo(String msg) {
        showInfo(msg, Toast.LENGTH_SHORT);
    }


    /**
     * 启动activity
     *
     * @param activity
     */
    public void startActivity(Class<?> activity) {
        try {
            mContext.startActivity(new Intent(mContext, activity));
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    /**
     * 带参数的启动
     *
     * @param o        传递参数
     * @param activity 要启动的avtivity
     * @return 当前实例
     */
    public CommonTools startActivity(Serializable o, Class<?> activity) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            mContext.startActivity(intent);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return this;
    }

    /**
     * 得到上一页面传递过来的参数
     *
     * @param o 已经实例化的类型传递参数
     * @return 上一页面传递过来的参数
     */
    public Serializable getParam(@NonNull Serializable o) {
        Bundle bundle = ((Activity) mContext).getIntent().getExtras();
        if (bundle == null)
            return "";
        return (Serializable) bundle.get(o.getClass().getName());
    }

    /**
     * 得到上一页面传递过来的参数
     *
     * @param c 要接收的类型
     * @return 上一页面传递过来的参数
     */
    public Serializable getParam(@NonNull Class c) {
        Bundle bundle = ((Activity) mContext).getIntent().getExtras();
        if (bundle == null)
            return null;
        return (Serializable) bundle.get(c.getName());
    }

    /**
     * 启动activity,得到返回结果
     *
     * @param activity 要启动的activity
     */
    public void startActivityForResult(Class<?> activity, int requestCode) {
        try {
            ((Activity) mContext).startActivityForResult(new Intent(mContext, activity), requestCode);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    /**
     * 带参数的启动
     *
     * @param o        xx
     * @param activity XXX
     * @return 当前实例
     */
    public CommonTools startActivityForResult(Serializable o, Class<?> activity, int requestCode) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return this;
    }


    public int getScreenWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        return dpMetrics.widthPixels;
    }

    /**
     * 设置空间布局为屏幕宽度的ratio倍，如1/3
     *
     * @param ratio
     */
    public void setHeightByWindow(View v, float ratio) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = (int) (getScreenWidth() * ratio);
        v.setLayoutParams(lp);
    }

    /**
     * 打印log
     *
     * @param msg
     */
    public void log(String msg) {
        if (msg == null) msg = "null";
        Log.i("IFManager_DEBUG_" + mContext.getClass().getSimpleName(), msg);
    }

    public void setManagerId(String id) {
        editor.putString(IFMValue.KEY_USERNAME, id);
        editor.commit();
    }

    public String getManagerId() {
        return sp.getString(IFMValue.KEY_USERNAME, "not_login");
    }

    /**
     * 从网络平滑加载图片到指定ImageView上
     */
    public CommonTools showImage(String url, ImageView imageView) {
        return showImage(url, imageView, R.drawable.img_holder);
    }

    public CommonTools showImage(String url, ImageView imageView, @DrawableRes int errRes) {

        if (imageView == null || url == null || "".equals(url)) return this;

        Glide.with(mContext).load(url).error(errRes)
                .centerCrop().crossFade().into(imageView);

        return this;
    }

    public void setIP(String ip) {
        editor.putString(IFMValue.IP_ADDRESS, ip);
        editor.commit();
    }

    // 获得服务器ip地址
    public String getIP() {
        return sp.getString(IFMValue.IP_ADDRESS, IFMValue.IP);
    }

    // 获取服务器数据地址
    public String getServAdd() {
        return "http://" + getIP() + ":" + IFMValue.PORT + "//IFarm/";
//        return ConstantValue.SERVICE_ADDRESS; //固定地址
    }

    public String getSocketAdd() {
        return "http://" + getIP() + ":" + IFMValue.PORT + "/IFarm/";
    }

    /**
     * 存入token
     *
     * @param token
     * @return
     */
    public CommonTools setToken(String token) {
        editor.putString(IFMValue.KEY_TOKEN, token);
        editor.commit();
        return this;
    }

    /**
     * 得到token
     *
     * @return token信息
     */
    public String getToken() {
        return sp.getString(IFMValue.KEY_TOKEN, "");
    }

    public CommonTools setLoginFlag(boolean isLogin) {
        editor.putBoolean("LOGIN_FLAG", isLogin);
        editor.commit();
        return this;
    }

    public boolean isLogin() {
        return sp.getBoolean("LOGIN_FLAG", false);
    }

    public void showPopFormBottom(PopupWindow win, View mainView, final PopupWindow.OnDismissListener l) {
        final WindowManager.LayoutParams[] params = {((Activity) mContext).getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(params[0]);

        // 设置Popupwindow显示位置（从底部弹出）
        win.showAtLocation(mainView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        win.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = ((Activity) mContext).getWindow().getAttributes();
                params[0].alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(params[0]);
                if (l != null) l.onDismiss();
            }
        });
    }

    public void showPopFormBottom(PopupWindow win, View mainView) {
        showPopFormBottom(win, mainView, null);
    }

    /**
     * 设置性别图标
     *
     * @param sex
     * @param imageView
     */
    public CommonTools showSex(String sex, ImageView imageView) {
        if (sex == null) {//为空就直接返回了避免错误
            return this;
        }
        Bitmap female = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.female);
        Bitmap male = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.male);
        //设置性别图标
        switch (sex) {
            case "男":
                imageView.setImageBitmap(male);
                break;
            case "女":
                imageView.setImageBitmap(female);
                break;
            default:
                log("显示性别不成功:sex[" + sex + "],imageView[" + imageView.toString() + "]");
                break;
        }
        return this;
    }

    // 返回null表示没有用户被选择
    public ComUser getUserInfo() {

        if (!isUserSelected()) return null;

        ComUser user = new ComUser();

        if (cacheHelper != null)
            user = cacheHelper.getAsSerializable(IFMValue.KEY_COMUSERINFO);

        return user;
    }

    /**
     * 通过用户Id存储用户信息到本地
     *
     * @param userInfo 要保存用户信息
     * @return 当前实例
     */
    public CommonTools setUserInfo(ComUser userInfo) {
        setUserSelected(true);
        cacheHelper.put(IFMValue.KEY_COMUSERINFO, userInfo);
        return this;
    }

    public CommonTools setUserSelected(boolean isSelected) {
        editor.putBoolean("USER_SELETED_FLAG", isSelected);
        editor.commit();
        return this;
    }

    public boolean isUserSelected() {
        return sp.getBoolean("USER_SELETED_FLAG", false);
    }
}
