package com.example.colibri.model;

public class UserDetailModel {
    String Name,userName;
    String pic;

    public UserDetailModel(String name, String userName, String pic) {
        Name = name;
        this.userName = userName;
        this.pic = pic;
    }
    public UserDetailModel(){    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
