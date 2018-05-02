package com.example.jhj.drawingquiz.model;

import java.io.Serializable;

/**
 * Created by JHJ on 2017-11-20.
 */

public class GameModel implements Serializable
{
    public String roomnumber;
    public String roomname;
    public String roomrank;






    public String getRoomnumber() {
        return roomnumber;
    }
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getRoomrank() {
        return roomrank;
    }

    public void setRoomrank(String roomrank) {
        this.roomrank = roomrank;
    }
}
