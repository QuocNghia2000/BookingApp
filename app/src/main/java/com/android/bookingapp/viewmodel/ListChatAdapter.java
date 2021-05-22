package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private List<Doctor> listContact;
    private List<User> listContactDoc;
    private List<Message> listMess;
    private DatabaseReference dbRef;
    //private  User user;
    private Doctor doctor;
    private Context context;
    private int id_user=-1;

    public ListChatAdapter(int id_user,Context context){
        this.id_user = id_user;
        this.listMess = new ArrayList<>();
        this.listContact = new ArrayList<>();
        this.context = context;
        getListMessUser();
    }

    public ListChatAdapter(Doctor doctor,Context context){
        this.doctor = doctor;
        this.listMess = new ArrayList<>();
        this.listContactDoc = new ArrayList<>();
        this.context = context;
        getListMessDoctor();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_list_chat, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.MyViewHolder holder, int position) {
        if(this.id_user!=-1){
            holder.name.setText(listContact.get(position).getFullname());
            for(Message mess: listMess){
                if (mess.getId_Doctor() == listContact.get(position).getId() ){
                    holder.content.setText(mess.getContent());
                }
            }
        }
        else {
            holder.name.setText(listContactDoc.get(position).getFullname());
            for(Message mess: listMess){
                if (mess.getId_User() == listContactDoc.get(position).getId() ){
                    holder.content.setText(mess.getContent());
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if(this.id_user!=-1){
            return listContact.size();
        }
        else return listContactDoc.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_listchat);
            content = itemView.findViewById(R.id.tv_content_listchat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if(id_user!=-1)
                    {
                        bundle.putInt("id_user",id_user);
                        bundle.putSerializable("doctor",listContact.get(getAdapterPosition()));
                        bundle.putBoolean("isUser",true);
                    }
                    else
                    {
                        bundle.putSerializable("doctor",doctor);
                        bundle.putInt("id_user",listContactDoc.get(getAdapterPosition()).getId());
                        bundle.putBoolean("isUser",false);
                    }
                    Navigation.findNavController(v).navigate(R.id.action_listChatFragment_to_detailMessFragment,bundle);
                }
            });
            //notifyDataSetChanged();
        }
    }

    public void getListMessUser(){
//        List<Integer> idDoctor = new ArrayList<>();
//        List<Integer> idDoctorFirst = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Message");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> idDoctor = new ArrayList<>();
                List<Integer> idDoctorFirst = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren())
                {
                    Message mess = data.getValue(Message.class);
                    if(mess.getId_User() == id_user)
                    {
                        idDoctorFirst.add(mess.getId_Doctor());
                        int t = getPositionList(listMess,mess.getId_Doctor());
                        if(t > -1)
                        {
                            listMess.remove(t);
                            listMess.add(t,mess);
                        }
                        else
                        {
                            listMess.add(mess);
                        }
                    }
                }
                listContact = new ArrayList<>();
                for (int i=idDoctorFirst.size()-1;i>=0;i--){
                    if(!isConstrainList(idDoctor,idDoctorFirst.get(i))){
                        idDoctor.add(idDoctorFirst.get(i));
                    }
                }

                dbRef = FirebaseDatabase.getInstance().getReference("Doctor");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren())
                        {
                            Doctor d = data.getValue(Doctor.class);
                            if(isConstrainList(idDoctor,d.getId()))
                            {
                                listContact.add(d);
                            }
                        }
                        List<Doctor> doctemp = new ArrayList<>();
                        for(int i=0;i<idDoctor.size();i++){
                            for(Doctor d : listContact){
                                if (idDoctor.get(i) == d.getId())
                                {
                                    doctemp.add(d);
                                    break;
                                }
                            }
                        }
                        listContact = new ArrayList<>(doctemp);
                        //DaoList();
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

    public void getListMessDoctor(){
        dbRef = FirebaseDatabase.getInstance().getReference("Message");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> idUser = new ArrayList<>();
                List<Integer> idUserFirst = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren())
                {
                    Message mess = data.getValue(Message.class);
                    if(mess.getId_Doctor() == doctor.getId())
                    {
                        idUserFirst.add(mess.getId_User());
                        int t = getPositionList(listMess,mess.getId_User());
                        if(t > -1)
                        {
                            listMess.remove(t);
                            listMess.add(t,mess);
                        }
                        else
                        {
                            listMess.add(mess);
                        }
                    }
                }

                for (int i=idUserFirst.size()-1;i>=0;i--){
                    if(!isConstrainList(idUser,idUserFirst.get(i))){
                        idUser.add(idUserFirst.get(i));
                    }
                }
                listContactDoc = new ArrayList<>();
                dbRef = FirebaseDatabase.getInstance().getReference("User");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren())
                        {
                            User u = data.getValue(User.class);
                            if(isConstrainList(idUser,u.getId()))
                            {
                                listContactDoc.add(u);
                            }
                        }

                        List<User> usertemp = new ArrayList<>();
                        for(int i=0;i<idUser.size();i++){
                            for(User u : listContactDoc){
                                if (idUser.get(i) == u.getId())
                                {
                                    usertemp.add(u);
                                    break;
                                }
                            }
                        }
                        listContactDoc = new ArrayList<>(usertemp);
                        //DaoList();
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

    public boolean isConstrainList(List<Integer> listID,int idDoc){
        for(int id : listID){
            if (id == idDoc) return true;
        }
        return false;
    }

    public int getPositionList(List<Message> listM,int idDoc){
        int i=0;
        for(Message m : listM ){
            if(m.getId_Doctor() == idDoc) return i;
            i++;
        }
        return -1;
    }

    public void DaoList(){
        List<Doctor> doc = new ArrayList<>(listContact);
        listContact.clear();
        for (int i=doc.size()-1;i>=0;i--){
            listContact.add(doc.get(i));
        }
    }
}
