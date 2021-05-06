package com.android.bookingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.bookingapp.model.Date;
import com.android.bookingapp.model.User;

import java.util.ArrayList;

public class moreInfoFragment extends Fragment  {
    private User user;
    private EditText edt_job,edt_address;
    private Button bt_submit;
    RadioButton male;
    Spinner spinner_day,spinner_month,spinner_year;
    private String nDay,nMonth,nYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user=(User)getArguments().getSerializable("user");
        }
    }
    public void addItemsOnSpinner() {

        ArrayList<Integer> day = new ArrayList<>();
        ArrayList<Integer> month = new ArrayList<>();
        ArrayList<Integer> year = new ArrayList<>();
        for(int i=1;i<32;i++)
        {
            if(i<13) month.add(i);
            day.add(i);
        }
        for(int i=1930;i<2021;i++)
        {
            year.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, day);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dataAdapter);
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
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nMonth= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> dataAdapter3 = new ArrayAdapter<Integer>((Context) getActivity(),
                android.R.layout.simple_spinner_item, year);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(dataAdapter3);
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addItemsOnSpinner();
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý xác thực gmail
                User user1=new User(user.getId(),user.getEmail(),user.getPassword(),user.getFullname(),user.getPhone(),
                        new Date(nDay,nMonth,nYear),male.isChecked(),edt_job.getText().toString(),edt_address.getText().toString());
                if(!edt_job.getText().toString().isEmpty()&&!edt_address.getText().toString().isEmpty())
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user1);
                    Navigation.findNavController(v).navigate(R.id.action_moreInfoFragment_to_confrimFragment, bundle);
                }
                else
                {
                    Toast.makeText(getActivity(),"Vui lòng điền đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_more_info, container, false);
        edt_job=view.findViewById(R.id.job);
        edt_address=view.findViewById(R.id.address);
        male=view.findViewById(R.id.male);
        bt_submit=view.findViewById(R.id.bt_submit);
        spinner_day = (Spinner) view.findViewById(R.id.spinner_date);
        spinner_month = (Spinner) view.findViewById(R.id.spinner_month);
        spinner_year = (Spinner) view.findViewById(R.id.spinner_year);
        return view;
    }

}