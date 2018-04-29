/**
 * 分区
 */
package com.qican.ifarmmanager.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Subarea implements Serializable {
    private String farmId;
    private String name;
    private String imgUrl;
    private Map<String, List<String>> dataMap;
    private Farm farm;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public Map<String, List<String>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<String>> dataMap) {
        this.dataMap = dataMap;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
