package com.evyatartzik.android2_project.Models;

import java.util.List;

public class User {

    private String Name;
    private String Email;
    private String Password;

    public List<String> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(List<String> userPreferences) {
        this.userPreferences = userPreferences;
    }

    List<String> userPreferences;

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


    public User(String name,String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }
}
