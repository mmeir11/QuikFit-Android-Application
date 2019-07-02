package com.evyatartzik.android2_project.Models;

public class ProfileImageUpload {
    String mName;

    String mImageUrl;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }



    public ProfileImageUpload(){}

    public ProfileImageUpload(String name, String imageUrl)
    {
        if(name.trim().equals(""))
        {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
    }
}
