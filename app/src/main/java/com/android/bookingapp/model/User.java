package com.android.bookingapp.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private Date birthday;
    private boolean gender;
    private String job;
    private String adđress;

    public User(){}

    public User(String email, String password, String fullname, String phone) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
    }

    public User(int id, String email, String password, String fullname, String phone, Date birthday, boolean gender, String job, String adđress) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.job = job;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAdđress() {
        return adđress;
    }

    public void setAdđress(String adđress) {
        this.adđress = adđress;
    }
}
