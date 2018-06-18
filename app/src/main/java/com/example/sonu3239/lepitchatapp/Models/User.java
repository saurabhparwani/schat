package com.example.sonu3239.lepitchatapp.Models;

public class User {
    String name,status,image,thumbnail,uid,device_token,online;

    public User() {
    }

    public User(String name, String status, String image, String thumbnail,String uid,String device_token,String online) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.thumbnail = thumbnail;
        this.uid=uid;
        this.device_token=device_token;
        this.online=online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
