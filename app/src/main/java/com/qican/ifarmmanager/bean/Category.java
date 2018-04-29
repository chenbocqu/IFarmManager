package com.qican.ifarmmanager.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

    String name;
    String category;

    ArrayList<DeviceType> types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<DeviceType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<DeviceType> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", types=" + showTypes() +
                '}';
    }

    private String showTypes() {
        String ret = "";
        for (DeviceType type : types)
            ret = ret + type.toString();
        return ret;
    }
}
