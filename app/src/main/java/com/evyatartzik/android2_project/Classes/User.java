package com.evyatartzik.android2_project.Classes;

public class User {

    private String Name;
    private String Email;
    private String Password;

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
