package com.evyatartzik.android2_project.Models;

import java.util.UUID;

public class ChatMessage {

    private String sender;
    private String receiver;
    private String message;

    private String name;
    private String date;
    private String time;
    private String uuid;

    public ChatMessage() {
    }


    public ChatMessage(String date, String message, String name, String time, String uuid) {
        this.message = message;
        this.name = name;
        this.date = date;
        this.time = time;
        this.uuid = uuid;


    }

//    public ChatMessage(String sender, String receiver, String message) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.message = message;
//    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }
}
