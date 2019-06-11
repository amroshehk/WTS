package com.firatnet.wts.entities;

public class Receiver {
    int id;
    String name;
    String email;
    String phone_no;

    public Receiver(int id, String name, String email, String phone_no) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
    }

    public Receiver(String name, String email, String phone_no) {
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
