package com.android.bookingapp.view;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.bookingapp.R;

public class MainActivity extends AppCompatActivity {
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if(check)
            super.onBackPressed();
        check=true;
        Toast.makeText(this,"Nhấn lần nữa để thoát",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                check=false;
            }
        },2000);
    }
}