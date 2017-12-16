package com.example.namgiwon.band;

/**
 * Created by namgiwon on 2017. 11. 18..
 */

public class ChatItem {
    private String message;
    private String seq;
    private String sender_name;
    private String roomid;
    private String sender_id;
    private String time;
    private String sender_photo;

    public String getSender_photo() {
        return sender_photo;
    }

    public void setSender_photo(String sender_photo) {
        this.sender_photo = sender_photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }


    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
