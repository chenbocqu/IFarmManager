package com.qican.ifarmmanager.bean;

public class Expert {
    String name;
    String imgUrl;
    String desc;

    public Expert() {
    }

    public Expert(String name, String imgUrl, String desc) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.desc = desc;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
