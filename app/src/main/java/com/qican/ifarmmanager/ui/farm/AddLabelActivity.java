package com.qican.ifarmmanager.ui.farm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.Label;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddLabelActivity extends TitleBarActivity {
    public static final String KEY_LABEL = "KEY_LABEL";
    private Label label;
    private List<String> mDatas;

    EditText edtLabel;
    GridView gvLabel;

    private CommonAdapter<String> mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_addlabel;
    }

    @Override
    public void init() {

        edtLabel = findViewById(R.id.edtLabel);
        gvLabel = findViewById(R.id.gvLabel);

        registerClickListener(R.id.tvDelAll);
        registerClickListener(R.id.tvAdd);

        label = (Label) myTool.getParam(Label.class);
        mDatas = label.getLabelList();

        mAdapter = new CommonAdapter<String>(this, R.layout.item_label, mDatas) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String label, int position) {
                helper.setText(R.id.tvLabel, label);
                RelativeLayout rl = helper.getView(R.id.rl_item);
                MyClickListener mListener = new MyClickListener(label);
                rl.setOnClickListener(mListener);
            }
        };
        gvLabel.setAdapter(mAdapter);

        setRightMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(KEY_LABEL, label);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    class MyClickListener implements View.OnClickListener {
        private String item;

        public MyClickListener(String item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            new SweetAlertDialog(AddLabelActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("删除标签")
                    .setContentText("确认删除标签[" + item + "]吗？")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog dialog) {
                            dialog.dismissWithAnimation();

                            mDatas.remove(item);

                            replaceAll(mDatas);
                        }
                    })
                    .setCancelText("取消")
                    .show();
        }
    }

    void tvAdd() {
        String label = edtLabel.getText().toString().trim();
        if ("".equals(label)) {
            return;
        }
        if (label.length() > 7) {
            shakeEdtWithMsg(edtLabel, "标签长度不超过7哦！");
            return;
        }
        edtLabel.setText("");
        mDatas.add(label);

        mAdapter.replaceAll(mDatas);
    }

    private void shakeEdtWithMsg(final EditText edt, final String msg) {
        String info = edt.getText().toString().trim();
        YoYo.with(Techniques.Shake)
                .duration(1000)
                .withListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        edt.requestFocus();
                        edt.setError(msg);
                    }
                })
                .playOn(edt);
    }


    void tvDelAll() {
        // 删除所有标签
        mDatas.clear();
        mAdapter.replaceAll(mDatas);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvAdd:
                tvAdd();
                break;
            case R.id.tvDelAll:
                tvDelAll();
                break;
        }
    }

    public void addAll(List<String> data) {
        mAdapter.addAll(data);
    }

    public void add(@NonNull String item) {
        mAdapter.add(item);
    }

    public void remove(@NonNull String item) {
        mAdapter.remove(item);
    }

    public void replaceAll(List<String> data) {
        mAdapter.replaceAll(data);
    }
}
