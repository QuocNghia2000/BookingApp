package com.android.bookingapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseOpenHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BookingApp.db";
    private static final String EXAMPLE = "customer";
    SQLiteDatabase db;


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Database created succesfully!");
    }


    public void createMessageTable() {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE if not exists " + DbContract.MenuEntry.TABLE_MESSAGE + "(" +
                DbContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MenuEntry.COLUMN_ID_USER + " INTEGER NOT NULL," +
                DbContract.MenuEntry.COLUMN_ID_DOCTOR + " INTEGER NOT NULL," +
                DbContract.MenuEntry.COLUMN_CONTENT + " TEXT," +
                DbContract.MenuEntry.COLUMN_DATE_TIME + " TEXT ," +
                DbContract.MenuEntry.COLUMN_FROM_PERSON + " INTEGER NOT NULL," +
                DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_ + " INTEGER " + ");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }

    public void saveMessageTableToDB(ArrayList<Message> messages) throws IOException {
        ContentValues contentValues = new ContentValues();
        for (Message message : messages) {
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_USER, message.getId_User());
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_DOCTOR, message.getId_Doctor());
            contentValues.put(DbContract.MenuEntry.COLUMN_CONTENT, message.getContent());
            contentValues.put(DbContract.MenuEntry.COLUMN_DATE_TIME, message.getDate_time());
            if (message.isFromPerson())
                contentValues.put(DbContract.MenuEntry.COLUMN_FROM_PERSON, 1);
            else
                contentValues.put(DbContract.MenuEntry.COLUMN_FROM_PERSON, 0);
            contentValues.put(DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_, 0);
            db.insert(DbContract.MenuEntry.TABLE_MESSAGE, null, contentValues);
        }
    }

    public void insertMessageToSqlite(Message message) throws IOException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.MenuEntry.COLUMN_ID_USER, message.getId_User());
        contentValues.put(DbContract.MenuEntry.COLUMN_ID_DOCTOR, message.getId_Doctor());
        contentValues.put(DbContract.MenuEntry.COLUMN_CONTENT, message.getContent());
        contentValues.put(DbContract.MenuEntry.COLUMN_DATE_TIME, message.getDate_time());
        contentValues.put(DbContract.MenuEntry.COLUMN_FROM_PERSON, message.isFromPerson() ? 1 : 0);
        contentValues.put(DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_, message.getCheckLocalMes());
        db.insert(DbContract.MenuEntry.TABLE_MESSAGE, null, contentValues);
    }

    public Cursor getMessageFromSqlite() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_MESSAGE, null);
        return cursor;
    }

    public Cursor getDetailFromMessage(int id_doctor) {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_MESSAGE + " where " + DbContract.MenuEntry.COLUMN_ID_DOCTOR
                + "=" + id_doctor, null);
        return cursor;
    }

    public Cursor getDoctorFromMessage() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_MESSAGE + " where " + DbContract.MenuEntry.COLUMN_FROM_PERSON
                + "=" + 0, null);
        return cursor;
    }

    public void deleteInformationUser() {
        db.execSQL("drop table " + DbContract.MenuEntry.TABLE_MESSAGE);
        db.execSQL("drop table " + DbContract.MenuEntry.TABLE_USER);
    }

    public ArrayList<Message> getMessageToUpdate() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_MESSAGE + " where " + DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_
                + "=1", null);
        ArrayList<Message> messages = new ArrayList<>();
        while (cursor.moveToNext()) {
            int idUser = cursor.getInt(1);
            int idDoctor = cursor.getInt(2);
            String content = cursor.getString(3);
            String date_time = cursor.getString(4);
            int from_person = cursor.getInt(5);
            Message message = new Message(idUser, idDoctor, content, date_time, from_person == 1);
            messages.add(message);
        }
        return messages;
    }

    public void updateMessageSqlite() {
        db.execSQL("update " + DbContract.MenuEntry.TABLE_MESSAGE + " set " + DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_ + "=0 where " +
                DbContract.MenuEntry.COLUMN_CHECK_MESS_LOCAL_ + "=1");
    }

    public void createUserTable() {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE if not exists  " + DbContract.MenuEntry.TABLE_USER + "(" +
                DbContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MenuEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                DbContract.MenuEntry.COLUMN_PASSWORD + " TEXT NOT NULL," +
                DbContract.MenuEntry.COLUMN_FULLNAME + " TEXT," +
                DbContract.MenuEntry.COLUMN_PHONE + " TEXT ," +
                DbContract.MenuEntry.COLUMN_GENDER + " INTEGER ," +
                DbContract.MenuEntry.COLUMN_JOB + " TEXT ," +
                DbContract.MenuEntry.COLUMN_ADDRESS + " TEXT ," +
                DbContract.MenuEntry.COLUMN_BIRTHDAY + " TEXT ," +
                DbContract.MenuEntry.COLUMN_ID_USER + " INTEGER " + ");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }

    public void saveUserTableToDB(ArrayList<User> users) throws IOException {
        ContentValues contentValues = new ContentValues();
        for (User user : users) {
            int gender;
            if (user.isGender() == true) gender = 1;
            else gender = 0;
            String birthday = user.getBirthday().day + "/" + user.getBirthday().month + "/" + user.getBirthday().year;
            contentValues.put(DbContract.MenuEntry.COLUMN_EMAIL, user.getEmail());
            contentValues.put(DbContract.MenuEntry.COLUMN_PASSWORD, user.getPassword());
            contentValues.put(DbContract.MenuEntry.COLUMN_FULLNAME, user.getFullname());
            contentValues.put(DbContract.MenuEntry.COLUMN_PHONE, user.getPhone());
            contentValues.put(DbContract.MenuEntry.COLUMN_GENDER, gender);
            contentValues.put(DbContract.MenuEntry.COLUMN_JOB, user.getJob());
            contentValues.put(DbContract.MenuEntry.COLUMN_ADDRESS, user.getAddress());
            contentValues.put(DbContract.MenuEntry.COLUMN_BIRTHDAY, birthday);
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_USER, user.getId());
            db.insert(DbContract.MenuEntry.TABLE_USER, null, contentValues);
        }
    }

    public Cursor getUserFromSqlite() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_USER, null);
        return cursor;
    }

    public Cursor getUserFromUser(int id_user) {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_USER + " where " + DbContract.MenuEntry._ID
                + "=" + id_user, null);
        return cursor;
    }

    public Cursor getUserBookFromUser(int id_user) {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_USER + " where " + DbContract.MenuEntry._ID
                + "=" + id_user, null);
        return cursor;
    }

    public void createDoctorTable() {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE if not exists " + DbContract.MenuEntry.TABLE_DOCTOR + "(" +
                DbContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MenuEntry.COLUMN_ID_DEPARTMENT + " INTERGER," +
                DbContract.MenuEntry.COLUMN_EMAIL_DOCTOR + " INTEGER NOT NULL," +
                DbContract.MenuEntry.COLUMN_PASSWORD_DOCTOR + " INTEGER NOT NULL," +
                DbContract.MenuEntry.COLUMN_FULLNAME_DOCTOR + " TEXT," +
                DbContract.MenuEntry.COLUMN_PHONE_DOCTOR + " TEXT ," +
                DbContract.MenuEntry.COLUMN_ACHIVEMENT + " TEXT ," +
                DbContract.MenuEntry.COLUMN_ADDRESS_DOCTOR + " TEXT " + ");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }

    public Cursor getDoctorFromSqlite() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_DOCTOR, null);
        return cursor;
    }

    public void updateUserSqlite(User user) {
        int gender;
        if (user.isGender() == true) gender = 1;
        else gender = 0;
        String birthday = user.getBirthday().day + "/" + user.getBirthday().month + "/" + user.getBirthday().year;
        db.execSQL("update " + DbContract.MenuEntry.TABLE_USER + " set " + DbContract.MenuEntry.COLUMN_EMAIL + "= '" + user.getEmail() + "', "
                + DbContract.MenuEntry.COLUMN_PASSWORD + "= '" + user.getPassword() + "', "
                + DbContract.MenuEntry.COLUMN_FULLNAME + "= '" + user.getFullname() + "', "
                + DbContract.MenuEntry.COLUMN_PHONE + "= '" + user.getPhone() + "', "
                + DbContract.MenuEntry.COLUMN_JOB + "= '" + user.getJob() + "', "
                + DbContract.MenuEntry.COLUMN_ADDRESS + "= '" + user.getAddress() + "', "
                + DbContract.MenuEntry.COLUMN_BIRTHDAY + "= '" + birthday + "', "
                + DbContract.MenuEntry.COLUMN_GENDER + "=" + gender +
                " where " + DbContract.MenuEntry._ID + "=1");
    }

    public void saveDoctorTableToDB(ArrayList<Doctor> doctors) throws IOException {
        ContentValues contentValues = new ContentValues();
        for (Doctor doctor : doctors) {
            contentValues.put(DbContract.MenuEntry.COLUMN_EMAIL, doctor.getEmail());
            contentValues.put(DbContract.MenuEntry.COLUMN_PASSWORD, doctor.getPassword());
            contentValues.put(DbContract.MenuEntry.COLUMN_FULLNAME, doctor.getFullname());
            contentValues.put(DbContract.MenuEntry.COLUMN_PHONE, doctor.getPhone());
            contentValues.put(DbContract.MenuEntry.COLUMN_ID_DEPARTMENT, doctor.getDepartment());
            contentValues.put(DbContract.MenuEntry.COLUMN_ACHIVEMENT, doctor.getAchivement());
            contentValues.put(DbContract.MenuEntry.COLUMN_ADDRESS_DOCTOR, doctor.getAddress());
            db.insert(DbContract.MenuEntry.TABLE_DOCTOR, null, contentValues);
        }
    }

    public void createDepartmentTable() {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE if not exists " + DbContract.MenuEntry.TABLE_DEPARTMENT + "(" +
                DbContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MenuEntry.COLUMN_NAME_DEPARTMENT + " TEXT " + ");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
    }

    public Cursor getDepartmentFromSqlite() {
        Cursor cursor = db.rawQuery("Select * from " + DbContract.MenuEntry.TABLE_DEPARTMENT, null);
        return cursor;
    }

    public void saveDepartmentTableToDB(ArrayList<Department> departments) throws IOException {
        ContentValues contentValues = new ContentValues();
        for (Department department : departments) {
            contentValues.put(DbContract.MenuEntry.COLUMN_NAME_DEPARTMENT, department.getName());
            db.insert(DbContract.MenuEntry.TABLE_DEPARTMENT, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DbContract.MenuEntry.TABLE_MESSAGE);
        onCreate(db);
    }
}
