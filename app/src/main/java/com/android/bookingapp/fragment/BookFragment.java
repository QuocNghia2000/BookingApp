package com.android.bookingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bookingapp.R;
import com.android.bookingapp.databinding.FragmentBookBinding;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.model.Reservation;
import com.android.bookingapp.model.Time;
import com.android.bookingapp.model.User;
import com.android.bookingapp.viewmodel.BookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookFragment extends Fragment {
    private RecyclerView rvBook;
    private BookAdapter bookAdapter;
    private List<String> listTime,listDate,listReser;
    private Spinner spBook;
    private Doctor doctor;
    private ImageView back;
    DatabaseReference myRef;
    private FragmentBookBinding binding;
    private EditText edtSymptom,edtMedicine;
    private Button btnDone;
    private int idReservation = 0;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            doctor=(Doctor) getArguments().getSerializable("doctor");
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_book,null,false);
        View view =binding.getRoot();
        binding.setDoctor(doctor);
        binding.setUser(user);

        rvBook = view.findViewById(R.id.rv_book);
        spBook = view.findViewById(R.id.sp_book);
        edtSymptom = view.findViewById(R.id.edt_symptom_book);
        edtMedicine = view.findViewById(R.id.edt_medicine_book);
        btnDone = view.findViewById(R.id.btn_done_book);
        back = view.findViewById(R.id.img_back);

        listTime = new ArrayList<>();
        listDate = new ArrayList<>();
        listReser = new ArrayList<>();

        bookAdapter = new BookAdapter(listTime,getContext(),listReser);

        rvBook.setLayoutManager(new GridLayoutManager(getActivity(),4));
        rvBook.setAdapter(bookAdapter);

        getAvailableTime();
        getDateInSpinner();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_detailDepartmentFragment_to_bookFragment);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookAdapter.getItemSelected() == -1)
                {
                    Toast.makeText(getContext(),"Hãy chọn giờ muốn đặt lịch!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String symptom = edtSymptom.getText().toString();
                    String medicine = edtMedicine.getText().toString();
                    Time time = getTimePush(listTime.get(bookAdapter.getItemSelected()));
                    com.android.bookingapp.model.Date date = getDatePush(spBook.getSelectedItem().toString());
                    Reservation reservation = new Reservation(++idReservation,user.getId(),doctor.getId(),symptom,medicine,time,date);

                    myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child("Reservation").push().setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getContext(),"Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.action_bookFragment_to_mainScreenFragment);
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        return view;
    }

    public void getAvailableTime(){
        listTime.add("07:00");listTime.add("07:30");listTime.add("08:00");listTime.add("08:30");
        listTime.add("09:00");listTime.add("09:30");listTime.add("10:00");listTime.add("10:30");
        listTime.add("13:00");listTime.add("13:30");listTime.add("14:00");listTime.add("14:30");
        listTime.add("15:00");listTime.add("15:30");listTime.add("16:00");listTime.add("16:30");
    }


    public void getDateInSpinner(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String day;
        Date d  = calendar.getTime();
        day = simpleDateFormat.format(d);
        listDate.add(day);

        calendar.add(Calendar.DAY_OF_YEAR,1);
        d  = calendar.getTime();
        day = simpleDateFormat.format(d);
        listDate.add(day);

        calendar.add(Calendar.DAY_OF_YEAR,1);
        d  = calendar.getTime();
        day = simpleDateFormat.format(d);
        listDate.add(day);


        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,listDate);
        spBook.setAdapter(spinnerAdapter);
        spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listReser.clear();
                myRef = FirebaseDatabase.getInstance().getReference("Reservation");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int idRetemp = 0;
                        for(DataSnapshot data: snapshot.getChildren())
                        {
                            idRetemp++;
                            Reservation reservation = data.getValue(Reservation.class);
                            com.android.bookingapp.model.Date d = reservation.getDate();
                            String date = d.getDay() + "-" + d.getMonth() + "-" + d.getYear();

                            if (reservation.getId_doctor() == doctor.getId()){
                                if(date.equals(listDate.get(position))){
                                    Time t = reservation.getTime();
                                    String time = ((t.getHour()<10)?"0":"") + t.getHour() + ":" + ((t.getMinute()<10)?"0":"") + t.getMinute();
                                    listReser.add(time);
                                }
                            }
                        }
                        idReservation = idRetemp;
                        bookAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Time getTimePush(String stringTime){
        String[] temp = stringTime.split(":");
        return new Time(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
    }

    public com.android.bookingapp.model.Date getDatePush(String stringDate){
        String[] temp = stringDate.split("-");
        return new com.android.bookingapp.model.Date(temp[0],temp[1],temp[2]);
    }

}