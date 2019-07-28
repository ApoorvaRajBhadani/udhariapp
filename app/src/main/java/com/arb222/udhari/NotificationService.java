package com.arb222.udhari;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//import androidx.core.app.NotificationManagerCompat;

import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.arb222.udhari.Base.SHAREDPREF_FCMTOKEN;

public class NotificationService extends FirebaseMessagingService {

    private NotificationManagerCompat notifMangComp;
    ContactDbHelper contactDbHelper;

    String[] projection = {ContactContract.ContactEntry.COLUMN_DISPLAY_NAME, ContactContract.ContactEntry.COLUMN_PHONE_NUMBER};
    String selection = ContactContract.ContactEntry.COLUMN_UID + "=?";

    public NotificationService() {
    }

    @Override
    public void onNewToken(String s) {
        Log.d("NotificationService", "token: "+s);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString(SHAREDPREF_FCMTOKEN, s).apply();
    }

    public void showNotification(String title,String message){
        notifMangComp = NotificationManagerCompat.from(this);
        Intent notificationIntent = new Intent(this,MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, Base.CHANNEL_PAYMENT_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(intent)
                .setGroup(Base.CHANNEL_PAYMENT_GROUPKEY)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notifMangComp.notify((int) SystemClock.uptimeMillis(),notification);
        Log.d("Notification service", "showNotification: "+title);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        contactDbHelper = new ContactDbHelper(this);
        final SQLiteDatabase contactDb = contactDbHelper.getReadableDatabase();
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size()>0){
//            String title = remoteMessage.getData().get("title").toString();
//            String body = remoteMessage.getData().get("body").toString();
            String connection = remoteMessage.getData().get("connection").toString();
            String notice =remoteMessage.getData().get("notice").toString();
            String displayname = remoteMessage.getData().get("displayname").toString();
            String amt = remoteMessage.getData().get("amt").toString();
            String desc = remoteMessage.getData().get("desc").toString();
            String selection = ContactContract.ContactEntry.COLUMN_UID + "=?";
            String[] selectionArgs = {connection};
            Cursor contactData = contactDb.query(ContactContract.ContactEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (contactData.getCount() >= 1) {
                contactData.moveToPosition(0);
                displayname = contactData.getString(contactData.getColumnIndex(ContactContract.ContactEntry.COLUMN_DISPLAY_NAME));
                contactData.close();
            }
            showNotification(notice+" "+displayname+" ₹"+amt,"For : "+desc);
        }
    }
}
