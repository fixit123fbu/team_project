package com.example.fixit.Models;

public class User {

    private String username;
    private String profilePhoto;
    private String userUid;


    public User(){}

    public User(String username, String userUid){
        this.username = username;
        this.userUid = userUid;
        this.profilePhoto = null;
    }

    public User(String username, String password, String userUid, String profilePhoto){
        this.equals(new User(username, userUid));
        this.profilePhoto = profilePhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
