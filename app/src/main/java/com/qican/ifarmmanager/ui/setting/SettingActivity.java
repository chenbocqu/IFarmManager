package com.qican.ifarmmanager.ui.setting;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.widget.Toast;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.myprinter.Global;
import com.qican.ifarmmanager.myprinter.WorkService;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;
import com.qican.ifarmmanager.ui.bt.ConnectBTMacActivity;
import com.qican.ifarmmanager.ui.bt.ConnectBTPairedActivity;
import com.qican.ifarmmanager.ui.bt.SearchBTActivity;
import com.qican.ifarmmanager.utils.FileUtils;

import java.lang.ref.WeakReference;

public class SettingActivity extends TitleBarActivity {

    private static Handler mHandler = null;
    private static String TAG = "MainActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public String getUITitle() {
        return "设置";
    }

    @Override
    public void init() {
        registerClickListener(R.id.rl_connect_mac);
        registerClickListener(R.id.rl_paired);
        registerClickListener(R.id.rl_searech_connect);
        registerClickListener(R.id.rl_disconnect);
        registerClickListener(R.id.btn_stop_printer_server);

        // 初始化字符串资源
        InitGlobalString();

        mHandler = new MHandler(this);
        WorkService.addHandler(mHandler);

        if (null == WorkService.workThread) {
            Intent intent = new Intent(this, WorkService.class);
            startService(intent);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // remove the handler
        WorkService.delHandler(mHandler);
        mHandler = null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 连接指定MAC
            case R.id.rl_connect_mac:
                myTool.startActivity(ConnectBTMacActivity.class);
                break;
            // 连接已配对打印机
            case R.id.rl_paired:
                myTool.startActivity(ConnectBTPairedActivity.class);
                break;
            //搜索并连接
            case R.id.rl_searech_connect:
                myTool.startActivity(SearchBTActivity.class);
                break;

            // 断开连接
            case R.id.rl_disconnect:
                WorkService.workThread.disconnectBle();
                WorkService.workThread.disconnectBt();
                WorkService.workThread.disconnectNet();
                WorkService.workThread.disconnectUsb();
                break;

            // 退出服务
            case R.id.btn_stop_printer_server:
                stopService(new Intent(this, WorkService.class));
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void InitGlobalString() {
        Global.toast_success = getString(R.string.toast_success);
        Global.toast_fail = getString(R.string.toast_fail);
        Global.toast_notconnect = getString(R.string.toast_notconnect);
        Global.toast_usbpermit = getString(R.string.toast_usbpermit);
    }

    static class MHandler extends Handler {

        WeakReference<SettingActivity> mActivity;

        MHandler(SettingActivity activity) {
            mActivity = new WeakReference<SettingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SettingActivity theActivity = mActivity.get();
            switch (msg.what) {

            }
        }
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            } else {
                handleSendRaw(intent);
            }
        }
    }

    private void handleSendText(Intent intent) {
        Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            // Update UI to reflect text being shared

            if (WorkService.workThread.isConnected()) {
                byte[] buffer = {0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x39, 0x01}; // 设置中文，切换双字节编码。
                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                WorkService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
            }
            if (WorkService.workThread.isConnected()) {
                String path = textUri.getPath();
                String strText = FileUtils.ReadToString(path);
                byte buffer[] = strText.getBytes();

                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                data.putInt(Global.INTPARA3, 128);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_WRITE_BT_FLOWCONTROL, data);

            } else {
                Toast.makeText(this, Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }

    private void handleSendRaw(Intent intent) {
        Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            // Update UI to reflect text being shared
            if (WorkService.workThread.isConnected()) {
                String path = textUri.getPath();
                byte buffer[] = FileUtils.ReadToMem(path);
                // Toast.makeText(this, "length:" + buffer.length,
                // Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                data.putInt(Global.INTPARA3, 256);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_WRITE_BT_FLOWCONTROL, data);

            } else {
                Toast.makeText(this, Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }

            // finish();
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            String path = getRealPathFromURI(imageUri);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            opts.inJustDecodeBounds = false;
            if (opts.outWidth > 1200) {
                opts.inSampleSize = opts.outWidth / 1200;
            }

            Bitmap mBitmap = BitmapFactory.decodeFile(path);

            if (mBitmap != null) {
                if (WorkService.workThread.isConnected()) {
                    Bundle data = new Bundle();
                    data.putParcelable(Global.PARCE1, mBitmap);
                    data.putInt(Global.INTPARA1, 384);
                    data.putInt(Global.INTPARA2, 0);
                    WorkService.workThread.handleCmd(
                            Global.CMD_POS_PRINTPICTURE, data);
                } else {
                    Toast.makeText(this, Global.toast_notconnect,
                            Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null,
                null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
