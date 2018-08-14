package com.qican.ifarmmanager.ui.device;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Device;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.qrcode.ScanActivity;
import com.qican.ifarmmanager.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class VerifyDeviceActivity extends TitleBarActivity {

    private static final int REQUEST_DEVICE_INFO = 1;
    private static final int REQUEST_ADD_INFO = 2; // 设备添加信息
    String mId, mType, mVerifyCode;

    EditText edtId, edtVerifyCode;
    SweetAlertDialog mDialog;

    Device mDevice;

    @Override
    public String getUITitle() {
        return "第一步";
    }

    @Override
    public void init() {

        mDevice = (Device) myTool.getParam(Device.class);
        if (mDevice == null)
            mDevice = new Device();

        edtId = findViewById(R.id.edt_id);
        edtVerifyCode = findViewById(R.id.edt_very_code);

        setIconMenu(R.drawable.scan, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTool.startActivityForResult(ScanActivity.class, REQUEST_DEVICE_INFO);
            }
        });

        registerClickListener(R.id.btn_verify);

        if (mDevice.getId() != null) edtId.setText(mDevice.getId());
        if (mDevice.getVerifyCode() != null) edtVerifyCode.setText(mDevice.getVerifyCode());

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_device;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_verify:
                verify();
                break;
        }
    }

    private void verify() {

        mId = edtId.getText().toString().trim();
        mVerifyCode = edtVerifyCode.getText().toString().trim();

        if ("".equals(mId)) {
            TextUtils.with(this).hintEdt(edtId, "设备号不能为空！");
            return;
        }
        if ("".equals(mVerifyCode)) {
            TextUtils.with(this).hintEdt(edtVerifyCode, "验证码不能为空！");
            return;
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("验证中...");
        mDialog.show();

        OkHttpUtils.post().url(myTool.getServAdd() + "device/check")
                .addParams("managerId", myTool.getManagerId())
                .addParams("token", myTool.getToken())
                .addParams("deviceId", mId)
                .addParams("deviceVerification", mVerifyCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        verifyFaild(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log(response);
                        if (response == null) return;

                        try {
                            final JSONObject obj = new JSONObject(response);

                            switch (obj.getString("response")) {
                                case "success":

                                    // 验证成功跳转到下一页
                                    mDialog.setTitleText("验证成功，正在跳转...");

                                    edtId.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            mDialog.dismissWithAnimation();

                                            myTool.log("device type : " + mDevice.getType());

                                            // 根据不同的设备类型进行添加
                                            switch (mDevice.getType()) {
                                                case "concentrator":
                                                    myTool.startActivityForResult(mDevice, AddCollectorActivity.class, REQUEST_ADD_INFO);
                                                    break;

                                                // 采集设备
                                                case "collectorDevice":
                                                case "collectorType5":
                                                    myTool.startActivityForResult(mDevice, AddAcquisitorActivity.class, REQUEST_ADD_INFO);
                                                    break;

                                                // 控制设备
                                                case "controlDevice":
                                                    try {
                                                        JSONObject device = obj.getJSONObject("device");
                                                        mDevice.setDeviceType(device.getString("deviceType"));
                                                    } catch (JSONException e) {
                                                        myTool.showInfo("验证失败，err ：" + e.getMessage());
                                                        return;
                                                    }
                                                    myTool.startActivityForResult(mDevice, AddControllerActivity.class, REQUEST_ADD_INFO);
                                                    break;

                                                // 控制设备
                                                case "four_way":
                                                case "eight_way":
                                                case "two_way":

                                                    break;
                                            }
                                        }
                                    }, 500);

                                    break;
                                case "no_id":
                                    verifyFaild("查无此设备！");
                                    break;
                                case "check_error":
                                    verifyFaild("设备信息验证失败，请确保设备信息！");
                                    break;
                            }

                        } catch (JSONException e) {
                            verifyFaild(e.getMessage());
                        }
                    }
                });
    }

    private void verifyFaild(String err) {

        mDialog
                .setTitleText("失败")
                .setContentText(err)
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_DEVICE_INFO:

                if (resultCode == RESULT_OK) {

                    mId = data.getStringExtra("id");
                    mType = data.getStringExtra("type");
                    mVerifyCode = data.getStringExtra("verifyCode");

                    mDevice.setId(mId);
                    mDevice.setType(mType);
                    mDevice.setVerifyCode(mVerifyCode);

                    setText(R.id.edt_id, mId);
                    setText(R.id.edt_very_code, mVerifyCode);
                }

                break;

            case REQUEST_ADD_INFO:

                if (resultCode == RESULT_OK) {
                    finish();
                }

                if (resultCode == AddCollectorActivity.RESULT_EXIST) {
                    setText(R.id.edt_id, "");
                    setText(R.id.edt_very_code, "");
                }

                break;

        }
    }
}
