/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.cqu.stuexpress.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqu.stuexpress.R;
import com.cqu.stuexpress.bean.Contact;
import com.cqu.stuexpress.bean.User;
import com.cqu.stuexpress.utils.CommonTools;
import com.cqu.stuexpress.utils.DataMaker;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import okhttp3.Call;

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final int REQUESR_FOR_USERNAME = 001;//注册成功返回用户名和密码

    private CommonTools myTool;
    private ImageView ivBack;
    private EditText edtUserName, edtPassword;
    private ImageView ivDelUserName, ivDelPassword, ivHeadImg, ivBgImg;
    private Button btnLogin, btnRegister, btnResetPwd;
    private SweetAlertDialog mLoginDialog;

    private String userName, password;
    private final String REGISTER = "REGISTER";//注册
    private final String RESETPWD = "RESETPWD";//重置密码


    private User userInfo;
    TextView tvIPModify;
    com.cqu.stuexpress.ui.login.IpModifyDialog ipModifyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 页面设置为沉浸式状态栏风格
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        initView();
        initData();

        initEvent();
    }

    private void initData() {


        //如果有存用户名则显示出来
        if (!"".equals(myTool.getUserId())) {
            edtUserName.setText(myTool.getUserId());
            ivDelUserName.setVisibility(View.VISIBLE);
            edtPassword.requestFocus();
        }

        userInfo = myTool.getUserInfo();
        if (userInfo == null) return;

        //设置头像
        myTool.showImage(userInfo.getImageUrl(),
                ivHeadImg, "男"
                        .equals(userInfo.getSex()) ?
                        R.drawable.default_head_male :
                        R.drawable.default_head_female);

        // 设置背景图片
        myTool.showImage(userInfo.getBgUrl(),
                ivBgImg,
                R.mipmap.head_bg);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        ivDelUserName.setOnClickListener(this);
        ivDelPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnResetPwd.setOnClickListener(this);
        tvIPModify.setOnClickListener(this);
        setTextListener();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);

        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ivDelUserName = (ImageView) findViewById(R.id.iv_del_username);
        ivDelPassword = (ImageView) findViewById(R.id.iv_del_password);
        ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);
        ivBgImg = (ImageView) findViewById(R.id.iv_bgimg);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnResetPwd = (Button) findViewById(R.id.btn_resetpwd);
        tvIPModify = (TextView) findViewById(R.id.tv_ipmodify);

        ipModifyDialog = new com.cqu.stuexpress.ui.login.IpModifyDialog(this, R.style.Translucent_NoTitle);
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
            case R.id.tv_ipmodify:
                ipModifyDialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_del_username:
                edtUserName.setText("");
                break;
            case R.id.iv_del_password:
                edtPassword.setText("");
                break;
            case R.id.btn_login:
                toLogin();//登录
                break;
            case R.id.btn_register:
                // 注册的手机验证
                verifyPhone(REGISTER);
                break;
            case R.id.btn_resetpwd:
                //重置密码的手机验证
                verifyPhone(RESETPWD);
                break;
        }
    }

    /**
     * 验证手机
     *
     * @param reseaon，用于注册，还是忘记密码
     */
    private void verifyPhone(final String reseaon) {
        myTool.log("verifyPhone！");
        // 打开验证页面
        RegisterPage page = new RegisterPage();
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”

                    // 利用国家代码和手机号码进行后续的操作
                    switch (reseaon) {
                        case REGISTER:
                            // 验证手机和国家后，注册用户到服务器
                            myTool.startActivityForResult(new Contact(phone, country), com.cqu.stuexpress.ui.login.RegisterActivity.class, REQUESR_FOR_USERNAME);
                            break;
                        case RESETPWD:
                            // 验证手机和国家后，注册用户到服务器
                            myTool.startActivityForResult(new Contact(phone, country), com.cqu.stuexpress.ui.login.ResetPwdActivity.class, REQUESR_FOR_USERNAME);
                            break;
                    }
                } else {
                    // TODO 处理错误的结果
                    myTool.showInfo("手机验证失败稍后再试！");
                }
            }
        });
        page.show(this);


    }

    private void attempLogin() {
        byte[] data = new byte[0];
        try {
            data = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            myTool.showExceptionInfo(e);
            return;
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        myTool.log("base64：" + base64);

        mLoginDialog.setTitleText("正在登录服务器···");
        mLoginDialog.show();

        OkHttpUtils.post()
                .url(myTool.getServAdd() + "user/login")
                .addParams("token", myTool.getToken())
                .addParams("userId", userName)
                .addParams("userPwd", base64)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoginDialog.setTitleText("失败")
                                .setContentText("登录失败，异常信息 e-->[" + e.toString() + "]")
                                .setConfirmText("稍后重试")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log("response:" + response);
                        String result[] = response.split(":");
                        String msg = response;
                        String token = "";
                        if (result.length == 2) {
                            msg = result[0];
                            token = result[1];
                        }
                        switch (msg) {

                            case "success":


                                myTool.setLoginFlag(true);
                                myTool.setUserName(userName);
                                myTool.setToken(token);

                                // 更新用户信息
                                DataMaker.updateUserInfo(com.cqu.stuexpress.ui.login.LoginActivity.this);

//                                // 开启任务服务
//                                startService(new Intent(LoginActivity.this, TaskService.class));

                                mLoginDialog.setTitleText("登录成功")
                                        .setContentText("您好" + userInfo.getNickName() + "，学递来啦欢迎您！")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                new CountDownTimer(6000, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        mLoginDialog.setConfirmText("立即开启（" + millisUntilFinished / 1000 + "s）");
                                    }

                                    @Override
                                    public void onFinish() {
                                        finish();
                                    }
                                }.start();
//                                toLoginEm();//登录环信账号
                                break;
                            case "wrong":
                                mLoginDialog.setTitleText("登录失败")
                                        .setContentText("密码或用户名错误,请重新输入！")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                edtPassword.setText("");
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            case "errorToken":
                                myTool.log("errorToken");
                                mLoginDialog.setTitleText("正在重新获取Token···");
                                attempGetToken();
                                break;
                        }
                    }
                });
    }

    private void toLogin() {
        userName = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString();

        if ("".equals(edtUserName.getText().toString().trim())) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtUserName.requestFocus();
                    edtUserName.setError("请输入手机号码！");
                }
            })
                    .playOn(edtUserName);
            return;
        }
        if (edtUserName.getText().toString().trim().length() != 11) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtUserName.requestFocus();
                    edtUserName.setError("格式有误，11位手机号码！");
                }
            })
                    .playOn(edtUserName);
            return;
        }
        if (edtPassword.getText().toString().length() < 6) {
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
        mLoginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mLoginDialog.setTitleText("正在获取Token信息···");
        mLoginDialog.show();
        attempGetToken();
    }

    private void attempGetToken() {
        OkHttpUtils.post().url(myTool.getServAdd() + "user/getUserToken")
                .addParams("userId", userName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoginDialog.setTitleText("失败")
                                .setContentText("登录失败，异常信息 e-->[" + e.toString() + "]")
                                .setConfirmText("稍后重试")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log("token response:" + response);
                        myTool.setToken(response);
                        attempLogin();
                    }
                });
    }

    //登录环信
    private void toLoginEm() {
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void setTextListener() {
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    //没有显示时就不做操作
                    if (ivDelUserName.getVisibility() == View.GONE) {
                        ivDelUserName.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomIn)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_del_username));
                    }
                } else {
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ivDelUserName.setVisibility(View.GONE);
                                }
                            })
                            .playOn(findViewById(R.id.iv_del_username));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    //没有显示时就不做操作
                    if (ivDelPassword.getVisibility() == View.GONE) {
                        ivDelPassword.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomIn)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_del_password));
                    }
                } else {
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ivDelPassword.setVisibility(View.GONE);
                                }
                            })
                            .playOn(findViewById(R.id.iv_del_password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功返回用户名和密码
        if (requestCode == REQUESR_FOR_USERNAME) {

        }
    }


}
