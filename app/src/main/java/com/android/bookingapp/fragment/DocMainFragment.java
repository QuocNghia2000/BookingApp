package com.android.bookingapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.bookingapp.R;
import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.User;
import com.android.bookingapp.viewmodel.DocMainAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DocMainFragment extends Fragment {
    private ImageView imgChat;
    private Doctor doctor;
    private Spinner spinner_day, spinner_month, spinner_year;
    private RecyclerView rcvDocMain;
    private String nDay,nMonth,nYear;
    private DocMainAdapter mainAdapter;
    private Button btnSearch;
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doc_main, container, false);

        imgChat = view.findViewById(R.id.imv_chat_docMain);
        spinner_day = view.findViewById(R.id.spinner_date_docMain);
        spinner_month = view.findViewById(R.id.spinner_month_docMain);
        spinner_year = view.findViewById(R.id.spinner_year_docMain);
        rcvDocMain = view.findViewById(R.id.rcv_docMain);
        btnSearch = view.findViewById(R.id.bt_search_docMain);

        date = getDateNow();

        if(getArguments()!=null)
        {
            doctor=(Doctor) getArguments().getSerializable("doctor");
        }

        mainAdapter = new DocMainAdapter(date,doctor,getContext());
        rcvDocMain.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcvDocMain.setAdapter(mainAdapter);

        addItemsOnSpinner();


//        Toast.makeText(getContext(),String.valueOf(doctor.getFullname()),Toast.LENGTH_SHORT).show();

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctor",doctor);
                Navigation.findNavController(view).navigate(R.id.action_docMainFragment_to_listChatFragment,bundle);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = getDateInSpinner();
                mainAdapter = new DocMainAdapter(date,doctor,getContext());
                rcvDocMain.setAdapter(mainAdapter);
                mainAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void addItemsOnSpinner() {
        Date date = getDateNow();

        ArrayList<Integer> day = new ArrayList<>();
        ArrayList<Integer> month = new ArrayList<>();
        ArrayList<Integer> year = new ArrayList<>();
        for(int i=1;i<32;i++)
        {
            if(i<13) month.add(i);
            day.add(i);
        }
        for(int i=2021;i<2023;i++)
        {
            year.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, day);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dataAdapter);
        spinner_day.setSelection(Integer.valueOf(date.getDay())-1);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nDay= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(dataAdapter2);
        spinner_month.setSelection(Integer.valueOf(date.getMonth())-1);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nMonth= parent.getItemAtPosition(position).toString();
                //Toast.makeText(getContext(),String.valueOf(nMonth),Toast.LENGTH_SHORT).show();
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
                nYear= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Date getDateNow(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String day = simpleDateFormat.format(calendar.getTime());

        String[] temp = day.split("-");
        return new Date(temp[0],temp[1],temp[2]);
    }

    public Date getDateInSpinner(){
        String day =  spinner_day.getSelectedItem().toString();
        if(Integer.valueOf(day)<10) day="0" + day;
        String month =  spinner_month.getSelectedItem().toString();
        if(Integer.valueOf(month)<10) month="0" + month;
        String year =  spinner_year.getSelectedItem().toString();
        return new Date(day,month,year);
    }
}