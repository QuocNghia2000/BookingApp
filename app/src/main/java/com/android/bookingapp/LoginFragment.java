package com.android.bookingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bookingapp.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {
    private EditText username,pass;
    private TextView resetpass;
    private Button bt_login,bt_register;
    private ArrayList<User> users;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users=new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
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
                if(posCurrent(username.getText().toString(),pass.getText().toString())!=-1)
                {
                    Toast.makeText(getContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Sai thông tin đăng nhập",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment, new Bundle());
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
}