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

import com.arb222.udhari.NewTransactionActivity;
import com.arb222.udhari.R;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder> {

    private Context mCtx;
    private List<Connection> connectionsList;

    public ConnectionAdapter(Context mCtx, List<Connection> connectionsList) {
        this.mCtx = mCtx;
        this.connectionsList = connectionsList;
    }

    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mCtx).inflate(R.layout.connections_card,parent,false);
        ConnectionViewHolder vh = new ConnectionViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, final int position) {
        holder.profilepicImageView.setImageResource(connectionsList.get(position).getProfilepicresid());
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
