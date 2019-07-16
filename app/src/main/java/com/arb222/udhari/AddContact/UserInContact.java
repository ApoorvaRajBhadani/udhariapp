package com.arb222.udhari.AddContact;

public class UserInContacts {

    private String displayName,phoneNumber,uid,connectionId;
    //Connection ID only if already connected


    public UserInContacts(String displayName, String phoneNumber, String uid, String connectionId) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.uid = uid;
        this.connectionId = connectionId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public String getConnectionId() {
        return connectionId;
    }
}
