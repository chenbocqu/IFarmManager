package com.qican.ifarmmanager.beanfromservice;

import java.io.Serializable;

public class FarmPara implements Serializable {

    private String sensorId;
    private String name;
    private String value;
    private String time;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FarmPara{" +
                "sensorId='" + sensorId + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
