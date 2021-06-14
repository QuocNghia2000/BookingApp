package com.android.bookingapp.fragment;

import android.os.Bundle;
import android.util.Log;
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

import com.android.bookingapp.R;
import com.android.bookingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConfirmFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText edt_code;
    String code;
    private User user;
    private Button bt_done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
            code = getArguments().getString("code");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retype_code = edt_code.getText().toString();
                if (retype_code.equals(code)) {
                    myRef.child("User" + user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.action_confrimFragment_to_loginFragment, new Bundle());
                            } else {
                                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show();
                    Log.d("code", code);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        bt_done = view.findViewById(R.id.bt_done);
        edt_code = view.findViewById(R.id.edt_code);
        return view;
    }

}