package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private List<String> listTime;
    private int SelectedTime;
    private Context context;
    private List<String> listReser;

    public BookAdapter(List<String> listTime,Context context,List<String> listReser){
        this.listTime = listTime;
        this.context = context;
        this.SelectedTime = -1;
        this.listReser = listReser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_book, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTime.setText(listTime.get(position));
        if(SelectedTime == position)
        {
            holder.mcvBook.setStrokeColor(context.getColor(R.color.purple_700));
            holder.tvTime.setTextColor(context.getColor(R.color.white));
            holder.mcvBook.setCardBackgroundColor(context.getColor(R.color.purple_700));
        }
        else if (!checkReser(listTime.get(position))){
            holder.mcvBook.setStrokeColor(context.getColor(R.color.gray));
            holder.tvTime.setTextColor(context.getColor(R.color.white));
            holder.mcvBook.setCardBackgroundColor(context.getColor(R.color.gray));
        }else {
                holder.mcvBook.setStrokeColor(context.getColor(R.color.black));
                holder.tvTime.setTextColor(context.getColor(R.color.black));
                holder.mcvBook.setCardBackgroundColor(context.getColor(R.color.white));
            }
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime;
        private MaterialCardView mcvBook;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_book);
            mcvBook = itemView.findViewById(R.id.mcv_book);

            mcvBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkReser(listTime.get(getAdapterPosition())))
                    {
                        SelectedTime = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public boolean checkReser(String time){
        if(listReser.contains(time)) return false;
        else return true;
    }
}
