package com.hackcovid.covidhack;

public class lastMessage {

    String senderId, last_message, timestamp,receiverId,mainId;
    boolean isseen;

    public lastMessage() {
    }

    public lastMessage(String senderId, String last_message, String timestamp, String receiverId, String mainId, boolean isseen) {
        this.senderId = senderId;
        this.last_message = last_message;
        this.timestamp = timestamp;
        this.receiverId = receiverId;
        this.mainId = mainId;
        this.isseen = isseen;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
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

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
