package com.android.bookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
    FirebaseDatabase database;
    DatabaseReference myRef;

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        users=new ArrayList<>();
        database = FirebaseDatabase.getInstance();
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
                int index=posCurrent(username.getText().toString(),pass.getText().toString());
                if(index!=-1)
                {
                    //Toast.makeText(getContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("user",users.get(index));
                    startActivity(intent);
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
                Bundle bundle=new Bundle();
                bundle.putInt("id",users.size());
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment, bundle);
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