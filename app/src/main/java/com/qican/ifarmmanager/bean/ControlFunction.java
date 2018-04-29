package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class ControlFunction implements Serializable {
    public final static String funType[] = new String[]{"浇灌", "施肥", "施药", "通风", "温度", "湿度", "光照", "遮阳"};// "二氧化碳", "氧气",
    private String id;
    private String name;
    private String desc;//描述
    private String imgUrl;//图片
    private int index;//对应的索引

    public static String getFunType(int i) {
        if (i >= funType.length)
            return "";
        else
            return funType[i];
    }

    public ControlFunction(String name) {
        this.name = name;
        setIndex();
    }

    private void setIndex() {
        if (this.name == null) {
            return;
        }
        switch (this.name) {
            case "灌溉":
                this.index = 0;
                break;
            case "施肥":
                this.index = 1;
                break;
            case "施药":
                this.index = 2;
                break;
            case "通风":
                this.index = 3;
                break;
            case "温度":
                this.index = 4;
                break;
            case "湿度":
                this.index = 5;
                break;
//            case "CO2":
//                this.index = 5;
//                break;
//            case "O2":
//                this.index = 6;
//                break;
            case "光照":
                this.index = 6;
                break;
            case "遮阳":
                this.index = 7;
                break;
            case "新功能":
                this.index = 8;
                break;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
