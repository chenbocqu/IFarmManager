
/**
 * 设备参数，用于农场监测数据
 */

package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class DevicePara implements Serializable {

    String name;
    String code;
    String value;
    String unit; // 单位

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DevicePara{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
