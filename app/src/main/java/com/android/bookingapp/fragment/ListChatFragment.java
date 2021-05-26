package com.android.bookingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.viewmodel.ListChatAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class ListChatFragment extends Fragment {
    private List<Message> listMess;
    //private User user;
    private Doctor doctor;
    private RecyclerView rcvListChat;
    private ListChatAdapter listChatAdapter;
    private List<Doctor> listDocContact;
    private DatabaseReference dbRef;
    private boolean isPerson;
    private boolean person;
    private ImageView imvBack;
    private int id_user;
    private SearchView searchView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listChatAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_chat, container, false);

        listMess = new ArrayList<>();
        rcvListChat = v.findViewById(R.id.rcv_listchat);
        imvBack=v.findViewById(R.id.imv_back_listChat);
        rcvListChat.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(getArguments().getInt("id_user",-1)!=-1)
        {
            id_user = getArguments().getInt("id_user");
            listChatAdapter = new ListChatAdapter(id_user,getContext());
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

        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        searchView = v.findViewById(R.id.sv_listChat);


        return v;
    }

}