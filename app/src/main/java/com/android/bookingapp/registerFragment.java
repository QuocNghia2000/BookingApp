package com.android.bookingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.bookingapp.model.User;


public class registerFragment extends Fragment {
    private EditText fullname,email,phone,pass,confirm_pass;
    private Button login,contiue;

    public registerFragment() {
        // Required empty public constructor
    }

    public static registerFragment newInstance(String param1, String param2) {
        registerFragment fragment = new registerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"dsds",Toast.LENGTH_SHORT).show();
                if(pass.getText().equals(confirm_pass.getText()))
                {
                    User user = new User(email.getText().toString(),pass.getText().toString(),fullname.getText().toString(),phone.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_confrimFragment, new Bundle());
                }
                else
                {
                    Toast.makeText(getActivity(),"Mật khẩu và xác nhận phải trùng nhau",Toast.LENGTH_SHORT).show();
                }
            }
        });

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