package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 生产时的设备类型选择
 */

public class DeviceCategory implements Serializable {
    String type;
    String typeCode;
    String desc;
    String code;
    String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeviceCategory{" +
                "type='" + type + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", desc='" + desc + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
