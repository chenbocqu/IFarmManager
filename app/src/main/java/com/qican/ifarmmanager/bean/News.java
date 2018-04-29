package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 资讯
 */
public class News implements Serializable {

    String title;
    String newsDesc;
    String time;
    String imgUrl;

    public News() {
    }

    public News(String title, String newsDesc, String time, String imgUrl) {
        this.title = title;
        this.newsDesc = newsDesc;
        this.time = time;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", newsDesc='" + newsDesc + '\'' +
                ", time='" + time + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
