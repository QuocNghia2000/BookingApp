package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.databinding.ItemRvDocMainBinding;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Reservation;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DocMainAdapter extends RecyclerView.Adapter<DocMainAdapter.MyViewHolder> {
    private int doctorID;
    private Date date;
    private DatabaseReference myRef;
    private List<Reservation> listRE;
    private List<User> listUser;
    private Context context;

    public DocMainAdapter(Date date,int doctorID,Context context){
        this.doctorID = doctorID;
        this.date = date;
        this.listUser = new ArrayList<>();
        this.listRE = new ArrayList<>();
        this.context = context;
        getReservation();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRvDocMainBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_rv_doc_main,parent,false);
        View v = binding.getRoot();
        return new MyViewHolder(v,binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DocMainAdapter.MyViewHolder holder, int position) {
        holder.tvTime.setText(listRE.get(position).getTime().getHour() + ":" + listRE.get(position).getTime().getMinute());
        holder.tvSymptom.setText(listRE.get(position).getSymptorn());
        for(User u : listUser){
            if(listRE.get(position).getId_user() == u.getId()){
                holder.binding.setUser(u);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listRE.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime;
        private TextView tvSymptom;
        private ItemRvDocMainBinding binding;

        public MyViewHolder(@NonNull View itemView,ItemRvDocMainBinding binding) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_docMain);
            tvSymptom = itemView.findViewById(R.id.tv_symptom_docMain);
            this.binding = binding;
        }
    }


    public void getReservation(){
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Reservation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Reservation reservation = data.getValue(Reservation.class);
//                    if(isSameDate(date,reservation.getDate())) Toast.makeText(context,date.getDay(),Toast.LENGTH_SHORT).show();
                    if(reservation.getId_doctor()==doctorID && isSameDate(date,reservation.getDate())){
                        listRE.add(reservation);
                    }
                }

                List<Reservation> temp = new ArrayList<>();
                for(int i=listRE.size()-1;i>=0;i--){
                    temp.add(listRE.get(i));
                }
                listRE = new ArrayList<>(temp);

                myRef.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            User user = data.getValue(User.class);
                            if(isContainList(user.getId())){
                                listUser.add(user);
                            }
                        }
                       notifyDataSetChanged();
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

    public boolean isSameDate(Date d1,Date d2){
        if(!d1.getDay().equals(d2.getDay())) return false;
        if(!d1.getMonth().equals(d2.getMonth())) return false;
        if(!d1.getYear().equals(d2.getYear())) return false;
        return true;
    }
    public boolean isContainList(int idUser){
        for (Reservation r : listRE){
            if(r.getId_user() == idUser){
                return true;
            }
        }
        return false;
    }
}
