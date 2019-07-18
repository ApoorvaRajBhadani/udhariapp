package com.arb222.udhari.POJO;

public class Notification {
    String notice,txnId,connection,connectionId,desc;
    double displayableAmt;
    long timestamp;
    int status;

    public int getStatus() {
        return status;
    }

    public String getNotice() {
        return notice;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getConnection() {
        return connection;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getDesc() {
        return desc;
    }

    public double getDisplayableAmt() {
        return displayableAmt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Notification(String notice, String txnId, String connection,
                        String connectionId, String desc, double displayableAmt,
                        long timestamp,int status) {
        this.notice = notice;
        this.txnId = txnId;
        this.connection = connection;
        this.connectionId = connectionId;
        this.desc = desc;
        this.displayableAmt = displayableAmt;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Notification() {
    }
}
