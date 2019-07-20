package com.arb222.udhari.Connection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb222.udhari.Transactions.ConnectionActivity;
import com.arb222.udhari.NewTransactionActivity;
import com.arb222.udhari.POJO.UserInfo;
import com.arb222.udhari.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder> {

    private Context mCtx;
    private List<ConnectionModel> connectionsList;

    public ConnectionAdapter(Context mCtx, List<ConnectionModel> connectionsList) {
        this.mCtx = mCtx;
        this.connectionsList = connectionsList;
    }

    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.connections_card, parent, false);
        ConnectionViewHolder vh = new ConnectionViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ConnectionViewHolder holder, final int position) {
        holder.profilepicImageView.setImageResource(R.drawable.user);
        DatabaseReference connectedToUserInfoRef = FirebaseDatabase.getInstance().getReference("userinfo").child(connectionsList.get(position).getConnectedTo());
        connectedToUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo connectedToUserInfo = dataSnapshot.getValue(UserInfo.class);
                if(connectedToUserInfo.getProfileStatus()==3) {
                    Picasso.get().load(connectedToUserInfo.getProfilePictureLink())
                            .placeholder(R.drawable.user)
                            .fit()
                            .centerCrop()
                            .into(holder.profilepicImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.profilepicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connectionActivityIntent = new Intent(mCtx,ConnectionActivity.class);
                connectionActivityIntent.putExtra("display_name",connectionsList.get(position).getDisplayName());
                connectionActivityIntent.putExtra("connection_uid",connectionsList.get(position).getConnectedTo());
                connectionActivityIntent.putExtra("conn_id",connectionsList.get(position).getConnectionId());
                connectionActivityIntent.putExtra("my_type",connectionsList.get(position).getMyType());
                connectionActivityIntent.putExtra("me_to_pay",connectionsList.get(position).getToPay());

                mCtx.startActivity(connectionActivityIntent);
            }
        });
        holder.newTransactionButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx,"New Transaction",Toast.LENGTH_LONG).show();
                Intent txnIntent = new Intent(mCtx, NewTransactionActivity.class);
                txnIntent.putExtra("uid_of_connection",connectionsList.get(position).getConnectedTo());
                txnIntent.putExtra("display_name",connectionsList.get(position).getDisplayName());
                txnIntent.putExtra("connection_id",connectionsList.get(position).getConnectionId());
                txnIntent.putExtra("my_type",connectionsList.get(position).getMyType());
                mCtx.startActivity(txnIntent);
            }
        });
        holder.toPayTextView.setText("₹"+connectionsList.get(position).getToPay());
        holder.displayNameTextView.setText(connectionsList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    public class ConnectionViewHolder extends RecyclerView.ViewHolder{

        TextView displayNameTextView,toPayTextView;
        ImageView newTransactionButtonImageView,profilepicImageView;


        public ConnectionViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTextView = (TextView) itemView.findViewById(R.id.connection_displayname_textview);
            toPayTextView = (TextView) itemView.findViewById(R.id.connection_topay_textview);
            newTransactionButtonImageView = (ImageView) itemView.findViewById(R.id.connection_addtransaction_imagebutton);
            profilepicImageView = (ImageView) itemView.findViewById(R.id.connection_profilepic_imageview);
        }
    }
}
