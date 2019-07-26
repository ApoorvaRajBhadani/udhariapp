package com.arb222.udhari;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    private NotificationManagerCompat notifMangComp;

    public NotificationService() {
    }

    @Override
    public void onNewToken(String s) {
        Log.d("NotificationService", "token: "+s);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("FCMTOKEN", s).apply();
    }

    public void showNotification(String title,String message){
        notifMangComp = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this,NotificationBase.CHANNEL_PAYMENT_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notifMangComp.notify(NotificationBase.CHANNEL_PAYMENT_NOTIFY_ID,notification);
        Log.d("Notification service", "showNotification: "+title);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size()>0){
            String title = remoteMessage.getData().get("title").toString();
            String body = remoteMessage.getData().get("body").toString();
            showNotification(title,body);
        }
    }
}
