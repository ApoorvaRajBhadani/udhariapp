package com.arb222.udhari.AddContact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arb222.udhari.Connection;
import com.arb222.udhari.R;
import com.arb222.udhari.UserConnection;
import com.arb222.udhari.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserInContactsAdapter extends RecyclerView.Adapter<UserInContactsAdapter.UserListRecyclerViewHolder> {

    ArrayList<UserInContacts> userList;

    public UserInContactsAdapter(ArrayList<UserInContacts> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_user_card, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListRecyclerViewHolder rcv = new UserListRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInContactsAdapter.UserListRecyclerViewHolder holder, final int position) {
        holder.nameTextView.setText(userList.get(position).getDisplayName());
        holder.phoneTextView.setText(userList.get(position).getPhoneNumber());
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference currentUserconnectionref = holder.UserConnectionRef.child(myUid);
        final DatabaseReference connectionUserconnectionref = holder.UserConnectionRef.child(userList.get(position).getUid());
        final String newConnectionId = myUid+"@+@"+userList.get(position).getUid();
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.get(position).getConnectionId()=="NA"){
                    UserConnection type1 = new UserConnection(userList.get(position).getUid(),newConnectionId,userList.get(position).getPhoneNumber(),1,0);
                    currentUserconnectionref.child(newConnectionId).setValue(type1);
                    UserConnection type2 = new UserConnection(myUid,newConnectionId,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString(),2,0);
                    connectionUserconnectionref.child(newConnectionId).setValue(type2);
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
}
