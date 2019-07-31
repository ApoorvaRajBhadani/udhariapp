package com.arb222.udhari.AddContact;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.R;
import com.arb222.udhari.POJO.UserConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserInContactAdapter extends RecyclerView.Adapter<UserInContactAdapter.UserListRecyclerViewHolder> {

    ArrayList<UserInContact> userList;
    ContactDbHelper contactDbHelper;
    Context context;

    public UserInContactAdapter(ArrayList<UserInContact> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_user_card, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        context = parent.getContext();
        contactDbHelper = new ContactDbHelper(parent.getContext());
        UserListRecyclerViewHolder rcv = new UserListRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInContactAdapter.UserListRecyclerViewHolder holder, final int position) {
        holder.nameTextView.setText(userList.get(position).getDisplayName());
        holder.phoneTextView.setText(userList.get(position).getPhoneNumber());
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference currentUserconnectionref = holder.UserConnectionRef.child(myUid);
        currentUserconnectionref.keepSynced(true);
        final DatabaseReference connectionUserconnectionref = holder.UserConnectionRef.child(userList.get(position).getUid());
        connectionUserconnectionref.keepSynced(true);
        final String newConnectionId = myUid+"@+@"+userList.get(position).getUid();

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.get(position).getConnectionId().equals("NA")){
                    if(isNetworkAvailable()) {
                        long timestamp = System.currentTimeMillis();
                        UserConnection type1 = new UserConnection(userList.get(position).getUid(), newConnectionId, userList.get(position).getPhoneNumber(), 1, 0, timestamp);
                        currentUserconnectionref.child(newConnectionId).setValue(type1);
                        UserConnection type2 = new UserConnection(myUid, newConnectionId, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString(), 2, 0, timestamp);
                        connectionUserconnectionref.child(newConnectionId).setValue(type2);
                        Log.d("Adapter", "Created");

                        SQLiteDatabase db = contactDbHelper.getWritableDatabase();
                        ContentValues value = new ContentValues();
                        value.put(ContactContract.ContactEntry.COLUMN_CONNECTION_ID, newConnectionId);
                        String[] whereArgs = {userList.get(position).getUid()};
                        db.update(ContactContract.ContactEntry.TABLE_NAME, value, ContactContract.ContactEntry.COLUMN_UID + "=?", whereArgs);
                        Log.d("Adapter", "DB updated");
                        Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                        ((FindActiveUsersActivity) context).finish();
                    }else {
                        Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d("Adapter","user already present");
                    Toast.makeText(context,"Already Connected",Toast.LENGTH_SHORT).show();
                    ((FindActiveUsersActivity)context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, phoneTextView;
        public CardView card;
        DatabaseReference UserConnectionRef;
        public UserListRecyclerViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.active_user_card_name_textview);
            phoneTextView = view.findViewById(R.id.active_user_card_phonenumber_textview);
            card = view.findViewById(R.id.userincontact_overall_cardview);
            UserConnectionRef = FirebaseDatabase.getInstance().getReference("userconnection");

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
