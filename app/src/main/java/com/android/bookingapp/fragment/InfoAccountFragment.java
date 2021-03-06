package com.android.bookingapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoAccountFragment extends Fragment {
    private User user_now, user_save;
    private int id;
    private Button confirm;
    private EditText name, mail, phone, job, address, password;
    private RadioButton male, female;
    private Spinner spinner_day, spinner_month, spinner_year;
    private String nDay,nMonth,nYear;
    private ImageView back;
    FirebaseDatabase database;
    DatabaseReference myRef;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    DatabaseOpenHelper db;

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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_infoAccountFragment_to_mainScreenFragment);
            }
        });
        db = new DatabaseOpenHelper(getContext());
        if(CheckInternet.checkInternet(getContext())) {
            user_save = getDetailLocalUser();
            user_now = user_save;
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("User");
            myRef.child("User" + user_save.getId()).setValue(new User(user_save.getId(),user_save.getEmail(),user_save.getPassword(),
                    user_save.getFullname(),user_save.getPhone(),user_save.getBirthday(),user_save.isGender(),
                    user_save.getJob(),user_save.getAddress())).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } else {
            user_now = getDetailLocalUser();
            handle();
        }
        dialogBuilder=new AlertDialog.Builder(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if(user.getId()==id)
                    {
                        user_now=user;
                        break;
                    }
                }
                handle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!extractNumber(name.getText().toString())) {
                    Toast.makeText(getActivity(), "L???i h??? v?? t??n", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(phone.getText().toString())) {
                    Toast.makeText(getActivity(), "L???i s??? ??i???n tho???i", Toast.LENGTH_SHORT).show();
                } else  if(!extractNumber(job.getText().toString())) {
                    Toast.makeText(getActivity(), "L???i ngh??? nghi???p", Toast.LENGTH_SHORT).show();
                } else {
                    showLogoutDialog();
                }
            }
        });
    }

    private void handle () {
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
        int day = Integer.parseInt(user_now.getBirthday().getDay())-1;
        spinner_day.setSelection(day);
        int month = Integer.parseInt(user_now.getBirthday().getMonth())-1;
        spinner_month.setSelection(month);
        int year = Integer.parseInt(user_now.getBirthday().getYear()) - 1930;
        spinner_year.setSelection(year);
    }

    public void showLogoutDialog(){
        dialogBuilder.setMessage("B???n c?? ch???c ch???n kh??ng?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(CheckInternet.checkInternet(getContext()))
                {
                    myRef.child("User" + user_now.getId()).setValue(new User(user_now.getId(),user_now.getEmail(),password.getText().toString(),
                            name.getText().toString(),phone.getText().toString(),new Date(nDay, nMonth, nYear),male.isChecked(),
                            job.getText().toString(),address.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.updateUserSqlite(user_now);
                            Toast.makeText(getContext(), "X??c nh???n th??nh c??ng", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"L???i",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogInterface.dismiss();
                    Navigation.findNavController(getView()).navigate(R.id.action_infoAccountFragment_to_mainScreenFragment, new Bundle());
                }
                else
                {
                    user_now = new User(user_now.getId(),user_now.getEmail(),password.getText().toString(),
                            name.getText().toString(),phone.getText().toString(),new Date(nDay, nMonth, nYear),male.isChecked(),
                            job.getText().toString(),address.getText().toString());
                    db.updateUserSqlite(user_now);
                    Toast.makeText(getContext(),"X??c nh???n th??nh c??ng",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    Navigation.findNavController(getView()).navigate(R.id.action_infoAccountFragment_to_mainScreenFragment, new Bundle());
                }
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public boolean extractNumber( String str) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                break;
            }
        }
        if(sb.toString().isEmpty())
            return true;
        else
            return false;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public User getDetailLocalUser() {
        User user = new User();
        Cursor cursor=db.getUserFromSqlite();
        while (cursor.moveToNext())
        {
            int idUser=cursor.getInt(9);
            String email = cursor.getString(1);
            String password=cursor.getString(2);
            String fullname=cursor.getString(3);
            String phone=cursor.getString(4);
            String birth=cursor.getString(8);
            String[] date = birth.split("/");
            Date birthday= new Date(date[0],date[1],date[2]);
            int gender = Integer.valueOf(cursor.getString(5));
            String job=cursor.getString(6);
            String address=cursor.getString(7);

            user = new User(idUser,email, password,fullname,phone,birthday,gender==1?true:false,job,address);
        }
        return user;
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
        back = view.findViewById(R.id.img_back);
        db=new DatabaseOpenHelper(getContext());
        return view;
    }
}
