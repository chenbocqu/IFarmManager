package com.qican.ifarmmanager.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class IFarmCamera extends EZCamera implements Serializable, Comparable<IFarmCamera> {
    protected String id;
    protected String name;
    protected String location;
    protected String createTime;


    protected int shedNo; //大棚编号
    private String preImgUrl;

    private Subarea subarea;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getShedNo() {
        return shedNo;
    }

    public void setShedNo(int shedNo) {
        this.shedNo = shedNo;
    }

    public String getPreImgUrl() {
        return preImgUrl;
    }

    public void setPreImgUrl(String preImgUrl) {
        this.preImgUrl = preImgUrl;
    }

    public Subarea getSubarea() {
        return subarea;
    }

    public void setSubarea(Subarea subarea) {
        this.subarea = subarea;
    }

    @Override
    public String toString() {
        return "IFarmCamera{" +
                "name='" + name + '\'' +
                "deviceSerial='" + getDeviceSerial() + '\'' +
                "channelNo='" + getChannelNo() + '\'' +
                "status='" + getStatus() + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull IFarmCamera another) {

        int biger = -1;

        if (another.getName() == null) return biger;
        if (this.getName() == null) return -biger;

        if (!another.getName().contains("视频")) return biger;
        if (!this.getName().contains("视频")) return -biger;

        // 都包含视频字段，解析下数字
        return (parseNum(getName()) - parseNum(another.getName()));
    }

    private int parseNum(String name) {
        if (name.length() < 3) return 0;

        int num = name.charAt(2) - '0';

        if (num >= 10 || num < 0) return 0;

        if (name.length() > 3
                && name.charAt(3) - '0' >= 0
                && name.charAt(3) - '0' <= 9)
            num = num * 10 + (name.charAt(3) - '0');

        return num;
    }

    public static class Builder {
        private IFarmCamera camera;

        public Builder() {
            camera = new IFarmCamera();
        }

        public Builder setId(String id) {
            camera.setId(id);
            return this;
        }

        public Builder setName(String name) {
            camera.setName(name);
            return this;
        }

        public Builder setLoaction(String location) {
            camera.setLocation(location);
            return this;
        }

        public Builder setDeviceSerial(String serial) {
            camera.setDeviceSerial(serial);
            return this;
        }

        public Builder setChannelNo(int no) {
            camera.setChannelNo(no);
            return this;
        }

        public Builder setShedNo(int shedNo) {
            camera.setShedNo(shedNo);
            return this;
        }

        public Builder setPreImgUrl(String imgUrl) {
            camera.setPreImgUrl(imgUrl);
            return this;
        }

        public Builder setVerifyCode(String verifyCode) {
            camera.setVerifyCode(verifyCode);
            return this;
        }

        public IFarmCamera build() {
            return camera;
        }
    }
}
