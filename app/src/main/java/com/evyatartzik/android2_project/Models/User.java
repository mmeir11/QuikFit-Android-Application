package com.evyatartzik.android2_project.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String Name;
    private String Email;
    private String uID;
    private ArrayList<UserPreferences> userPreferences;
    private float latitude;
    private float longitude;
    private String profile_pic_path;
    private String location_string;



    private String about;

    public User(){ }


    public User(String uID, String name, String email, ArrayList<UserPreferences> userPreferences, float latitude, float longitude,String location_string , String profile_pic_path, String about)
    {
        this.uID = uID;
        Name = name;
        Email = email;
        this.userPreferences = userPreferences;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profile_pic_path = profile_pic_path;
        this.about = about;
        this.location_string = location_string;
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

    public String getProfile_pic_path() {
        return profile_pic_path;
    }

    public void setProfile_pic_path(String profile_pic_path) {
        this.profile_pic_path = profile_pic_path;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<UserPreferences> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(ArrayList<UserPreferences> userPreferences) {
        this.userPreferences = userPreferences;
    }
    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation_string() {
        return location_string;
    }

    public void setLocation_string(String location_string) {
        this.location_string = location_string;
    }
}
