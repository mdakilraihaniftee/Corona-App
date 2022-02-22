package com.hackcovid.covidhack;

public class notification {
    private String comment,date,postid,publisherid,receiverId,notificationId;

    public notification() {
    }

    public notification(String comment, String date, String postid, String publisherid, String receiverId, String notificationId) {
        this.comment = comment;
        this.date = date;
        this.postid = postid;
        this.publisherid = publisherid;
        this.receiverId = receiverId;
        this.notificationId = notificationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
