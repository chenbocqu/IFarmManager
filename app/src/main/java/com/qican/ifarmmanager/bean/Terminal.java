package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 控制系统终端
 */

public class Terminal implements Serializable {

    String id;
    int controlDeviceBit;
    String sysId;
    String controlType;
    String funcCode;
    String funcName;
    String controlDeviceId;
    String identify; // 系统终端标识
    String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getControlDeviceBit() {
        return controlDeviceBit;
    }

    public void setControlDeviceBit(int controlDeviceBit) {
        this.controlDeviceBit = controlDeviceBit;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getControlDeviceId() {
        return controlDeviceId;
    }

    public void setControlDeviceId(String controlDeviceId) {
        this.controlDeviceId = controlDeviceId;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "controlDeviceBit=" + controlDeviceBit +
                ", sysId='" + sysId + '\'' +
                ", controlType='" + controlType + '\'' +
                ", funcCode='" + funcCode + '\'' +
                ", funcName='" + funcName + '\'' +
                ", controlDeviceId='" + controlDeviceId + '\'' +
                ", identify='" + identify + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
