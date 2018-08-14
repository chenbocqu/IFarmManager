/**
 * 设备
 */
package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class Device implements Serializable {

    private String id;
    private String name;
    private String imgUrl;
    private String status;

    String type;       // 设备类型
    String verifyCode; // 验证码
    String deviceType; // 设备类型

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Device() {
    }

    public Device(String name, String status) {
        this.name = name;
        this.status = status;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
