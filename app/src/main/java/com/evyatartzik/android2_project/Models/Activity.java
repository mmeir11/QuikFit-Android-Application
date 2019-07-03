package com.evyatartzik.android2_project.Models;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Activity {

    private String title;
    private Location location;
    private float latitude;
    private float longitude;
    private String type;
    private String date;
    private String description;
    private ArrayList<UUID> users;


    public Activity(){}
    public Activity(String title, Location location, String type, String date, String description, ArrayList users) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.date = date;
        this.description = description;
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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

    public ArrayList getUsers() {
        return users;
    }

    public void setUsers(ArrayList users) {
        this.users = users;
    }
}
