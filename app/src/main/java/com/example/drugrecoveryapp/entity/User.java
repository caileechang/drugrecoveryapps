package com.example.drugrecoveryapp.entity;

public class User {
    public String userid,username,email,phone_number,fullName,countryName,stateName,birthday,age,occupation,gender,relationship;
    public User(){
        //empty constructor
    }

    //constructor used to save info in database
    public User(String userid, String username, String email, String phone_number, String countryName, String gender) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.countryName = countryName;
        this.gender = gender;

    }

    //accessors
    public String getUid() {
        return userid;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getPhone_number() {
        return phone_number;
    }

    public String getCountryName() {
        return countryName;
    }


    public String getGender() {
        return gender;
    }


    //mutators
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }



}
