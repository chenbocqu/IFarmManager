/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.cqu.stuexpress.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cqu.stuexpress.R;
import com.cqu.stuexpress.bean.Contact;
import com.cqu.stuexpress.ui.base.TitleBarActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;


public class RegisterActivity extends TitleBarActivity implements View.OnClickListener {

    private EditText edtPassword, edtConPwd;
    ImageView ivDelPwd, ivDelConPwd;
    private String userName = "", password, confirmPwd;
    private SweetAlertDialog mDialog;
    private Contact phoneInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewById();

        setListener();

        initData();
    }

    private void viewById() {

        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConPwd = (EditText) findViewById(R.id.edt_conpwd);

        ivDelPwd = (ImageView) findViewById(R.id.iv_del_password);
        ivDelConPwd = (ImageView) findViewById(R.id.iv_del_conpwd);
    }

    private void setListener() {

        setTextListener(edtPassword, ivDelPwd);
        setTextListener(edtConPwd, ivDelConPwd);

        registerClickListener(R.id.iv_del_username);
        registerClickListener(R.id.iv_del_password);
        registerClickListener(R.id.iv_del_conpwd);
        registerClickListener(R.id.btn_register);

    }

    private void initData() {
        phoneInfo = (Contact) myTool.getParam(Contact.class);

        if (phoneInfo != null)
            userName = phoneInfo.getPhone();

        setText(R.id.tv_username, userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 点击事件
     *
     * @param v：所点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_del_username:
                setText(R.id.tv_username, "");
                break;
            case R.id.iv_del_password:
                edtPassword.setText("");
                break;
            case R.id.iv_del_conpwd:
                edtConPwd.setText("");
                break;
            case R.id.btn_register:
                attempRegister();//登录
                break;
        }
    }

    @Override
    public String getUITitle() {
        return "注册";
    }

    @Override
    public void init() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    private void attempRegister() {
        password = edtPassword.getText().toString();
        confirmPwd = edtConPwd.getText().toString();

        if (password.length() < 6) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtPassword.requestFocus();
                    edtPassword.setError("密码在6位以上！");
                }
            })
                    .playOn(edtPassword);
            return;
        }

        if (!password.equals(confirmPwd)) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtConPwd.requestFocus();
                    edtConPwd.setError("两次密码不一致！");
                }
            })
                    .playOn(edtConPwd);
            return;
        }

        mDialog = null;
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在注册···");
        mDialog.show();

        String url = myTool.getServAdd() + "user/register";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userName)
                .addParams("userPwd", password)
                .addParams("userName", "用户" + userName)
                .addParams("userSignature", "这个家伙很懒，没有任何介绍！")
                .addParams("userSex", "男")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toOtherCase(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            String msg = object.getString("message");
                            switch (msg) {
                                case "success":
                                    myTool.setToken(object.getString("token"));
                                    toLoginSuccess(response);
                                    break;
                                case "repeat":
                                    mDialog.setTitleText("提示")
                                            .setContentText("该用户已存在！")
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    break;
                                case "error":
                                    mDialog.setTitleText("错误")
                                            .setContentText("用户已存在或其他系统错误！")
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    break;
                                default:
                                    toOtherCase(response);
                                    break;
                            }

                        } catch (JSONException e) {
                            myTool.showInfo(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 创建一个环信账号
     */
    private void toCreateEmCount() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    EMClient.getInstance().createAccount(userName, password);

                    myTool.setEMCountExist(true);

                    myTool.log("创建环信用户成功！");
                } catch (final HyphenateException e) {
                    switch (e.getErrorCode()) {
                        case EMError.USER_ALREADY_EXIST:
                            myTool.log("创建环信用户是出错：该账号已存在！（" + e.toString() + ")");
                            break;
                    }
                }
            }
        }.start();
    }

    private void toOtherCase(String response) {
        mDialog.setTitleText("提示")
                .setContentText("服务器返回的信息：" + response + "！")
                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
    }

    private void toLoginSuccess(String response) {
        toCreateEmCount();//创建环信账户，用以聊天
        mDialog.setTitleText("注册成功")
                .setContentText("欢迎注册智能农场！")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }

    private void setTextListener(EditText edt, final ImageView iv) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, iv);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 根据输入信息显示删除控件
     *
     * @param s
     * @param ivDel
     */
    private void showIvDelByEdt(CharSequence s, final ImageView ivDel) {
        if (s.length() != 0) {
            //没有显示时就不做操作
            if (ivDel.getVisibility() == View.GONE) {
                ivDel.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn)
                        .duration(700)
                        .playOn(ivDel);
            }
        } else {
            YoYo.with(Techniques.ZoomOut)
                    .duration(700)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ivDel.setVisibility(View.GONE);
                        }
                    })
                    .playOn(ivDel);
        }
    }
}
