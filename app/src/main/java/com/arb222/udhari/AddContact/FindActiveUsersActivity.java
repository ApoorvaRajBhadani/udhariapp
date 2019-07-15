package com.arb222.udhari;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindActiveUsersActivity extends AppCompatActivity {


    private RecyclerView mUserRVList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserInContacts> contactList, userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_active_users);
        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    private void initializeRecyclerView() {
        mUserRVList = findViewById(R.id.active_users_recyclerview);
        mUserRVList.setNestedScrollingEnabled(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserRVList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserInContactsAdapter(userList);
        mUserRVList.setAdapter(mUserListAdapter);

    }

    private void getContactList() {

        String ISOprefix = getCountryISO();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String displayName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replace("(", "");
            phoneNumber = phoneNumber.replace(")", "");
            phoneNumber = phoneNumber.replace("-", "");

            if (!String.valueOf(phoneNumber.charAt(0)).equals("+"))
                phoneNumber = ISOprefix + phoneNumber;

            UserInContacts mContact = new UserInContacts(displayName, phoneNumber);
            contactList.add(mContact);
            getUserDetails(mContact);
        }
    }

    private void getUserDetails(final UserInContacts mContact) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("userinfo");
        Query query = dbr.orderByChild("phoneNumber").equalTo(mContact.getPhoneNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone = "", name = "";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if (childSnapshot.child("phoneNumber").getValue() != null)
                            phone = childSnapshot.child("phoneNumber").getValue().toString();
                        if (childSnapshot.child("firstName").getValue() != null && childSnapshot.child("lastName").getValue() != null) {
                            //name = childSnapshot.child("firstName").getValue().toString();
                            //name = name.concat(" " + childSnapshot.child("lastName").getValue().toString());
                            name=mContact.getDisplayName();
                        }
                        UserInContacts mUser = new UserInContacts(name, phone);
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getCountryISO() {

        String iso = "IN";

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null) {
            if (!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();
        }
        return CountryToPhonePrefix.getPhone(iso);
    }
}