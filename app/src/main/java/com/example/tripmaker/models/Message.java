package com.example.tripmaker.models;

import com.google.firebase.Timestamp;

public class Message {
    private String id;
    private String text;
    private String imgUrl;
    private Timestamp timeStamp;
    private String sender;

    public Message(String id, String text, String imgUrl, Timestamp timeStamp, String sender) {
        this.id = id;
        this.text = text;
        this.imgUrl = imgUrl;
        this.timeStamp = timeStamp;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;

    }
}
