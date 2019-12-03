package com.example.tripmaker.models;

import com.google.firebase.Timestamp;

import java.util.Comparator;
import java.util.Objects;

public class Message {
    private String id;
    private String text;
    private String imgUrl;
    private Timestamp timeStamp;
    private String sender;

    public Message() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id.equals(message.id) &&
                text.equals(message.text) &&
                imgUrl.equals(message.imgUrl) &&
                timeStamp.equals(message.timeStamp) &&
                sender.equals(message.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, imgUrl, timeStamp, sender);
    }

    public static class MessageSort implements Comparator<Message>{

        @Override
        public int compare(Message o1, Message o2) {
            return o1.getTimeStamp().compareTo(o2.getTimeStamp());
        }
    }
}
