package com.arb222.udhari.POJO;

public class UserInfo {
    private String phoneNumber, firstName, lastName, profilePictureLink, uid;
    private int profileStatus = 0;
    // status 0 - status NA
    // status 1 - Only UID and phone no available. FirstName and ProfilePicLink NA ,,, LastName blank
    // status 2 - UID , phone no and Name Available
    // status 3 - UID , phone no and profile pic Available


    public UserInfo(String phoneNumber, String firstName, String lastName, String profilePictureLink, int profileStatus,String uid) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureLink = profilePictureLink;
        this.profileStatus = profileStatus;
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    public String getUid() {
        return uid;
    }

    public int getProfileStatus() {
        return profileStatus;
    }

    public UserInfo() {
    }

}
