package com.arb222.udhari.ContactDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.arb222.udhari.ContactDB.ContactContract.ContactEntry;

public class ContactDbHelper  extends SQLiteOpenHelper {

    public static final String LOG_TAG = ContactDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "userincontact.db";

    private static final int DATABASE_VERSION = 1;

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_UIC_TABLE = "CREATE TABLE "+ ContactEntry.TABLE_NAME+
                " ("
                + ContactEntry.COLUMN_UID + " TEXT PRIMARY KEY, "
                + ContactEntry.COLUMN_DISPLAY_NAME + " TEXT, "
                + ContactEntry.COLUMN_PHONE_NUMBER + " TEXT, "
                + ContactEntry.COLUMN_CONNECTION_ID + " TEXT );";
        db.execSQL(SQL_CREATE_UIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
