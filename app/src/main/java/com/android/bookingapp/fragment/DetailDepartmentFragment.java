package com.android.bookingapp.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Department;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.viewmodel.DetailDepartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DetailDepartmentFragment extends Fragment {
    private Department department;
    private ImageView back;
    private TextView tvNameDepart;
    private RecyclerView rvDetailDeparts;
    private ArrayList<Doctor> mDoctors;
    private DetailDepartAdapter detailDepartAdapter;
    private int id_user;
    DatabaseOpenHelper db;
    private DatabaseReference dbReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            department= (Department) getArguments().getSerializable("detailDepart");
            id_user= getArguments().getInt("id_user");
            dbReference= FirebaseDatabase.getInstance().getReference("Doctor");
        }
        db=new DatabaseOpenHelper(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDoctors=new ArrayList<>();
        detailDepartAdapter = new DetailDepartAdapter(mDoctors,id_user);
        rvDetailDeparts.setLayoutManager(new GridLayoutManager(getContext(),1));
        rvDetailDeparts.setAdapter(detailDepartAdapter);
        if(CheckInternet.checkInternet(getContext())){
            getDoctor();
        }
        else getDoctorOff();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
    }

    public void getDoctorOff(){
        ArrayList<Doctor> docTemp = getDetailLocalDoctor();
        for(Doctor doctor : docTemp){
            if(doctor.getDepartment()==department.getId()-1)
            {
                mDoctors.add(doctor);
                tvNameDepart.setText(department.getName());
            }
        }
        detailDepartAdapter.notifyDataSetChanged();
    }

    public ArrayList<Doctor> getDetailLocalDoctor(){
        ArrayList<Doctor> doctors=new ArrayList<>();
        Cursor cursor=db.getDoctorFromSqlite();
        while (cursor.moveToNext())
        {
            int id=cursor.getInt(0);
            int id_Depart=cursor.getInt(1);
            String email=cursor.getString(2);
            String password=cursor.getString(3);
            String fullname=cursor.getString(4);
            String phone=cursor.getString(5);
            String achivement=cursor.getString(6);
            String address=cursor.getString(7);

            Doctor doctor = new Doctor(id, email,password, fullname,phone,id_Depart,achivement,address);
            doctors.add(doctor);
        }
        return doctors;
    }

    public void getDoctor()
    {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDoctors.clear();
                for(DataSnapshot item:snapshot.getChildren())
                {
                    Doctor doctor=item.getValue(Doctor.class);
                    if(doctor.getDepartment()==department.getId())
                    {
                        mDoctors.add(doctor);
                        tvNameDepart.setText(department.getName());
                    }
                }
                detailDepartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail_department, container, false);
        rvDetailDeparts=view.findViewById(R.id.rv_detailDepart);
        tvNameDepart=view.findViewById(R.id.tv_nameDepart);
        back=view.findViewById(R.id.img_back);
        return view;
    }
}