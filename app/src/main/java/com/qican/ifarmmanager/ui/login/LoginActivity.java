package com.qican.ifarmmanager.ui.login;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class LoginActivity extends TitleBarActivity {

    ProgressBar mPb;
    EditText edtId, edtPwd;

    String mId, pwd;

    @Override
    public String getUITitle() {
        return "欢迎登录后台";
    }

    @Override
    public void init() {
        mPb = findViewById(R.id.pb);
        registerClickListener(R.id.btn_login);

        edtId = findViewById(R.id.edt_id);
        edtPwd = findViewById(R.id.edt_pwd);

        TextUtils.with(this).restrictTextLenth(edtId, 11, "最大不超过11");
        TextUtils.with(this).restrictTextLenth(edtPwd, 18, "最大不超过18");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                toLogin();
                break;
        }
    }

    private void toLogin() {

        if (TextUtils.isEmpty(edtId)) {
            TextUtils.with(this).hintEdt(edtId, "用户名不为空！");
            return;
        }
        if (TextUtils.isEmpty(edtPwd)) {
            TextUtils.with(this).hintEdt(edtPwd, "用户密码不为空！");
            return;
        }

        mId = edtId.getText().toString().trim();
        pwd = edtPwd.getText().toString().trim();

        mPb.setIndeterminate(true);

        OkHttpUtils.post().url(myTool.getServAdd() + "manager/login")
                .addParams("managerId", mId)
                .addParams("managerPwd", pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        loginErr("登录失败，" + e.getMessage() + "！");
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log(response);

                        // 登录方式
                        if (response == null) {
                            loginErr("登录失败，response == null");
                            return;
                        }

                        if (response.contains(":")) {

                            String[] rps = response.split(":");

                            if (rps.length != 2) {
                                loginErr("登录失败，rps.length != 2");
                                return;
                            }

                            switch (rps[0]){

                                case "success":
                                    myTool.setToken(rps[1]);
                                    mPb.setIndeterminate(false);
                                    myTool.setManagerId(mId);
                                    myTool.setLoginFlag(true);
                                    myTool.showInfo("登录成功！");

                                    finish();

                                    break;

                                case "error":
                                    loginErr("登录失败，稍后重试！");
                                    break;
                            }

                            return;
                        }

                        try {

                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("response");

                            switch (status) {
                                case "success":
                                    myTool.setToken(obj.getString("token"));
                                    mPb.setIndeterminate(false);
                                    myTool.setManagerId(mId);
                                    myTool.setLoginFlag(true);
                                    myTool.showInfo("登录成功！");

                                    finish();

                                    break;
                                case "error":
                                    loginErr("登录失败，稍后重试！");
                                    break;
                            }

                        } catch (JSONException e) {
                            myTool.log("error ... ");
                            loginErr("登录失败，" + e.getMessage() + "！");
                        }
                    }
                });
    }

    void loginErr(String err) {
        mPb.setIndeterminate(false);
        myTool.showInfo(err);
    }

}
