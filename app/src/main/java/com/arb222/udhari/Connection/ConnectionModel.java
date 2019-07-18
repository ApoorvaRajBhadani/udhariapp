package com.arb222.udhari.Connection;

import java.util.Comparator;

public class ConnectionModel {
    String connectedTo,phoneNumber,displayName,connectionId;
    int myType,profilepicresid;
    double toPay;
    long lastContacted;

    public ConnectionModel(){

    }

    public int getProfilepicresid() {
        return profilepicresid;
    }

    public ConnectionModel(String connectedTo, String phoneNumber, String displayName, String connectionId, int myType, double toPay, int profilepicresid, long lastContacted) {
        this.connectedTo = connectedTo;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.connectionId = connectionId;
        this.myType = myType;
        this.toPay = toPay;
        this.profilepicresid = profilepicresid;
        this.lastContacted = lastContacted;
    }

    public long getLastContacted() {
        return lastContacted;
    }

    public static final Comparator<ConnectionModel> BY_LAST_CONTACTED = new Comparator<ConnectionModel>() {
        @Override
        public int compare(ConnectionModel o1, ConnectionModel o2) {
            if(o1.getLastContacted() < o2.getLastContacted()){
                return 1;
            }else if (o1.getLastContacted() > o2.getLastContacted()){
                return -1;
            }else {
                return 0;
            }
        }
    };

    public String getConnectedTo() {
        return connectedTo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public int getMyType() {
        return myType;
    }

    public double getToPay() {
        return toPay;
    }
}
