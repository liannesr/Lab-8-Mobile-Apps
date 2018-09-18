package com.techexhange.mobileapps.lab8;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class ContactSqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "TechExchange_Contacts";
    private static final int VERSION = 1;

    ContactSqliteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContactDBSchema.TABLE_NAME +
                "( _id integer primary key autoincrement, " +
                ContactDBSchema.Cols.NAME + ", " +
                ContactDBSchema.Cols.NUMBER + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
