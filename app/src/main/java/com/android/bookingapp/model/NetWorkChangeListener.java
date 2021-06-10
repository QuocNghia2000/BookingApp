package com.android.bookingapp.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetWorkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!CheckInternet.checkInternet(context))
        {
            Toast.makeText(context,"detailMessFragment.getDateTimeNow()",Toast.LENGTH_SHORT).show();
        }
    }
}
