package com.qican.ifarmmanager.utils;


import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

public class TextUtils {

    private Activity mActivity;
    private CommonTools myTool;

    private TextUtils(Activity mActivity) {
        this.mActivity = mActivity;
        myTool = new CommonTools(mActivity);
    }

    public static TextUtils with(Activity activity) {
        return new TextUtils(activity);
    }

    // 限制EditText的最大长度，并给出提示
    public TextUtils restrictTextLenth(final EditText edt, final int maxLen, final String hint) {
        return restrictTextLenth(edt, maxLen, hint, null);
    }

    public TextUtils restrictTextLenth(final EditText edt, final int maxLen, final String hint, final TextView tv) {

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                int len = input.length();
                // 最长maxLen个字符
                if (len > maxLen) {
                    input = input.substring(0, maxLen);
                    edt.setText(input);
                    edt.setSelection(input.length());//将光标移至文字末尾
                    myTool.showInfo(hint);          // myTool.showInfo("最大长度不超过" + maxLen + "！");
                }
                if (tv != null && len <= maxLen) tv.setText(len + "/" + maxLen);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }

    // 抖动EditText，并给出错误提示
    public TextUtils hintEdt(final EditText edt, final String info) {

        YoYo.with(Techniques.Shake)
                .duration(800)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        edt.requestFocus();
                        edt.setError(info);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(edt);

        return this;
    }


    public static boolean isEmpty(EditText edt) {

        if (edt == null) return true;

        String info = edt.getText().toString().trim();

        return info.isEmpty();
    }
}
