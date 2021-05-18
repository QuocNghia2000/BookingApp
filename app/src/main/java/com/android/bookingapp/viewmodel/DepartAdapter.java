package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Department;
import com.android.bookingapp.model.User;

import java.util.ArrayList;

public class DepartAdapter extends RecyclerView.Adapter<DepartAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Department> mDeparts;
    private User user;

    public DepartAdapter(ArrayList<Department> mDeparts,User user){
        this.user = user;
        this.mDeparts=mDeparts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_main, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.btDepartment.setText(mDeparts.get(position).getName());
        holder.btDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("detailDepart",mDeparts.get(position));
                bundle.putSerializable("user",user);
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_detailDepartmentFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeparts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private Button btDepartment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btDepartment=itemView.findViewById(R.id.department);

        }
    }

}
