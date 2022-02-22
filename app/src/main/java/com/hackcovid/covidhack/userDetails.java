package com.hackcovid.covidhack;

public class userDetails {

    private String name,mobileNo,gender,email,imgUrl,userId,lowercaseName,status;
    boolean profileLock;

    public userDetails() {

    }

    public userDetails(String name, String mobileNo, String gender, String email, String imgUrl, String userId, String lowercaseName, String status, boolean profileLock) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.email = email;
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.lowercaseName = lowercaseName;
        this.status = status;
        this.profileLock = profileLock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLowercaseName() {
        return lowercaseName;
    }

    public void setLowercaseName(String lowercaseName) {
        this.lowercaseName = lowercaseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isProfileLock() {
        return profileLock;
    }

    public void setProfileLock(boolean profileLock) {
        this.profileLock = profileLock;
    }
}
