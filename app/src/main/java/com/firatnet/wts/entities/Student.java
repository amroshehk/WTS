package com.firatnet.wts.entities;

public class Student {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String photo_url;
    private String faculty;
    private String department;
    private String subject;
    private String level;
    private String password;
    private String created_at;
    private String updated_at;

    public Student(String id, String name, String email, String phone, String photo_url,
                   String faculty, String department, String subject, String level, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo_url = photo_url;
        this.faculty = faculty;
        this.department = department;
        this.subject = subject;
        this.level = level;
        this.password = password;
    }

    public Student(String id, String name, String email, String phone, String photo_url,
                   String faculty, String department, String subject, String level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo_url = photo_url;
        this.faculty = faculty;
        this.department = department;
        this.subject = subject;
        this.level = level;
    }

    public Student(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
