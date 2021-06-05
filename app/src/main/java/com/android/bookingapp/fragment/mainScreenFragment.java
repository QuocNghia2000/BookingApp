package com.android.bookingapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.RelativeLayout;
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
import com.android.bookingapp.model.Department;
import com.android.bookingapp.model.Doctor;
import com.android.bookingapp.view.LoginActivity;
import com.android.bookingapp.viewmodel.DepartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class mainScreenFragment extends Fragment {
    private ArrayList<Department> mDeparts;
    private ArrayList<Doctor> listDoc;
    private RecyclerView rvDeparts;
    private DepartAdapter departAdapter;
    private DatabaseReference dbReference;
    private ImageView ivAccount,ivLogout;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Dialog dialogSearch;
    private TextView tvSearch;
    //private User user;
    private ImageView imvChat;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private int idUser=-1;
    private List<String> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(getActivity().getIntent().getSerializableExtra("doctor")!=null)
        {
            Doctor doctor= (Doctor) getActivity().getIntent().getSerializableExtra("doctor");
            Bundle bundle = new Bundle();
            bundle.putSerializable("doctor",doctor);
            Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_docMainFragment,bundle);
        }

        if(getActivity().getIntent()!=null)
        {
            idUser= getActivity().getIntent().getIntExtra("id",-1);
        }

        mDeparts=new ArrayList<>();


        dialogBuilder=new AlertDialog.Builder(getContext());

        departAdapter = new DepartAdapter(mDeparts,idUser);
        rvDeparts.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvDeparts.setAdapter(departAdapter);
        getAllDoctor();
        getAllDepart();
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        ivAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",idUser);
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_infoAccountFragment, bundle);
            }
        });
        imvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id_user",idUser);
                Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_listChatFragment,bundle);
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSearch = new Dialog(getContext());
                dialogSearch.setContentView(R.layout.dialog_searchable_spinner);
                dialogSearch.getWindow().setLayout(950,1200);
                dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogSearch.show();

                EditText editText = dialogSearch.findViewById(R.id.edt_search_mainScreen);
                ListView listView = dialogSearch.findViewById(R.id.lv_search_mainScreen);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,list);
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
                        DialogInfordoctorBinding binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.dialog_infordoctor,null,false);
                        newDialog.setContentView(binding.getRoot());
                        binding.setDoctor(listDoc.get(position));
                        for(Department d : mDeparts){
                            if(listDoc.get(position).getDepartment() == d.getId()){
                                binding.setDepartment(d);
                            }
                        }


                        ImageView imvChat = newDialog.findViewById(R.id.imv_chat_dialog);
                        ImageView imvBook = newDialog.findViewById(R.id.imv_book_dialog);
                        RelativeLayout rl = newDialog.findViewById(R.id.rl_dialog);

                        newDialog.getWindow().setLayout(1440,1200);
                        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //Toast.makeText(getContext(),String.valueOf(rl.),Toast.LENGTH_SHORT).show();
                        newDialog.show();

                        imvChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("id_user",idUser);
                                bundle.putSerializable("doctor",listDoc.get(position));
                                bundle.putBoolean("isUser",true);
                                Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_detailMessFragment,bundle);
                                newDialog.dismiss();
                                dialogSearch.dismiss();
                            }
                        });

                        imvBook.setOnClickListener((new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("id_user",idUser);
                                bundle.putSerializable("doctor",listDoc.get(position));
                                Navigation.findNavController(view).navigate(R.id.action_mainScreenFragment_to_bookFragment,bundle);
                                newDialog.dismiss();
                                dialogSearch.dismiss();
                            }
                        }));
                        //dialogSearch.dismiss();
                    }
                });
            }
        });
    }
    public void showLogoutDialog(){
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

    public void getAllDepart()
    {
        dbReference= FirebaseDatabase.getInstance().getReference("Department");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDeparts.clear();
                for(DataSnapshot item:snapshot.getChildren())
                {
                    Department department=item.getValue(Department.class);
                    mDeparts.add(department);
                }
                departAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getAllDoctor(){
        listDoc = new ArrayList<>();
        list = new ArrayList<>();
        dbReference= FirebaseDatabase.getInstance().getReference("Doctor");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDoc.clear();
                for(DataSnapshot item:snapshot.getChildren())
                {
                    Doctor d = item.getValue(Doctor.class);
                    listDoc.add(d);
                    list.add(d.getFullname());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_screen, container, false);

        rvDeparts=view.findViewById(R.id.rv_department);
        ivAccount=view.findViewById(R.id.iv_account);
        ivLogout=view.findViewById(R.id.iv_ogout);
        imvChat = view.findViewById(R.id.imv_listchat_main);
        tvSearch = view.findViewById(R.id.tv_search_mainScreen);
        return view;
    }
}