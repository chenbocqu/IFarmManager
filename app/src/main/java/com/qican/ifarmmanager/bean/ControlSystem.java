/**
 * 控制系统
 */
package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class ControlSystem implements Serializable {
    String sysId;
    String farmId;
    String collectorId;
    String sysType;
    String sysDistrict;
    int fertilizingNum;

    int districtSum;
    String location;

    Farm farm;//属于哪个农场
    String indexInFarm;//农场中的第几号系统
    String name;
    String desc;
    String time;
    String imgUrl;

    public ControlSystem(String name, String desc, String time, String imgUrl) {
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.imgUrl = imgUrl;
    }

    public ControlSystem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public String getIndexInFarm() {
        return indexInFarm;
    }

    public void setIndexInFarm(String indexInFarm) {
        this.indexInFarm = indexInFarm;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getSysDistrict() {
        return sysDistrict;
    }

    public void setSysDistrict(String sysDistrict) {
        this.sysDistrict = sysDistrict;
    }

    public int getFertilizingNum() {
        return fertilizingNum;
    }

    public void setFertilizingNum(int fertilizingNum) {
        this.fertilizingNum = fertilizingNum;
    }

    public int getDistrictSum() {
        return districtSum;
    }

    public void setDistrictSum(int districtSum) {
        this.districtSum = districtSum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
