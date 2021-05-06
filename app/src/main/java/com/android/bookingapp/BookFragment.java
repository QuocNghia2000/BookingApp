package com.android.bookingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.bookingapp.viewmodel.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {
    private RecyclerView rvBook;
    private BookAdapter bookAdapter;
    private List<String> listTime,listDate,listReser;
    private Spinner spBook;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book, container, false);
        listTime = new ArrayList<>();
        listDate = new ArrayList<>();
        listReser = new ArrayList<>();
        listTime.add("07:00");
        listTime.add("07:30");
        listTime.add("08:00");
        listTime.add("08:30");
        listTime.add("09:00");
        listTime.add("09:30");
        listTime.add("10:00");
        listTime.add("10:30");
        listTime.add("13:00");
        listTime.add("13:30");
        listTime.add("14:00");
        listTime.add("14:30");
        listTime.add("15:00");
        listTime.add("15:30");
        listTime.add("16:00");
        listTime.add("16:30");

        listDate.add("5-5-2021");
        listDate.add("6-5-2021");
        listDate.add("7-5-2021");
        listDate.add("8-5-2021");

        listReser.add("07:00");
        listReser.add("08:00");
        listReser.add("13:00");
        listReser.add("14:00");
        listReser.add("14:30");
        listReser.add("15:30");

        spBook = v.findViewById(R.id.sp_book);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,listDate);

        spBook.setAdapter(spinnerAdapter);

        spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = "position :" + position + " value :" + listDate.get(position);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });

        bookAdapter = new BookAdapter(listTime,getContext(),listReser);
        rvBook = v.findViewById(R.id.rv_book);
        rvBook.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rvBook.setAdapter(bookAdapter);
        return v;




    }
}