package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * 水肥药一体化系统信息
 */

public class ComSys implements Serializable {
    String id;
    String farmId;
    String code;
    String type;
    String typeCode;
    String district;
    String no;
    String location;
    String desc;

    int medicineNum;
    int districtNum;
    int fertierNum;

    String createTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ComSys{" +
                "id='" + id + '\'' +
                ", farmId='" + farmId + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", district='" + district + '\'' +
                ", no='" + no + '\'' +
                ", location='" + location + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
