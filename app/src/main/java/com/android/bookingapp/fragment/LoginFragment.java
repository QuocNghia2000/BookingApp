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
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
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
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String FULLNAME = "nameDoctor";
    public static final String VALIDATION = "validation";
    public static final String ID_CURRENT = "ID_USER";
    FirebaseDatabase database;
    DatabaseReference myRef;
    AlertDialog.Builder dialogBuilder;
    SharedPreferences sharedpreferences;
    DatabaseOpenHelper db;
    private EditText username, pass;
    private TextView resetpass;
    private Button bt_login, bt_register;
    private ArrayList<User> users;
    private Doctor doctor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        users = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        dialogBuilder = new AlertDialog.Builder(getContext());

        db = new DatabaseOpenHelper(getContext());
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

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
                    username.setError("Vui l??ng ??i???n ????ng ?????a ch??? email");
                } else {
                    if (CheckInternet.checkInternet(getContext())) {
                        int index = posCurrent(username.getText().toString(), pass.getText().toString(), users);
                        if (index != -1) {
                            saveData(username.getText().toString(),  users.get(index).getFullname(), users.get(index).getId(), "user");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("id", users.get(index).getId());
                            getActivity().finish();
                            startActivity(intent);
                        } else {
                            getDoctor(username.getText().toString(), pass.getText().toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "Don't have Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", users.size());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username = view.findViewById(R.id.username);
        pass = view.findViewById(R.id.password);
        resetpass = view.findViewById(R.id.resetpass);
        bt_login = view.findViewById(R.id.bt_login);
        bt_register = view.findViewById(R.id.bt_register);
        username.setFocusable(true);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (isLogining() && validation().equals("user")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("id", sharedpreferences.getInt(ID_CURRENT, -1));
            intent.putExtra("nameUser", sharedpreferences.getString(FULLNAME, ""));
            getActivity().finish();
            startActivity(intent);
        }
        if (isLogining() && validation().equals("doctor")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("doctorID", sharedpreferences.getInt(ID_CURRENT, -1));
            intent.putExtra("nameDoctor", sharedpreferences.getString(FULLNAME, ""));
            getActivity().finish();
            startActivity(intent);
        }
        return view;
    }

    public void getDoctor(String email, String pass) {
        myRef = database.getReference("Doctor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Doctor d = data.getValue(Doctor.class);
                    if (d.getEmail().equals(email) && d.getPassword().equals(pass)) {
                        doctor = d;
                    }
                }
                if (doctor != null) {
                    saveData(username.getText().toString(), doctor.getFullname(), doctor.getId(), "doctor");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("doctorID", doctor.getId());
                    intent.putExtra("nameDoctor", doctor.getFullname());
                    getActivity().finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Sai th??ng tin ????ng nh???p", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveData(String username, String fullname, int idUser, String validation) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(FULLNAME, fullname);
        editor.putString(VALIDATION, validation);
        editor.putInt(ID_CURRENT, idUser);
        editor.commit();
    }

    private String validation() {
        return sharedpreferences.getString(VALIDATION, "");

    }

    private boolean isLogining() {
        return sharedpreferences.contains(USERNAME);
    }

    public int posCurrent(String email, String pass, ArrayList<User> users) {
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                if (email.equals(users.get(i).getEmail()) && pass.equals(users.get(i).getPassword())) {
                    return i;
                }
            }
        }
        return -1;
    }

}