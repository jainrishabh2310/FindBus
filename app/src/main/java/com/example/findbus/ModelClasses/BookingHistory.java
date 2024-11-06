package com.example.findbus.ModelClasses;

import com.example.findbus.Booking_History;

public class BookingHistory {
    String Busid;
    String time;

    public BookingHistory(String busid, String time) {
        Busid = busid;
        this.time = time;
    }

    public String getBusid() {
        return Busid;
    }

    public void setBusid(String busid) {
        Busid = busid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    BookingHistory(){

    }
}
