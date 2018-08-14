package com.qican.ifarmmanager.bean;

/**
 * 采集设备
 */

public class AcquisitorDevice extends DeviceInfo {
    String orderNo;
    String district;
    String farmId;
    String version;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AcquisitorDevice{" +
                "orderNo='" + orderNo + '\'' +
                ", district='" + district + '\'' +
                ", farmId='" + farmId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
