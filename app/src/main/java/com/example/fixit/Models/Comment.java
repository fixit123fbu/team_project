package com.example.fixit.Models;

public class Comment {
    private String message;
    private FixitUser user;

    public Comment(){}

    public Comment(String message, FixitUser user){
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public FixitUser getUser() {
        return user;
    }
}
