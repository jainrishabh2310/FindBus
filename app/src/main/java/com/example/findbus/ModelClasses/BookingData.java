package com.example.findbus.ModelClasses;

public class BookingData
{
    String userid;
    String busid;
    int seated_book;


    public BookingData(String userid, String busid, int seated_book, int price, String time) {
        this.userid = userid;
        this.busid = busid;
        this.seated_book = seated_book;
        this.price = price;
        this.time = time;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public int getSeated_book() {
        return seated_book;
    }

    public void setSeated_book(int seated_book) {
        this.seated_book = seated_book;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    BookingData(){

    }

    int price;
    String time;


}
