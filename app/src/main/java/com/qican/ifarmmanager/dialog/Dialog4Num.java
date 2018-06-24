package com.qican.ifarmmanager.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qican.ifarmmanager.R;
import com.qican.ifarmmanager.listener.PopwindowListener;
import com.qican.ifarmmanager.utils.CommonTools;


public class Dialog4Num extends PopupWindow implements OnClickListener {

    PopwindowListener<Object, Object> mCallBack;

    private TextView tvA, tvB, tvC, tvD, tvE, tvF, tvPrice;
    private ImageView ivCancel;
    private int minPrice = 0, maxPrice = 25;

    private Button btnOK;
    private View mMenuView;
    int mPrice;

    ImageView ivSub, ivAdd;
    CommonTools myTool;

    public Dialog4Num(Activity context, Integer price) {
        super(context);

        myTool = new CommonTools(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.dialog_select_num, null);
        initView(mMenuView);

        selectPrice(price);

        initEvent();
        seWindowStyle();
    }

    private void seWindowStyle() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popwindow_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initEvent() {
        //取消按钮
        btnOK.setOnClickListener(this);

        tvA.setOnClickListener(this);
        tvB.setOnClickListener(this);
        tvC.setOnClickListener(this);
        tvD.setOnClickListener(this);
        tvE.setOnClickListener(this);
        tvF.setOnClickListener(this);

        ivSub.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

        ivCancel.setOnClickListener(this);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void initView(View view) {

        btnOK = (Button) view.findViewById(R.id.btn_ok);

        tvA = (TextView) view.findViewById(R.id.tv_A);
        tvB = (TextView) view.findViewById(R.id.tv_B);
        tvC = (TextView) view.findViewById(R.id.tv_C);
        tvD = (TextView) view.findViewById(R.id.tv_D);
        tvE = (TextView) view.findViewById(R.id.tv_E);
        tvF = (TextView) view.findViewById(R.id.tv_F);

        ivSub = (ImageView) view.findViewById(R.id.iv_sub);
        ivAdd = (ImageView) view.findViewById(R.id.iv_add);

        tvPrice = (TextView) view.findViewById(R.id.tv_price);

        ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);//取消按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_A:
                selectPrice(3);
                break;
            case R.id.tv_B:
                selectPrice(5);
                break;
            case R.id.tv_C:
                selectPrice(7);
                break;
            case R.id.tv_D:
                selectPrice(9);
                break;
            case R.id.tv_E:
                selectPrice(12);
                break;
            case R.id.tv_F:
                selectPrice(15);
                break;

            case R.id.iv_sub:
                selectPrice(--mPrice);
                break;
            case R.id.iv_add:
                selectPrice(++mPrice);
                break;

            case R.id.iv_cancel:
                dismiss();
                break;

            case R.id.btn_ok:
                if (mCallBack != null) mCallBack.infoChanged(this, mPrice);
                dismiss();
                break;
        }
    }

    private void selectPrice(int price) {

        if (mPrice < minPrice) {
            myTool.showInfo("悬赏不低于 " + minPrice + " ！");
            mPrice = minPrice;
            return;
        }

        if (mPrice > maxPrice) {
            myTool.showInfo("悬赏不超过 " + maxPrice + " ！");
            mPrice = maxPrice;
            return;
        }

        mPrice = price;
        switch (mPrice) {
            case 3:
                selectView(tvA);
                break;
            case 5:
                selectView(tvB);
                break;
            case 7:
                selectView(tvC);
                break;
            case 9:
                selectView(tvD);
                break;
            case 12:
                selectView(tvE);
                break;
            case 15:
                selectView(tvF);
                break;
            default:
                resetView();
        }
        tvPrice.setText(mPrice + "");
    }

    /**
     * 选中TextView
     */
    void selectView(TextView tv) {

        resetView();

        tv.setBackgroundResource(R.drawable.item_selected);
        tv.setTextColor(Color.parseColor("#019978"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(true);
    }

    void resetView() {
        unSelectView(tvA);
        unSelectView(tvB);
        unSelectView(tvC);
        unSelectView(tvD);
        unSelectView(tvE);
        unSelectView(tvF);
    }

    void unSelectView(TextView tv) {
        tv.setBackgroundResource(R.drawable.item_unselected);
        tv.setTextColor(Color.parseColor("#888888"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(false);
    }

    public void setOnPowwinListener(PopwindowListener<Object, Object> callBack) {
        this.mCallBack = callBack;
    }
}