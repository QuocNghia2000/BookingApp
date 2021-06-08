package com.android.bookingapp.model;

public class Message {
    private int id;
    private int id_User;
    private int id_Doctor;
    private String content;
    private Date date;
    private Time time;
    private boolean fromPerson;
    private int checkLocalMess;
    //true: User , false: Doctor

    public Message(){}

    public Message(int id, int id_User, int id_Doctor, String content, Date date, Time time,boolean fromPerson) {
        this.id = id;
        this.id_User = id_User;
        this.id_Doctor = id_Doctor;
        this.content = content;
        this.date = date;
        this.time = time;
        this.fromPerson = fromPerson;
    }

    public Message(int id, int id_User, int id_Doctor, String content, Date date, Time time, boolean fromPerson, int checkLocalMess) {
        this.id = id;
        this.id_User = id_User;
        this.id_Doctor = id_Doctor;
        this.content = content;
        this.date = date;
        this.time = time;
        this.fromPerson = fromPerson;
        this.checkLocalMess = checkLocalMess;
    }

    public int getCheckLocalMes() {
        return checkLocalMess;
    }

    public void setCheckLocalMes(int checkLocalMes) {
        this.checkLocalMess = checkLocalMes;
    }

    public boolean isFromPerson() {
        return fromPerson;
    }

    public void setFromPerson(boolean fromPerson) {
        this.fromPerson = fromPerson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public int getId_Doctor() {
        return id_Doctor;
    }

    public void setId_Doctor(int id_Doctor) {
        this.id_Doctor = id_Doctor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
