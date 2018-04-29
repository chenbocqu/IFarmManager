package com.qican.ifarmmanager.bean;


import java.io.Serializable;

public class Operations implements Serializable {

    String name;
    String controlOperation;

    boolean isSelected = false;

    public Operations() {
    }

    public Operations(String name, String controlOperation) {
        this.name = name;
        this.controlOperation = controlOperation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getControlOperation() {
        return controlOperation;
    }

    public void setControlOperation(String controlOperation) {
        this.controlOperation = controlOperation;
    }
}
