package com.android.bookingapp.viewmodel;

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
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.User;
import java.util.ArrayList;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> implements Filterable {
    private final ArrayList<Message> listMess;
    private ArrayList<Doctor> listContact;
    private ArrayList<User> listContactDoc;
    private ArrayList<Doctor> listContactAll;
    private ArrayList<User> listContactDocAll;
    private int doctorID, id_user = -1, countMess;

    public ListChatAdapter(int id_user, ArrayList<Doctor> listContact, ArrayList<Message> listMess) {
        this.id_user = id_user;
        this.listMess = listMess;
        this.listContactAll = listContact;
        this.listContact = listContact;
    }

    public ListChatAdapter(int doctorID, ArrayList<User> listContactDoc, ArrayList<Message> listMess, String doctor) {
        this.doctorID = doctorID;
        this.listMess = listMess;
        this.listContactDocAll = listContactDoc;
        this.listContactDoc = listContactDoc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_list_chat, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.MyViewHolder holder, int position) {
        if (this.id_user != -1) {
            holder.name.setText(listContact.get(position).getFullname());
            for (Message mess : listMess) {
                if (mess.getId_Doctor() == listContact.get(position).getId()) {
                    if (mess.isFromPerson() == true ) {
                        holder.content.setText("Bạn: " + mess.getContent());
                    } else {
                        holder.content.setText(mess.getContent());
                    }
                }
            }
        } else {
            holder.name.setText(listContactDoc.get(position).getFullname());
            for (Message mess : listMess) {
                if (mess.getId_User() == listContactDoc.get(position).getId()) {
                    if (mess.isFromPerson() == false) {
                        holder.content.setText("Bạn: " + mess.getContent());
                    } else {
                        holder.content.setText(mess.getContent());
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.id_user != -1) {
            return listContact.size();
        } else return listContactDoc.size();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (id_user != -1) {
                    if (strSearch.isEmpty()) {
                        listContact = listContactAll;
                    } else {
                        ArrayList<Doctor> list = new ArrayList<>();
                        for (Doctor d : listContactAll) {
                            if (d.getFullname().toLowerCase().contains(strSearch.toLowerCase())) {
                                list.add(d);
                            }
                        }
                        listContact = list;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listContact;
                    return filterResults;
                } else {
                    if (strSearch.isEmpty()) {
                        listContactDoc = listContactDocAll;
                    } else {
                        ArrayList<User> list = new ArrayList<>();
                        for (User u : listContactDocAll) {
                            if (u.getFullname().toLowerCase().contains(strSearch.toLowerCase())) {
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
                if (id_user != -1) {
                    listContact = (ArrayList<Doctor>) results.values;
                    notifyDataSetChanged();
                } else {
                    listContactDoc = (ArrayList<User>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_listchat);
            content = itemView.findViewById(R.id.tv_content_listchat);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (id_user != -1) {
                        bundle.putInt("id_user", id_user);
                        bundle.putInt("doctorID", listContact.get(getAdapterPosition()).getId());
                        bundle.putBoolean("isUser", true);
                    } else {
                        bundle.putInt("doctorID", doctorID);
                        bundle.putInt("id_user", listContactDoc.get(getAdapterPosition()).getId());
                        bundle.putBoolean("isUser", false);
                    }
                    Navigation.findNavController(v).navigate(R.id.action_listChatFragment_to_detailMessFragment, bundle);
                }
            });
        }
    }
}
