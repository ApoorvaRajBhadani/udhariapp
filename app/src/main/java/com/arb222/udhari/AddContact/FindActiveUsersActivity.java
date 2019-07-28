package com.arb222.udhari.AddContact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.arb222.udhari.AddContact.UserInContact;
import com.arb222.udhari.AddContact.UserInContactAdapter;
import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FindActiveUsersActivity extends AppCompatActivity {


    private RecyclerView mUserRVList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserInContact>  userList;

    private ContactDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_active_users);
        userList = new ArrayList<>();
        mDbHelper = new ContactDbHelper(this);

        mUserRVList = findViewById(R.id.active_users_recyclerview);
        mUserRVList.setNestedScrollingEnabled(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserRVList.setLayoutManager(mUserListLayoutManager);
        mUserRVList.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));


        mUserListAdapter = new UserInContactAdapter(userList);
        mUserRVList.setAdapter(mUserListAdapter);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = ContactContract.ContactEntry.COLUMN_DISPLAY_NAME + " ASC";
        String selection = ContactContract.ContactEntry.COLUMN_UID+"!=?";
        String [] selectionArgs = {FirebaseAuth.getInstance().getCurrentUser().getUid()};
        Cursor c = db.query(ContactContract.ContactEntry.TABLE_NAME,null,selection,selectionArgs,null,null,sortOrder);
        try {
            int uidColumnIndex = c.getColumnIndex(ContactContract.ContactEntry.COLUMN_UID);
            int displaynameColIndex = c.getColumnIndex(ContactContract.ContactEntry.COLUMN_DISPLAY_NAME);
            int phonenoColIndex = c.getColumnIndex(ContactContract.ContactEntry.COLUMN_PHONE_NUMBER);
            int connectionidColIndex = c.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONNECTION_ID);
            while (c.moveToNext()) {
                String UID = c.getString(uidColumnIndex);
                String DISPLAY_NAME = c.getString(displaynameColIndex);
                String PHONE_NUMBER = c.getString(phonenoColIndex);
                String CONN_ID = c.getString(connectionidColIndex);
                UserInContact user = new UserInContact(DISPLAY_NAME, PHONE_NUMBER, UID, CONN_ID);
                userList.add(user);
            }
            mUserListAdapter.notifyDataSetChanged();
        }
        finally {
            c.close();
        }
    }
}