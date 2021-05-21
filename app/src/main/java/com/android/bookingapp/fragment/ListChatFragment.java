package com.android.bookingapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.Time;
import com.android.bookingapp.model.User;
import com.android.bookingapp.viewmodel.ListChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ListChatFragment extends Fragment {
    private List<Message> listMess;
    private User user;
    private Doctor doctor;
    private RecyclerView rcvListChat;
    private ListChatAdapter listChatAdapter;
    private List<Doctor> listDocContact;
    private DatabaseReference dbRef;
    private boolean isPerson;
    private boolean person;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_chat, container, false);

        listMess = new ArrayList<>();
        rcvListChat = v.findViewById(R.id.rcv_listchat);
        rcvListChat.setLayoutManager(new GridLayoutManager(getContext(),1));

        if(getArguments().getSerializable("user")!=null)
        {
            user = (User) getArguments().getSerializable("user");
            listChatAdapter = new ListChatAdapter(user,getContext());
        }
        else
        {
            doctor = (Doctor) getArguments().getSerializable("doctor");
            listChatAdapter = new ListChatAdapter(doctor,getContext());
        }

        rcvListChat.setAdapter(listChatAdapter);

//        Date d = new Date("20","5","2021");
//        Time t = new Time(22,04);
//        Message m = new Message(0,1,2,"Banh kem Thanh Hien!!",d,t,true);
//        myRef = FirebaseDatabase.getInstance().getReference();
//        myRef.child("Message").push().setValue(m);



        return v;
    }

}