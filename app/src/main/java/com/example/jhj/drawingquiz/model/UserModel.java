package com.example.jhj.drawingquiz.model;

/**
 * Created by JHJ on 2017-11-16.
 */

public class UserModel {
    public String userName;
    public String userEmail;
    public String uid;
    public int point;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPoint() { return point; }

    public void setPoint(int point) {this.point = point; }
}
