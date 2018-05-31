package com.example.nurlanov.ps_app;

public class Arendator_info {
    private String id;

    private String namePS,pass,address,num, hour, minute;
    private double Longitude,Latitude;
    private int mesta;
    private boolean empty;


    public Arendator_info(String id, String namePS, String pass, String address, String num, String hour, String minute, double longitude, double latitude, int mesta, boolean empty) {
        this.id = id;
        this.namePS = namePS;
        this.pass = pass;
        this.address = address;
        this.num = num;
        this.hour = hour;
        this.minute = minute;
        Longitude = longitude;
        Latitude = latitude;
        this.mesta = mesta;
        this.empty = empty;
    }

    public String getNum() {
        return num;
    }

    public String getNamePS() {
        return namePS;
    }

    public String getPass() {
        return pass;
    }

    public String getAddress() {
        return address;
    }

    public int getMesta() {
        return mesta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNamePS(String namePS) {
        this.namePS = namePS;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setMesta(int mesta) {
        this.mesta = mesta;
    }

    public Arendator_info() {

    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {

        return empty;
    }
}
