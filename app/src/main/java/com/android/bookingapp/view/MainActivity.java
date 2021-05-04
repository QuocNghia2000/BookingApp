package com.android.bookingapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        User user = new User(2, "abc","abc","abc","abc",new Date("18","03","2000"), true, "abc", "abc");
        DatabaseReference myRef = database.getReference("User");
        myRef.child("User"+user.getId()).setValue(user);;
    }
}