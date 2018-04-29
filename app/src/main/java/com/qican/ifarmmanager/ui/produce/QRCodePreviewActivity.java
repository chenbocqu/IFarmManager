package com.qican.ifarmmanager.ui.produce;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.bean.DeviceInfo;
import com.qican.ifarmmanager.ui.base.TitleBarActivity;

import java.util.ArrayList;
import java.util.List;

public class QRCodePreviewActivity extends TitleBarActivity implements ViewPager.OnPageChangeListener {

    public static final String KEY_DEVICE = "KEY_DEVICE";
    FragmentPagerAdapter mAdapter;
    List<QrcodeFragment> mFragments;
    ViewPager mViewPage;
    ArrayList<DeviceInfo> mData;
    int curIndex = 0, pageIndex = 0;
    TextView tvDeviceId, tvVerifyCode;

    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode_preview;
    }

    @Override
    public void init() {
        startWifi();
        mData = (ArrayList<DeviceInfo>) myTool.getParam(ArrayList.class);

        tvDeviceId = findViewById(R.id.tv_deviceid);
        tvVerifyCode = findViewById(R.id.tv_verifycode);

        mViewPage = findViewById(R.id.viewpager);
        registerClickListener(R.id.btn_print_qrcode);
        registerClickListener(R.id.iv_share);

        loadFragment();
    }

    private void startWifi() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                /* 启动蓝牙 */
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null != adapter) {
                    if (!adapter.isEnabled()) {
                        if (adapter.enable()) {
                            // while(!adapter.isEnabled());
                            Log.v("SearchBTActivity", "Enable BluetoothAdapter");
                        } else {
                            myTool.showInfo("打开蓝牙失败，请手动打开！");
                            return;
                        }
                    }
                }

				/* 启动WIFI */
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                switch (wifiManager.getWifiState()) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        wifiManager.setWifiEnabled(true);
                        break;
                    default:
                        break;
                }
            }
        }, 1000);
    }

    private void loadFragment() {

        mFragments = new ArrayList<>();

        for (DeviceInfo data : mData) {
            QrcodeFragment fg = new QrcodeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_DEVICE, data);
            fg.setArguments(bundle);
            mFragments.add(fg);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        mViewPage.setAdapter(mAdapter);
        mViewPage.setOnPageChangeListener(this);

        mViewPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 初始选择第一个
                if (!mData.isEmpty()) {

                    DeviceInfo info = mData.get(curIndex);

                    setText(R.id.tv_page, (curIndex + 1) + "/" + mData.size());
                    setTitle(info.getName());

                    tvDeviceId.setText(info.getId());
                    tvVerifyCode.setText(info.getVerifyCode());

                    mFragments.get(curIndex).update();
                }
            }
        }, 200);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        // 设置title
        if (position < mData.size())
            setTitle(mData.get(position).getName());

        curIndex = position;
        setText(R.id.tv_page, (curIndex + 1) + "/" + mData.size());
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        // 0是停完全,分发update函数
        if (state == 0)
            refreshCurPage(); // 刷新
    }

    private void refreshCurPage() {

        if (pageIndex == curIndex) return;

        pageIndex = curIndex;

        DeviceInfo info = mData.get(pageIndex);

        setText(R.id.tv_page, (pageIndex + 1) + "/" + mData.size());
        setTitle(info.getName());

        tvDeviceId.setText(info.getId());
        tvVerifyCode.setText(info.getVerifyCode());

        mFragments.get(pageIndex).update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mFragments.isEmpty()) mFragments.get(curIndex).update();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_print_qrcode:
                // 打印当前页的二维码
                mFragments.get(curIndex).printQRcode();
                break;
            case R.id.iv_share:
                // 分享二维码
                mFragments.get(curIndex).shareQRcode();
                break;
        }
    }
}
