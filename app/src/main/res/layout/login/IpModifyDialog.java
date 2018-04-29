/**
 * 配置ip地址
 */
package com.cqu.stuexpress.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cqu.stuexpress.R;
import com.cqu.stuexpress.utils.CommonTools;


public class IpModifyDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private CommonTools myTool;
    ImageView ivCannel;
    Button btnOk;
    EditText etIp1, etIp2, etIp3, etIp4;
    int ip1 = 0, ip2 = 0, ip3 = 0, ip4 = 0;
    String iPAdd;


    public IpModifyDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public IpModifyDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ipmodify);
        this.setCanceledOnTouchOutside(false);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        ivCannel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void initData() {
        iPAdd = myTool.getIP();
        String[] ip = iPAdd.split(".");
        if (ip.length == 4) {
            etIp1.setText(ip[0]);
            etIp2.setText(ip[1]);
            etIp3.setText(ip[2]);
            etIp4.setText(ip[3]);
        }
    }

    private void initView() {
        myTool = new CommonTools(mContext);
        ivCannel = (ImageView) findViewById(R.id.iv_cannel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        etIp1 = (EditText) findViewById(R.id.et_ip1);
        etIp2 = (EditText) findViewById(R.id.et_ip2);
        etIp3 = (EditText) findViewById(R.id.et_ip3);
        etIp4 = (EditText) findViewById(R.id.et_ip4);
    }


    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
                initData();
            } catch (Exception e) {
                Log.i("DEBUG", "show: " + e.getMessage());
            }
        }
    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cannel:
                dismiss();
                break;
            case R.id.btn_ok:
                saveIP();
                break;
        }
    }

    /**
     * 保存IP
     */
    private void saveIP() {

        if ("".equals(etIp1.getText().toString().trim()) ||
                "".equals(etIp2.getText().toString().trim()) ||
                "".equals(etIp3.getText().toString().trim()) ||
                "".equals(etIp4.getText().toString().trim())) {
            myTool.showInfo("请输入完整的IP地址！");
            return;
        }

        ip1 = Integer.parseInt(etIp1.getText().toString().trim());
        ip2 = Integer.parseInt(etIp2.getText().toString().trim());
        ip3 = Integer.parseInt(etIp3.getText().toString().trim());
        ip4 = Integer.parseInt(etIp4.getText().toString().trim());

        if (ip1 > 255 || ip1 < 0 ||
                ip2 > 255 || ip2 < 0 ||
                ip3 > 255 || ip3 < 0 ||
                ip4 > 255 || ip4 < 0) {
            myTool.showInfo("IP格式有误，请检查！");
            return;
        }

        iPAdd = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
        myTool.setIP(iPAdd);

        myTool.showInfo("服务器IP配置成功！");
        etIp1.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 500); //延迟退出
    }
}
