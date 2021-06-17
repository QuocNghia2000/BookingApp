package com.android.bookingapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.model.CheckInternet;
import com.android.bookingapp.model.DatabaseOpenHelper;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Message;
import com.android.bookingapp.model.User;
import com.android.bookingapp.view.LoginActivity;
import com.android.bookingapp.viewmodel.DocMainAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DocMainFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs";
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    SharedPreferences sharedpreferences;
    DatabaseOpenHelper db;
    private ImageView imgChat, imgLogout;
    private int doctorID;
    private Spinner spinner_day, spinner_month, spinner_year;
    private RecyclerView rcvDocMain;
    private String nDay, nMonth, nYear;
    private DocMainAdapter mainAdapter;
    private DatabaseReference dbReference;
    private Button btnSearch;
    private Date date;
    private TextView nameDoctor;
    private ArrayList<Message> messages;
    private ArrayList<User> listUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_main, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        imgChat = view.findViewById(R.id.imv_chat_docMain);
        imgLogout = view.findViewById(R.id.iv_logout);
        spinner_day = view.findViewById(R.id.spinner_date_docMain);
        spinner_month = view.findViewById(R.id.spinner_month_docMain);
        spinner_year = view.findViewById(R.id.spinner_year_docMain);
        rcvDocMain = view.findViewById(R.id.rcv_docMain);
        nameDoctor = view.findViewById(R.id.name_doctor);
        btnSearch = view.findViewById(R.id.bt_search_docMain);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dbReference = FirebaseDatabase.getInstance().getReference();
        db = new DatabaseOpenHelper(getContext());
        date = getDateNow();
        getAllMessage();
        getUser();
        if (getArguments() != null) {
            doctorID = getArguments().getInt("doctorID", -1);
            nameDoctor.setText("Xin Chào \n" + getArguments().getString("nameDoctor") + "!");
        }
        rcvDocMain.setLayoutManager(new GridLayoutManager(getContext(), 1));
        addItemsOnSpinner();

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckInternet.checkInternet(getContext())) {
                    Toast.makeText(getContext(), "Offline", Toast.LENGTH_SHORT).show();
                } else {
                    showLogoutDialog();
                }
            }
        });

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("doctorID", doctorID);
                Navigation.findNavController(view).navigate(R.id.action_docMainFragment_to_listChatFragment, bundle);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchReservation();
            }
        });
        searchReservation();
        return view;
    }

    @Override
    public void onResume() {
        searchReservation();
        super.onResume();
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

    public void addItemsOnSpinner() {
        Date date = getDateNow();

        ArrayList<Integer> day = new ArrayList<>();
        ArrayList<Integer> month = new ArrayList<>();
        ArrayList<Integer> year = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            if (i < 13) month.add(i);
            day.add(i);
        }
        for (int i = 2021; i < 2023; i++) {
            year.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, day);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dataAdapter);
        spinner_day.setSelection(Integer.valueOf(date.getDay()) - 1);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nDay = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(dataAdapter2);
        spinner_month.setSelection(Integer.valueOf(date.getMonth()) - 1);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nMonth = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter3 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, year);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(dataAdapter3);
        spinner_year.setSelection(0);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nYear = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    if (message.getId_Doctor() == doctorID) messages.add(message);
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

    public void getUser() {
        listUser = new ArrayList<>();
        dbReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    listUser.add(user);
                }
                try {
                    db.createUserTable();
                    Cursor cursor = db.getUserFromSqlite();
                    if (cursor.getCount() == 0) db.saveUserTableToDB(listUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public Date getDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String day = simpleDateFormat.format(calendar.getTime());

        String[] temp = day.split("-");
        return new Date(temp[0], temp[1], temp[2]);
    }

    public Date getDateInSpinner() {
        String day = spinner_day.getSelectedItem().toString();
        if (Integer.valueOf(day) < 10) day = "0" + day;
        String month = spinner_month.getSelectedItem().toString();
        if (Integer.valueOf(month) < 10) month = "0" + month;
        String year = spinner_year.getSelectedItem().toString();
        return new Date(day, month, year);
    }

    public void searchReservation() {
        date = getDateInSpinner();
        mainAdapter = new DocMainAdapter(date, doctorID, getContext());
        rcvDocMain.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
    }
}