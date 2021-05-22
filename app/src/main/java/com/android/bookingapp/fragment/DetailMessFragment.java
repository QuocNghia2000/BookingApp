package com.android.bookingapp.fragment;

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
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.Time;
import com.android.bookingapp.model.User;
import com.android.bookingapp.viewmodel.DetailChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DetailMessFragment extends Fragment {
    private int id_user;
    private Doctor doctor;
    private RecyclerView rcvDetailMess;
    private DetailChatAdapter myAdapter;
    private boolean isUser;
    private TextView tvName;
    private EditText edtContent;
    private ImageView imvSend,ivBack;
    private DatabaseReference myRef;
    private User user;
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_mess, container, false);

        if(getArguments()!=null)
        {
            doctor = (Doctor) getArguments().getSerializable("doctor");
            id_user = getArguments().getInt("id_user");
            isUser = getArguments().getBoolean("isUser");
        }
        myRef = FirebaseDatabase.getInstance().getReference();
        myAdapter = new DetailChatAdapter(doctor,id_user,isUser,getContext(),this);
        rcvDetailMess = v.findViewById(R.id.rcv_detail_mess);
        rcvDetailMess.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcvDetailMess.setAdapter(myAdapter);

        tvName = v.findViewById(R.id.tv_name_detailMess);
        edtContent = v.findViewById(R.id.edt_text_detailMess);
        imvSend = v.findViewById(R.id.imv_send_listMess);
        ivBack=v.findViewById(R.id.imv_back_detailMess);
        searchView=v.findViewById(R.id.sv_detailMess);

        //getdata->fullname
        myRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    User user1 = data.getValue(User.class);
                    if(user1.getId()==id_user)
                    {
                        user=new User(user1.getId(),user1.getEmail(),user1.getPassword(),user1.getFullname(),user1.getPhone());
                        break;
                    }
                }
                if(isUser){
                    tvName.setText(doctor.getFullname());
                }
                else tvName.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();
                if (!content.equals(""))
                {
                    boolean check;
                    if(isUser) check = true;
                    else check = false;
                    Message message = new Message(0, id_user, doctor.getId(), content,getDateNow(),getTimeNow(),check);
                    myRef.child("Message").push().setValue(message);
                    edtContent.setText("");
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



        return v;
    }

    public void scrollView()
    {
        rcvDetailMess.scrollToPosition(rcvDetailMess.getAdapter().getItemCount() - 1);
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