package com.example.namgiwon.band;

import java.io.Serializable;

/**
 * Created by namgiwon on 2017. 11. 6..
 */

public class ChattingRoomItem implements Serializable {

    private int roomid;
    private String member;
    private int count;
    private String roomname;
    private String lastchat;
    private Boolean isNew = false;

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public String getLastchat() { return lastchat; }

    public void setLastchat(String lastchat) {
        this.lastchat = lastchat;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getMember() {return member;}
    public void setMember(String member) {this.member = member;}


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
