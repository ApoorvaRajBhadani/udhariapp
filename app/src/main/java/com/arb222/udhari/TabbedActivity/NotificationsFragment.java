package com.arb222.udhari.TabbedActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arb222.udhari.AddContact.FindActiveUsersActivity;
import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.Notification.NotificationAdapter;
import com.arb222.udhari.Notification.NotificationModel;
import com.arb222.udhari.POJO.Notification;
import com.arb222.udhari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {
    View view;
    private RecyclerView notificationRecyclerView;

    private List<NotificationModel> notificationList = new ArrayList<>();
    ContactDbHelper contactDbHelper;
    String dn = "Name unsaved";
    private DatabaseReference ref;

    public NotificationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationRecyclerView = (RecyclerView) view.findViewById(R.id.notification_recyclerview);
        final NotificationAdapter adapter = new NotificationAdapter(getContext(), notificationList);
        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(notificationRecyclerView.getContext(),DividerItemDecoration.VERTICAL));
        ref = FirebaseDatabase.getInstance().getReference("usernotification").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.keepSynced(true);
        contactDbHelper = new ContactDbHelper(getActivity());
        final SQLiteDatabase contactDb = contactDbHelper.getReadableDatabase();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final Notification newNotification = childSnapshot.getValue(Notification.class);
                    String[] projection = {ContactContract.ContactEntry.COLUMN_DISPLAY_NAME, ContactContract.ContactEntry.COLUMN_PHONE_NUMBER};
                    String selection = ContactContract.ContactEntry.COLUMN_UID + "=?";
                    String[] selectionArgs = {newNotification.getConnection()};
                    String displayName;
                    Cursor contactData = contactDb.query(ContactContract.ContactEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                    if (contactData.getCount() >= 1) {
                        contactData.moveToPosition(0);
                        displayName = contactData.getString(contactData.getColumnIndex(ContactContract.ContactEntry.COLUMN_DISPLAY_NAME));
                        addToList(newNotification, displayName);
                        contactData.close();
                    } else {
                        final DatabaseReference connectedToUserInfoRef = FirebaseDatabase.getInstance().getReference("userinfo").child(newNotification.getConnection());
                        connectedToUserInfoRef.keepSynced(true);
                        connectedToUserInfoRef.child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dn = dataSnapshot.getValue().toString();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //todo: not working
                        displayName = dn;
                        addToList(newNotification, displayName);
                    }

                }

                Collections.sort(notificationList, NotificationModel.BY_TIMESTAMP);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationRecyclerView.setAdapter(adapter);
        return view;
    }

    public void addToList(Notification newNotification, String displayName) {
        NotificationModel newNotifModelValue = new NotificationModel(newNotification.getNotice()
                , newNotification.getTxnId()
                , newNotification.getConnection()
                , newNotification.getConnectionId()
                , newNotification.getDesc()
                , displayName
                , newNotification.getDisplayableAmt()
                , newNotification.getTimestamp()
                , newNotification.getStatus());
        notificationList.add(newNotifModelValue);
    }
}
