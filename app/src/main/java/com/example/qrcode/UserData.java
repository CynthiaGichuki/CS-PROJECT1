package com.example.qrcode;

public class UserData {
    private String userId;
    private String fullname;
    private String email;
    private String phonenumber;
    private String imgUrl;

    public UserData(String userId, String fullname, String email, String phonenumber, String imgUrl) {
        this.userId = userId;
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.imgUrl = imgUrl;
    }

    public UserData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
