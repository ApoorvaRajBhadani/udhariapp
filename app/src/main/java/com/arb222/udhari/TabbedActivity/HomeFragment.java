package com.arb222.udhari.TabbedActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arb222.udhari.AddContact.FindActiveUsersActivity;
import com.arb222.udhari.Connection.ConnectionModel;
import com.arb222.udhari.Connection.ConnectionAdapter;
import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.R;
import com.arb222.udhari.POJO.UserConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    View view;
    private RecyclerView connectionRecyclerView;
    private FloatingActionButton fab;

    private List<ConnectionModel> connectionModelList = new ArrayList<>();
    ContactDbHelper contactDbHelper;


    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        connectionRecyclerView = (RecyclerView) view.findViewById(R.id.connection_recyclerview);
        fab = (FloatingActionButton) view.findViewById(R.id.new_connection_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FindActiveUsersActivity.class));
            }
        });

        final ConnectionAdapter  adapter= new ConnectionAdapter(getContext(), connectionModelList);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userconnection").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        contactDbHelper = new ContactDbHelper(getActivity());
        final SQLiteDatabase contactDb = contactDbHelper.getReadableDatabase();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                connectionModelList.clear();
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    UserConnection newConnection = childSnapshot.getValue(UserConnection.class);
                    String [] projection = {ContactContract.ContactEntry.COLUMN_DISPLAY_NAME, ContactContract.ContactEntry.COLUMN_PHONE_NUMBER};
                    String selection = ContactContract.ContactEntry.COLUMN_UID+"=?";
                    String [] selectionArgs = {newConnection.getConnectedTo()};
                    String displayName;
                    Cursor contactData = contactDb.query(ContactContract.ContactEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                    if(contactData.getCount()>=1) {
                        contactData.moveToPosition(0);
                        displayName = contactData.getString(contactData.getColumnIndex(ContactContract.ContactEntry.COLUMN_DISPLAY_NAME));
                    }else {
                        displayName = newConnection.getConnectedToPhoneNumber();
                    }
                    ConnectionModel newConnectionModelValue = new ConnectionModel(newConnection.getConnectedTo(),
                            newConnection.getConnectedToPhoneNumber(),
                            displayName,
                            newConnection.getConnectionId(),
                            newConnection.getMyType(),
                            newConnection.getPay(),
                            R.mipmap.ic_launcher,
                            newConnection.getLastContacted());
                    connectionModelList.add(newConnectionModelValue);

                }
                Collections.sort(connectionModelList, ConnectionModel.BY_LAST_CONTACTED);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        connectionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        connectionRecyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}