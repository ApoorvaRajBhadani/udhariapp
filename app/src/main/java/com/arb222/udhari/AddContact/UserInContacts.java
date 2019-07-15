package com.arb222.udhari;

public class UserInContacts {

    private String displayName,phoneNumber;

    public UserInContacts(String displayName, String phoneNumber) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
