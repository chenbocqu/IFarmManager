/**
 * 普通用户
 */
package com.qican.ifarmmanager.bean;

import com.qican.ifarmmanager.beanfromservice.User;

import java.io.Serializable;

public class ComUser implements Serializable {
    private String id;
    private String phone;
    private String nickName;
    private String password;
    private String sex;
    private String email;
    private String realName;
    private String registerTime;
    private String lastLoginTime;
    private String headImgUrl;
    private String signature;
    private String bgImgUrl;
    private String labels;
    String role;

    public ComUser() {
    }

    /**
     * 构造函数，部分属性待转换
     *
     * @param user
     */
    public ComUser(User user) {
        this.setId(user.getUserId());//Id
        this.setPhone(user.getUserPhone());
        this.setNickName(user.getUserName());
        this.setPassword(user.getUserPwd());
        this.setSex(user.getUserSex());

//        private String userOtherAssociateId;
        this.setEmail(user.getUserEmail());
        this.setHeadImgUrl(user.getUserImageUrl());
        this.setRegisterTime(user.getUserRegisterTime());
        this.setLastLoginTime(user.getUserLastLoginTime());
        this.setSignature(user.getUserSignature());
        this.setBgImgUrl(user.getUserBackImageUrl());
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private final String split = ",";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        if (this.labels != null && this.labels != "") {
            labels = labels + split + label;
        } else {
            labels = label;
        }
    }

    @Override
    public String toString() {
        return "ComUser{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", realName='" + realName + '\'' +
                ", registerTime='" + registerTime + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", signature='" + signature + '\'' +
                ", bgImgUrl='" + bgImgUrl + '\'' +
                ", labels='" + labels + '\'' +
                '}';
    }

    public static class Builder {
        private ComUser user;

        public Builder() {
            user = new ComUser();
        }

        public Builder setId(String id) {
            user.setId(id);
            return this;
        }

        public Builder setPhone(String phone) {
            user.setPhone(phone);
            return this;
        }

        public Builder setNickName(String nickName) {
            user.setNickName(nickName);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder setSex(String sex) {
            user.setSex(sex);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setRealName(String realName) {
            user.setRealName(realName);
            return this;
        }

        public Builder setRegisterTime(String registerTime) {
            user.setRegisterTime(registerTime);
            return this;
        }

        public Builder setLastLoginTime(String lastLoginTime) {
            user.setLastLoginTime(lastLoginTime);
            return this;
        }

        public Builder setHeadImgUrl(String headImgUrl) {
            user.setHeadImgUrl(headImgUrl);
            return this;
        }

        public Builder setSignature(String signature) {
            user.setSignature(signature);
            return this;
        }

        public Builder setLabels(String labels) {
            user.setLabels(labels);
            return this;
        }

        public Builder setBgImgUrl(String bgImgUrl) {
            user.setBgImgUrl(bgImgUrl);
            return this;
        }

        public ComUser build() {
            return user;
        }
    }
}
