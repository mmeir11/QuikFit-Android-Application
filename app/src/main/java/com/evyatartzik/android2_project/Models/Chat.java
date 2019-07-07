package com.evyatartzik.android2_project.Models;

import java.util.ArrayList;

public class Chat {

    private String title;
    private ArrayList<String> usersIDList;
    private ArrayList<ChatMessage> chatMessageArrayList;

    public Chat(String title) {
        this.title = title;
    }

    public Chat(String title, ArrayList<String> usersIDList, ArrayList<ChatMessage> chatMessageArrayList) {
        this.title = title;
        this.usersIDList = usersIDList;
        this.chatMessageArrayList = chatMessageArrayList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getUsersIDList() {
        return usersIDList;
    }

    public void setUsersIDList(ArrayList<String> usersIDList) {
        this.usersIDList = usersIDList;
    }

    public ArrayList<ChatMessage> getChatMessageArrayList() {
        return chatMessageArrayList;
    }

    public void setChatMessageArrayList(ArrayList<ChatMessage> chatMessageArrayList) {
        this.chatMessageArrayList = chatMessageArrayList;
    }
}
