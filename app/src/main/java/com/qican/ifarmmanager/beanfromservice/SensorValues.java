package com.qican.ifarmmanager.beanfromservice;


import java.io.Serializable;

public class SensorValues implements Serializable {
    private Integer sensorValueId;
    private String sensorId;
    private String sensorParamCode;
    private Double sensorValues;
    private String updateTime;

    public void setSensorValueId(Integer sensorValueId) {
        this.sensorValueId = sensorValueId;
    }

    public Integer getSensorValueId() {
        return sensorValueId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorParamCode(String sensorParamCode) {
        this.sensorParamCode = sensorParamCode;
    }

    public String getSensorParamCode() {
        return sensorParamCode;
    }

    public void setSensorValues(Double sensorValues) {
        this.sensorValues = sensorValues;
    }

    public Double getSensorValues() {
        return sensorValues;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
