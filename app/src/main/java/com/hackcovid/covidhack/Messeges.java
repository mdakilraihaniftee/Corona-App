package com.hackcovid.covidhack;

public class Messeges{


    String messege,senderId,timestamp,receiverId,messageId;
    boolean isseen;

    public Messeges() {
    }

    public Messeges(String messege, String senderId, String timestamp, String receiverId, String messageId, boolean isseen) {
        this.messege = messege;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.receiverId = receiverId;
        this.messageId = messageId;
        this.isseen = isseen;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
