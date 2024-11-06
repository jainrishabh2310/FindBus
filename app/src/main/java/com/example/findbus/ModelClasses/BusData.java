package com.example.findbus.ModelClasses;

public class BusData {
    private String busname;
    private int busprice;
    private String busroute;
    private int busseat;
    private String from;
    private String owner;
    private String to;

    // Getters and setters for each field
    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public int getBusprice() {
        return busprice;
    }

    public void setBusprice(int busprice) {
        this.busprice = busprice;
    }

    public String getBusroute() {
        return busroute;
    }

    public void setBusroute(String busroute) {
        this.busroute = busroute;
    }

    public int getBusseat() {
        return busseat;
    }

    public void setBusseat(int busseat) {
        this.busseat = busseat;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

