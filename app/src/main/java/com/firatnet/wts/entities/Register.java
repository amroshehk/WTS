package com.firatnet.wts.entities;

public class Register {

    String id;
    String name;
    String email;
    String created_at;
    String phone;
    String country;
    String status;
    String photo_url;
    String password;

    public Register(String id, String name, String email, String created_at, String phone, String country, String status, String photo_url ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.phone = phone;
        this.country = country;
        this.status = status;
        this.photo_url = photo_url;
    }

    public Register(String name, String email, String phone, String country) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
