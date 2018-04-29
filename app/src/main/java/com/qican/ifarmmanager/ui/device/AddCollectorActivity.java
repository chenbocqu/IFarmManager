package com.qican.ifarmmanager.ui.device;

/**
 * 添加集中器
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Device;
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

public class AddCollectorActivity extends TitleBarActivity {

    private static final int REQUEST_FOR_FARM = 1;
    public static final int RESULT_EXIST = 2;

    Farm mFarm;
    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2
    };

    String types[] = {
            "环境数据采集", "终端设备控制"
    };

    String type;
    EditText edtLocation, edtVersion;
    Device mDevice;
    SweetAlertDialog mDialog;

    @Override
    public String getUITitle() {
        return "第二步";
    }

    @Override
    public void init() {

        mDevice = (Device) myTool.getParam(Device.class);

        edtLocation = findViewById(R.id.edt_location);
        edtVersion = findViewById(R.id.edt_version);

        registerClickListener(R.id.rl_farm);
        registerClickListener(R.id.tv_item1);
        registerClickListener(R.id.tv_item2);
        registerClickListener(R.id.btn_add);

        choose(0);

        TextUtils.with(this).restrictTextLenth(edtLocation, 20, "最长不超过20字");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_collector;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.rl_farm:
                myTool.startActivityForResult(FarmListActivity.KEY_FARM, FarmListActivity.class, REQUEST_FOR_FARM);
                break;

            case R.id.tv_item1:
                choose(0);
                break;

            case R.id.tv_item2:
                choose(1);
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

        if (mFarm == null) {
            myTool.showInfo("请选择要关联的农场！");
            return;
        }

        if (TextUtils.isEmpty(edtLocation)) {
            TextUtils.with(this).hintEdt(edtLocation, "请输入位置！");
            return;
        }

        String loc = edtLocation.getText().toString();


        map.put("collectorId", mDevice.getId());
        map.put("farmId", mFarm.getId());
        map.put("collectorLocation", loc);
        map.put("collectorType", type);

        if (!TextUtils.isEmpty(edtVersion)) {
            String version = edtVersion.getText().toString();
            map.put("collectorVersion", version);
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在添加...");
        mDialog.show();

        OkHttpUtils.post().url(myTool.getServAdd() + "device/concentrator/addition")
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

                                case "exist":
                                    mDialog.setTitleText("添加失败")
                                            .setConfirmText("确定")
                                            .setContentText("设备已绑定，请重新选择设备添加！")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    mDialog.dismissWithAnimation();
                                                    setResult(RESULT_EXIST);
                                                    // 延迟退出
                                                    edtLocation.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    }, 500);

                                                }
                                            })
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);

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
                .setConfirmText("重试")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                        toAdd();
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
