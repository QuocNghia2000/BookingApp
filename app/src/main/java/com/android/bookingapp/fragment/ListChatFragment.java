package com.android.bookingapp.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
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
    private int doctorID;
    private RecyclerView rcvListChat;
    private ListChatAdapter listChatAdapter;
    private DatabaseReference dbRef;
    private ImageView imvBack;
    private int id_user;
    private SearchView searchView;
    private ArrayList<Doctor> listContact;
    private ArrayList<User> listContactDoc;
    DatabaseOpenHelper db;

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
        init(v);
        if(getArguments().getInt("id_user",-1)!=-1)
        {
            id_user = getArguments().getInt("id_user");
            listContact = new ArrayList<>();
            if(CheckInternet.checkInternet(getContext())){
                getListMessUser();
            }
            else getListMessUserOff();

        }
        else
        {
            doctorID =  getArguments().getInt("doctorID");
            getListMessDoctor();
        }

        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void init(View v) {
        listMess = new ArrayList<>();
        listContact = new ArrayList<>();
        listContactDoc = new ArrayList<>();
        db=new DatabaseOpenHelper(getContext());
        rcvListChat = v.findViewById(R.id.rcv_listchat);
        imvBack=v.findViewById(R.id.imv_back_listChat);
        searchView = v.findViewById(R.id.sv_listChat);
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void getListMessUser(){
        dbRef.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    Message mess = data.getValue(Message.class);
                    if(mess.getId_User() == id_user)
                    {
                        listMess.add(mess);
                    }
                }
                dbRef.child("Doctor").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Integer> idDoctor=listIdDoctorLatest();
                        for(DataSnapshot data: snapshot.getChildren())
                        {
                            Doctor doctor = data.getValue(Doctor.class);
                            if(idDoctor.indexOf(doctor.getId())!=-1)
                            {
                                listContact.add(doctor);
                            }
                        }
                        listContact =updateListChatUser(idDoctor);
                        setListChatUserAdapter();
                        listChatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getListMessUserOff(){
        ArrayList<Message> messTemp = getDetailLocalMessage();
        ArrayList<Doctor> docTemp = getDetailLocalDoctor();
        for(Message mess : messTemp){
            if(mess.getId_User() == id_user)
            {
                listMess.add(mess);
            }
        }
        ArrayList<Integer> idDoctor = listIdDoctorLatest();
        for(Doctor doctor : docTemp){
            if(idDoctor.indexOf(doctor.getId())!=-1)
            {
                listContact.add(doctor);
            }
        }
        listContact = updateListChatUser(idDoctor);
        setListChatUserAdapter();
        listChatAdapter.notifyDataSetChanged();
    }


    public void getListMessDoctor(){
        dbRef.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    Message mess = data.getValue(Message.class);
                    if(mess.getId_Doctor() == doctorID)
                    {
                        listMess.add(mess);
                    }
                }

                dbRef.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Integer> idUser=listIdUserLatest();
                        for(DataSnapshot data: snapshot.getChildren())
                        {
                            User user = data.getValue(User.class);
                            if(idUser.indexOf(user.getId())!=-1)
                            {
                                listContactDoc.add(user);
                            }
                        }

                        listContactDoc =updateListChatDoctor(idUser);
                        setListChatDoctorAdapter();
                        listChatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private ArrayList<Integer> listIdDoctorLatest()
    {
        ArrayList<Integer> idDoctor = new ArrayList<>();
        for(int i=listMess.size()-1;i>=0;i--)
        {
            if(idDoctor.indexOf(listMess.get(i).getId_Doctor())==-1)
            {
                idDoctor.add(listMess.get(i).getId_Doctor());
            }
        }
        return idDoctor;
    }
    private ArrayList<Integer> listIdUserLatest()
    {
        ArrayList<Integer> idUser = new ArrayList<>();
        for(int i=listMess.size()-1;i>=0;i--)
        {
            if(idUser.indexOf(listMess.get(i).getId_User())==-1)
            {
                idUser.add(listMess.get(i).getId_User());
            }
        }
        return idUser;
    }
    private ArrayList<Doctor> updateListChatUser(ArrayList<Integer> idDoctor)
    {
        ArrayList<Doctor> doctemp = new ArrayList<>();
        for(int i=0;i<idDoctor.size();i++){
            for(Doctor d : listContact){
                if (idDoctor.get(i) == d.getId())
                {
                    doctemp.add(d);
                    break;
                }
            }
        }
        return doctemp;
    }
    private ArrayList<User> updateListChatDoctor(ArrayList<Integer> idUser)
    {
        ArrayList<User> doctemp = new ArrayList<>();
        for(int i=0;i<idUser.size();i++){
            for(User d : listContactDoc){
                if (idUser.get(i) == d.getId())
                {
                    doctemp.add(d);
                    break;
                }
            }
        }
        return doctemp;
    }

    private void setListChatUserAdapter()
    {
        rcvListChat.setLayoutManager(new GridLayoutManager(getContext(),1));
        listChatAdapter = new ListChatAdapter(id_user,listContact, (ArrayList<Message>) listMess);
        rcvListChat.setAdapter(listChatAdapter);
    }
    private void setListChatDoctorAdapter()
    {
        rcvListChat.setLayoutManager(new GridLayoutManager(getContext(),1));
        listChatAdapter = new ListChatAdapter(doctorID,listContactDoc, (ArrayList<Message>) listMess,"");
        rcvListChat.setAdapter(listChatAdapter);
    }

    public ArrayList<Doctor> getDetailLocalDoctor(){
        ArrayList<Doctor> doctors=new ArrayList<>();
        Cursor cursor=db.getDoctorFromSqlite();
        while (cursor.moveToNext())
        {
            int id=cursor.getInt(0);
            int id_Depart=cursor.getInt(1);
            String email=cursor.getString(2);
            String password=cursor.getString(3);
            String fullname=cursor.getString(4);
            String phone=cursor.getString(5);
            String achivement=cursor.getString(6);
            String address=cursor.getString(7);

            Doctor doctor = new Doctor(id, email,password, fullname,phone,id_Depart,achivement,address);
            doctors.add(doctor);
        }
        return doctors;
    }

    public ArrayList<Message> getDetailLocalMessage()
    {
        ArrayList<Message> messages=new ArrayList<>();
        Cursor cursor=db.getMessageFromSqlite();
        while (cursor.moveToNext())
        {
            int id=cursor.getInt(0);
            int idUser=cursor.getInt(1);
            int idDoctor=cursor.getInt(2);
            String content=cursor.getString(3);
            int from_person=cursor.getInt(5);
            int checkLocalMess=cursor.getInt(6);
            Message message = new Message(id, idUser,idDoctor, content,getDateTimeNow(),from_person==1?true:false,checkLocalMess);
            messages.add(message);
        }
        return messages;
    }


    public String getDateTimeNow(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        String time = simpleTimeFormat.format(calendar.getTime());
        java.util.Date day  = calendar.getTime();
        return  simpleDateFormat.format(day)+" "+time;
    }

}