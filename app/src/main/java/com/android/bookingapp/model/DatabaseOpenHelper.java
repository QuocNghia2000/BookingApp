package com.android.bookingapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BookingApp.db";

    SQLiteDatabase db;


    public DatabaseOpenHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        createMessageTable();

    }
    private void createMessageTable()
    {
        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE "+DbContract.MenuEntry.TABLE_MESSAGE+"("+
                DbContract.MenuEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.MenuEntry.COLUMN_ID_USER+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_ID_DOCTOR+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_CONTENT+" TEXT,"+
                DbContract.MenuEntry.COLUMN_DATE_TIME+" TEXT ,"+
                DbContract.MenuEntry.COLUMN_DATE_TIME+" TEXT,"+
                DbContract.MenuEntry.COLUMN_FROM_PERSON+" INTEGER "+");";

        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }
    public void saveMessageTableToDB(ArrayList<Message> messages) throws IOException {
        ContentValues contentValues=new ContentValues();
        for(Message message:messages)
        {
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_USER,message.getId_User());
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_DOCTOR,message.getId_Doctor());
            contentValues.put(DbContract.MenuEntry.COLUMN_CONTENT,message.getContent());
            contentValues.put(DbContract.MenuEntry.COLUMN_DATE_TIME,message.getDate().toString());
            contentValues.put(DbContract.MenuEntry.COLUMN_DATE_TIME,message.getTime().toString());
            contentValues.put(DbContract.MenuEntry.COLUMN_FROM_PERSON,message.isFromPerson());
            db.insert(DbContract.MenuEntry.TABLE_MESSAGE,null,contentValues);

        }
    }

    private void createUserTable()
    {
        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE "+DbContract.MenuEntry.TABLE_USER+"("+
                DbContract.MenuEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.MenuEntry.COLUMN_EMAIL+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_PASSWORD+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_FULLNAME+" TEXT,"+
                DbContract.MenuEntry.COLUMN_PHONE+" TEXT "+");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }
    public void saveUserTableToDB(ArrayList<User> users) throws IOException {
        ContentValues contentValues=new ContentValues();
        for(User user:users)
        {
            contentValues.put(DbContract.MenuEntry.COLUMN_EMAIL,user.getEmail());
            contentValues.put(DbContract.MenuEntry.COLUMN_PASSWORD,user.getPassword());
            contentValues.put(DbContract.MenuEntry.COLUMN_FULLNAME,user.getFullname());
            contentValues.put(DbContract.MenuEntry.COLUMN_PHONE,user.getPassword());
            db.insert(DbContract.MenuEntry.TABLE_USER,null,contentValues);
        }
    }
    private void createDoctorTable()
    {
        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE "+DbContract.MenuEntry.TABLE_DOCTOR+"("+
                DbContract.MenuEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.MenuEntry.COLUMN_ID_DEPARTMENT+" INTERGER,"+
                DbContract.MenuEntry.COLUMN_EMAIL_DOCTOR+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_PASSWORD_DOCTOR+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_FULLNAME_DOCTOR+" TEXT,"+
                DbContract.MenuEntry.COLUMN_PHONE_DOCTOR+" TEXT ,"+
                DbContract.MenuEntry.COLUMN_ACHIVEMENT+" TEXT ,"+
                DbContract.MenuEntry.COLUMN_ADDRESS_DOCTOR+" TEXT "+");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }
    public void saveDoctorTableToDB(ArrayList<Doctor> doctors) throws IOException {
        ContentValues contentValues=new ContentValues();
        for(Doctor doctor:doctors)
        {
            contentValues.put(DbContract.MenuEntry.COLUMN_EMAIL,doctor.getEmail());
            contentValues.put(DbContract.MenuEntry.COLUMN_PASSWORD,doctor.getPassword());
            contentValues.put(DbContract.MenuEntry.COLUMN_FULLNAME,doctor.getFullname());
            contentValues.put(DbContract.MenuEntry.COLUMN_PHONE,doctor.getPassword());
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_DEPARTMENT,doctor.getDepartment());
            contentValues.put(DbContract.MenuEntry.COLUMN_ACHIVEMENT,doctor.getAchivement());
            contentValues.put(DbContract.MenuEntry.COLUMN_ADDRESS_DOCTOR,doctor.getAddress());
            db.insert(DbContract.MenuEntry.TABLE_DOCTOR,null,contentValues);
        }
    }
    private void createReservationTable()
    {
        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE "+DbContract.MenuEntry.TABLE_RESERVATION+"("+
                DbContract.MenuEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.MenuEntry.COLUMN_ID_USER_RESERVATION+" INTERGER,"+
                DbContract.MenuEntry.COLUMN_ID_DOCTOR_RESERVATION+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_SYMPTORN+" INTEGER NOT NULL,"+
                DbContract.MenuEntry.COLUMN_MEDICINE+" TEXT,"+
                DbContract.MenuEntry.COLUMN_DATE_TIME_RESERVATION+" TEXT "+");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }
    public void saveReservationTableToDB(ArrayList<Reservation> reservations) throws IOException {
        ContentValues contentValues=new ContentValues();
        for(Reservation reservation:reservations)
        {
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_USER_RESERVATION,reservation.getId_user());
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_DOCTOR_RESERVATION,reservation.getId_doctor());
            contentValues.put(DbContract.MenuEntry.COLUMN_SYMPTORN,reservation.getSymptorn());
            contentValues.put(DbContract.MenuEntry.COLUMN_MEDICINE,reservation.getMedicine());
            contentValues.put(DbContract.MenuEntry.COLUMN_DATE_TIME_RESERVATION,reservation.getDate().toString());
            db.insert(DbContract.MenuEntry.TABLE_RESERVATION,null,contentValues);
        }
    }
    private void createDepartmentTable()
    {
        final String SQL_CREATE_BUGS_TABLE="CREATE TABLE "+DbContract.MenuEntry.TABLE_RESERVATION+"("+
                DbContract.MenuEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.MenuEntry.COLUMN_NAME_DEPARTMENT+" TEXT "+");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }
    public void saveDepartmentTableToDB(ArrayList<Department> departments) throws IOException {
        ContentValues contentValues=new ContentValues();
        for(Department department:departments)
        {
            contentValues.put(DbContract.MenuEntry.COLUMN_NAME_DEPARTMENT,department.getName());
            db.insert(DbContract.MenuEntry.TABLE_DEPARTMENT,null,contentValues);
        }
    }
    public Cursor getMessage()
    {
        Cursor cursor=db.rawQuery("Select * from "+DbContract.MenuEntry.TABLE_MESSAGE,null);
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DbContract.MenuEntry.TABLE_MESSAGE);
    }
}
