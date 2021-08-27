package com.example.colibri.model;

public class ChatModel {
    String reciver;
    String sender;
    String message;
    String time;
    boolean isSeen;

    public ChatModel() {
    }

    public ChatModel(String reciver, String sender, String message, String time, boolean isSeen) {
        this.reciver = reciver;
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.isSeen = isSeen;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
