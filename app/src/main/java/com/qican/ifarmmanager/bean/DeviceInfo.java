package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    protected String id;
    protected String collectorId;
    protected String verifyCode;

    protected String name;

    protected String type; // 设备类型编码

    protected String produceTime;
    protected String location;
    protected String creatTime;
    protected String desc;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
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
