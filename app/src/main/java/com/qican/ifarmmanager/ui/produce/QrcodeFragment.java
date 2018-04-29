package com.qican.ifarmmanager.ui.produce;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.DeviceInfo;
import com.qican.ifarmmanager.myprinter.Global;
import com.qican.ifarmmanager.myprinter.WorkService;
import com.qican.ifarmmanager.ui.base.FragmentWithOnResume;
import com.qican.ifarmmanager.utils.CommonTools;
import com.qican.ifarmmanager.utils.ScreenShotUtil;
import com.qican.ifarmmanager.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.qican.ifarmmanager.ui.produce.QRCodePreviewActivity.KEY_DEVICE;

public class QrcodeFragment extends FragmentWithOnResume {

    private static final int SET_IMG = 1;
    private static final String KEY_IMG = "KEY_IMG";
    View view;
    DeviceInfo mDeviceInfo;
    CommonTools myTool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myTool = new CommonTools(getActivity());

        view = inflater.inflate(R.layout.fragment_device_qrcode, container, false);

        mDeviceInfo = (DeviceInfo) getArguments().getSerializable(KEY_DEVICE);

        return view;
    }

    @Override
    public void update() {
        super.update();

        if (mDeviceInfo == null) return;

        setText(R.id.tv_name, mDeviceInfo.getName());
        setText(R.id.tv_time, TimeUtils.formatTimeYear(mDeviceInfo.getProduceTime()));

        setText(R.id.tv_deviceid, mDeviceInfo.getId());
        setText(R.id.tv_verifycode, mDeviceInfo.getVerifyCode());

        // 生产二维码
        generateQRcode();
    }

    private void generateQRcode() {

        final JSONObject object = new JSONObject();

        try {
            object.put("id", mDeviceInfo.getId());
            object.put("type", mDeviceInfo.getType());
            object.put("verycode", mDeviceInfo.getVerifyCode());

        } catch (JSONException e) {
            myTool.showInfo("error : " + e.getMessage());
        }

        new Thread() {
            @Override
            public void run() {
                super.run();

                final Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(object.toString(), 200);

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_IMG, bitmap);
                msg.setData(bundle);
                msg.what = SET_IMG;

                mHandler.sendMessage(msg);


            }
        }.start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SET_IMG:

                    Bundle bundle = msg.getData();
                    Bitmap bm = bundle.getParcelable(KEY_IMG);

                    setImg(R.id.iv_qrcode, bm);
                    break;
            }
        }
    };

    private void setImg(@IdRes int id, Bitmap bm) {
        ImageView iv = view.findViewById(id);
        if (iv != null && bm != null) iv.setImageBitmap(bm);
    }

    public void printQRcode() {
        Bitmap bitmap = ScreenShotUtil.getBitmapFromViewForPrint(view.findViewById(R.id.ll_shangbiao));

        // 控制发送速度
        int nPaperWidth = 384;
        if (bitmap != null) {
            if (WorkService.workThread.isConnected()) {
                Bundle data = new Bundle();
                // data.putParcelable(Global.OBJECT1, mBitmap);
                data.putParcelable(Global.PARCE1, bitmap);
                data.putInt(Global.INTPARA1, nPaperWidth);
                data.putInt(Global.INTPARA2, 0);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_PRINTBWPICTURE, data);
            } else {
                myTool.showInfo("请先用蓝牙连接打印机！");
            }
        }
    }

    // 分享商标

    public void shareQRcode() {
        Bitmap bitmap = ScreenShotUtil.getBitmapFromViewForPrint(view.findViewById(R.id.ll_shangbiao));
        if (!ScreenShotUtil.saveImageAndShare(getActivity(), bitmap)) myTool.showInfo("分享失败！");
    }

}
