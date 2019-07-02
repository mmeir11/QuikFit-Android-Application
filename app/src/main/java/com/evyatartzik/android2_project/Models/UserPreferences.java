package com.evyatartzik.android2_project.Models;

public class UserPreferences {

    private String name;
    private Boolean isSelected;

    public UserPreferences(String name) {
        this.name = name;
    }
    UserPreferences(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setCheck(Boolean b) {
        isSelected = b;
    }
    public Boolean getCheck() {
       return isSelected ;
    }
}
