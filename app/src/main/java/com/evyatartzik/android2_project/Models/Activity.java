package com.evyatartzik.android2_project.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Activity implements Serializable {

    private String title;
    private String location;
    private float latitude;
    private float longitude;
    private String type;
    private String date;
    private String time;
    private String description;
    private ArrayList<String> usersIDList;
    private int maxParticipents;
    private Boolean isConfirm;



    public Activity(){}

/*    public Activity(String title, String location, String type, String date, String time, int maxParticipents, String description, ArrayList usersIDList) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.date = date;
        this.time = time;
        this.description = description;
        this.usersIDList = usersIDList;
        this.maxParticipents = maxParticipents;
        isConfirm = false;
    }*/
    public Activity(String date, String description, String location, int maxParticipents, String time, String title,  String type,  ArrayList<String> usersIDList) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.date = date;
        this.time = time;
        this.description = description;
        this.usersIDList = usersIDList;
        this.maxParticipents = maxParticipents;
        isConfirm = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getUsersIDList() {
        return usersIDList;
    }

    public void setUsersIDList(ArrayList usersIDList) {
        this.usersIDList = usersIDList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public int getMaxParticipents() {
        return maxParticipents;
    }

    public void setMaxParticipents(int maxParticipents) {
        this.maxParticipents = maxParticipents;
    }

    public int getAmountOfParticipents(){
        if(usersIDList == null) return 0;
        return usersIDList.size();
    }

    public boolean isInActivity(UUID uuid){
        if(usersIDList.contains(uuid))
            isConfirm = true;

        else
            isConfirm = false;

        return isConfirm;

    }

    public  void addParticipents(String uuid){
        usersIDList.add(uuid);
    }

    public  void removeParticipents(String uuid){
        usersIDList.remove(uuid);
    }


}
