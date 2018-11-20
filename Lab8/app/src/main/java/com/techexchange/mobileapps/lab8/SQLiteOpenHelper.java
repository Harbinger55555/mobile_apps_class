package com.techexchange.mobileapps.lab8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ContactSqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "TechExchange_Contacts";
    private static final int VERSION = 1;

    ContactSqliteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContactDbSchema.TABLE_NAME +
                "( _id integer primary key autoincrement, " +
                ContactDbSchema.Cols.NAME + ", " +
                ContactDbSchema.Cols.NUMBER + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
