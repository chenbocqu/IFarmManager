package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 系统类型
 */

public class SysType implements Serializable{

    String systemCode;
    String systemType;
    String systemTypeCode;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemTypeCode() {
        return systemTypeCode;
    }

    public void setSystemTypeCode(String systemTypeCode) {
        this.systemTypeCode = systemTypeCode;
    }

    @Override
    public String toString() {
        return "SysType{" +
                "systemCode='" + systemCode + '\'' +
                ", systemType='" + systemType + '\'' +
                ", systemTypeCode='" + systemTypeCode + '\'' +
                '}';
    }
}
