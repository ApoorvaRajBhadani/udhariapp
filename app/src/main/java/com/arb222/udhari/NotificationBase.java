package com.arb222.udhari;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;

public class NotificationBase extends Application {
    public static final String CHANNEL_PAYMENT_ID = "channelPayment";
    public static final String CHANNEL_PAYMENT_DESC = "This channel receives new payment notifications";
    public static final int CHANNEL_PAYMENT_NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

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
