package com.arb222.udhari.Transactions;

import com.arb222.udhari.Notification.NotificationModel;

import java.util.Comparator;

public class TransactionModel {
    String notice,desc,txnId,connId,connectedTo,deleteOther;
    long timestamp;

    public TransactionModel(){
    }

    public TransactionModel(String notice, String desc, String txnId, String connId, String connectedTo, long timestamp,String deleteOther) {
        this.notice = notice;
        this.desc = desc;
        this.txnId = txnId;
        this.connId = connId;
        this.connectedTo = connectedTo;
        this.timestamp = timestamp;
        this.deleteOther = deleteOther;
    }

    public String getNotice() {
        return notice;
    }

    public String getDesc() {
        return desc;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getConnId() {
        return connId;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeleteOther() {
        return deleteOther;
    }

    public static final Comparator<TransactionModel> BY_TIMESTAMP = new Comparator<TransactionModel>() {

        @Override
        public int compare(TransactionModel o1, TransactionModel o2) {
            if(o1.getTimestamp() < o2.getTimestamp()){
                return 1;
            }else if (o1.getTimestamp() > o2.getTimestamp()){
                return -1;
            }else {
                return 0;
            }
        }
    };
}
