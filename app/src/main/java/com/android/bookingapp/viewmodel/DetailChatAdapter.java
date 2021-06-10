package com.android.bookingapp.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Message;

import java.util.ArrayList;

public class DetailChatAdapter extends RecyclerView.Adapter<DetailChatAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Message> listMess;
    private ArrayList<Message> listMessAll;
    private boolean isUser;

    public DetailChatAdapter(ArrayList<Message> messages,boolean isUser){
        this.listMess = messages;
        this.listMessAll = messages;
        this.isUser = isUser;
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
                if(listMess.get(position).getCheckLocalMes()==1) holder.ivReload.setVisibility(View.VISIBLE);
                else holder.ivReload.setVisibility(View.INVISIBLE);
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
        private ImageView ivReload;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSend = itemView.findViewById(R.id.sms_send);
            tvReceive = itemView.findViewById(R.id.sms_receive);
            ivReload=itemView.findViewById(R.id.iv_reload);
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
                    ArrayList<Message> list = new ArrayList<>();
                    for(Message m: listMessAll)
                    {
                        if(m.getContent().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            list.add(m);
                        }
                    }
                    listMess =  list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listMess;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listMess = (ArrayList<Message>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
