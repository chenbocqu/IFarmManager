package com.qican.ifarmmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3 0003.
 */
public class PhoneInfo implements Serializable {
    private String country;
    private String phone;

    public PhoneInfo() {
    }

    public PhoneInfo(String phone, String country) {
        this.phone = phone;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "country='" + country + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
