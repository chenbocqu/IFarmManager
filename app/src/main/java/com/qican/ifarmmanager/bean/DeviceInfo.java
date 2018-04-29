package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    String id;
    String verifyCode;

    String name;

    String type; // 设备类型编码

    String produceTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(String produceTime) {
        this.produceTime = produceTime;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id='" + id + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", produceTime='" + produceTime + '\'' +
                '}';
    }
}
