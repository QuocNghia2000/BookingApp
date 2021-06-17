package com.android.bookingapp.view;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.NetWorkChangeListener;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    NetWorkChangeListener netWorkChangeListener;
    String nameUser,nameDoctor;
    boolean check;
    private int  countMess;
    private DatabaseReference myRef;
    private ArrayList<Message> listMess;
    private int idUser;
    Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netWorkChangeListener = new NetWorkChangeListener();
        listMess=new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference();
        idUser=getIntent().getIntExtra("id",-1);
        if(idUser!=-1)
        {
            check=true;
            getData(idUser);
        }
        else
        {
            check=false;
            getData(getIntent().getIntExtra("doctorID",-1));
        }
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangeListener, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(netWorkChangeListener);
        super.onStop();
    }

    public void getData(int id) {
        myRef.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countMess = listMess.size();
                listMess.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message.getId_User() == id && idUser!=-1 && check)
                    {
                        listMess.add(message);
                    }
                    else if(message.getId_Doctor() == id && !check)
                    {
                        listMess.add(message);
                    }
                }
                if (listMess.size()-countMess > 0 && countMess>0) {
                    for (int i = countMess; i < listMess.size(); i++) {
                        message=listMess.get(i);
                    }

                    if (message.isFromPerson()&&!check) {
                        myRef.child("User").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    if (user.getId() == message.getId_User())
                                    {
                                        nameUser=user.getFullname();
                                    }
                                }
                                CheckInternet.sendNotification(nameUser, message.getContent(), getApplicationContext());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    else if(check&!message.isFromPerson())
                    {
                        myRef.child("Doctor").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Doctor doctor = data.getValue(Doctor.class);
                                    if (doctor.getId() == message.getId_Doctor())
                                    {
                                        nameDoctor=doctor.getFullname();
                                    }
                                }
                                CheckInternet.sendNotification(nameDoctor, message.getContent(), getApplicationContext());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}