package com.arb222.udhari.Transactions;

public class TransactionModel {
    String notice,desc,txnId,connId,connectedTo;
    long timestamp;

    public TransactionModel(){
    }

    public TransactionModel(String notice, String desc, String txnId, String connId, String connectedTo, long timestamp) {
        this.notice = notice;
        this.desc = desc;
        this.txnId = txnId;
        this.connId = connId;
        this.connectedTo = connectedTo;
        this.timestamp = timestamp;
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
}
