package com.arb222.udhari.Transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb222.udhari.Notification.NotificationModel;
import com.arb222.udhari.R;

import java.util.List;

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
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.descTextView.setSelected(true);
        holder.descTextView.setText(transactionModelList.get(position).getDesc());
        holder.noticeTextView.setSelected(true);
        holder.noticeTextView.setText(transactionModelList.get(position).getNotice());
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
