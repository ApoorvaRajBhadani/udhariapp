package com.arb222.udhari.Transactions;

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

import com.arb222.udhari.Notification.NotificationModel;
import com.arb222.udhari.R;
import com.arb222.udhari.RejectATransaction;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context mCtx;
    private List<TransactionModel> transactionModelList;

    public TransactionAdapter(Context mCtx, List<TransactionModel> transactionModelList) {
        this.mCtx = mCtx;
        this.transactionModelList = transactionModelList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.transaction_card, parent, false);
        TransactionViewHolder vh = new TransactionViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, final int position) {
        holder.descTextView.setSelected(true);
        holder.descTextView.setText("For: "+transactionModelList.get(position).getDesc());
        holder.noticeTextView.setSelected(true);
        holder.noticeTextView.setText(transactionModelList.get(position).getNotice());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(transactionModelList.get(position).getTimestamp());
        String date = DateFormat.format("MMM dd", cal).toString();
        holder.timeTextView.setText(date);
        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(transactionModelList.get(position).getDeleteOther().equals("myself")||transactionModelList.get(position).getDeleteOther().equals("yes"))){
                    RejectATransaction object = new RejectATransaction();
                    object.init(transactionModelList.get(position).getTxnId(),transactionModelList.get(position).getConnId(),
                            transactionModelList.get(position).getConnectedTo(),0);
                    Toast.makeText(mCtx,"Rejection Successful",Toast.LENGTH_SHORT).show();
                    ((ConnectionActivity)mCtx).finish();
                }else{
                    Toast.makeText(mCtx,"Can't be rejected",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        TextView noticeTextView,timeTextView,descTextView;
        Button rejectButton;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTextView = itemView.findViewById(R.id.transaction_card_notice_tv);
            timeTextView = itemView.findViewById(R.id.transaction_card_time_tv);
            descTextView = itemView.findViewById(R.id.transaction_card_desc_tv);
            rejectButton = itemView.findViewById(R.id.transaction_card_reject_button);
        }
    }
}
