package com.arb222.udhari.Notification;

import com.arb222.udhari.Connection.ConnectionModel;

import java.util.Comparator;

public class NotificationModel {
    String notice,txnId,connectedTo,connId,desc,displayName;
    double displayableAmt;
    long timestamp;
    int status;

    public NotificationModel(String notice, String txnId, String connectedTo, String connId, String desc, String displayName, double displayableAmt, long timestamp, int status) {
        this.notice = notice;
        this.txnId = txnId;
        this.connectedTo = connectedTo;
        this.connId = connId;
        this.desc = desc;
        this.displayName = displayName;
        this.displayableAmt = displayableAmt;
        this.timestamp = timestamp;
        this.status = status;
    }

    public NotificationModel() {
    }

    public String getNotice() {
        return notice;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public String getConnId() {
        return connId;
    }

    public String getDesc() {
        return desc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDisplayableAmt() {
        return displayableAmt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public static final Comparator<NotificationModel> BY_TIMESTAMP = new Comparator<NotificationModel>() {

        @Override
        public int compare(NotificationModel o1, NotificationModel o2) {
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
