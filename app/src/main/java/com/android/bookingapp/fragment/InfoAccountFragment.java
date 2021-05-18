package com.android.bookingapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class InfoAccountFragment extends Fragment {
    private User user_now;
    private int id;
    private Button confirm;
    private EditText name, mail, phone, job, address, password;
    private RadioButton male, female;
    private Spinner spinner_day, spinner_month, spinner_year;
    private String nDay,nMonth,nYear;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ArrayList<User> users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    public void addItemsOnSpinner() {

        ArrayList<Integer> day = new ArrayList<>();
        ArrayList<Integer> month = new ArrayList<>();
        ArrayList<Integer> year = new ArrayList<>();
        for(int i=1;i<32;i++)
        {
            if(i<13) month.add(i);
            day.add(i);
        }
        for(int i=1930;i<2021;i++)
        {
            year.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, day);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dataAdapter);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nDay= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(dataAdapter2);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nMonth= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter3 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, year);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(dataAdapter3);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nYear= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addItemsOnSpinner();
        users = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    users.add(user);
                }
                handle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public int posCurrent(int id) {
        if(users!=null){
            for (int i = 0; i < users.size(); i++) {
                if (id==users.get(i).getId()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void handle () {
        user_now = users.get(posCurrent(id));
        name.setText(user_now.getFullname());
        mail.setText(user_now.getEmail());
        phone.setText(user_now.getPhone());
        job.setText(user_now.getJob());
        address.setText(user_now.getAddress());
        password.setText(user_now.getPassword());
        if (user_now.isGender()) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        addItemsOnSpinner();
        int day = Integer.parseInt(user_now.getBirthday().getDay())-1;
        spinner_day.setSelection(day);
        int month = Integer.parseInt(user_now.getBirthday().getMonth())-1;
        spinner_month.setSelection(month);
        int year = Integer.parseInt(user_now.getBirthday().getYear()) - 1930;
        spinner_year.setSelection(year);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("password", password.getText().toString());
                hashMap.put("fullname", name.getText().toString());
                hashMap.put("phone", phone.getText().toString());
                hashMap.put("gender", male.isChecked());
                hashMap.put("job", job.getText().toString());
                hashMap.put("address", address.getText().toString());
                hashMap.put("birthday", new Date(nDay, nMonth, nYear));
                myRef.child("User" + user_now.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_infoAccountFragment_to_mainScreenFragment);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_info_account, container, false);
        name = view.findViewById(R.id.name);
        mail = view.findViewById(R.id.mail);
        phone = view.findViewById(R.id.phone);
        job = view.findViewById(R.id.job);
        address = view.findViewById(R.id.address);
        password = view.findViewById(R.id.password);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        spinner_day = view.findViewById(R.id.spinner_day);
        spinner_month = view.findViewById(R.id.spinner_month);
        spinner_year = view.findViewById(R.id.spinner_year);
        confirm = view.findViewById(R.id.confirm);
        return view;
    }
}