package com.qican.ifarmmanager.ui.sys;

/**
 * 添加水肥药控制系统
 */

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Farm;
import com.qican.ifarmmanager.dialog.Dialog4Num;
import com.qican.ifarmmanager.listener.PopwindowListener;
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

public class AddWfmSysActivity extends TitleBarActivity {

    private static final int REQUEST_FOR_FARM = 1;
    private static final int REQUEST_FOR_SYS_TYPE = 2;
    Farm mFarm;

    EditText edtLocation, edtDistrict, edtSysNo, edtDesc;
    SweetAlertDialog mDialog;
    int feiNum = 3, yaoNum = 3, areaNum = 5;
    Dialog4Num dialog4FeiNum, dialog4YaoNum, dialog4AreaNum;

    @Override
    public String getUITitle() {
        return "添加水肥系统";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_wfm_sys;
    }

    @Override
    public void init() {

        edtLocation = findViewById(R.id.edt_location);
        edtDistrict = findViewById(R.id.edt_district);
        edtSysNo = findViewById(R.id.edt_no);
        edtDesc = findViewById(R.id.edt_desc);

        registerClickListener(R.id.rl_farm);
        registerClickListener(R.id.btn_add);
        registerClickListener(R.id.rl_type);

        registerClickListener(R.id.rl_fei);
        registerClickListener(R.id.rl_yao);
        registerClickListener(R.id.rl_area);

        TextUtils.with(this).restrictTextLenth(edtLocation, 20, "最长不超过20字");

        // 设置初始显示
        setText(R.id.tv_feiguan_num, feiNum + " 个");
        setText(R.id.tv_yao_num, yaoNum + " 个");
        setText(R.id.tv_area_num, areaNum + " 个");

        dialog4FeiNum = new Dialog4Num(this, feiNum);
        dialog4YaoNum = new Dialog4Num(this, yaoNum);
        dialog4AreaNum = new Dialog4Num(this, areaNum);

        initEvents();
    }

    private void initEvents() {

        dialog4FeiNum.setOnPowwinListener(new PopwindowListener<Object, Object>() {
            @Override
            public void infoChanged(Object w, Object obj) {

                feiNum = (int) obj;
                setText(R.id.tv_feiguan_num, feiNum + " 个");

            }
        });

        dialog4YaoNum.setOnPowwinListener(new PopwindowListener<Object, Object>() {
            @Override
            public void infoChanged(Object w, Object obj) {

                yaoNum = (int) obj;
                setText(R.id.tv_yao_num, yaoNum + " 个");

            }
        });

        dialog4AreaNum.setOnPowwinListener(new PopwindowListener<Object, Object>() {
            @Override
            public void infoChanged(Object w, Object obj) {

                areaNum = (int) obj;
                setText(R.id.tv_area_num, areaNum + " 个");

            }
        });

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
//                myTool.startActivityForResult(SysTypeListActivity.class, REQUEST_FOR_SYS_TYPE);
                break;

            // 添加水肥系统
            case R.id.btn_add:
                toAdd();
                break;

            case R.id.rl_fei:
                myTool.showPopFormBottom(dialog4FeiNum, findViewById(R.id.ll_main));
                break;

            case R.id.rl_yao:
                myTool.showPopFormBottom(dialog4YaoNum, findViewById(R.id.ll_main));
                break;

            case R.id.rl_area:
                myTool.showPopFormBottom(dialog4AreaNum, findViewById(R.id.ll_main));
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

        if (TextUtils.isEmpty(edtLocation)) {
            TextUtils.with(this).hintEdt(edtLocation, "请输入位置！");
            return;
        }

        if (TextUtils.isEmpty(edtDistrict)) {
            TextUtils.with(this).hintEdt(edtDistrict, "请输入分区！");
            return;
        }

        if (TextUtils.isEmpty(edtSysNo)) {
            TextUtils.with(this).hintEdt(edtSysNo, "请输入系统编号");
            return;
        }

        if (TextUtils.isEmpty(edtDesc)) {
            TextUtils.with(this).hintEdt(edtDesc, "请输入系统描述");
            return;
        }

        if (mFarm == null) {
            myTool.showInfo("请选择要关联的农场！");
            return;
        }

        String loc = edtLocation.getText().toString();
        String district = edtDistrict.getText().toString();
        String sysNo = edtSysNo.getText().toString();
        String desc = edtDesc.getText().toString();

        map.put("managerId", myTool.getManagerId());
        map.put("token", myTool.getToken());

        map.put("farmId", mFarm.getId());

        map.put("systemCode", "waterFertilizerMedicine");
        map.put("systemType", "水肥药系统");
        map.put("systemTypeCode", "waterFertilizerMedicineControl");

        map.put("systemDistrict", district);
        map.put("systemNo", sysNo);
        map.put("systemLocation", loc);
        map.put("systemDescription", desc);

        map.put("fertierNum", feiNum + "");
        map.put("medicineNum", yaoNum + "");
        map.put("districtNum", areaNum + "");

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在添加...");
        mDialog.show();

        // 采集设备添加
        OkHttpUtils.post().url(myTool.getServAdd() + "farmControlSystem/wfm/addition")
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
                                    mDialog.setTitleText("系统添加成功")
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

                                case "error":
                                    showFailed("添加失败，请稍后重试！");
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

            case REQUEST_FOR_SYS_TYPE:
                if (resultCode == RESULT_OK) {
                }
                break;
        }
    }
}
