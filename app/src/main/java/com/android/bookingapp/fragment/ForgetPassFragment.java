package com.android.bookingapp.fragment;

import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPassFragment extends Fragment {
    private EditText email;
    private Button bt_login, bt_sendpass;
    private ArrayList<User> users;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int idUserCurrent;


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    private String randomCode() {
        int targetStringLength = 8;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = 48 + (int)
                    (random.nextFloat() * (122 - 48 + 1));
            if ((randomLimitedInt >= 91 && randomLimitedInt <=96) ||
                    (randomLimitedInt >= 58 && randomLimitedInt <=64)) {
                randomLimitedInt = 48 + (int)
                        (random.nextFloat() * (57 - 48 + 1));
            }
            buffer.append((char) randomLimitedInt);
        }
        System.out.println(buffer.toString());
        return buffer.toString();
    }

    Session session = null;
    String rec, subject, textMessage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        users=new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    User user = data.getValue(User.class);
                    if(user.getEmail()==email.getText().toString())
                    {
                        idUserCurrent=user.getId();
                    }
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
                Navigation.findNavController(v).navigate(R.id.action_ForgetPassFragment_to_loginFragment);
            }
        });
        bt_sendpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")) {
                    Toast.makeText(getContext(),"Vui lòng nhập đẩy đủ thông tin",Toast.LENGTH_SHORT).show();
                } else if(idUserCurrent!=-1) {
                    rec = email.getText().toString();
                    textMessage = randomCode();
                    subject = "Mật khẩu mới";
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");
                    session = Session.getDefaultInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return  new PasswordAuthentication("bm3doithong@gmail.com", "BM3doithong@");
                        }
                    });
                    RetreiveFeedTask task = new RetreiveFeedTask();
                    task.execute();
                    Toast.makeText(getContext(),"Vui mở gmail để nhận lại mật khẩu",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.action_ForgetPassFragment_to_loginFragment);

                    HashMap hashMap = new HashMap();
                    hashMap.put("password", textMessage);
                    String userId = String.valueOf(idUserCurrent);
                    myRef.child("User"+userId).updateChildren(hashMap);
                } else {
                    Toast.makeText(getContext(),"Vui lòng kiểm tra lại thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("bm3doithong@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_pass, container, false);;
        email = view.findViewById(R.id.edt_email);
        bt_login = view.findViewById(R.id.bt_login);
        bt_sendpass = view.findViewById(R.id.bt_sendpass);
        return view;
    }
}