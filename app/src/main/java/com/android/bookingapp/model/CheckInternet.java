package com.android.bookingapp.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.android.bookingapp.R;
import com.android.bookingapp.view.NotificationApplication;

import java.util.Random;

public class CheckInternet {

    public static boolean checkInternet(Context context) {
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
    public static void sendNotification(String title,String content,Context context)
    {
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
        Notification notification=new NotificationCompat.Builder(context, NotificationApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.messenger).setLargeIcon(bitmap).build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null)
        {
            Random random=new Random();

            notificationManager.notify(random.nextInt(),notification);
        }
    }

}
