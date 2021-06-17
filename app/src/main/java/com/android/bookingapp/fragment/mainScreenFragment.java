package com.android.bookingapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.databinding.DialogInfordoctorBinding;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Department;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.User;
import com.android.bookingapp.view.LoginActivity;
import com.android.bookingapp.viewmodel.DepartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class mainScreenFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs";
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    SharedPreferences sharedpreferences;
    DatabaseOpenHelper db;
    ArrayList<Message> messages;
    ArrayList<User> users;
    ArrayList<User> userAll;
    Dialog dialogSearch;
    private ArrayList<Department> mDeparts;
    private RecyclerView rvDeparts;
    private DepartAdapter departAdapter;
    private DatabaseReference dbReference;
    private ImageView ivAccount, ivLogout, imvChat;
    private int idUser = -1;
    private TextView tvSearch;
    private List<String> list;
    private ArrayList<Doctor> listDoc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int doctorID = getActivity().getIntent().getIntExtra("doctorID",-1);
        if (doctorID != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("doctorID", doctorID);
            bundle.putString("nameDoctor",getActivity().getIntent().getStringExtra("nameDoctor"));
            Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_docMainFragment, bundle);
        } else {
            idUser = getActivity().getIntent().getIntExtra("id", -1);
        }

        mDeparts = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference();
        db = new DatabaseOpenHelper(getContext());

        if (!CheckInternet.checkInternet(getContext())) {
            listDoc = getDetailLocalDoctor();
            mDeparts = getDetailLocalDepartment();
            list = new ArrayList<>();
            for (Doctor d : listDoc) {
                list.add(d.getFullname());
            }
        } else {
            getUserLogin();
            getAllDepart();
            getAllMessage();
            getAllDoctor();
        }
        dialogBuilder = new AlertDialog.Builder(getContext());
        departAdapter = new DepartAdapter(mDeparts, idUser);
        rvDeparts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvDeparts.setAdapter(departAdapter);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckInternet.checkInternet(getContext())) {
                    Toast.makeText(getContext(), "Offline", Toast.LENGTH_SHORT).show();
                } else {
                    showLogoutDialog();
                }
            }
        });

        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", idUser);
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_infoAccountFragment, bundle);
            }
        });

        imvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id_user", idUser);
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_listChatFragment, bundle);
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSearch = new Dialog(getContext());
                dialogSearch.setContentView(R.layout.dialog_searchable_spinner);
                dialogSearch.getWindow().setLayout(950, 1200);
                dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogSearch.show();

                EditText editText = dialogSearch.findViewById(R.id.edt_search_mainScreen);
                ListView listView = dialogSearch.findViewById(R.id.lv_search_mainScreen);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                        Dialog newDialog = new Dialog(getContext());
                        DialogInfordoctorBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_infordoctor, null, false);
                        newDialog.setContentView(binding.getRoot());
                        binding.setDoctor(listDoc.get(position));
                        for (Department d : mDeparts) {
                            if (listDoc.get(position).getDepartment() == d.getId() - 1) {
                                binding.setDepartment(d);
                            }
                        }


                        ImageView imvChat = newDialog.findViewById(R.id.imv_chat_dialog);
                        ImageView imvBook = newDialog.findViewById(R.id.imv_book_dialog);

                        newDialog.getWindow().setLayout(1440, 1200);
                        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        newDialog.show();

                        imvChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("id_user", idUser);
                                bundle.putSerializable("doctorID", listDoc.get(position).getId());
                                bundle.putSerializable("nameDisplay", listDoc.get(position).getFullname());
                                bundle.putBoolean("isUser", true);
                                Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_detailMessFragment, bundle);
                                newDialog.dismiss();
                                dialogSearch.dismiss();
                            }
                        });

                        imvBook.setOnClickListener((new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("id_user", idUser);
                                bundle.putSerializable("doctor", listDoc.get(position));
                                Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_bookFragment, bundle);
                                newDialog.dismiss();
                                dialogSearch.dismiss();
                            }
                        }));
                    }
                });
            }
        });
    }

    private void getAllMessage() {
        messages = new ArrayList<>();
        dbReference.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message.getId_User() == idUser) messages.add(message);
                }
                try {
                    db.createMessageTable();
                    Cursor cursor = db.getMessageFromSqlite();
                    if (cursor.getCount() == 0) db.saveMessageTableToDB(messages);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getUserLogin() {
        users = new ArrayList<>();
        userAll = new ArrayList<>();
        dbReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                userAll.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    userAll.add(user);
                    if (user.getId() == idUser) {
                        users.add(user);
                    }
                }
                try {
                    db.createUserTable();
                    Cursor cursor = db.getUserFromSqlite();
                    if (getActivity().getIntent().getSerializableExtra("doctor") != null) {
                        if (cursor.getCount() == 0) {
                            db.saveUserTableToDB(userAll);
                        }
                    } else if (cursor.getCount() == 0) db.saveUserTableToDB(users);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getAllDepart() {
        dbReference.child("Department").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDeparts.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Department department = item.getValue(Department.class);
                    mDeparts.add(department);
                }
                try {
                    db.createDepartmentTable();
                    Cursor cursor = db.getDepartmentFromSqlite();
                    if (cursor.getCount() == 0) db.saveDepartmentTableToDB(mDeparts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                departAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void showLogoutDialog() {
        dialogBuilder.setMessage("Bạn có muốn đăng xuất?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.deleteInformation();
                clearData();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        rvDeparts = view.findViewById(R.id.rv_department);
        ivAccount = view.findViewById(R.id.iv_account);
        ivLogout = view.findViewById(R.id.iv_ogout);
        imvChat = view.findViewById(R.id.imv_listchat_main);
        tvSearch = view.findViewById(R.id.tv_search_mainScreen);
        return view;
    }

    public void getAllDoctor() {
        listDoc = new ArrayList<>();
        list = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference("Doctor");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDoc.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Doctor d = item.getValue(Doctor.class);
                    listDoc.add(d);
                    list.add(d.getFullname());
                }
                try {
                    db.createDoctorTable();
                    Cursor cursor = db.getDoctorFromSqlite();
                    if (cursor.getCount() == 0) db.saveDoctorTableToDB(listDoc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public ArrayList<Doctor> getDetailLocalDoctor() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        Cursor cursor = db.getDoctorFromSqlite();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_Depart = cursor.getInt(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String fullname = cursor.getString(4);
            String phone = cursor.getString(5);
            String achivement = cursor.getString(6);
            String address = cursor.getString(7);
            Doctor doctor = new Doctor(id, email, password, fullname, phone, id_Depart, achivement, address);
            doctors.add(doctor);
        }
        return doctors;
    }

    public ArrayList<Department> getDetailLocalDepartment() {
        ArrayList<Department> departments = new ArrayList<>();
        Cursor cursor = db.getDepartmentFromSqlite();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Department department = new Department(id, name);
            departments.add(department);
        }
        return departments;
    }
}