package com.android.bookingapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Department;

import java.util.ArrayList;

public class DepartAdapter extends RecyclerView.Adapter<DepartAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Department> mDeparts;

    public DepartAdapter(ArrayList<Department> mDeparts){
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
