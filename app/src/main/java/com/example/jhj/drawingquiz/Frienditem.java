package com.example.jhj.drawingquiz;

import java.io.Serializable;

/**
 * Created by namgiwon on 2017. 10. 4..
 */

public class Frienditem implements Serializable {

    private String name;
    private String id;
    private String phonenumber;
    private  String nickname;
    private String sno;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

}
