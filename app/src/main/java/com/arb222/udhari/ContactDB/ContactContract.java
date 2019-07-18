package com.arb222.udhari.ContactDB;

import android.provider.BaseColumns;

public final class ContactContract  {

    private ContactContract() { }

    public static final class ContactEntry implements BaseColumns{

        //Table Name for the users found in contact
        public final static String TABLE_NAME = "users";


        public final static String COLUMN_UID = "UID";
        public final static String COLUMN_DISPLAY_NAME = "DISPLAYNAME";
        public final static String COLUMN_PHONE_NUMBER = "PHONENUMBER";
        public final static String COLUMN_CONNECTION_ID = "CONNECTIONID";


        public static final String CONNECTION_ID_NA = "NA";


    }
}
