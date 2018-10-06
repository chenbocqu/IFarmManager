package com.qican.ifarmmanager.beanfromservice;

import java.io.Serializable;

public class Farm implements Serializable{
    private Integer farmId;
    private String userId;
    private String farmName;
    private String farmLocation;
    private String farmDetailAddress;
    private String farmImageUrl;
    private String farmLabel;
    private String farmDescribe;
    private String farmCreateTime;
    private String farmState;

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmDetailAddress(String farmDetailAddress) {
        this.farmDetailAddress = farmDetailAddress;
    }

    public String getFarmDetailAddress() {
        return farmDetailAddress;
    }

    public void setFarmImageUrl(String farmImageUrl) {
        this.farmImageUrl = farmImageUrl;
    }

    public String getFarmImageUrl() {
        return farmImageUrl;
    }

    public String getFarmLabel() {
        return farmLabel;
    }

    public void setFarmLabel(String farmLabel) {
        this.farmLabel = farmLabel;
    }

    public void setFarmDescribe(String farmDescribe) {
        this.farmDescribe = farmDescribe;
    }

    public String getFarmDescribe() {
        return farmDescribe;
    }

    public void setFarmCreateTime(String farmCreateTime) {
        this.farmCreateTime = farmCreateTime;
    }

    public String getFarmCreateTime() {
        return farmCreateTime;
    }

    public String getFarmState() {
        return farmState;
    }

    public void setFarmState(String farmState) {
        this.farmState = farmState;
    }
}
