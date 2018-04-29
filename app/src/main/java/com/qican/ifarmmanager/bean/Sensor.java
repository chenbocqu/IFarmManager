/**
 * @Fun 传感器
 * @Com 祺璨科技
 * @Author 陈波
 * @Time 2017-2-16
 */
package com.qican.ifarmmanager.bean;

import com.qican.ifarmmanager.beanfromservice.FarmSensor;

import java.io.Serializable;

public class Sensor implements Serializable {

    private String id;
    private String farmId;
    private String collectorId;
    private String version;
    private String type;
    private String location;
    private String createTime;

    public Sensor() {
    }

    /**
     * 从数据库类型中转化为本地设计的bean
     *
     * @param sensor Database中的传感器实例
     */
    public Sensor(FarmSensor sensor) {
        this.setId(sensor.getSensorId());
        this.setFarmId(String.valueOf(sensor.getFarmId()));
        this.setCollectorId(sensor.getCollectorId());
        this.setVersion(sensor.getSensorVersion());
        this.setType(sensor.getSensorType());
        this.setLocation(sensor.getSensorLocation());
        this.setCreateTime(String.valueOf(sensor.getSensorCreateTime()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", farmId='" + farmId + '\'' +
                ", collectorId='" + collectorId + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
