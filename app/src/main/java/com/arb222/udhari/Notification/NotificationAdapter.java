package com.arb222.udhari.Notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb222.udhari.R;
import com.arb222.udhari.RejectATransaction;
import com.arb222.udhari.RejectActiviy;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mCtx;
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(Context mCtx, List<NotificationModel> notificationModelList) {
        this.mCtx = mCtx;
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.notification_card, parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {
        holder.timeTextView.setText(String.valueOf(notificationModelList.get(position).getTimestamp()));
        holder.descTextView.setText(notificationModelList.get(position).getDesc());
        holder.displayableAmtTextView.setText("₹ " + notificationModelList.get(position).getDisplayableAmt());
        holder.displayNameTextView.setText(notificationModelList.get(position).getDisplayName());
        holder.noticeTextView.setText(notificationModelList.get(position).getNotice());
        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(notificationModelList.get(position).getStatus() == 3 || notificationModelList.get(position).getStatus() == 4 || notificationModelList.get(position).getStatus() == 5)) {
                    Toast.makeText(mCtx, "zreject Clicked", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mCtx, RejectActiviy.class);
                    i.putExtra("txid", notificationModelList.get(position).getTxnId());
                    i.putExtra("conto", notificationModelList.get(position).getConnectedTo());
                    i.putExtra("conid", notificationModelList.get(position).getConnId());
                    i.putExtra("status", notificationModelList.get(position).getStatus());
                    mCtx.startActivity(i);
                }
                else {
                    Toast.makeText(mCtx,"Can't reject this",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView noticeTextView, displayNameTextView, displayableAmtTextView, descTextView, timeTextView;
        Button rejectButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTextView = (TextView) itemView.findViewById(R.id.notification_notice_textview);
            displayNameTextView = (TextView) itemView.findViewById(R.id.notification_displayname_textview);
            displayableAmtTextView = (TextView) itemView.findViewById(R.id.notification_displayableamt_textview);
            descTextView = (TextView) itemView.findViewById(R.id.notification_desc_textview);
            timeTextView = (TextView) itemView.findViewById(R.id.notification_time_textview);
            rejectButton = (Button) itemView.findViewById(R.id.notification_reject_button);
        }
    }
}
