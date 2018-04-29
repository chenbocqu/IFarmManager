/**
 * 农场
 */
package com.qican.ifarmmanager.bean;


import java.io.Serializable;
import java.util.List;

public class Farm implements Serializable {
    private String id;
    private String name;
    private String desc;
    private String time;
    private String imgUrl;
    private String labels;
    String location;
    String detialAddress;
    private List<String> labelList;
    private Label label;

    private boolean isSelected = false;

    public Farm() {
    }

    public Farm(com.qican.ifarmmanager.beanfromservice.Farm farm) {
        setId(String.valueOf(farm.getFarmId()));
        setName(farm.getFarmName());
        setDesc(farm.getFarmDescribe());
        setTime(farm.getFarmCreateTime());
        setImgUrl(farm.getFarmImageUrl());
        setLabels(farm.getFarmLabel());
        if (farm.getFarmLabel() != null) {
            Label label = new Label(farm.getFarmLabel());
            setLabel(label);
            setLabelList(label.getLabelList());
        }
    }

    public String getDetialAddress() {
        return detialAddress;
    }

    public void setDetialAddress(String detialAddress) {
        this.detialAddress = detialAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * 添加标签
     *
     * @param label
     */
    public void addLabel(String label) {
        if (this.labels != null && this.labels != "") {
            labels = labels + "," + label;
        } else {
            labels = label;
        }
        this.labelList.clear();
        String tempLabels[] = this.labels.split(",");
        for (int i = 0; i < tempLabels.length; i++) {
            this.labelList.add(tempLabels[i]);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;

        Label label = new Label(labels);
        setLabel(label);
        setLabelList(label.getLabelList());
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
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

    public static class Builder {
        private Farm farm;

        public Builder() {
            farm = new Farm();
        }

        public Builder setId(String id) {
            farm.setId(id);
            return this;
        }

        public Builder setName(String name) {
            farm.setName(name);
            return this;
        }

        public Builder setDesc(String desc) {
            farm.setDesc(desc);
            return this;
        }

        public Builder setTime(String time) {
            farm.setTime(time);
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            farm.setImgUrl(imgUrl);
            return this;
        }

        public Builder setLabel(Label l) {
            farm.setLabel(l);
            farm.setLabelList(l.getLabelList());
            farm.setLabels(l.getLabelStr());
            return this;
        }

        public Farm build() {
            return farm;
        }
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", labels='" + labels + '\'' +
                ", labelList=" + labelList +
                ", label=" + label +
                '}';
    }
}
