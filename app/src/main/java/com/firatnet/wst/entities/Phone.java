package com.firatnet.wst.entities;

public class Phone {
    private int id;
    private String number;

    public Phone(int id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
