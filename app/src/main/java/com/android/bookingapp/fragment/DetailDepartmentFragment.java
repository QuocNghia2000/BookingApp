package com.android.bookingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Department;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.User;
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
    private User user;
    private DatabaseReference dbReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            department= (Department) getArguments().getSerializable("detailDepart");
            user= (User) getArguments().getSerializable("user");
            dbReference= FirebaseDatabase.getInstance().getReference("Doctor");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDoctors=new ArrayList<>();
        detailDepartAdapter = new DetailDepartAdapter(mDoctors,user,getContext());
        rvDetailDeparts.setLayoutManager(new GridLayoutManager(getContext(),1));
        rvDetailDeparts.setAdapter(detailDepartAdapter);
        getDoctor();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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