package com.android.bookingapp.model;

import java.io.Serializable;

public class Reservation implements Serializable {
    private int id;
    private int id_user;
    private int id_doctor;
    private String symptorn;
    private String medicine;
    private Time time;
    private Date date;

    public Reservation() {
    }

    public Reservation(int id, int id_user, int id_doctor, String symptorn, String medicine, Time time, Date date) {
        this.id = id;
        this.id_user = id_user;
        this.id_doctor = id_doctor;
        this.symptorn = symptorn;
        this.medicine = medicine;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getSymptorn() {
        return symptorn;
    }

    public void setSymptorn(String symptorn) {
        this.symptorn = symptorn;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateTime(){
        String datetime = date.day + "-" + date.getMonth() + "-" + date.getYear() + " " + time.getHour() + ":" + time.getMinute();
        return datetime;
    }
}
