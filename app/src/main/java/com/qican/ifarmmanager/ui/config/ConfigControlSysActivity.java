package com.qican.ifarmmanager.ui.config;

/**
 * 控制系统配置
 */

import android.content.Intent;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ControlSys;
import com.qican.ifarmmanager.bean.Device;
import com.qican.ifarmmanager.bean.Farm;
import com.qican.ifarmmanager.bean.SysType;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.farm.FarmListActivity;
import com.qican.ifarmmanager.ui.qrcode.ScanActivity;
import com.qican.ifarmmanager.ui.sys.SysTypeListActivity;
import com.qican.ifarmmanager.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

import static com.qican.ifarmmanager.ui.farm.FarmListActivity.SYS_LIST;
import static com.qican.ifarmmanager.ui.sys.ControlSysListActivity.KEY_SYS;

public class ConfigControlSysActivity extends TitleBarActivity {

    private static final int REQUEST_FOR_CONTROL_SYS = 1;
    private static final int REQUEST_FOR_SYS_TYPE = 2;
    private static final int REQUEST_DEVICE_INFO = 3;

    String mId, mType, mVerifyCode;
    int mOutBit;

    Device mDevice;
    Farm mFarm;
    SysType mSysType;

    String type;
    EditText edtDeviceId, edtOutBit;
    SweetAlertDialog mDialog;

    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2,
            R.id.tv_item3,
            R.id.tv_item4,
            R.id.tv_item5,
            R.id.tv_item6,
            R.id.tv_item7,
            R.id.tv_item8,
            R.id.tv_item9
    };

    String sysCode;
    String sysCodes[] = {
            "temperature",
            "humidity",
            "ventilate",
            "supplementaryLighting",
            "sunshade",
            "rollerShutters",
            "waterRollerShutters",
            "carbonDioxide",
            "oxygen"
    };

    ControlSys mSys;

    @Override
    public String getUITitle() {
        return "控制系统配置";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_config_control_sys;
    }

    @Override
    public void init() {

        edtDeviceId = findViewById(R.id.edt_device_id);
        edtOutBit = findViewById(R.id.edt_output_bit);

        registerClickListener(R.id.ll_scan);

        registerClickListener(R.id.rl_control_sys);
        registerClickListener(R.id.rl_type);

        registerClickListener(R.id.btn_config);

        for (int tvId : tvItemIds)
            registerClickListener(tvId);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.rl_control_sys:

                if (sysCode == null) {
                    myTool.showInfo("请先选择控制任务！");
                    return;
                }

                // 不同的syscode-XXX，请求不同的控制系统列表
                myTool.startActivityForResult(SYS_LIST + "-" + sysCode, FarmListActivity.class, REQUEST_FOR_CONTROL_SYS);

                break;

            // 设备类型选择
            case R.id.rl_type:
                myTool.startActivityForResult(SysTypeListActivity.class, REQUEST_FOR_SYS_TYPE);
                break;

            // 配置系统
            case R.id.btn_config:
                configSys();
                break;

            // 扫码获取Id
            case R.id.ll_scan:
                myTool.startActivityForResult(ScanActivity.class, REQUEST_DEVICE_INFO);
                break;

        }

        for (int i = 0; i < tvItemIds.length; i++) {
            if (v.getId() == tvItemIds[i])
                choose(i);
        }
    }

    private void configSys() {

        Map<String, String> map = new HashMap<>();

        if (TextUtils.isEmpty(edtDeviceId)) {
            TextUtils.with(this).hintEdt(edtDeviceId, "请输入设备ID！");
            return;
        }

        if (TextUtils.isEmpty(edtOutBit)) {
            TextUtils.with(this).hintEdt(edtOutBit, "请输入终端输出位！");
            return;
        }

        mOutBit = Integer.parseInt(edtOutBit.getText().toString());

        if (mOutBit < 1 || mOutBit > 16) {
            TextUtils.with(this).hintEdt(edtOutBit, "输出位范围有误，请检查！");
            return;
        }


        mId = edtDeviceId.getText().toString();

        map.put("controlDeviceId", mId);

        map.put("systemId", "");

        map.put("controlDeviceBit", mOutBit + "");

        map.put("controlType", "");

        map.put("functionName", "");
        map.put("functionCode", "");

        map.put("terminalIdentifying", "");

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在配置系统...");
        mDialog.show();

        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "farmControl/terminal/addition")
                .addParams("managerId", myTool.getManagerId())
                .addParams("token", myTool.getToken())
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        showFailed(e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log(response);

                        if (response == null) return;

                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getString("response")) {
                                case "success":
                                    mDialog.setTitleText("配置成功")
                                            .setConfirmText("确定")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    mDialog.dismissWithAnimation();

                                                    setResult(RESULT_OK); // 添加成功

                                                    // 延迟退出
                                                    edtDeviceId.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    }, 500);
                                                }
                                            })
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    break;

                                // 无设备或集中器ID
                                case "no_id":
                                    showFailed("系统中没有检索到该集中器，请检查后重试！");
                                    break;

                                case "exist":
                                    showFailed("该设备已被添加，请勿重复添加！");
                                    break;

                                case "error":
                                    showFailed("配置失败，稍后重试！");
                                    break;

                            }

                        } catch (JSONException e) {
                            showFailed(e.getMessage());
                        }
                    }
                });
    }

    private void showFailed(String err) {
        mDialog.setTitleText("配置失败")
                .setContentText(err)
                .setConfirmText("好的")
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
            case REQUEST_FOR_CONTROL_SYS:

                if (resultCode == RESULT_OK) {
                    mSys = (ControlSys) data.getSerializableExtra(KEY_SYS);
                    setText(R.id.tv_farm_name, mSys.getSystemNo());
                }
                break;

            case REQUEST_FOR_SYS_TYPE:
                if (resultCode == RESULT_OK) {

                    mSysType = (SysType) data.getSerializableExtra(SysTypeListActivity.KEY_TYPE);

                    setText(R.id.tv_type_name, mSysType.getSystemType());

                    type = mSysType.getSystemType();

                }
                break;

            case REQUEST_DEVICE_INFO:

                if (resultCode == RESULT_OK) {

                    mId = data.getStringExtra("id");
                    mType = data.getStringExtra("type");
                    mVerifyCode = data.getStringExtra("verifyCode");

                    mDevice = new Device();
                    mDevice.setId(mId);
                    mDevice.setType(mType);
                    mDevice.setVerifyCode(mVerifyCode);

                    setText(R.id.edt_device_id, mId);
                }

                break;
        }
    }

    private void choose(int i) {
        resetView();
        // select one
        selected(i);
    }

    // unselect all
    void resetView() {
        for (int index = 0; index < tvItemIds.length; index++)
            unSelectView(index);
    }

    void selected(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);

        if (tv == null) return;

        int color = getResources().getColor(R.color.white);

        tv.setBackgroundResource(R.drawable.item_selected);
        tv.setTextColor(color);

        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(true);

        // 选择code
        sysCode = sysCodes[i];
    }

    void unSelectView(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);
        if (tv == null) return;

        int color = getResources().getColor(R.color.text_color);
        tv.setBackgroundResource(R.drawable.item_unselected);
        tv.setTextColor(color);

        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(false);
    }

}
