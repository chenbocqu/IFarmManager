package com.qican.ifarmmanager.ui.device;

/**
 * 添加控制设备
 */

import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Device;
import com.qican.ifarmmanager.bean.DeviceType;
import com.qican.ifarmmanager.bean.Farm;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.farm.FarmListActivity;
import com.qican.ifarmmanager.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class AddControllerActivity extends TitleBarActivity {

    private static final int REQUEST_FOR_FARM = 1;
    private static final int REQUEST_FOR_DEVICE_TYPE = 2;
    Farm mFarm;
    DeviceType mDeviceType;
    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2
    };

    String types[] = {
            "环境数据采集", "终端设备控制"
    };

    String type, district;
    EditText edtLocation, edtVersion, edtDistrict, edtCollectorId;
    Device mDevice;
    SweetAlertDialog mDialog;

    @Override
    public String getUITitle() {
        return "第二步";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_acquisitor;
    }

    @Override
    public void init() {

        mDevice = (Device) myTool.getParam(Device.class);

        edtLocation = findViewById(R.id.edt_location);
        edtVersion = findViewById(R.id.edt_version);
        edtDistrict = findViewById(R.id.edt_district);
        edtCollectorId = findViewById(R.id.edt_collector_id);

        registerClickListener(R.id.rl_farm);
        registerClickListener(R.id.tv_item1);
        registerClickListener(R.id.tv_item2);
        registerClickListener(R.id.btn_add);
        registerClickListener(R.id.rl_type);

        TextUtils.with(this).restrictTextLenth(edtLocation, 20, "最长不超过20字");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.rl_farm:
                myTool.startActivityForResult(FarmListActivity.KEY_FARM, FarmListActivity.class, REQUEST_FOR_FARM);
                break;

            // 设备类型选择
            case R.id.rl_type:
                myTool.startActivityForResult(DeviceTypeListActivity.class, REQUEST_FOR_DEVICE_TYPE);
                break;

            // 添加集中器
            case R.id.btn_add:
                toAdd();
                break;

        }
    }

    /**
     * http://localhost:8080/IFarm/device/concentrator/addition
     * post参数：
     * {
     * collectorId：long，
     * farmId：int,  //通过userId去查找对应的farmId
     * collectorLocation:String,
     * collectorType:String, //环境数据采集集中器  和  终端设备控制集中器
     * collectorVersion:String,
     * }
     * <p>
     * 返回结果：
     * {"response":"success"} 或者
     * {"response":"error"}
     */
    private void toAdd() {

        Map<String, String> map = new HashMap<>();

        if (mDevice == null) return;


        if (TextUtils.isEmpty(edtLocation)) {
            TextUtils.with(this).hintEdt(edtLocation, "请输入位置！");
            return;
        }

        if (TextUtils.isEmpty(edtDistrict)) {
            TextUtils.with(this).hintEdt(edtDistrict, "请输入分区！");
            return;
        }

        if (TextUtils.isEmpty(edtCollectorId)) {
            TextUtils.with(this).hintEdt(edtCollectorId, "请输入集中器ID！");
            return;
        }

        if (mFarm == null) {
            myTool.showInfo("请选择要关联的农场！");
            return;
        }

        if (type == null) {
            myTool.showInfo("请选择设备类型！");
            return;
        }

        String collectorId = edtCollectorId.getText().toString();
        String loc = edtLocation.getText().toString();
        district = edtDistrict.getText().toString();

        map.put("deviceId", mDevice.getId());
        map.put("collectorId", collectorId);
        map.put("farmId", mFarm.getId());

        if (!TextUtils.isEmpty(edtVersion)) {
            String version = edtVersion.getText().toString();
            map.put("deviceVersion", version);
        }

        map.put("deviceType", type);
        map.put("deviceDistrict", district);
        map.put("deviceLocation", loc);

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在添加...");
        mDialog.show();

        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "device/collectorDevice/addition")
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
                                    mDialog.setTitleText("添加成功")
                                            .setConfirmText("确定")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    mDialog.dismissWithAnimation();

                                                    setResult(RESULT_OK); // 添加成功

                                                    // 延迟退出
                                                    edtLocation.postDelayed(new Runnable() {
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
                                    showFailed("网络异常请稍后重试！");
                                    break;

                            }

                        } catch (JSONException e) {
                            showFailed(e.getMessage());
                        }
                    }
                });
    }

    private void showFailed(String err) {
        mDialog.setTitleText("添加失败")
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
            case REQUEST_FOR_FARM:

                if (resultCode == RESULT_OK) {

                    mFarm = (Farm) data.getSerializableExtra(FarmListActivity.KEY_FARM);

                    setText(R.id.tv_farm_name, mFarm.getName());

                }
                break;

            case REQUEST_FOR_DEVICE_TYPE:
                if (resultCode == RESULT_OK) {

                    mDeviceType = (DeviceType) data.getSerializableExtra(DeviceTypeListActivity.KEY_TYPE);

                    setText(R.id.tv_type_name, mDeviceType.getName());

                    type = mDeviceType.getCode();

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

        tv.setBackgroundResource(R.drawable.item_selected);
        tv.setTextColor(Color.parseColor("#ffffff"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(true);

        // 选择类型
        type = types[i];
    }

    void unSelectView(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);
        if (tv == null) return;

        tv.setBackgroundResource(R.drawable.item_unselected);
        tv.setTextColor(Color.parseColor("#888888"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(false);
    }
}
