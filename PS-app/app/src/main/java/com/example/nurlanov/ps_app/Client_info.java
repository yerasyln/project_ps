package com.example.nurlanov.ps_app;

public class Client_info {
    String id, pass,num;

    public Client_info() {
    }

    public Client_info(String id,  String pass, String num) {
        this.id = id;
        this.pass = pass;
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public String getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }
}
