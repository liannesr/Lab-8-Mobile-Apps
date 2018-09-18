package com.techexhange.mobileapps.lab8;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_FILE_NAME = "Contacts_SharedPrefs";
    private static String CONTACT_NAME="";
    private static String CONTACT_NUMBER="";

    private SQLiteDatabase database;


    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSaveButtonPressed);

        Button loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(this:: onLoadButtonPressed);

        database = new ContactSqliteHelper(this).getWritableDatabase();
    }

    private void onSaveButtonPressed(View v) {
        EditText name = (EditText) findViewById(R.id.name_edit_text);
        String nameText = name.getText().toString();
        EditText number = (EditText) findViewById(R.id.number_edit_text);
        String numberText = number.getText().toString();
        ContentValues content = new ContentValues();
        content.put(ContactDBSchema.Cols.NAME, nameText);
        content.put(ContactDBSchema.Cols.NUMBER, numberText);
        if(nameText.contentEquals("") || numberText.contentEquals("") || numberText.length()!=12){
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show();
            return;
        }
        try (Cursor cursor = database.query(
                ContactDBSchema.TABLE_NAME,
                null,
                ContactDBSchema.Cols.NAME + " =?" ,
                 new String[]{nameText},
                null, null, null)) {
            Log.d(TAG, "About to move to first");
            cursor.moveToFirst();
            if (cursor.isAfterLast()) {
                System.out.println("IN NEW INSERT!");
                database.insert(ContactDBSchema.TABLE_NAME, null, content);
            } else {
                System.out.println("IN UPDATE");
                Log.d(TAG, "Content: " + content);
                database.update(ContactDBSchema.TABLE_NAME,content, ContactDBSchema.Cols.NAME+"= ?", new String[]{nameText});
            }
        }
    }

    private void onLoadButtonPressed(View v){
        EditText name = (EditText) findViewById(R.id.name_edit_text);
        String nameText = name.getText().toString();
        EditText number = (EditText) findViewById(R.id.number_edit_text);

        if(nameText.contentEquals("")){
            Toast.makeText(this, "Empty input", Toast.LENGTH_LONG).show();
            return;
        }
        try (Cursor cursor = database.query(  ContactDBSchema.TABLE_NAME,
                null,
                ContactDBSchema.Cols.NAME + " =?",
                new String[]{nameText},
                null, null, null)){
            cursor.moveToFirst();
            if(cursor.isAfterLast()){
                Toast.makeText(this, "The user entered was not found", Toast.LENGTH_LONG).show();
                return;
            }
            int colIndex = cursor.getColumnIndex(ContactDBSchema.Cols.NUMBER);
            if(colIndex < 0){
                Toast.makeText(this, "The query was not valid", Toast.LENGTH_LONG).show();
                return;
            }
            String numberStr = cursor.getString(colIndex);
            number.setText(numberStr);
        }

    }
}


//        onSaveButton PRESSED WITH SHARED PREFERENCES
//        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        EditText contact = (EditText) findViewById(R.id.name_edit_text);
//        EditText number = (EditText) findViewById(R.id.number_edit_text);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.putString(contact.getText().toString(), number.getText().toString());
//        editor.commit();

//      ONLOADBUTTON PRESSED WITH SHARED PREFERENCES
//    SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//            Context.MODE_PRIVATE);
//    EditText contact = (EditText) findViewById(R.id.name_edit_text);
//    SharedPreferences.Editor editor = sharedPrefs.edit();
//
//    EditText number = (EditText) findViewById(R.id.number_edit_text);
//        number.setText(sharedPrefs.getString(contact.getText().toString(), null), null);