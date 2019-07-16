package com.arb222.udhari;

public class Connection {
    String connectedTo,phoneNumber,displayName,connectionId;
    int myType,profilepicresid;
    double toPay;

    public Connection(){

    }

    public int getProfilepicresid() {
        return profilepicresid;
    }

    public Connection(String connectedTo, String phoneNumber, String displayName, String connectionId, int myType, double toPay, int profilepicresid) {
        this.connectedTo = connectedTo;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.connectionId = connectionId;
        this.myType = myType;
        this.toPay = toPay;
        this.profilepicresid = profilepicresid;
    }

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
