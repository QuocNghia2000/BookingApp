package com.android.bookingapp.model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private boolean gender;
    private Department department;
    private String achivement;
    private String adđress;

    public Doctor(){}

    public Doctor(int id, String email, String password, String fullname, String phone, boolean gender, Department department, String achivement, String adđress) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.gender = gender;
        this.department = department;
        this.achivement = achivement;
        this.adđress = adđress;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getAchivement() {
        return achivement;
    }

    public void setAchivement(String achivement) {
        this.achivement = achivement;
    }

    public String getAdđress() {
        return adđress;
    }

    public void setAdđress(String adđress) {
        this.adđress = adđress;
    }
}

