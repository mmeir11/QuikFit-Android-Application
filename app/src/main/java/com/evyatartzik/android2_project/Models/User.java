package com.evyatartzik.android2_project.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private String Name;
    private String Email;
    private String Password;
    private ArrayList<UserPreferences> userPreferences;
    private float latitude;
    private float longitude;
    private String profile_pic_path;
    private UUID uuid;
    private String about;

    public User(){ }

    public User(String name,String email, String password,ArrayList<UserPreferences> userPreferences) {
        Name = name;
        Email = email;
        Password = password;
        this.userPreferences = userPreferences;
        uuid = UUID.randomUUID();
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public ArrayList<UserPreferences> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(ArrayList<UserPreferences> userPreferences) {
        this.userPreferences = userPreferences;
    }


}
