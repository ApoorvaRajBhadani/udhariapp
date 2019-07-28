package com.arb222.udhari;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

public class Base extends Application {
    public static final String CHANNEL_PAYMENT_ID = "channelPayment";
    public static final String CHANNEL_PAYMENT_DESC = "This channel receives new payment notifications";
    public static final int CHANNEL_PAYMENT_NOTIFY_ID = 1;

    public static final String SHAREDPREF_FCMTOKEN = "FCMTOKEN";
    public static final String SHAREDPREF_AUTHUSERDISPLAYNAME = "AUTHUSERDISPLAYNAME";
    public static final String SHAREDPREF_AUTHUSERPHONENUMBER = "AUTHUSERPHONENUMBER";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channelPayment = new NotificationChannel(CHANNEL_PAYMENT_ID,"Payments Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channelPayment.setDescription(CHANNEL_PAYMENT_DESC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelPayment);
        }
    }
}
