package com.android.bookingapp.model;

import android.provider.BaseColumns;

public class DbContract {
    public static final class MenuEntry implements BaseColumns
    {
        //table Message
        public static final String TABLE_MESSAGE="Message";
        public static final String COLUMN_ID_USER="id_user";
        public static final String COLUMN_ID_DOCTOR="id_doctor";
        public static final String COLUMN_CONTENT="content";
        public static final String COLUMN_DATE_TIME="date_time";
        public static final String COLUMN_FROM_PERSON="from_person";
        public static final String COLUMN_CHECK_MESS_LOCAL_="MessLocal";
        //table User
        public static final String TABLE_USER="User";
        public static final String COLUMN_EMAIL="email";
        public static final String COLUMN_PASSWORD="password";
        public static final String COLUMN_FULLNAME="fullname";
        public static final String COLUMN_PHONE="phone";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_JOB = "job";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_BIRTHDAY = "birthday";

        //table Doctor
        public static final String TABLE_DOCTOR="Doctor";
        public static final String COLUMN_EMAIL_DOCTOR="email";
        public static final String COLUMN_PASSWORD_DOCTOR="password";
        public static final String COLUMN_FULLNAME_DOCTOR="fullname";
        public static final String COLUMN_PHONE_DOCTOR="phone";
        public static final String COLUMN_ID_DEPARTMENT="id_department";
        public static final String COLUMN_ACHIVEMENT="achivement";
        public static final String COLUMN_ADDRESS_DOCTOR="address";
        //table Reservation
        public static final String TABLE_RESERVATION="Reservation";
        public static final String COLUMN_ID_USER_RESERVATION="id_user";
        public static final String COLUMN_ID_DOCTOR_RESERVATION="id_doctor";
        public static final String COLUMN_SYMPTORN="symptorn";
        public static final String COLUMN_MEDICINE="medicine";
        public static final String COLUMN_DATE_TIME_RESERVATION="date_time";

        //table Department
        public static final String TABLE_DEPARTMENT="Department";
        public static final String COLUMN_NAME_DEPARTMENT="name";
    }
}
