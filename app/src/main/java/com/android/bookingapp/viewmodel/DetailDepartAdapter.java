package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.databinding.ItemRvDepartmentBinding;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.User;

import java.util.ArrayList;

public class DetailDepartAdapter extends RecyclerView.Adapter<DetailDepartAdapter.MyViewHolder> {
    private Context context;
    private User user;
    private ArrayList<Doctor> mDoctors;

    public DetailDepartAdapter(ArrayList<Doctor> mDoctors,User user,Context context){
        this.user = user;
        this.mDoctors=mDoctors;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        ItemRvDepartmentBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_rv_department,parent,false);
        view=binding.getRoot();
        return new  MyViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setDoctor(mDoctors.get(position));
        holder.doctor=mDoctors.get(position);
        holder.imvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctor",mDoctors.get(position));
                bundle.putSerializable("user",user);
                bundle.putBoolean("isUser",true);
                Navigation.findNavController(v).navigate(R.id.action_detailDepartmentFragment_to_detailMessFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDoctors.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        View view;
        private Doctor doctor;
        private ItemRvDepartmentBinding binding;
        private ImageView imvChat;

        public MyViewHolder(@NonNull View itemView,ItemRvDepartmentBinding binding) {
            super(itemView);
            view=itemView;
            this.binding=binding;
            imvChat = itemView.findViewById(R.id.imv_chat_detailDepart);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("doctor", doctor);
                    bundle.putSerializable("user",user);
                    Navigation.findNavController(v).navigate(R.id.action_detailDepartmentFragment_to_bookFragment, bundle);
                }
            });

        }
    }

}
