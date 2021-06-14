package com.android.bookingapp.model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private boolean gender;
    private int department;
    private String achivement;
    private String address;

    public Doctor() {
    }

    public Doctor(int id, String email, String password, String fullname, String phone, int department, String achivement, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.gender = gender;
        this.department = department;
        this.achivement = achivement;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int id_department) {
        this.department = id_department;
    }

    public String getAchivement() {
        return achivement;
    }

    public void setAchivement(String achivement) {
        this.achivement = achivement;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

