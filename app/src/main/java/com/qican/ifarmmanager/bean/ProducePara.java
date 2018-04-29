package com.qican.ifarmmanager.bean;

//生产参数

import java.io.Serializable;

public class ProducePara implements Serializable {

    String categoryName;
    String categoryCode;    // 请求码

    String typeName;
    String typeCode;        // 请求码

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public String toString() {
        return "ProducePara{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeCode='" + typeCode + '\'' +
                '}';
    }
}
