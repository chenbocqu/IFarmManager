/**
 * 操作分区
 */
package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class OperationArea implements Serializable {

    private String id;
    private String name;

    public OperationArea() {
    }

    public OperationArea(String id, String name) {
        this.id = id;
        this.name = name;
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

    //OptionsPickerView需要
    @Override
    public String toString() {
        return getName();
    }
}
