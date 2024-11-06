package com.example.findbus.ModelClasses;

public class UserData {

    String name;
    String image;
    String gender;
    String dob;
    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserData(String name, String image, String gender, String dob, String address) {
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
    }
    UserData(){

    }

}
