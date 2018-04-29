package com.qican.ifarmmanager.ui.produce;

import android.graphics.Bitmap;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.ui.base.BaseActivityWithTitlebar;
import com.qican.ifarmmanager.utils.ScreenShotUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class ProduceActivity extends BaseActivityWithTitlebar {
    @Override
    protected int getContentView() {
        return R.layout.activity_produce;
    }

    @Override
    public String getUITitle() {
        return "生产设备";
    }

    @Override
    public void init() {

        final JSONObject object = new JSONObject();

        try {
            object.put("id", "100000012");
            object.put("type", "camera");
            object.put("verycode", "SEGDPH");

        } catch (JSONException e) {
            myTool.showInfo("error : " + e.getMessage());
        }

        new Thread() {
            @Override
            public void run() {
                super.run();

                final Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(object.toString(), 200);
                if (bitmap != null) runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setImg(R.id.iv_qrcode, bitmap);

                        print();
                    }


                });

            }
        }.start();
    }

    private void print() {
        myTool.log("分享商标！");
        Bitmap bitmap = ScreenShotUtil.getBitmapFromViewForPrint(findViewById(R.id.ll_shangbiao));
//        if (!ScreenShotUtil.saveImageAndShare(this, bitmap)) myTool.showInfo("分享失败！");
    }


}
