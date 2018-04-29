package com.qican.ifarmmanager.bean;


import java.io.Serializable;

public class ControlSys implements Serializable {

    String controName;

    String systemId;
    String farmId;
    String farmName;
    String systemCode;
    String systemType;
    String systemTypeCode;
    String systemDistrict;
    String systemNo;

    String canNum;
    String systemDescription;
    String districtSum;
    String systemLocation;
    String systemCreateTime;

    int medicineNum;
    int districtNum;
    int fertierNum;

    String controlType;
    String controlOperation;

    public String getControName() {
        return controName;
    }

    public void setControName(String controName) {
        this.controName = controName;
    }

    public String getControlOperation() {
        return controlOperation;
    }

    public void setControlOperation(String controlOperation) {
        this.controlOperation = controlOperation;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public int getMedicineNum() {
        return medicineNum;
    }

    public void setMedicineNum(int medicineNum) {
        this.medicineNum = medicineNum;
    }

    public int getDistrictNum() {
        return districtNum;
    }

    public void setDistrictNum(int districtNum) {
        this.districtNum = districtNum;
    }

    public int getFertierNum() {
        return fertierNum;
    }

    public void setFertierNum(int fertierNum) {
        this.fertierNum = fertierNum;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public String getSystemTypeCode() {
        return systemTypeCode;
    }

    public void setSystemTypeCode(String systemTypeCode) {
        this.systemTypeCode = systemTypeCode;
    }

    public String getSystemCreateTime() {
        return systemCreateTime;
    }

    public void setSystemCreateTime(String systemCreateTime) {
        this.systemCreateTime = systemCreateTime;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

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

    public String getSystemDistrict() {
        return systemDistrict;
    }

    public void setSystemDistrict(String systemDistrict) {
        this.systemDistrict = systemDistrict;
    }


    public String getCanNum() {
        return canNum;
    }

    public void setCanNum(String canNum) {
        this.canNum = canNum;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription(String systemDescription) {
        this.systemDescription = systemDescription;
    }

    public String getDistrictSum() {
        return districtSum;
    }

    public void setDistrictSum(String districtSum) {
        this.districtSum = districtSum;
    }

    public String getSystemLocation() {
        return systemLocation;
    }

    public void setSystemLocation(String systemLocation) {
        this.systemLocation = systemLocation;
    }

    @Override
    public String toString() {
        return "ControlSys{" +
                "systemId='" + systemId + '\'' +
                ", farmId='" + farmId + '\'' +
                ", farmName='" + farmName + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", systemType='" + systemType + '\'' +
                ", systemDistrict='" + systemDistrict + '\'' +
                ", canNum='" + canNum + '\'' +
                ", systemDescription='" + systemDescription + '\'' +
                ", districtSum='" + districtSum + '\'' +
                ", systemLocation='" + systemLocation + '\'' +
                ", systemCreateTime='" + systemCreateTime + '\'' +
                '}';
    }
}
