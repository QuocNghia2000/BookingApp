package com.android.bookingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class registerFragment extends Fragment {
    private EditText fullname,email,phone,pass,confirm_pass;
    private Button login,contiue;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<User> users;



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
        contiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fullname.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!phone.getText().toString().isEmpty()
                        &&!pass.getText().toString().isEmpty()&&!confirm_pass.getText().toString().isEmpty())
                {
                    if(!emailExist(email.getText().toString()))
                    {
                        Toast.makeText(getActivity(),"hehehe",Toast.LENGTH_SHORT).show();
                        if(pass.getText().toString().trim().equals(confirm_pass.getText().toString().trim()))
                        {
                            User user = new User(getArguments().getInt("id"),email.getText().toString(),pass.getText().toString(),fullname.getText().toString(),phone.getText().toString());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user",user);
                            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_moreInfoFragment, bundle);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Mật khẩu và xác nhận phải trùng nhau",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else Toast.makeText(getActivity(),"Email đã tồn tại",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"Vui lòng điền đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

    }

    public boolean emailExist(String email)
    {
        for(User user:users)
        {
            if(user.getEmail().equals(email)) return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        fullname=view.findViewById(R.id.edt_fullname);
        email=view.findViewById(R.id.edt_email);
        phone=view.findViewById(R.id.edt_phone);
        pass=view.findViewById(R.id.edt_pass);
        confirm_pass=view.findViewById(R.id.edt_confirmpass);
        login=view.findViewById(R.id.bt_login);
        contiue=view.findViewById(R.id.bt_continue);
        return view;
    }
}