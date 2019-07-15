package com.arb222.udhari;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull UserInContactsAdapter.UserListRecyclerViewHolder holder, int position) {
        holder.nameTextView.setText(userList.get(position).getDisplayName());
        holder.phoneTextView.setText(userList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, phoneTextView;

        public UserListRecyclerViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.active_user_card_name_textview);
            phoneTextView = view.findViewById(R.id.active_user_card_phonenumber_textview);
        }
    }
}
