package com.example.tripmaker.models;

public class Message {
    String senderID;
    String messageText;
    String imageUrl;

    public Message(String senderID, String messageText, String imageUrl) {
        this.senderID = senderID;
        this.messageText = messageText;
        this.imageUrl = imageUrl;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
