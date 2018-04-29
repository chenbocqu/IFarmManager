package com.qican.ifarmmanager.bean;

import java.util.ArrayList;

public class Question {

    String title;
    String desc;

    ArrayList<String> imgUrls;
    String time;

    public Question() {
    }

    public Question(String title, String desc, ArrayList<String> imgUrls, String time) {
        this.title = title;
        this.desc = desc;
        this.imgUrls = imgUrls;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", imgUrls=" + imgUrls +
                ", time='" + time + '\'' +
                '}';
    }
}
