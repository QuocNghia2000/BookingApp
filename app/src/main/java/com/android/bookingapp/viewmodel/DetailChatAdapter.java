package com.android.bookingapp.viewmodel;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.NotificationApplication;
import com.android.bookingapp.R;
import com.android.bookingapp.fragment.DetailMessFragment;
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
import java.util.Random;

public class DetailChatAdapter extends RecyclerView.Adapter<DetailChatAdapter.MyViewHolder> implements Filterable {

    private Doctor doctor;
    private int id_user;
    private DatabaseReference myRef;
    private List<Message> listMess;
    private List<Message> messageList;
    private List<Message> listMessAll;
    private boolean isUser;
    private Context context;
    private DetailMessFragment detailMessFragment;
    private User user;
    String content="";

    public DetailChatAdapter(Doctor doctor,int id_user,boolean isUser,Context context, DetailMessFragment detailMessFragment){
        this.doctor = doctor;
        this.id_user = id_user;
        this.isUser = isUser;
        this.listMess = new ArrayList<>();
        this.listMessAll = listMess;
        messageList=new ArrayList<>();
        this.context=context;
        this.detailMessFragment=detailMessFragment;
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
        List<Message> messages=new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                if(listMess!=null)
                    messages.addAll(listMess);
                listMess.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    Message m = data.getValue(Message.class);
                    if(m.getId_Doctor() == doctor.getId() && m.getId_User() == id_user){
                        listMess.add(m);
                    }
                }
                if(messages.size()!=0)
                {
                    for(int i=messages.size();i<listMess.size();i++)
                    {
                        messageList.add(listMess.get(i));
                        content=listMess.get(i).getContent();
                    }
                    myRef.child("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot data: snapshot.getChildren())
                            {
                                User user1 = data.getValue(User.class);
                                if(user1.getId()==id_user)
                                {
                                    user=new User(user1.getId(),user1.getEmail(),user1.getPassword(),user1.getFullname(),user1.getPhone());
                                }
                            }
                            sendNotification();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                detailMessFragment.scrollView();
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendNotification()
    {
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round);
        Notification notification=new NotificationCompat.Builder(context, NotificationApplication.CHANNEL_ID)
                .setContentTitle(user.getFullname())
                .setContentText(content)
                .setSmallIcon(R.drawable.messenger).setLargeIcon(bitmap).build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null)
        {
            Random random=new Random();

            notificationManager.notify(random.nextInt(),notification);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty())
                {
                    listMess = listMessAll;
                }
                else
                {
                    List<Message> list = new ArrayList<>();
                    for(Message m: listMessAll)
                    {
                        if(m.getContent().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            list.add(m);
                        }
                    }
                    listMess = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listMess;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listMess = (List<Message>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
