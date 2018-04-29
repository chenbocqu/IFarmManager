package com.qican.ifarmmanager.ui.produce;

import android.content.Intent;
import android.view.View;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.DeviceInfo;
import com.qican.ifarmmanager.bean.ProducePara;
import com.qican.ifarmmanager.listener.PopwindowListener;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class DeviceProduceActivity extends TitleBarActivity implements View.OnClickListener, PopwindowListener<Object, Object> {


    public static final int REQUEST_FOR_TYPE = 1;
    public static final String KEY_TYPE = "KEY_TYPE";
    ProducePara mPara;
    Dialog4Batch dialog4Batch;
    int mBatch = 1;
    SweetAlertDialog mDialog;
    ArrayList<DeviceInfo> mDeviceInfo;

    @Override
    public void init() {

        setTitle("生产设备");
        setText(R.id.tv_batch, mBatch + "个");

        dialog4Batch = new Dialog4Batch(this, mBatch);

        registerClickListener(R.id.btn_add);
        registerClickListener(R.id.rl_type);
        registerClickListener(R.id.rl_batch);

        dialog4Batch.setOnPowwinListener(this);

        mDeviceInfo = new ArrayList<DeviceInfo>();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_device_produce;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.rl_type:
                myTool.startActivityForResult(CategoryListActivity.class, REQUEST_FOR_TYPE);
                break;

            case R.id.rl_batch:
                myTool.showPopFormBottom(dialog4Batch, findViewById(R.id.ll_main));
                break;

            case R.id.btn_add:
                produce();
                break;

        }
    }

    private void produce() {
        if (mPara == null) {
            myTool.showInfo("请选择要生产的设备类型！");
            return;
        }
        if (mPara.getCategoryCode() == null || mPara.getTypeCode() == null) {
            myTool.showInfo("参数错误，稍后重试！");
            return;
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在添加中...");
        mDialog.show();

        // 刷新数据
        OkHttpUtils.post().url(myTool.getServAdd() + "device/production")
                .addParams("userId", myTool.getManagerId())
                .addParams("signature", myTool.getToken())
                .addParams("deviceCategory", mPara.getCategoryCode())
                .addParams("deviceType", mPara.getTypeCode())
                .addParams("batch", mBatch + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log(response);
                        if (response == null || response.length() == 0 || '{' != response.charAt(0)) {
                            showErrorInfo("参数异常");
                            return;
                        }
                        try {
                            JSONObject obj = new JSONObject(response);

                            switch (obj.getString("response")) {
                                case "success":

                                    mDeviceInfo.clear();

                                    JSONArray array = obj.getJSONArray("devices");
                                    for (int i = 0; i < array.length(); i++) {
                                        DeviceInfo info = new DeviceInfo();

                                        JSONObject deviceObj = array.getJSONObject(i);

                                        info.setName(mPara.getTypeName());
                                        info.setType(mPara.getTypeCode());
                                        info.setId(deviceObj.getString("deviceId"));
                                        info.setVerifyCode(deviceObj.getString("deviceVerification"));
                                        info.setProduceTime(deviceObj.getString("createTime"));

                                        mDeviceInfo.add(info);
                                    }

                                    mDialog.setTitleText("添加成功")
                                            .setContentText("是否现在生产二维码商标？")
                                            .setConfirmText("确定")
                                            .setCancelText("取消")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    mDialog.dismissWithAnimation();
                                                    // 跳转至生产设备,打印二维码
                                                    myTool.startActivity(mDeviceInfo, QRCodePreviewActivity.class);
                                                }
                                            })
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    break;
                            }


                        } catch (JSONException e) {
                            showErrorInfo(e.getMessage());
                        }


                    }
                });


    }

    void showErrorInfo(String err) {
        mDialog.setTitleText("添加失败")
                .setContentText("错误信息[" + err + "],请稍后重试！")
                .setConfirmText("确定")
                .showCancelButton(false)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FOR_TYPE:

                if (resultCode == RESULT_OK) {
                    mPara = (ProducePara) data.getSerializableExtra(KEY_TYPE);

                    if (mPara != null)
                        setText(R.id.tv_type, mPara.getCategoryName() + " > " + mPara.getTypeName());
                }

                break;
        }
    }


    @Override
    public void infoChanged(Object w, Object obj) {
        if ("Dialog4Batch".equals(w.getClass().getSimpleName())) {
            mBatch = (int) obj;
            setText(R.id.tv_batch, mBatch + "个");
        }
    }
}
