package com.qican.ifarmmanager.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qican.ifarmmanager.utils.CommonTools;


public class FragmentWithOnResume extends Fragment {

    protected CommonTools myTool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myTool = new CommonTools(getActivity());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // 使用此方法更新UI
    public void update() {

    }

    // 设置文本，此类方法都在update()中调用
    protected void setText(@IdRes int id, String text) {

        if (getView() == null) return;

        TextView tv = (TextView) getView().findViewById(id);

        if (tv != null && text != null)
            tv.setText(text);
    }
}
