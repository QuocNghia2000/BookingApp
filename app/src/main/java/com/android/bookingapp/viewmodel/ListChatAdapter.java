package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
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

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Doctor> listContact;
    private ArrayList<User> listContactDoc;
    private ArrayList<Message> listMess;
    private ArrayList<Doctor> listContactAll;
    private ArrayList<User> listContactDocAll;
    private Doctor doctor;
    private Context context;
    private int id_user=-1;

    public ListChatAdapter(int id_user,Context context,ArrayList<Doctor> listContact,ArrayList<Message> listMess){
        this.id_user = id_user;
        this.listMess = listMess;
        this.listContactAll = listContact;
        this.context = context;
        this.listContact = listContact;
    }

    public ListChatAdapter(Doctor doctor,Context context,ArrayList<User> listContactDoc,ArrayList<Message> listMess){
        this.doctor = doctor;
        this.listMess = listMess;
        this.listContactDocAll = listContactDoc;
        this.context = context;
        this.listContactDoc = listContactDoc;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_list_chat, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.MyViewHolder holder, int position) {
        if(CheckInternet.checkInternet(context)){
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
        else
        {
            holder.name.setText(listContact.get(position).getFullname());
            for(Message mess: listMess){
                if (mess.getId_Doctor() == listContact.get(position).getId()-1 ){
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



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(id_user!=-1)
                {
                    if (strSearch.isEmpty())
                    {
                        listContact = listContactAll;
                    }
                    else
                    {
                        ArrayList<Doctor> list = new ArrayList<>();
                        for(Doctor d: listContactAll)
                        {
                            if(d.getFullname().toLowerCase().contains(strSearch.toLowerCase()))
                            {
                                list.add(d);
                            }
                        }
                        listContact = list;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listContact;
                    return filterResults;
                }
                else
                {
                    if (strSearch.isEmpty())
                    {
                        listContactDoc = listContactDocAll;
                    }
                    else
                    {
                        ArrayList<User> list = new ArrayList<>();
                        for(User u: listContactDocAll)
                        {
                            if(u.getFullname().toLowerCase().contains(strSearch.toLowerCase()))
                            {
                                list.add(u);
                            }
                        }
                        listContactDoc = list;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listContactDoc;
                    return filterResults;
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(id_user!=-1){
                    listContact = (ArrayList<Doctor>) results.values;
                    notifyDataSetChanged();
                }
                else
                {
                    listContactDoc = (ArrayList<User>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
