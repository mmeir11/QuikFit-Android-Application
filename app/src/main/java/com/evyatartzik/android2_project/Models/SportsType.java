package com.evyatartzik.android2_project.Models;

import android.graphics.Bitmap;

public class SportsType {

    private String name;
    private Bitmap picture;
    private String picUri;


    public SportsType(String name, Bitmap picture, String picUri) {
        this.name = name;
        this.picture = picture;
        this.picUri = picUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public void DownloadUriPicToBitMap(){}
}
