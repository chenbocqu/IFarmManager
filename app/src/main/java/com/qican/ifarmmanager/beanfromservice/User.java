package com.qican.ifarmmanager.beanfromservice;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String userPhone;
    private String userOtherAssociateId;
    private String userName;
    private String userPwd;
    private String userSex;
    private String userEmail;
    private String userRealName;
    private String userRegisterTime;
    private String userLastLoginTime;
    private String userImageUrl;
    private String userBackImageUrl;
    private String userSignature;
    private String userRole;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserBackImageUrl() {
        return userBackImageUrl;
    }

    public void setUserBackImageUrl(String userBackImageUrl) {
        this.userBackImageUrl = userBackImageUrl;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserOtherAssociateId() {
        return userOtherAssociateId;
    }

    public void setUserOtherAssociateId(String userOtherAssociateId) {
        this.userOtherAssociateId = userOtherAssociateId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserRegisterTime() {
        return userRegisterTime;
    }

    public void setUserRegisterTime(String userRegisterTime) {
        this.userRegisterTime = userRegisterTime;
    }

    public String getUserLastLoginTime() {
        return userLastLoginTime;
    }

    public void setUserLastLoginTime(String userLastLoginTime) {
        this.userLastLoginTime = userLastLoginTime;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userOtherAssociateId='" + userOtherAssociateId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRealName='" + userRealName + '\'' +
                ", userRegisterTime='" + userRegisterTime + '\'' +
                ", userLastLoginTime='" + userLastLoginTime + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", userBackImageUrl='" + userBackImageUrl + '\'' +
                ", userSignature='" + userSignature + '\'' +
                '}';
    }
}
