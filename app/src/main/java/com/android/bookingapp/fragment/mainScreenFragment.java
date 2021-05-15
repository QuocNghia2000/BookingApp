package com.android.bookingapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Department;
import com.android.bookingapp.viewmodel.DepartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class mainScreenFragment extends Fragment {
    private ArrayList<Department> mDeparts;
    private RecyclerView rvDeparts;
    private DepartAdapter departAdapter;

    private DatabaseReference dbReference;
    private ImageView ivAccount,ivLogout;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDeparts=new ArrayList<>();
        dbReference= FirebaseDatabase.getInstance().getReference("Department");

        dialogBuilder=new AlertDialog.Builder(getContext());

        departAdapter = new DepartAdapter(mDeparts);
        rvDeparts.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvDeparts.setAdapter(departAdapter);
        getAllDepart();

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_infoAccountFragment, new Bundle());
            }
        });

    }
    public void showLogoutDialog(){
        dialogBuilder.setMessage("Bạn có muốn đăng xuất?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //XÓa quay lại main activity
                dialogInterface.dismiss();
                getActivity().onBackPressed();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public void getAllDepart()
    {

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDeparts.clear();
                for(DataSnapshot item:snapshot.getChildren())
                {
                    Department department=item.getValue(Department.class);
                    mDeparts.add(department);
                }
                departAdapter.notifyDataSetChanged();
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
        View view=inflater.inflate(R.layout.fragment_main_screen, container, false);

        rvDeparts=view.findViewById(R.id.rv_department);
        ivAccount=view.findViewById(R.id.iv_account);
        ivLogout=view.findViewById(R.id.iv_ogout);

        return view;
    }
}