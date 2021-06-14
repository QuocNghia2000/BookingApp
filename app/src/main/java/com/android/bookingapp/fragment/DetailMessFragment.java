package com.android.bookingapp.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.viewmodel.DetailChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DetailMessFragment extends Fragment {
    private int id_user,doctorID;
    private RecyclerView rcvDetailMess;
    private DetailChatAdapter myAdapter;
    private boolean isUser;
    private TextView tvName;
    private EditText edtContent;
    private ImageView imvSend,ivBack;
    private DatabaseReference myRef;
    private SearchView searchView;
    DatabaseOpenHelper db;
    private ArrayList<Message> listMess;
    String nameDisplay,contentNotification;
    boolean checkisUser;
    Message message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_mess, container, false);
        init(v);
        handle();
        return v;
    }

    private void handle() {
        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString().trim();
                if (!content.equals(""))
                {
                    if(CheckInternet.checkInternet(getContext()))
                    {
                        Message message = new Message(0,id_user, doctorID, content,getDateTimeNow(),isUser,0);
                        myRef.child("Message").push().setValue(message);
                        try {
                            db.insertMessageToSqlite(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        edtContent.setText("");
                    }
                    else
                    {
                        Message message = new Message(0,id_user, doctorID, content,getDateTimeNow(),isUser,1);
                        try {
                            db.insertMessageToSqlite(message);
                            getData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        edtContent.setText("");
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Hãy nhập nội dung tin nhắn!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        edtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView();
            }
        });


    }

    private void init(View v) {
        if(getArguments()!=null)
        {
            doctorID = getArguments().getInt("doctorID");
            id_user = getArguments().getInt("id_user");
            isUser = getArguments().getBoolean("isUser");
            nameDisplay=getArguments().getString("nameDisplay");
        }
        listMess = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference();
        rcvDetailMess = v.findViewById(R.id.rcv_detail_mess);
        myAdapter = new DetailChatAdapter(listMess,isUser);
        rcvDetailMess.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcvDetailMess.setAdapter(myAdapter);


        tvName = v.findViewById(R.id.tv_name_detailMess);
        edtContent = v.findViewById(R.id.edt_text_detailMess);
        imvSend = v.findViewById(R.id.imv_send_listMess);
        ivBack=v.findViewById(R.id.imv_back_detailMess);
        searchView=v.findViewById(R.id.sv_detailMess);
        db=new DatabaseOpenHelper(getContext());
        getData();

        if(isUser)  tvName.setText(nameDisplay);
        else tvName.setText(nameDisplay);

    }

    public void getData(){
        if(CheckInternet.checkInternet(getContext()))
        {
            ArrayList<Message> messages=new ArrayList<>();
            myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("Message").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messages.clear();
                    if(listMess.size()>0) messages.addAll(listMess);
                    listMess.clear();
                    for(DataSnapshot data: snapshot.getChildren()){
                        Message m = data.getValue(Message.class);
                        if(m.getId_Doctor() == doctorID && m.getId_User() == id_user){
                            listMess.add(m);
                        }
                    }
                    if(messages.size()!=0)
                    {
                        for (int i = messages.size(); i < listMess.size(); i++) {
                            message=listMess.get(i);
                            checkisUser=listMess.get(i).isFromPerson();
                            contentNotification=listMess.get(i).getContent();
                        }
                        if(isUser&&!checkisUser)
                        {
                            CheckInternet.sendNotification(nameDisplay,contentNotification,getContext());
                            try {
                                db.insertMessageToSqlite(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(!isUser&&checkisUser)
                        {
                            CheckInternet.sendNotification(nameDisplay,contentNotification,getContext());
                        }
                    }
                    if(getDetailLocalMessage()!=null)
                    {
                        if(listMess.size()!=getDetailLocalMessage().size())
                        {
                           if(db.getMessageToUpdate().size()>0)
                           {
                               for(Message message:db.getMessageToUpdate())
                               {
                                   myRef.child("Message").push().setValue(message);
                               }
                           }
                            db.updateMessageSqlite();
                        }
                    }
                    scrollView();
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else
        {
            listMess.clear();
            listMess.addAll(getDetailLocalMessage());
            scrollView();
            myAdapter.notifyDataSetChanged();
        }
//        if(!CheckInternet.checkInternet(getContext()))
//        {
//            listMess.clear();
//            listMess.addAll(getDetailLocalMessage());
//            scrollView();
//            Toast.makeText(getContext(),String.valueOf("có vô k internet"),Toast.LENGTH_SHORT).show();
//            myAdapter.notifyDataSetChanged();
//        }
    }

    public ArrayList<Message> getDetailLocalMessage()
    {
        ArrayList<Message> messages=new ArrayList<>();
        Cursor cursor=db.getDetailFromMessage(doctorID);
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
    public void scrollView()
    {
        rcvDetailMess.scrollToPosition(rcvDetailMess.getAdapter().getItemCount() - 1);
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