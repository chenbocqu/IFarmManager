/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.cqu.stuexpress.ui.login;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqu.stuexpress.R;
import com.cqu.stuexpress.bean.Contact;
import com.cqu.stuexpress.ui.base.TitleBarActivity;
import com.cqu.stuexpress.utils.CommonTools;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class ResetPwdActivity extends TitleBarActivity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack;
    private TextView tvUserName;
    private EditText edtPassword, edtConPwd;
    private ImageView ivDelUserName, ivDelPassword, ivDelConPwd;
    private Button btnReset;
    private String userName, password, confirmPwd;
    private SweetAlertDialog mDialog;
    private Contact phoneInfo;

    private void initData() {

        phoneInfo = (Contact) myTool.getParam(Contact.class);

        userName = phoneInfo.getPhone();

        tvUserName.setText(userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initEvents() {
        llBack.setOnClickListener(this);
        ivDelUserName.setOnClickListener(this);
        ivDelPassword.setOnClickListener(this);
        ivDelConPwd.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        setTextListener();
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        tvUserName = (TextView) findViewById(R.id.tv_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConPwd = (EditText) findViewById(R.id.edt_conpwd);
        ivDelUserName = (ImageView) findViewById(R.id.iv_del_username);
        ivDelPassword = (ImageView) findViewById(R.id.iv_del_password);
        ivDelConPwd = (ImageView) findViewById(R.id.iv_del_conpwd);

        btnReset = (Button) findViewById(R.id.btn_resetpwd);

        myTool = new CommonTools(this);
    }

    /**
     * 点击事件
     *
     * @param v：所点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_del_username:
                tvUserName.setText("");
                break;
            case R.id.iv_del_password:
                edtPassword.setText("");
                break;
            case R.id.iv_del_conpwd:
                edtConPwd.setText("");
                break;
            case R.id.btn_resetpwd:
                toReset();//重置密码
                break;
        }
    }

    @Override
    public void init() {
        initView();
        initData();

        initEvents();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_resetpwd;
    }

    private void toReset() {
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
                .setTitleText("正在修改密码···");
        mDialog.show();

        String url = myTool.getServAdd() + "user/updateUser";
        OkHttpUtils.get().url(url)
                .addParams("signature", myTool.getToken())
                .addParams("userId", userName)
                .addParams("userPwd", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toOtherCase(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (response) {
                            case "success":
                                mDialog.setTitleText("重置成功")
                                        .setContentText("重置密码成功了，现在去登录？")
                                        .setConfirmText("好  的")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                new CountDownTimer(1000, 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        finish();
                                                    }
                                                }.start();
                                            }
                                        }).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                break;
                            case "error":
                                mDialog.setTitleText("修改失败")
                                        .setContentText("出现了一些小插曲，请稍后重试！")
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            default:
                                toOtherCase(response);
                                break;
                        }
                    }
                });
    }

    private void toOtherCase(String response) {
        mDialog.setTitleText("提示")
                .setContentText("服务器返回的信息：" + response + "！")
                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
    }


    private void setTextListener() {
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, ivDelPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtConPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, ivDelConPwd);
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
