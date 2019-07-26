package com.arb222.udhari.Notification;

import android.content.Context;
import android.text.format.DateFormat;
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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(notificationModelList.get(position).getTimestamp());
        String date = DateFormat.format("MMM dd", cal).toString();
        holder.timeTextView.setText(date);
        holder.descTextView.setText("  For : "+notificationModelList.get(position).getDesc());
        holder.displayableAmtTextView.setText("₹ " + notificationModelList.get(position).getDisplayableAmt());
        holder.noticeTextView.setText(notificationModelList.get(position).getNotice() + " " + notificationModelList.get(position).getDisplayName());
        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(notificationModelList.get(position).getStatus() == 3 || notificationModelList.get(position).getStatus() == 4 || notificationModelList.get(position).getStatus() == 5)) {
                    RejectATransaction object = new RejectATransaction();
                    object.init(notificationModelList.get(position).getTxnId(),notificationModelList.get(position).getConnId(),
                            notificationModelList.get(position).getConnectedTo(),notificationModelList.get(position).getStatus());
                    Toast.makeText(mCtx,"Rejection Successful",Toast.LENGTH_SHORT).show();
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

        TextView noticeTextView, displayableAmtTextView, descTextView, timeTextView;
        Button rejectButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTextView = (TextView) itemView.findViewById(R.id.notification_notice_textview);
            displayableAmtTextView = (TextView) itemView.findViewById(R.id.notification_displayableamt_textview);
            descTextView = (TextView) itemView.findViewById(R.id.notification_desc_textview);
            timeTextView = (TextView) itemView.findViewById(R.id.notification_time_textview);
            rejectButton = (Button) itemView.findViewById(R.id.notification_reject_button);
        }
    }
}
