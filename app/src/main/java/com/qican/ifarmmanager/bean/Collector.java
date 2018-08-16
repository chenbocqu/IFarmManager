package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 集中器
 */

public class Collector implements Serializable {

    String id;
    String location;
    String version;
    String type;
    String createTime;
    String farmId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    @Override
    public String toString() {
        return "Collector{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", createTime='" + createTime + '\'' +
                ", farmId='" + farmId + '\'' +
                '}';
    }

}
