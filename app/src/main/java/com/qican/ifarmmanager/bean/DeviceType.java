package com.qican.ifarmmanager.bean;


import java.io.Serializable;

public class DeviceType implements Serializable {

//    {"header":"五合一设备,七合一设备","code":"collectorType5,collectorType7"}

    String type;
    String name;
    String code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "DeviceType{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
