package com.android.bookingapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.User;
import com.android.bookingapp.view.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginFragment extends Fragment {
    private EditText username,pass;
    private TextView resetpass;
    private Button bt_login,bt_register;
    private ArrayList<User> users;
    private Doctor doctor;
    FirebaseDatabase database;
    DatabaseReference myRef;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String ID_USER = "ID_USER";
    public static final String REMEMBER = "remember";
    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        users=new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        dialogBuilder=new AlertDialog.Builder(getContext());

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(isLogining())
        {
            String email=sharedpreferences.getString(USERNAME,"");
            String pass=sharedpreferences.getString(PASS,"");
            Intent intent =new Intent(getActivity(), MainActivity.class);
            intent.putExtra("id",sharedpreferences.getString(ID_USER,""));
            getActivity().finish();
            startActivity(intent);
        }
        myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
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
    private void handle() {

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())
                {
                    username.setError("Vui lòng điền đúng địa chỉ email");
                }
                else
                {
                    int index=posCurrent(username.getText().toString(),pass.getText().toString());
                    if(index!=-1)
                    {
                        saveData(username.getText().toString(),pass.getText().toString(),users.get(index).getId());
                        //Toast.makeText(getContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("user",users.get(index));
                        getActivity().finish();
                        startActivity(intent);
                    }
                    else
                    {
                        getDoctor(username.getText().toString(),pass.getText().toString());
                    }
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("id",users.size());
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment, bundle);
            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_ForgetPassFragment);
            }
        });

    }

    public int posCurrent(String email, String pass) {
        if(users!=null){
            for (int i = 0; i < users.size(); i++) {
                if (email.equals(users.get(i).getEmail()) && pass.equals(users.get(i).getPassword())) {
                    return i;
                }
            }
        }
        return -1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        username =view.findViewById(R.id.username);
        pass=view.findViewById(R.id.password);
        resetpass=view.findViewById(R.id.resetpass);
        bt_login=view.findViewById(R.id.bt_login);
        bt_register=view.findViewById(R.id.bt_register);
        return view;
    }

    public void getDoctor(String email,String pass){
        myRef = database.getReference("Doctor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    Doctor d = data.getValue(Doctor.class);
                    if (d.getEmail().equals(email) && d.getPassword().equals(pass)){
                        doctor = d;
                    }
                }
                if(doctor!=null){
                    saveData(doctor.getEmail(),doctor.getPassword(),doctor.getId());
                    Intent intent =new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("doctor",doctor);
                    getActivity().finish();
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Sai thông tin đăng nhập",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveData(String username, String Pass,int idUser) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putInt(ID_USER,idUser);
        editor.commit();
    }

    private boolean isLogining() {
    return sharedpreferences.contains(USERNAME);
    }


}