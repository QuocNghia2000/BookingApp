package com.android.bookingapp.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

public class ImportFunction {
    Context context;

    public ImportFunction(Context context)
    {
        this.context=context;
    }

    public boolean checkInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    public int posCurrent(String email, String pass, ArrayList<User> users) {
        if(users!=null){
            for (int i = 0; i < users.size(); i++) {
                if (email.equals(users.get(i).getEmail()) && pass.equals(users.get(i).getPassword())) {
                    return i;
                }
            }
        }
        return -1;
    }
}
