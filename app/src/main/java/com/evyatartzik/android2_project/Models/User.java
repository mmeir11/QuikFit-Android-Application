package com.evyatartzik.android2_project.Models;

import java.util.List;

public class User {

    private String Name;
    private String Email;
    private String Password;
    List<UserPreferences> userPreferences;

    public User(){ }

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


    public User(String name,String email, String password,List<UserPreferences> userPreferences) {
        Name = name;
        Email = email;
        Password = password;
        this.userPreferences = userPreferences;
    }
    public List<UserPreferences> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(List<UserPreferences> userPreferences) {
        this.userPreferences = userPreferences;
    }
}
