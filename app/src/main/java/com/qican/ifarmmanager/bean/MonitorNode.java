package com.qican.ifarmmanager.bean;

import java.io.Serializable;
import java.util.List;

public class MonitorNode implements Serializable {

    private String id;//节点值的id

    // 添加 2017-11-27
    private String deviceId; //设备id
    private List<DevicePara> nodeDatas;
    private String orderNo;
    private String district;
    private String updateTime;//更新时间
    private String location;//节点位置
    private String type;

    boolean hashData = false;

    private String name;//监测节点名称
    private String imgUrl;//节点头像url
    private String status;//状态
    private String humidity;//湿度
    private String temperature1;//温度1
    private String temperature2;//温度2
    private String waterContent; //土壤水含量

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(String temperature1) {
        this.temperature1 = temperature1;
    }

    public String getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(String temperature2) {
        this.temperature2 = temperature2;
    }

    public String getWaterContent() {
        return waterContent;
    }

    public void setWaterContent(String waterContent) {
        this.waterContent = waterContent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<DevicePara> getNodeDatas() {
        return nodeDatas;
    }

    public void setNodeDatas(List<DevicePara> nodeDatas) {
        this.nodeDatas = nodeDatas;
    }

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

    public boolean hashData() {
        return hashData;
    }

    public void setHashData(boolean hashData) {
        this.hashData = hashData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Builder {
        private MonitorNode node;

        public Builder() {
            node = new MonitorNode();
        }

        public Builder setId(String id) {
            node.setId(id);
            return this;
        }

        public Builder setName(String name) {
            node.setName(name);
            return this;
        }

        public Builder setLocation(String location) {
            node.setLocation(location);
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            node.setImgUrl(imgUrl);
            return this;
        }

        public Builder setStatus(String status) {
            node.setStatus(status);
            return this;
        }

        public Builder setHumidity(String humidity) {
            node.setHumidity(humidity);
            return this;
        }

        public Builder setTemperature1(String temperature1) {
            node.setTemperature1(temperature1);
            return this;
        }

        public Builder setTemperature2(String temperature2) {
            node.setTemperature2(temperature2);
            return this;
        }

        public Builder setWaterContent(String waterContent) {
            node.setWaterContent(waterContent);
            return this;
        }

        public Builder setUpdateTime(String updateTime) {
            node.setUpdateTime(updateTime);
            return this;
        }

        public MonitorNode build() {
            return node;
        }
    }

    @Override
    public String toString() {
        return "MonitorNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", status='" + status + '\'' +
                ", humidity='" + humidity + '\'' +
                ", temperature1='" + temperature1 + '\'' +
                ", temperature2='" + temperature2 + '\'' +
                ", waterContent='" + waterContent + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
