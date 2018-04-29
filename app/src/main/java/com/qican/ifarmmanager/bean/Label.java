/**
 * 标签，农场标签，人的标签
 */
package com.qican.ifarmmanager.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Label implements Serializable {
    public final static String LABEL_SPLIT = ",";

    private List<String> labelList;
    private String[] labelArr;
    private String labelStr = "";

    public Label() {
        labelList = new ArrayList<>();
    }

    public Label(@NonNull String labelStr) {
        setLabelStr(labelStr);
    }

    public Label(@NonNull List<String> labelList) {
        setLabelList(labelList);
    }

    public Label(@NonNull String[] labelArr) {
        setLabelArr(labelArr);
    }


    /**
     * 通过List得到Str
     *
     * @param labelList
     * @return
     */
    public String getStrByList(List<String> labelList) {
        String str = "";
        for (int i = 0; i < labelList.size(); i++) {
            if ("".equals(str))
                str = labelList.get(i);
            else
                str = str + LABEL_SPLIT + labelList.get(i);
        }
        return str;
    }

    public List<String> getListByArr(String[] labelArr) {
        List<String> labelList = new ArrayList<>();
        for (int i = 0; i < labelArr.length; i++) {
            labelList.add(labelArr[i]);
        }
        return labelList;
    }

    public String[] getArrByStr(String labelStr) {
        return labelStr.split(LABEL_SPLIT);
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
        this.labelStr = getStrByList(labelList);
        this.labelArr = getArrByStr(this.labelStr);
    }

    public String[] getLabelArr() {
        return labelArr;
    }

    public void setLabelArr(String[] labelArr) {
        this.labelArr = labelArr;
        this.labelList = getListByArr(labelArr);
        this.labelStr = getStrByList(this.labelList);
    }

    public String getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(String labelStr) {
        this.labelStr = labelStr;
        this.labelArr = labelStr.split(LABEL_SPLIT);
        this.labelList = getListByArr(this.labelArr);
    }

    @Override
    public String toString() {
        return "Label{" +
                "labelList=" + labelList +
                ", labelArr=" + Arrays.toString(labelArr) +
                ", labelStr='" + labelStr + '\'' +
                '}';
    }

    public void addLabel(String label) {
        this.labelList.add(label);
        this.setLabelList(this.labelList);
    }
}
