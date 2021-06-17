package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.databinding.ItemRvDocMainBinding;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.Reservation;
import com.android.bookingapp.model.Time;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocMainAdapter extends RecyclerView.Adapter<DocMainAdapter.MyViewHolder> {
    private final int doctorID;
    private final Context context;
    private DatabaseReference myRef;
    private Date date;
    private List<Reservation> listRE;
    private List<User> listUser;
    DatabaseOpenHelper db;

    public DocMainAdapter(Context context,Date date,int doctorID) {
        this.doctorID = doctorID;
        this.date = date;
        this.context = context;
        db = new DatabaseOpenHelper(context);
        this.listRE = new ArrayList<>();
        this.listUser = new ArrayList<>();
        if(CheckInternet.checkInternet(context)){
            getUser();
            getReservation();
        }
        else{
            getReservationOff();
            getUserOff();
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRvDocMainBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_rv_doc_main, parent, false);
        View v = binding.getRoot();
        return new MyViewHolder(v, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DocMainAdapter.MyViewHolder holder, int position) {
        int hour = listRE.get(position).getTime().getHour();
        int minute = listRE.get(position).getTime().getMinute();
        String time = ((hour < 10) ? "0" : "") + hour + ":" + ((minute < 10) ? "0" : "") + minute;
        holder.tvTime.setText(time);
        holder.tvSymptom.setText(listRE.get(position).getSymptorn());
        if (holder.tvSymptom.getText().equals("")) {
            holder.tvSymptom.setText("Không có biểu hiện gì!");
        }
        User user = null;
        for (User u : listUser) {
            if (listRE.get(position).getId_user() == u.getId()) {
                holder.binding.setUser(u);
                user = u;
                break;
            }
        }
        User finalUser = user;
        holder.imChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctorID", doctorID);
                bundle.putInt("id_user", listRE.get(position).getId_user());
                bundle.putString("nameDisplay", finalUser.getFullname());
                bundle.putBoolean("isUser", false);
                Navigation.findNavController(v).navigate(R.id.action_docMainFragment_to_detailMessFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRE.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvSymptom;
        private final ItemRvDocMainBinding binding;
        private final ImageView imChat;

        public MyViewHolder(@NonNull View itemView, ItemRvDocMainBinding binding) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_docMain);
            tvSymptom = itemView.findViewById(R.id.tv_symptom_docMain);
            imChat = itemView.findViewById(R.id.imv_chatDetail_docMain);
            this.binding = binding;
        }
    }

    public void getReservation() {
        myRef = FirebaseDatabase.getInstance().getReference("Reservation");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listRE = new ArrayList<>();
                ArrayList<Reservation> RETemps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Reservation reservation = data.getValue(Reservation.class);
                    if (reservation.getId_doctor() == doctorID) {
                        RETemps.add(reservation);
                        if(isSameDate(date, reservation.getDate())){
                            listRE.add(reservation);
                        }
                    }
                }
                listRE = DaoList(listRE);
                notifyDataSetChanged();
                try {
                    db.createReservationTable();
                    Cursor cursor = db.getReservationFromSqlite();
                    if (cursor.getCount() == 0) db.saveReservationTableToDB(RETemps);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUser() {
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    listUser.add(user);
                }
                try {
                    db.createUserTable();
                    Cursor cursor = db.getUserFromSqlite();
                    if (cursor.getCount() == 0) db.saveUserTableToDB((ArrayList<User>) listUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public boolean isSameDate(Date d1, Date d2) {
        if (!d1.getDay().equals(d2.getDay())) return false;
        if (!d1.getMonth().equals(d2.getMonth())) return false;
        return d1.getYear().equals(d2.getYear());
    }

    public ArrayList<Reservation> getDetailLocalReservation() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        Cursor cursor = db.getReservationFromSqlite();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int idUser = cursor.getInt(1);
            int idDoctor = cursor.getInt(2);
            String symptorn = cursor.getString(3);
            String medicine = cursor.getString(4);
            String datetime = cursor.getString(5);
            String[] dt = datetime.split(" ");
            String[] d = dt[0].split("-");
            String[] t = dt[1].split(":");
            Date date = new Date(d[0],d[1],d[2]);
            Time time = new Time(Integer.parseInt(t[0]),Integer.parseInt(t[1]));
            Reservation reservation = new Reservation(id, idUser, idDoctor, symptorn, medicine, time,date);
            reservations.add(reservation);
        }
        return reservations;
    }

    public ArrayList<User> getDetailLocalUser() {
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.getUserFromSqlite();
        while (cursor.moveToNext()) {
            int idUser=cursor.getInt(9);
            String email = cursor.getString(1);
            String password=cursor.getString(2);
            String fullname=cursor.getString(3);
            String phone=cursor.getString(4);
            String birth=cursor.getString(8);
            String[] date = birth.split("/");
            Date birthday= new Date(date[0],date[1],date[2]);
            int gender = Integer.valueOf(cursor.getString(5));
            String job=cursor.getString(6);
            String address=cursor.getString(7);
            User user = new User(idUser,email, password,fullname,phone,birthday,(gender==1)?true:false,job,address);
            users.add(user);
        }
        return users;
    }

    public void getReservationOff(){
        List<Reservation> listREtemp = getDetailLocalReservation();
        for(Reservation reservation : listREtemp){
            if (reservation.getId_doctor() == doctorID) {
                if(isSameDate(date, reservation.getDate())){
                    listRE.add(reservation);
                }
            }
        }
        listRE = DaoList(listRE);
    }

    public void getUserOff(){
        List<User> listUserTemp = getDetailLocalUser();
        for(User user : listUserTemp){
            listUser.add(user);
        }
    }

    public List<Reservation> DaoList(List<Reservation> listRES){
        List<Reservation> temp = new ArrayList<>();
        for (int i = listRES.size() - 1; i >= 0; i--) {
            temp.add(listRES.get(i));
        }
        return temp;
    }

//    public boolean isContainList(int idUser) {
//        for (Reservation r : listRE) {
//            if (r.getId_user() == idUser) {
//                return true;
//            }
//        }
//        return false;
//    }
}