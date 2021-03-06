package com.android.bookingapp.fragment;

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

import com.android.bookingapp.R;
import com.android.bookingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class registerFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<User> users;
    private EditText fullname, email, phone, pass, confirm_pass;
    private Button login, contiue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private void handle() {
        contiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fullname.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()
                        && !pass.getText().toString().isEmpty() && !confirm_pass.getText().toString().isEmpty()) {
                    if (!extractNumber(fullname.getText().toString())) {
                        Toast.makeText(getActivity(), "L???i h??? v?? t??n", Toast.LENGTH_SHORT).show();
                    } else if (!isNumeric(phone.getText().toString())) {
                        Toast.makeText(getActivity(), "L???i s??? ??i???n tho???i", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!emailExist(email.getText().toString())) {
                            if (pass.getText().toString().trim().equals(confirm_pass.getText().toString().trim())) {
                                User user = new User(getArguments().getInt("id"), email.getText().toString(), pass.getText().toString(), fullname.getText().toString(), phone.getText().toString());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_moreInfoFragment, bundle);
                            } else {
                                Toast.makeText(getActivity(), "M???t kh???u v?? x??c nh???n ph???i tr??ng nhau", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getActivity(), "Email ???? t???n t???i", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Vui l??ng ??i???n ????? th??ng tin", Toast.LENGTH_SHORT).show();
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

    public boolean emailExist(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) return true;
        }
        return false;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        fullname = view.findViewById(R.id.edt_fullname);
        email = view.findViewById(R.id.edt_email);
        phone = view.findViewById(R.id.edt_phone);
        pass = view.findViewById(R.id.edt_pass);
        confirm_pass = view.findViewById(R.id.edt_confirmpass);
        login = view.findViewById(R.id.bt_login);
        contiue = view.findViewById(R.id.bt_continue);
        return view;
    }
}