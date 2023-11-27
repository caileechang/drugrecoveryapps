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

    //constructor used in Admin_Activity
    public User(String uid, String username, String fullName, String email, String phone_number) {
        this.userid=uid;
        this.username=username;
        this.fullName=fullName;
        this.email=email;
        this.phone_number=phone_number;
    }

    //accessors
    public String getUid() {
        return userid;
    }
    public String getUsername() {
        return username;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getCountryName() {
        return countryName;
    }
    public String getStateName() {
        return stateName;
    }
    public String getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public String getOccupation() {
        return occupation;
    }

    //mutators
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAge(String age) {
        this.age = age;
    }



}
