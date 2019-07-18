package com.arb222.udhari.POJO;

public class UserConnection {
    String connectedTo, connectionId, connectedToPhoneNumber;
    int myType;
    double pay;
    long lastContacted;

    public long getLastContacted() {
        return lastContacted;
    }

    public UserConnection(String connectedTo, String connectionId, String connectedToPhoneNumber, int myType, double pay, long lastContacted) {
        this.connectedTo = connectedTo;
        this.connectionId = connectionId;
        this.connectedToPhoneNumber = connectedToPhoneNumber;
        this.myType = myType;
        this.pay = pay;
        this.lastContacted = lastContacted;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getConnectedToPhoneNumber() {
        return connectedToPhoneNumber;
    }

    public int getMyType() {
        return myType;
    }

    public double getPay() {
        return pay;
    }

    public UserConnection() {
    }
}
