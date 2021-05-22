package com.android.bookingapp.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailChatAdapter extends RecyclerView.Adapter<DetailChatAdapter.MyViewHolder> {

    private Doctor doctor;
    private User user;
    private DatabaseReference myRef;
    private List<Message> listMess;
    private boolean isUser;

    public DetailChatAdapter(Doctor doctor,User user,boolean isUser){
        this.doctor = doctor;
        this.user = user;
        this.isUser = isUser;
        this.listMess = new ArrayList<>();
        getData();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_detail_mess, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailChatAdapter.MyViewHolder holder, int position) {
        if(isUser){
            if(listMess.get(position).isFromPerson()){
                holder.tvSend.setText(listMess.get(position).getContent());
                holder.tvSend.setVisibility(View.VISIBLE);
                holder.tvReceive.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.tvReceive.setText(listMess.get(position).getContent());
                holder.tvReceive.setVisibility(View.VISIBLE);
                holder.tvSend.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if(!listMess.get(position).isFromPerson()){
                holder.tvSend.setText(listMess.get(position).getContent());
                holder.tvSend.setVisibility(View.VISIBLE);
                holder.tvReceive.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.tvReceive.setText(listMess.get(position).getContent());
                holder.tvReceive.setVisibility(View.VISIBLE);
                holder.tvSend.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listMess.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSend;
        private TextView tvReceive;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSend = itemView.findViewById(R.id.sms_send);
            tvReceive = itemView.findViewById(R.id.sms_receive);
        }
    }

    public void getData(){
        myRef = FirebaseDatabase.getInstance().getReference("Message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMess.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    Message m = data.getValue(Message.class);
                    if(m.getId_Doctor() == doctor.getId() && m.getId_User() == user.getId()){
                        listMess.add(m);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
