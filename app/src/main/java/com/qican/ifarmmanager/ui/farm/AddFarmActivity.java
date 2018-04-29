package com.qican.ifarmmanager.ui.farm;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.ComUser;
import com.qican.ifarmmanager.bean.Label;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.login.LoginActivity;
import com.qican.ifarmmanager.utils.CommonTools;
import com.qican.ifarmmanager.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class AddFarmActivity extends TitleBarActivity {
    private static final int REQUEST_LABEL = 1;
    private static final int LABEL_MAX_SHOW_LEN = 10;//能标签显示的最大长度
    private CommonTools myTool;
    private String farmName, farmDesc;
    private SweetAlertDialog mDialog;

    EditText edtFarmName, edtFarmDesc;

    List<TextView> tvLabels;

    TextView tvMore;
    TextView tvChoose;

    private List<String> labels;
    private Label farmLabel;
    private String labelStr = "";

    @Override
    public String getUITitle() {
        return "添加农场";
    }

    /**
     * 添加农场
     */
    void addFarm() {

        if (TextUtils.isEmpty(edtFarmName)) {
            TextUtils.with(this).hintEdt(edtFarmName, "农场名不能为空哦！");
            return;
        }

        if (TextUtils.isEmpty(edtFarmDesc)) {
            TextUtils.with(this).hintEdt(edtFarmDesc, "还没有输入描述唷！");
            return;
        }

        farmName = edtFarmName.getText().toString().trim();
        farmDesc = edtFarmDesc.getText().toString().trim();

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在添加···");
        mDialog.showContentText(false);
        mDialog.show();

        ComUser user = myTool.getUserInfo();
        if (user == null) return;

        OkHttpUtils.post().url(myTool.getServAdd() + "farm/manager/addFarm")
                .addParams("managerId", myTool.getManagerId())
                .addParams("token", myTool.getToken())

                .addParams("userId", user.getId())
                .addParams("farmName", farmName)
                .addParams("farmDescribe", farmDesc)
                .addParams("farmLabel", labelStr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        addFailed("异常信息：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log("添加农场response：" + response);

                        if ("lose efficacy".equals(response)){
                            // 需要重新获取token
                            myTool.startActivity(LoginActivity.class);
                            myTool.showInfo("Token失效，请重新登陆！");
                        }

                        try {
                            JSONObject obj = new JSONObject(response);

                            switch (obj.getString("response")) {
                                case "success":
                                    mDialog.setTitleText("添加成功")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    edtFarmName.postDelayed(new Runnable() {
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
                                    addFailed("服务器异常！");
                                    break;
                            }

                        } catch (JSONException e) {
                            addFailed(e.getMessage());
                        }
                    }
                });
    }


    private void addFailed(String msg) {
        mDialog.setTitleText("添加失败")
                .setContentText(msg)
                .setConfirmText("重试")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        addFarm();
                    }
                })
                .setCancelText("取消")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LABEL && resultCode == RESULT_OK) {
            //返回结果直接给我的收货地址
            farmLabel = (Label) data.getSerializableExtra(AddLabelActivity.KEY_LABEL);
            labelStr = farmLabel.getStrByList(farmLabel.getLabelList());
            setTvByLabel(farmLabel.getLabelList());
            myTool.log(farmLabel.toString() + "labelStr:" + labelStr);
        }
    }

    /**
     * 通过数据设置标签显示
     *
     * @param labelList
     */
    private void setTvByLabel(List<String> labelList) {
        boolean showMore = false;
        tvChoose.setVisibility(labelList.isEmpty() ? View.VISIBLE : View.GONE);
        //全部设置为不见
        for (int i = 0; i < tvLabels.size(); i++) {
            tvLabels.get(i).setVisibility(View.GONE);
        }
        int totalLen = 0;
        for (int i = 0; i < tvLabels.size() && i < labelList.size(); i++) {
            totalLen = totalLen + labelList.get(i).length();
            if (totalLen > LABEL_MAX_SHOW_LEN) {
                showMore = true;
                break;
            }
            tvLabels.get(i).setVisibility(View.VISIBLE);
            tvLabels.get(i).setText(labelList.get(i));
        }
        // 本身比标签容量多，或者显示不够完全，则显示更多
        tvMore.setVisibility(
                labelList.size() > tvLabels.size() ||
                        showMore ?
                        View.VISIBLE : View.GONE);
    }

    @Override
    public void init() {
        myTool = new CommonTools(this);
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        labels = new ArrayList<>();
        farmLabel = new Label();

        tvChoose = findViewById(R.id.tvChoose);
        tvMore = findViewById(R.id.tvMore);
        edtFarmName = findViewById(R.id.edtFarmName);
        edtFarmDesc = findViewById(R.id.edtFarmDesc);

        tvLabels = new ArrayList<>();
        tvLabels.add((TextView) findViewById(R.id.tvLabel1));
        tvLabels.add((TextView) findViewById(R.id.tvLabel2));
        tvLabels.add((TextView) findViewById(R.id.tvLabel3));

        registerClickListener(R.id.btnAdd);
        registerClickListener(R.id.rlLabel);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_addfarm;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnAdd:
                addFarm();
                break;
            case R.id.rlLabel:
                myTool.startActivityForResult(farmLabel, AddLabelActivity.class, REQUEST_LABEL);
                break;
        }
    }
}
