package com.example.colibri.model;

public class Users {

    String phone;

    String userId;

    public Users(String phone) {
        this.phone = phone;
    }

    public Users() {
    }

    public String getUserId() {
        return userId;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
