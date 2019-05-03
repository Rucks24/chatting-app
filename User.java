package com.example.dell.tt;

public class User {

    public String username;
    public Boolean isGuide;
    public String district;
    public String city;
    public String userid;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, Boolean isGuide , String dist, String city,String userid) {
        this.username = username;
        this.isGuide = isGuide;
        this.district = dist;
        this.city = city;
        this.userid = userid;
    }

    public String getName() {
        return username;
    }

    public String getDistrict (){
        return district;
    }

    public String getUserid(){
        return userid;
    }
}
