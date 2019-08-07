package com.arb222.udhari.ContactDB;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.arb222.udhari.AddContact.UserInContact;
import com.arb222.udhari.ContactDB.ContactContract.ContactEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateContactDb {

    private Context mCtx;
    private ContactDbHelper mDbHelper;
    private List<String> connectionList = new ArrayList<>();
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final String TAG = "UpdateContactDb";

    public void initializeContactDb(Context context) {
        mDbHelper = new ContactDbHelper(context);
        mCtx = context;

        //getting Contact Data from user's phone
        getConnectionsList();
        //First initializing User's Country ISO prefix
    }

    private void getUserDetails(final String displayName, final String phoneNumber) {

    }

    private void getConnectionsList() {
        String authedUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userconnectionCurrentUserDbr = FirebaseDatabase.getInstance().getReference("userconnection").child(authedUserUID);
        //userconnectionCurrentUserDbr.keepSynced(true);
        userconnectionCurrentUserDbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    connectionList.add(snapshot.child("connectionId").getValue().toString());
                }
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadData(){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String iso = "IN";
        TelephonyManager telephonyManager = (TelephonyManager) mCtx.getApplicationContext().getSystemService(mCtx.getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null) {
            if (!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();
        }
        String ISOprefix = CountryToPhonePrefix.getPhone(iso);

        Cursor phones = mCtx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            final String displayName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replace("(", "");
            phoneNumber = phoneNumber.replace(")", "");
            phoneNumber = phoneNumber.replace("-", "");

            try {
                if (!String.valueOf(phoneNumber.charAt(0)).equals("+"))
                    phoneNumber = ISOprefix + phoneNumber;
            }catch (NullPointerException e){
                Log.d(TAG, "loadData: NullPointerException in phoneNumber.charAt(0)");
                continue;
            }

            getUserDetails(displayName, phoneNumber);

            DatabaseReference userinfoDbr = FirebaseDatabase.getInstance().getReference().child("userinfo");
            final String authedUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Query query = userinfoDbr.orderByChild("phoneNumber").equalTo(phoneNumber);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String phone = "";
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            if (childSnapshot.child("phoneNumber").getValue() != null)
                                phone = childSnapshot.child("phoneNumber").getValue().toString();
                            //if (childSnapshot.child("firstName").getValue() != null && childSnapshot.child("lastName").getValue() != null) {
                            //name = childSnapshot.child("firstName").getValue().toString();
                            //name = name.concat(" " + childSnapshot.child("lastName").getValue().toString());}

                            final String userUid = childSnapshot.child("uid").getValue().toString();
                            String connectionId = "NA";
                            if (connectionList.contains(authedUserUID + "@+@" + userUid))
                                connectionId = authedUserUID + "@+@" + userUid;
                            if (connectionList.contains(userUid + "@+@" + authedUserUID))
                                connectionId = userUid + "@+@" + authedUserUID;
                            UserInContact mUser = new UserInContact(displayName, phone, userUid, connectionId);
                            ContentValues value = new ContentValues();
                            value.put(ContactEntry.COLUMN_UID, mUser.getUid());
                            value.put(ContactEntry.COLUMN_DISPLAY_NAME, mUser.getDisplayName());
                            value.put(ContactEntry.COLUMN_PHONE_NUMBER, mUser.getPhoneNumber());
                            value.put(ContactEntry.COLUMN_CONNECTION_ID, mUser.getConnectionId());
                            long newRowId = db.insert(ContactEntry.TABLE_NAME, null, value);
                            if (newRowId == -1)
                                Log.d("UpdateContactDb", "a row not added successfully");
                            else
                                Log.d("UpdateContactDb", "new row added id :" + newRowId);
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        phones.close();
    }


}
