package com.android.bookingapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.Time;
import com.android.bookingapp.model.User;
import com.android.bookingapp.viewmodel.DetailChatAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DetailMessFragment extends Fragment {
    private User user;
    private Doctor doctor;
    private RecyclerView rcvDetailMess;
    private DetailChatAdapter myAdapter;
    private boolean isUser;
    private TextView tvName;
    private EditText edtContent;
    private ImageView imvSend;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_mess, container, false);

        if(getArguments()!=null)
        {
            doctor = (Doctor) getArguments().getSerializable("doctor");
            user = (User) getArguments().getSerializable("user");
            isUser = getArguments().getBoolean("isUser");
        }
        myAdapter = new DetailChatAdapter(doctor,user,isUser);
        rcvDetailMess = v.findViewById(R.id.rcv_detail_mess);
        rcvDetailMess.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcvDetailMess.setAdapter(myAdapter);

        tvName = v.findViewById(R.id.tv_name_detailMess);
        edtContent = v.findViewById(R.id.edt_text_detailMess);
        imvSend = v.findViewById(R.id.imv_send_listMess);

        if(isUser){
            tvName.setText(doctor.getFullname());
        }
        else tvName.setText(user.getFullname());

        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();
                if (!content.equals(""))
                {
                    boolean check;
                    if(isUser) check = true;
                    else check = false;
                    Message message = new Message(0, user.getId(), doctor.getId(), content,getDateNow(),getTimeNow(),check);
                    myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child("Message").push().setValue(message);
                    edtContent.setText("");
                }
                else
                {
                    Toast.makeText(getContext(),"Hãy nhập nội dung tin nhắn!!",Toast.LENGTH_SHORT).show();
                }
            }
        });



        return v;
    }

    public Date getDateNow(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date d  = calendar.getTime();
        String day = simpleDateFormat.format(d);


        String[] temp =  day.split("-");
        Date date = new Date(temp[0],temp[1],temp[2]);
        return date;
    }

    public Time getTimeNow(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(calendar.getTime());

        String[] temp = time.split(":");
        Time t = new Time(Integer.parseInt(temp[0]) ,Integer.parseInt(temp[1]));
        return t;
    }
}