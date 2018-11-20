package com.techexchange.mobileapps.lab8;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//    private static final String SHARED_PREFS_FILE_NAME = "SHARED_PREFS";

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> onSaveButtonPressed());
        Button loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(v -> onLoadButtonPressed());

        database = new ContactSqliteHelper(this).getWritableDatabase();
    }


    private void onSaveButtonPressed() {
//        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText numberEditText = findViewById(R.id.number_edit_text);
        String nameText = nameEditText.getText().toString();
        String numberText = numberEditText.getText().toString();

//        sharedPrefs.edit()
//                .putString(nameText, numberEditText.getText().toString())
//                .apply();
        if (nameText.length() == 0 || numberText.length() == 0) {
            Toast.makeText(this,
                    "Empty name or number input!", Toast.LENGTH_SHORT).show();
        } else {
            try (Cursor cursor = database.query(
                    ContactDbSchema.TABLE_NAME,
                    null,
                    ContactDbSchema.Cols.NAME + " = ?",
                    new String[]{nameText},
                    null, null, null)) {
                // Make the cursor point to the first row in the result set.
                cursor.moveToFirst();
                if (cursor.isAfterLast()) {
                    // No records exist. Need to insert a new one.
                    ContentValues values = new ContentValues();
                    values.put(ContactDbSchema.Cols.NAME, nameText);
                    values.put(ContactDbSchema.Cols.NUMBER, numberText);
                    database.insert(ContactDbSchema.TABLE_NAME, null, values);
                    Toast.makeText(this,
                            "Contact number saved!", Toast.LENGTH_SHORT).show();
                } else {
                    // A record already exists. Need to update it with the new number.
                    ContentValues values = new ContentValues();
                    values.put(ContactDbSchema.Cols.NUMBER, numberText);
                    database.update(ContactDbSchema.TABLE_NAME, values,
                            ContactDbSchema.Cols.NAME + " = ?",
                            new String[]{nameText});
                    Toast.makeText(this,
                            "Contact number updated!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void onLoadButtonPressed() {
//        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText numberEditText = findViewById(R.id.number_edit_text);
        String nameText = nameEditText.getText().toString();
//
//        String number = sharedPrefs.getString(nameEditText, /* defValue= */ -1);
//        numberEditText.setText(number);
        if (nameText.length() == 0) {
            Toast.makeText(this,
                    "Empty name field!", Toast.LENGTH_SHORT).show();
        } else {
            try (Cursor cursor = database.query(
                    ContactDbSchema.TABLE_NAME,
                    null,
                    ContactDbSchema.Cols.NAME + " = ?",
                    new String[]{nameText},
                    null, null, null)) {
                cursor.moveToFirst();
                if (cursor.isAfterLast()) {
                    // The contact was not found.
                    Toast.makeText(this,
                            "Contact not found!", Toast.LENGTH_SHORT).show();
                } else {
                    int colIndex = cursor.getColumnIndex(ContactDbSchema.Cols.NUMBER);
                    if (colIndex < 0) {
                        // The column was not found in the cursor.
                        Toast.makeText(this,
                                "No number saved for contact!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String numberStr = cursor.getString(colIndex);
                        numberEditText.setText(numberStr);
                        Toast.makeText(this,
                                "Contact loaded!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}