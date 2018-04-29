package com.qican.ifarmmanager.beanfromservice;

public class FarmSensor {
    private String sensorId;
    private Integer farmId;
    private String collectorId;
    private String sensorVersion;
    private String sensorType;
    private String sensorLocation;
    private String sensorCreateTime;

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setSensorVersion(String sensorVersion) {
        this.sensorVersion = sensorVersion;
    }

    public String getSensorVersion() {
        return sensorVersion;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorLocation(String sensorLocation) {
        this.sensorLocation = sensorLocation;
    }

    public String getSensorLocation() {
        return sensorLocation;
    }

    public void setSensorCreateTime(String sensorCreateTime) {
        this.sensorCreateTime = sensorCreateTime;
    }

    public String getSensorCreateTime() {
        return sensorCreateTime;
    }
}
